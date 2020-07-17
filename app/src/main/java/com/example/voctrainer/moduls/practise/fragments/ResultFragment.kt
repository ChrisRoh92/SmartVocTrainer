package com.example.voctrainer.moduls.practise.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.backend.database.entities.Test
import com.example.voctrainer.backend.database.entities.Voc
import com.example.voctrainer.moduls.practise.adapter.ResultMainRecyclerViewAdapter
import com.example.voctrainer.moduls.practise.adapter.ResultVocsRecyclerViewAdapter
import com.example.voctrainer.moduls.practise.viewmodel.PractiseViewModel
import com.example.voctrainer.moduls.practise.viewmodel.PractiseViewModelFactory

class ResultFragment(): Fragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView: View

    // Button:
    private lateinit var btnWeiter:Button
    private lateinit var btnRepeat:Button
    private lateinit var btnShare:ImageButton


    // RecyclerView-Stuff
    // RecyclerView für Vokabeln
    private lateinit var rvVocs: RecyclerView
    private lateinit var layoutManagerVocs: LinearLayoutManager
    private lateinit var adapterVocs: ResultVocsRecyclerViewAdapter
    // RecyclerView für Details
    private lateinit var rvDetails:RecyclerView
    private lateinit var layoutManagerDetails: LinearLayoutManager
    private lateinit var adapterDetails:ResultMainRecyclerViewAdapter

    // Values from Arguments:
    private var bookID = 0L
    private var newTestID = 0L
    private var settingsID = 0L

    // ViewModel
    private lateinit var viewModel: PractiseViewModel
    private lateinit var viewModelFactory: PractiseViewModelFactory

    // Test Stuff:
    private var newTest: Test? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_practise_result, container, false)

        // Get Arguments:
        bookID = requireArguments().getLong("bookID",-1L)
        newTestID = requireArguments().getLong("testID",-1L)
        settingsID = requireArguments().getLong("settingsID",-1L)

        viewModelFactory = PractiseViewModelFactory(bookID,settingsID,requireActivity().application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(PractiseViewModel::class.java)

        viewModel.getReadyForResult().observe(viewLifecycleOwner, Observer {
            newTest = it.last()
            viewModel.createTestVocListFromIDs(newTest!!.itemIds)
        })

        viewModel.getNewTestVocList().observe(viewLifecycleOwner, Observer {
            updateVocList(ArrayList(it!!))
        })




        initRecyclerViews()
        initButtons()

        return rootView
    }

    private fun updateVocList(vocs:ArrayList<Voc>)
    {
        var answers = newTest!!.solutions
        var question:ArrayList<String> = ArrayList()
        var solution:ArrayList<String> = ArrayList()
        for(i in vocs)
        {
            question.add(i.native2)
            solution.add(i.foreign)
        }
        adapterVocs.updateContent(question,answers,solution)
    }

    private fun initRecyclerViews()
    {
        // Details initialsieren
        fun initMainRecyclerView()
        {
            rvDetails = rootView.findViewById(R.id.fragment_result_rv_main)
            layoutManagerDetails = LinearLayoutManager(rootView.context, RecyclerView.VERTICAL,false)
            adapterDetails = ResultMainRecyclerViewAdapter(arrayListOf("34","6","85 %","4:15"),
                arrayListOf(0,14,-20,-94))
            rvDetails.layoutManager = layoutManagerDetails
            rvDetails.adapter = adapterDetails
        }

        fun initVocRecyclerView()
        {
            rvVocs = rootView.findViewById(R.id.fragment_result_rv_vocs)
            layoutManagerVocs = LinearLayoutManager(rootView.context, RecyclerView.VERTICAL,false)
            adapterVocs = ResultVocsRecyclerViewAdapter(ArrayList(), ArrayList(),ArrayList())
            rvVocs.layoutManager = layoutManagerVocs
            rvVocs.adapter = adapterVocs
        }

        initMainRecyclerView()
        initVocRecyclerView()
    }

    private fun initButtons()
    {
        btnWeiter = rootView.findViewById(R.id.fragment_result_btn_weiter)
        btnRepeat = rootView.findViewById(R.id.fragment_result_btn_repeat)
        btnShare = rootView.findViewById(R.id.fragment_result_btn_share)

        btnWeiter.setOnClickListener {
            var bundle = Bundle()
            bundle.putLong("bookId",bookID)
            bundle.putInt("posViewPager",1)
            findNavController().navigate(R.id.action_result_voc,bundle)
        }

        btnRepeat.setOnClickListener {

            var bundle = Bundle()
            bundle.putLong("bookID",bookID)
            bundle.putLong("settingsID",settingsID)
            findNavController().navigate(R.id.action_result_practise,bundle)
        }

        btnShare.setOnClickListener {
            Toast.makeText(rootView.context,"Result wird geteilt...",Toast.LENGTH_SHORT).show()
        }
    }

}