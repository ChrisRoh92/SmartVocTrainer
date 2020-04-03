package com.example.voctrainer.frontend.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.voctrainer.R
import com.example.voctrainer.frontend.main.adapter.MainRecyclerViewAdapter
import com.example.voctrainer.frontend.main.dialogs.DialogDeleteVoc
import com.example.voctrainer.frontend.main.dialogs.DialogImportVoc
import com.example.voctrainer.frontend.main.dialogs.DialogNewVoc
import com.example.voctrainer.frontend.main.viewmodel.MainViewModel
import com.example.voctrainer.frontend.standard.dialogs.DialogStandardAlert
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment() {

    // Allgemeines Stuff:
    private lateinit var rootView:View

    // RecyclerView-Stuff:
    private lateinit var rv:RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter:MainRecyclerViewAdapter

    // NavController
    private lateinit var navController: NavController

    // Toolbar
    private lateinit var toolbar: Toolbar

    // Fab Button
    private lateinit var fabAddVoc:FloatingActionButton



    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_main, container, false)
        initRecyclerView()

        navController = findNavController()
        initToolBar()

        initFab()


        return rootView
    }




    // Create RecyclerView
    private fun initRecyclerView()
    {
        rv = rootView.findViewById(R.id.main_rv)
        layoutManager = LinearLayoutManager(rootView.context, RecyclerView.VERTICAL,false)
        adapter = MainRecyclerViewAdapter(7)
        rv.layoutManager = layoutManager
        rv.adapter = adapter

        // Vokabelheft öffnen
        adapter.setOnAdapterShowButtonClick(object:MainRecyclerViewAdapter.OnAdapterShowButtonClick{
            override fun setOnAdapterShowButtonClick(pos: Int) {
                var bundle = bundleOf("position" to pos)
                navController.navigate(R.id.vocFragment,bundle)
            }

        })

        // Schnelle Übung starten
        adapter.setOnAdapterPractiseButtonClick(object:MainRecyclerViewAdapter.OnAdapterPractiseButtonClick{
            override fun setOnAdapterPractiseButtonClick(pos: Int) {
                var bundle = bundleOf("position" to pos)
                navController.navigate(R.id.action_main_practise,bundle)
            }


        })

        // Vokabelheft löschen
        adapter.setOnAdapterDeleteButtonClick(object:MainRecyclerViewAdapter.OnAdapterDeleteButtonClick{
            override fun setOnAdapterDeleteButtonClick(pos: Int) {
                // Vokabelheft wird endgültig gelöscht
                // Vorgang kann nicht rückgängig gemacht werden
                var dialog = DialogStandardAlert("Vokabelheft wird endgültig gelöscht!","Vorgang kann nicht rückgängig gemacht werden")
                dialog.show(childFragmentManager,"")
                dialog.setOnDialogClickListener(object:DialogStandardAlert.OnDialogClickListener{
                    override fun setOnDialogClickListener() {

                    }

                })
            }

        })

        // Vokabelheft teilen:
        adapter.setOnAdapterShareButtonClick(object:MainRecyclerViewAdapter.OnAdapterShareButtonClick{
            override fun setOnAdapterShareButtonClick(pos: Int) {
                Toast.makeText(rootView.context,"Vokabelheft wird geteilt...",Toast.LENGTH_SHORT).show()
            }

        })
    }



    private fun initToolBar()
    {
        toolbar = rootView.findViewById(R.id.fragment_main_toolbar)
        toolbar.setNavigationOnClickListener {
            var bundle = bundleOf("source" to 0)
            navController.navigate(R.id.action_main_setting,bundle)
        }
        toolbar.setOnMenuItemClickListener {
            if(it.itemId == R.id.menu_main_import)
            {
                var dialog = DialogImportVoc()
                dialog.show(childFragmentManager,"")
                dialog.setOnDialogClickListener(object:DialogImportVoc.OnDialogClickListener{
                    override fun setOnDialogClickListener() {
                        Toast.makeText(rootView.context,"Import erfolgreich",Toast.LENGTH_SHORT).show()
                    }

                })
            }
            else if(it.itemId == R.id.menu_main_search)
            {

            }
            true
        }

    }

    private fun initFab()
    {
        fabAddVoc = rootView.findViewById(R.id.main_fab)
        fabAddVoc.setOnClickListener {
            var dialog = DialogNewVoc()
            dialog.show(childFragmentManager,"")
            dialog.setOnDialogClickListener(object:DialogNewVoc.OnDialogClickListener{
                override fun setOnDialogClickListener(name: String) {

                }

            })
        }
    }

}
