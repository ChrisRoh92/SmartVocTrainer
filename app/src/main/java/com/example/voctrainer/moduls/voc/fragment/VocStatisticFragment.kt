package com.example.voctrainer.moduls.voc.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.backend.database.entities.Test
import com.example.voctrainer.moduls.voc.adapter.VocStatisticRecyclerViewAdapter
import com.example.voctrainer.moduls.voc.utils.MyPercentFormatter
import com.example.voctrainer.moduls.voc.utils.MyValueFormatter
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModel
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModelFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*



/*
****** Offene Tasks ******
-
-
- TODO() Daten für die Charts im VocViewModel bereitstellen lassen
- TODO() Charts implementieren
-

*

******************************************************************
 */
class VocStatisticFragment(): Fragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView: View

    // RecyclerView
    private var rv:RecyclerView? = null
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: VocStatisticRecyclerViewAdapter
    // Restliche Views
    private lateinit var tvNotLearnedocs:TextView
    private lateinit var tvInLearningVocs:TextView
    private lateinit var tvLearnedVocs:TextView

    // PieChart
    private lateinit var pieChart:PieChart
    // LineChart
    private lateinit var lineChart:LineChart

    // ViewModel:
    private lateinit var vocViewModel:VocViewModel
    private lateinit var vocViewModelFactory: VocViewModelFactory
    private var bookId:Long? = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bookId = arguments?.getLong("bookId",0)
        try {
            vocViewModelFactory = VocViewModelFactory(bookId!!,requireActivity().application)
            vocViewModel = ViewModelProvider(requireParentFragment(),vocViewModelFactory).get(
                VocViewModel::class.java)
            startObserver()
        } catch (e: Exception)
        {
            e.printStackTrace()

        }



    }

    private fun startObserver()
    {
        vocViewModel.getVocStatusValues().observe(viewLifecycleOwner, Observer { values ->
            // ProgressBar + Text dazu aktualisieren...
            // Hier brauche den Fortschritt:
            initPieChart(values)
            initViews(values)
        })

        vocViewModel.vocs.observe(viewLifecycleOwner, Observer { values ->
            // ProgressBar + Text dazu aktualisieren...
            // Hier brauche den Fortschritt:
            vocViewModel.createVocStatusValues()
        })

        vocViewModel.tests.observe(viewLifecycleOwner, Observer {

            val testResult = vocViewModel.createTestStatistics(it)
            initLineChart(ArrayList(it))
            var results = arrayListOf(
                testResult.testCount.toFloat(),
                testResult.itemCount,
                testResult.result,
                testResult.itemCount,
                testResult.itemFault)
            if(rv == null)
            {
                // ("Anzahl Test","Ø Abgefragte Vokabeln","Ø Erfolgsquote","Ø Anzahl Korrekt","Ø Anzahl Falsch")
                initRecyclerView(results)
            }
            else
            {
                Log.e("VocTrainer","VocHomeFragment - startObserver() - rv != null")
                adapter.updateContent(results)
            }

        })


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_voc_statistic, container, false)




        return rootView
    }

    private fun initViews(statusValues:ArrayList<Int>)
    {
        // TextViews etc...
        tvNotLearnedocs = rootView.findViewById(R.id.fragment_voc_statistic_tv_notlearned)
        tvInLearningVocs = rootView.findViewById(R.id.fragment_voc_statistic_tv_inlearning)
        tvLearnedVocs = rootView.findViewById(R.id.fragment_voc_statistic_tv_learned)

        tvNotLearnedocs.text = "${statusValues[0]}"
        tvInLearningVocs.text = "${statusValues[1]}"
        tvLearnedVocs.text = "${statusValues[2]}"

    }

    private fun initRecyclerView(values:ArrayList<Float>)
    {
        rv = rootView.findViewById(R.id.fragment_voc_statistic_rv)
        layoutManager = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapter = VocStatisticRecyclerViewAdapter(values)
        rv!!.layoutManager = layoutManager
        rv!!.adapter = adapter
    }

    private fun initPieChart(statusValues:ArrayList<Int>)
    {
        pieChart = rootView.findViewById(R.id.fragment_voc_statistic_piechart)
        Log.d("VocTrainer","VocStatisticFragment - initPieChart sum = ${statusValues.sum()}")
        Log.d("VocTrainer","VocStatisticFragment - statusValues = ${statusValues}")
        if(statusValues.sum() != 0)
        {
            // Ab hier den Rest...
            // Content:
            Log.d("VocTrainer","VocStatisticFragment - statusValues.sum() != 0")
            var values:ArrayList<PieEntry> = ArrayList()
            values.add(PieEntry(statusValues[0].toFloat(),"Nicht gelernt"))
            values.add(PieEntry(statusValues[1].toFloat(),"Im Lernprozess"))
            values.add(PieEntry(statusValues[2].toFloat(),"Fertig gelernt"))

            // Einstellungen:
            pieChart.setUsePercentValues(true)
            pieChart.description.isEnabled = false
            pieChart.setExtraOffsets(0f,0f,0f,10f)
            pieChart.setDrawCenterText(true)
            pieChart.setHoleColor(Color.WHITE)
            pieChart.transparentCircleRadius = 61f
            pieChart.setTransparentCircleAlpha(0)
            pieChart.dragDecelerationFrictionCoef = 0.2f
            pieChart.setDrawEntryLabels(false)

            // Legende
            var legend = pieChart.legend
            legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            legend.stackSpace = 10f
            legend.setDrawInside(true)

            // PieDataSet
            var dataSet = PieDataSet(values,"Aufteilung Lernstatus")
            dataSet.sliceSpace = 3f
            dataSet.valueTextColor = Color.WHITE
            dataSet.valueTextSize = 11f
            dataSet.label = ""
            dataSet.valueFormatter = MyPercentFormatter(pieChart)
            var colors = arrayListOf(
                ContextCompat.getColor(rootView.context,R.color.colorPrimaryLight),
                ContextCompat.getColor(rootView.context,R.color.colorPrimary),
                ContextCompat.getColor(rootView.context,R.color.colorPrimaryDark)
            )
            dataSet.colors = colors
            dataSet.setDrawValues(true)

            var data = PieData()
            data.dataSet = dataSet

            pieChart.data = data
            //pieChart.animateY(1000, Easing.EaseInOutCubic)
            pieChart.invalidate()

            // Alle Listener deaktivieren:
            pieChart.setTouchEnabled(false)
            pieChart.isDragDecelerationEnabled = false
        }
        else
        {
            Log.d("VocTrainer","VocStatisticFragment - statusValues.sum() != 0")
            pieChart.setNoDataText("Keine Daten verfügbar")
        }

    }

    private fun initLineChart(testResults: ArrayList<Test>)
    {
        lineChart = rootView.findViewById(R.id.fragment_voc_statistic_linechart)
        if(testResults.size > 1)
        {
            // Ab hier den Rest...
            var entries:ArrayList<Entry> = ArrayList()
            for ((index,i) in testResults.withIndex())
            {
                entries.add(Entry(index.toFloat()+1f,i.result))
            }

            var lineDataSet = LineDataSet(entries,"Test Results")
            var color = ContextCompat.getColor(rootView.context,R.color.colorPrimary)
            lineDataSet.label = "Test Ergebnise"
            lineDataSet.color = color
            //lineDataSet.valueFormatter = MyValueFormatter()
            lineDataSet.setDrawFilled(false)
            lineDataSet.fillColor = color
            lineDataSet.fillAlpha = 50
            lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            lineDataSet.setDrawCircles(true)
            lineDataSet.circleRadius = 3f
            lineDataSet.setCircleColor(color)
            lineDataSet.lineWidth = 3f

            var data = LineData(lineDataSet)
            //data.setValueFormatter(MyValueFormatter())
            data.setDrawValues(false)

            lineChart.data = data
            lineChart.description.isEnabled = false

            // Einstellungen Axis:
            var xAxis = lineChart.xAxis
            xAxis.setDrawAxisLine(true)
            xAxis.setDrawGridLines(true)
            xAxis.setDrawLabels(true)

            xAxis.position = XAxis.XAxisPosition.BOTTOM
//            xAxis.mAxisMaximum = testResults.size.toFloat()
            //xAxis.axisMinimum = 1f

            // Label Settings:

            if(testResults.size > 10)
            {
                xAxis.granularity = 2.0f
                xAxis.isGranularityEnabled = false
            }
            else
            {
                xAxis.labelCount = testResults.size
                xAxis.granularity = 1.0f
                xAxis.isGranularityEnabled = true
            }






            var yAxisLeft = lineChart.axisLeft
            yAxisLeft.setDrawLabels(true)
            yAxisLeft.setDrawGridLines(true)
            yAxisLeft.isEnabled = true
            yAxisLeft.axisMinimum = 0f
            yAxisLeft.axisMaximum = 100f
            yAxisLeft.valueFormatter = MyValueFormatter(lineChart)

            var yAxisRight = lineChart.axisRight
            yAxisRight.setDrawGridLines(false)
            yAxisRight.isEnabled = false

            // Einstellungen Legende:
            // Legende
            var legend = lineChart.legend
            legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            legend.stackSpace = 10f
            legend.setDrawInside(false)


            lineChart.extraBottomOffset = 10f
            lineChart.invalidate()

            // Alle Listener deaktivieren:
            lineChart.setTouchEnabled(false)
            lineChart.isDragDecelerationEnabled = false
        }
        else
        {
            lineChart.setNoDataText("Anzeige erst ab 2 durchgeführten Tests")
        }



    }


}