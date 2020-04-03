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
import com.example.voctrainer.frontend.voc.dialogs.DialogNewVocData
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VocDatasFragment: Fragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView: View

    // RecyclerView-Stuff:
    private lateinit var rv: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: VocDataRecyclerViewAdapter

    // View Elemente:
    private lateinit var fabAdd:FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_voc_data, container, false)
        initViews()
        initFragments()
        return rootView
    }


    private fun initViews()
    {
        fabAdd = rootView.findViewById(R.id.fragment_voc_datas_fab)
        fabAdd.setOnClickListener {
            var dialog = DialogNewVocData()
            dialog.show(childFragmentManager,"")
            dialog.setOnDialogClickListener(object:DialogNewVocData.OnDialogClickListener{
                override fun setOnDialogClickListener(vocNative: String, vocForeign: String) {

                }

            })
        }
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