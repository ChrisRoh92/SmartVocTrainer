package com.example.voctrainer.frontend.voc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.frontend.voc.adapter.VocStatisticRecyclerViewAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart

class VocStatisticFragment(): Fragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView: View

    // RecyclerView
    private lateinit var rv:RecyclerView
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_voc_statistic, container, false)
        initViews()
        initRecyclerView()
        initPieChart()
        initLineChart()
        return rootView
    }

    private fun initViews()
    {
        // TextViews etc...
        tvNotLearnedocs = rootView.findViewById(R.id.fragment_voc_statistic_tv_notlearned)
        tvInLearningVocs = rootView.findViewById(R.id.fragment_voc_statistic_tv_inlearning)
        tvLearnedVocs = rootView.findViewById(R.id.fragment_voc_statistic_tv_learned)

    }

    private fun initRecyclerView()
    {
        rv = rootView.findViewById(R.id.fragment_voc_statistic_rv)
        layoutManager = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapter = VocStatisticRecyclerViewAdapter(ArrayList(List<String>(10){ index -> "" }))
        rv.layoutManager = layoutManager
        rv.adapter = adapter
    }

    private fun initPieChart()
    {
        pieChart = rootView.findViewById(R.id.fragment_voc_statistic_piechart)
        // Ab hier den Rest...
    }

    private fun initLineChart()
    {
        lineChart = rootView.findViewById(R.id.fragment_voc_statistic_linechart)
        // Ab hier den Rest...
    }


}