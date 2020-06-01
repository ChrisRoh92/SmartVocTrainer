package com.example.voctrainer.moduls.csv_handler.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.moduls.csv_handler.CSVImportProcessor
import com.example.voctrainer.moduls.csv_handler.adapter.CSVImportRecyclerViewAdapter
import com.example.voctrainer.moduls.csv_handler.dialog.DialogLoading
import com.example.voctrainer.moduls.csv_handler.viewholder.CSVViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception


/*
Offene Aufgaben:
Content Uri Path als String übergeben bekommenm, sofern das geht?, sonst anders erhalten
ViewModel einrichten für Kontakt zur Datenbank
RecyclerView 

 */
class CSVImportFragment(): Fragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView:View
    private lateinit var viewModel: CSVViewModel

    // Stuff um Daten zu empfangen (Content URI)

    // View Elemente:
    private lateinit var fbtn:FloatingActionButton
    private lateinit var toolbar: Toolbar

    // View Elemente vom Vokabelheft:
    private lateinit var etVoc:EditText
    private lateinit var spinner:Spinner
    private lateinit var rbtnGroup:RadioGroup
    private lateinit var rbtnNew:RadioButton
    private lateinit var rbtnOld:RadioButton

    // Vokabelliste View Elemente
    // RecyclerView
    private lateinit var rv:RecyclerView
    private lateinit var layoutManager:LinearLayoutManager
    private lateinit var adapter: CSVImportRecyclerViewAdapter
    private  var content:ArrayList<String> = ArrayList()
    // Other Stuff:
    private lateinit var switchAll:Switch

    // Aktuellen Intent abrufen:
    private lateinit var currentIntent: Intent


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("VocTrainer","Aktuelle Zeit onActivityResult = ${System.currentTimeMillis()}")
        try {
            Log.d("VocTrainer","Intent onActivityResult = ${data!!.data}")
            viewModel.setNewData(data!!.data!!)

        } catch (e:Exception)
        {
            e.printStackTrace()
            Log.e("VocTrainer","Fehler beim Auslesen von intent")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("VocTrainer","Aktuelle Zeit onActivityCreated = ${System.currentTimeMillis()}")
        try {
            Log.d("VocTrainer","Intent 123 = ${activity!!.intent.data}")
            currentIntent = activity!!.intent


        } catch (e:Exception)
        {
            e.printStackTrace()
            Log.e("VocTrainer","Fehler beim Auslesen von intent")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_csv_import, container, false)
        // Start ViewModel
        // Warten Dialog:
        val dialog = DialogLoading()

        viewModel = ViewModelProvider(this).get(CSVViewModel::class.java)
        viewModel.getContentData().observe(viewLifecycleOwner, Observer {
            Log.d("VocTrainer","Aktuelle Zeit viewModel.getContentData().observe = ${System.currentTimeMillis()}")
            adapter.updateContent(it)
            dialog.dismiss()
        })



        initToolBar()
        // Get Arguments:
        /*var uri = arguments!!.getString("uri")
        try {
            Log.d("VocTrainer","Uri from Argument = ${activity!!.intent.data}")
            viewModel.setNewData(uri!!.toUri())

        } catch (e:Exception)
        {
            e.printStackTrace()
            Log.e("VocTrainer","Fehler beim Auslesen von intent")
        }*/
        initVocView(content)
        initViews()
        // Warten Dialog starten:

        dialog.show(childFragmentManager,"Waiting")
        viewModel.setNewData(activity!!.intent.data!!)



        return rootView
    }

    private fun initToolBar()
    {
        toolbar = rootView.findViewById(R.id.fragment_csv_import_toolbar)
        toolbar!!.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_csvimport_main)
        }
    }


    private fun initViews()
    {
        // Vokabelheft erstellen oder aussuchen:
        etVoc = rootView.findViewById(R.id.fragment_csv_import_et)
        spinner = rootView.findViewById(R.id.fragment_csv_import_spinner)
        rbtnGroup = rootView.findViewById(R.id.fragment_csv_import_rbtn_group)
        rbtnNew = rootView.findViewById(R.id.fragment_csv_import_rbtn_new)
        rbtnOld = rootView.findViewById(R.id.fragment_csv_import_rbtn_old)

        spinner.isEnabled = false
        etVoc.isEnabled = false

        // Testing Listener for rbtnGroup:
        rbtnGroup.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.fragment_csv_import_rbtn_new)
            {
                // Neuer Button ist geklickt
                Snackbar.make(rootView,"Neuer Button wurde ausgewählt",Snackbar.LENGTH_SHORT).show()
                spinner.isEnabled = false
                etVoc.isEnabled = true
            }
            else
            {
                Snackbar.make(rootView,"Alter Button wurde ausgewählt",Snackbar.LENGTH_SHORT).show()
                spinner.isEnabled = true
                etVoc.isEnabled = false
            }
        }

        // Spinner:
        // TODO() - Liste mit Vokabelheften aus dem ViewModel beladen...
        var content = arrayListOf("","Englisch - 01","Englisch - 02","Englisch - 03","Englisch - 04","Englisch - 05",
            "Spanisch - 01","Spanisch - 02","Spanisch - 03","Spanisch - 04","Spanisch - 05")
        var aAdapter = ArrayAdapter<String>(rootView.context,android.R.layout.simple_spinner_dropdown_item,content)
        spinner.adapter = aAdapter
        spinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long)
            {
                Snackbar.make(rootView,"${content[position]} was selected",Snackbar.LENGTH_SHORT).show()
            }

        }


        // FloatingActionButton:
        fbtn = rootView.findViewById(R.id.fragment_csv_import_fbtn)
        fbtn.setOnClickListener {
            // Prüfen ob alle Eingaben da sind (Welches Vokabelheft usw...)
            // Vokabeln in das ausgesuchte Vokabelheft eintragen
            Snackbar.make(rootView,"Vokabeln wurden importiert...",Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_csvimport_main)
        }



    }

    private fun initVocView(content:ArrayList<String>)
    {
        // RecyclerView:
        rv = rootView.findViewById(R.id.fragment_csv_import_rv)
        layoutManager = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapter = CSVImportRecyclerViewAdapter(content, createDummyVoc(false))
        rv.layoutManager = layoutManager
        rv.adapter = adapter

        // Switch Button:
        switchAll = rootView.findViewById(R.id.fragment_csv_import_switch_all)

    }


    private fun createDummyVoc(status:Boolean):ArrayList<String>
    {
        var export:ArrayList<String> = ArrayList()
        for(i in 0..50)
        {
            if(status)
            {
                export.add("Vokabel Foreign - $i")
            }
            else
            {
                export.add("Vokabel Native - $i")
            }

        }

        return export
    }



}