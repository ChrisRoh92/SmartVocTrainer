package com.example.voctrainer.moduls.practise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.moduls.practise.adapter.ResultMainRecyclerViewAdapter
import com.example.voctrainer.moduls.practise.adapter.ResultVocsRecyclerViewAdapter

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_practise_result, container, false)

        initRecyclerViews()
        initButtons()

        return rootView
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
            adapterVocs = ResultVocsRecyclerViewAdapter(20)
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
            findNavController().navigate(R.id.action_result_voc)
        }

        btnRepeat.setOnClickListener {
            findNavController().navigate(R.id.action_result_practise)
        }

        btnShare.setOnClickListener {
            Toast.makeText(rootView.context,"Result wird geteilt...",Toast.LENGTH_SHORT).show()
        }
    }

}