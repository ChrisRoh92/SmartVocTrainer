package com.example.voctrainer.moduls.voc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.moduls.voc.adapter.VocPractiseResultRecyclerViewAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VocPractiseFragment: Fragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView: View

    // View Elemente:
    private lateinit var fabStart:FloatingActionButton

    // RecyclerView:
    private lateinit var rvResults:RecyclerView
    private lateinit var managerResult:LinearLayoutManager
    private lateinit var adapterResult:VocPractiseResultRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_voc_practise, container, false)

        initViews()
        initRecyclerView()
        return rootView
    }

    private fun initViews()
    {
        // FloatingActionButton:
        fabStart = rootView.findViewById(R.id.fragment_voc_practise_fab)
        fabStart.setOnClickListener { findNavController().navigate(R.id.action_global_nested_practise) }
    }

    private fun initRecyclerView()
    {
        rvResults = rootView.findViewById(R.id.fragment_voc_practise_rv_result)
        managerResult = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapterResult = VocPractiseResultRecyclerViewAdapter(arrayListOf("","","","","","","","","","","","","","","","","","","","","","","","","","",""))
        rvResults.layoutManager = managerResult
        rvResults.adapter = adapterResult
        adapterResult.setOnItemClickListener(object:VocPractiseResultRecyclerViewAdapter.OnItemClickListener{
            override fun setOnItemClickListener(pos: Int) {
                findNavController().navigate(R.id.action_global_nested_practise)
            }

        })

    }
}