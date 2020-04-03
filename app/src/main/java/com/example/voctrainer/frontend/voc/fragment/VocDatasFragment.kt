package com.example.voctrainer.frontend.voc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.frontend.main.adapter.MainRecyclerViewAdapter
import com.example.voctrainer.frontend.voc.adapter.VocDataRecyclerViewAdapter

class VocDatasFragment: Fragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView: View

    // RecyclerView-Stuff:
    private lateinit var rv: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: VocDataRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_voc_data, container, false)
        Toast.makeText(rootView.context,"Position Nr: ${arguments?.getInt("position")}", Toast.LENGTH_SHORT).show()
        initFragments()
        return rootView
    }

    private fun initFragments()
    {
        rv = rootView.findViewById(R.id.fragment_voc_data_rv)
        layoutManager = LinearLayoutManager(rootView.context, RecyclerView.VERTICAL,false)
        adapter = VocDataRecyclerViewAdapter(arrayListOf("","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""))
        rv.layoutManager = layoutManager
        rv.adapter = adapter

    }






}