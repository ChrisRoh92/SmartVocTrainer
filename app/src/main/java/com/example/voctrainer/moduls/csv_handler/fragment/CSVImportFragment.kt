package com.example.voctrainer.moduls.csv_handler.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.moduls.csv_handler.CSVImportProcessor
import com.example.voctrainer.moduls.csv_handler.adapter.CSVImportRecyclerViewAdapter
import com.example.voctrainer.moduls.csv_handler.dialog.DialogChangeImportItem
import com.example.voctrainer.moduls.csv_handler.dialog.DialogImportError
import com.example.voctrainer.moduls.csv_handler.dialog.DialogLoading
import com.example.voctrainer.moduls.csv_handler.viewholder.CSVViewModel
import com.example.voctrainer.moduls.main.helper.LocalVoc
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception
import java.util.jar.Manifest



class CSVImportFragment(): Fragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView:View
    private lateinit var viewModel: CSVViewModel

    // Stuff um Daten zu empfangen (Content URI)
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSION_STORAGE = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    // View Elemente:
    /*private lateinit var fbtn:FloatingActionButton*/
    private lateinit var toolbar: Toolbar

    /*// View Elemente vom Vokabelheft:
    private lateinit var etVoc:EditText
    private lateinit var spinner:Spinner
    private lateinit var rbtnGroup:RadioGroup
    private lateinit var rbtnNew:RadioButton
    private lateinit var rbtnOld:RadioButton*/

    // Vokabelliste View Elemente
    // Buttons:
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button
    // RecyclerView
    private lateinit var rv:RecyclerView
    private lateinit var layoutManager:LinearLayoutManager
    private lateinit var adapter: CSVImportRecyclerViewAdapter
    // Other Stuff:
    private val dialog = DialogLoading()

    // BookID:
    private var bookID = -1L


    // Item Interaction:
    private var deleteAction = false
    private var undoAction = false
    private var itemPosition = -1



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_csv_import, container, false)
        // Start ViewModel
        // Warten Dialog:

        initToolBar()
        initVocView()
        initButton()

        viewModel = ViewModelProvider(this).get(CSVViewModel::class.java)
        viewModel.getContentData().observe(viewLifecycleOwner, Observer {
            Log.d("VocTrainer","Aktuelle Zeit viewModel.getContentData().observe = ${System.currentTimeMillis()}")
            adapter.updateContent(it,deleteAction,itemPosition,undoAction)
            deleteAction = false
            undoAction = false
            itemPosition = -1
            rv.viewTreeObserver.addOnGlobalLayoutListener(object:ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    dialog.dismiss()
                    rv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }

            })
        })




        // Check for Permissions:
        if(storagePermission())
        {
            dialog.show(parentFragmentManager,"Waiting")
            loadData()
        }
        else
        {
            ActivityCompat.requestPermissions(
                requireActivity(),
                PERMISSION_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }

        // BookID aus dem Bundle lesen....
        try {
            bookID = requireArguments().getLong("bookID")
            viewModel.setBookID(bookID)
        } catch (e:Exception)
        {
            Log.e("VocTrainer","Failed to load the bookID",e)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                returnToParentFragment()
            }

        })

        return rootView
    }

    private fun loadData()
    {

        try {
            var uri = requireArguments().getString("uri")
            Log.d("VocTrainer","Uri from Argument = $uri")
            viewModel.setNewData(uri!!.toUri())



        } catch (e:Exception)
        {
            e.printStackTrace()
            Log.e("VocTrainer","Fehler beim Auslesen von intent",e)
        }
    }

    private fun storagePermission():Boolean
    {
        var readPermission = ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)
        var writePermission = ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        return (readPermission == PackageManager.PERMISSION_GRANTED && writePermission  == PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE)
        {
            if(storagePermission())
            {
                dialog.show(parentFragmentManager,"Waiting")
                loadData()
            }
            else
            {
                Toast.makeText(requireContext(),"Sie haben die Permission für den Datenzugriff verweigert!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initToolBar()
    {
        toolbar = rootView.findViewById(R.id.fragment_csv_import_toolbar)
        toolbar!!.setNavigationOnClickListener {
            returnToParentFragment()
        }
    }

    private fun initButton()
    {
        btnSave = rootView.findViewById(R.id.fragment_csv_btn_save)
        btnAbort = rootView.findViewById(R.id.fragment_csv_btn_abort)

        btnSave.setOnClickListener {

            if(adapter.vocs.size <= 1000)
            {
                dialog.show(parentFragmentManager,"Loading To DataBase")
                viewModel.saveLocalVocToDataBase()
                viewModel.getImportComplete().observe(viewLifecycleOwner, Observer {
                    dialog.dismiss()
                    returnToParentFragment()
                    Toast.makeText(requireContext(),"Import komplett",Toast.LENGTH_SHORT).show()
                })
            }
            else
            {
                val msg1 = "Du versuchst ${adapter.vocs.size} Vokabel zu importieren"
                val msg2 = "Es können maximal 1000 Elemente importiert werden - Bitte Kürzen"
                var errorDialog = DialogImportError(msg1,msg2)
                errorDialog.show(parentFragmentManager,"Import Error")
                errorDialog.setOnDialogClickListener(object :DialogImportError.OnDialogClickListener{
                    override fun setOnDialogClickListener() {
                        errorDialog.dismiss()
                    }

                })

                errorDialog.setOnDismissDialogClickListener(object:DialogImportError.OnDismissDialogClickListener{
                    override fun setOnDismissDialogClickListener() {
                        errorDialog.dismiss()
                        returnToParentFragment()
                    }

                })


            }


        }
        btnAbort.setOnClickListener { returnToParentFragment() }
    }




    private fun initVocView()
    {
        // RecyclerView:
        rv = rootView.findViewById(R.id.fragment_csv_import_rv)
        layoutManager = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapter = CSVImportRecyclerViewAdapter(ArrayList())
        rv.layoutManager = layoutManager
        rv.adapter = adapter
        rv.setHasFixedSize(true)

        rv.addItemDecoration(DividerItemDecoration(rv.context,layoutManager.orientation))


        // Listener
        adapter.setOnItemClickListener(object :CSVImportRecyclerViewAdapter.OnItemClickListener{
            override fun setOnItemClickListener(voc: LocalVoc, position: Int) {
                var changeItemDialog = DialogChangeImportItem(voc)
                changeItemDialog.show(childFragmentManager,"ChangeItem")
                changeItemDialog.setOnDialogSaveClickListener(object: DialogChangeImportItem.OnDialogSaveClickListener{
                    override fun setOnDialogSaveClickListener(newVoc: LocalVoc) {
                        itemPosition = position
                        viewModel.updateLocalVoc(newVoc,position)
                    }

                })

            }

        })

        // Wenn gelöscht werden soll
        adapter.setOnItemLongClickListener(object:CSVImportRecyclerViewAdapter.OnItemLongClickListener{
            override fun setOnItemLongClickListener(voc: LocalVoc, position: Int) {
                deleteAction = true
                itemPosition = position
                viewModel.deleteLocalVoc(position)
                undoDeleteAction(voc,position)
            }

        })

    }


    // Methode aufrufen, um wieder zurück zu den VocFragments zu gelangen....
    private fun returnToParentFragment()
    {
        val bundle = Bundle()
        bundle.putLong("bookId",bookID!!)
        findNavController().navigate(R.id.action_csvimport_vocfragment,bundle)
    }

    private fun undoDeleteAction(voc:LocalVoc,position:Int)
    {
        // StartSnackbar
        Snackbar.make(rootView,"Rückgangig machen?",Snackbar.LENGTH_LONG)
            .setAction("Ja") {
                undoAction = true
                itemPosition = position
                viewModel.undoDeleteLocalVoc(voc,position)
            }.show()
    }






    // Archiv Stuff:
    /*
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
        /*// TODO() - Liste mit Vokabelheften aus dem ViewModel beladen...
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

        }*/


        // FloatingActionButton:
        fbtn = rootView.findViewById(R.id.fragment_csv_import_fbtn)
        fbtn.setOnClickListener {
            // Prüfen ob alle Eingaben da sind (Welches Vokabelheft usw...)
            // Vokabeln in das ausgesuchte Vokabelheft eintragen
            Snackbar.make(rootView,"Vokabeln wurden importiert...",Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_csvimport_main)
        }



    }
    */





}