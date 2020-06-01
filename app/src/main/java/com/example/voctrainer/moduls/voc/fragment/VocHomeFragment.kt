package com.example.voctrainer.moduls.voc.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.moduls.voc.adapter.VocHomeRecyclerViewAdapter
import com.example.voctrainer.moduls.voc.utils.TestResults
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModel
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModelFactory
import java.lang.Exception
import kotlin.math.roundToInt


/*
****** Offene Tasks ******
- TODO() Problem mit Darstellung der Veränderung vom letzten Test (Es gab den Fall Infinity
- TODO() Restliche Daten für Statusanzeige aus ViewModel erhalten und über Observer in die TextViews eintragen
- TODO() Datum vom letzten Tag erhalten und eintragen in 2. CardView
******************************************************************
 */

class VocHomeFragment: Fragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView: View

    // Recyclerview Stuff:
    private var rv:RecyclerView? = null
    private lateinit var adapter:VocHomeRecyclerViewAdapter
    private lateinit var layoutManager:LinearLayoutManager

    // StatusView Objects:
    private lateinit var pbProgress:ProgressBar
    private lateinit var tvProgress:TextView
    // Untere Anzeigen...
    private lateinit var tvQuote:TextView
    private lateinit var tvItemCorrect:TextView
    private lateinit var tvItemCount:TextView

    // ViewModel:
    private lateinit var vocViewModel:VocViewModel
    private lateinit var vocViewModelFactory: VocViewModelFactory
    private var bookId:Long? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_voc_home, container, false)

        initStatusViews()

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bookId = arguments?.getLong("bookId",0)
        try {
            vocViewModelFactory = VocViewModelFactory(bookId!!,activity!!.application)
            vocViewModel = ViewModelProvider(requireParentFragment(),vocViewModelFactory).get(VocViewModel::class.java)
            startObserver()
        } catch (e: Exception)
        {
            e.printStackTrace()

        }



    }

    private fun initTextViews(content:ArrayList<Float>)
    {
        // Untere Anzeigen:
        tvQuote = rootView.findViewById(R.id.fragment_voc_home_tv_quote)
        tvItemCorrect = rootView.findViewById(R.id.fragment_voc_home_tv_itemcorrect)
        tvItemCount = rootView.findViewById(R.id.fragment_voc_home_tv_itemcount)

        tvQuote.text = "%.2f ".format(content[0])+"%"
        tvItemCorrect.text = "%.2f".format(content[1])
        tvItemCount.text = "%.2f".format(content[2])
    }

    private fun initStatusViews()
    {
        // Progress:
        pbProgress = rootView.findViewById(R.id.fragment_voc_home_pb_main)
        tvProgress = rootView.findViewById(R.id.fragment_voc_home_tv_progress)



    }




    private fun startObserver()
    {
        vocViewModel.lastTwoTest.observe(viewLifecycleOwner, Observer {

            val testResult = vocViewModel.createTestResult(it)

            if(rv == null)
            {
                initRecyclerView(testResult)
            }
            else
            {
                Log.e("VocTrainer","VocHomeFragment - startObserver() - rv != null")
                adapter.updateContent(testResult)
            }
        })

        vocViewModel.tests.observe(viewLifecycleOwner,Observer{ tests ->
            val testStatistic = vocViewModel.createTestStatistics(tests)
            initTextViews(arrayListOf(testStatistic.result,testStatistic.itemCorrect,testStatistic.itemCount))
        })

        vocViewModel.book.observe(viewLifecycleOwner, Observer { book ->
            // ProgressBar + Text dazu aktualisieren...
            // Hier brauche den Fortschritt:
            var progress = 0f
            if(book.vocLearned > 0 && book.vocLearned > 0)
            {
                progress = (book.vocLearned.toFloat()/book.vocCount)*100
            }
            pbProgress.progress = progress.roundToInt()
            tvProgress.text = "${progress.roundToInt()} %"
        })
    }

    private fun initRecyclerView(testResult:TestResults)
    {
        rv = rootView.findViewById(R.id.fragment_voc_home_rv)
        layoutManager = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapter = VocHomeRecyclerViewAdapter(testResult)
        rv?.layoutManager = layoutManager
        rv?.setHasFixedSize(true)
        rv?.adapter = adapter
    }


}