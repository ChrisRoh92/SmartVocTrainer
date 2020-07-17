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
import com.example.voctrainer.MainActivity
import com.example.voctrainer.R
import com.example.voctrainer.moduls.csv_handler.CSVImportProcessor
import com.example.voctrainer.moduls.csv_handler.adapter.CSVImportRecyclerViewAdapter
import com.example.voctrainer.moduls.csv_handler.dialog.DialogChangeImportItem
import com.example.voctrainer.moduls.csv_handler.dialog.DialogImportError
import com.example.voctrainer.moduls.csv_handler.dialog.DialogLoading
import com.example.voctrainer.moduls.csv_handler.dialog.DialogSetBook
import com.example.voctrainer.moduls.csv_handler.viewholder.CSVViewModel
import com.example.voctrainer.moduls.main.helper.LocalVoc
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception
import java.util.jar.Manifest



class CSVImportFragment(): Fragment()
{
    // Allgemeine Variablen:
    private val TAG = "VocTrainer"
    private lateinit var rootView:View
    private lateinit var viewModel: CSVViewModel

    // Stuff um Daten zu empfangen (Content URI)
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSION_STORAGE = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    // View Elemente:
    /*private lateinit var fbtn:FloatingActionButton*/
    private lateinit var toolbar: Toolbar


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
    private var allBookIDs:ArrayList<Long> = ArrayList()
    private var allBooks:ArrayList<String> = ArrayList()


    // Item Interaction:
    private var deleteAction = false
    private var undoAction = false
    private var itemPosition = -1



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_csv_import, container, false)


        initToolBar()
        initVocView()
        initButton()
        initViewModelWithObservers()


        // Check for Permissions for Storage!
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


        // Handle the OnBackButtonPress!
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                returnToParentFragment()
            }

        })

        return rootView
    }

    // Init the ViewModel and register Observers!
    private fun initViewModelWithObservers()
    {
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
        viewModel.getBooks().observe(viewLifecycleOwner, Observer { books ->
            for(i in books)
            {
                allBookIDs.add(i.id)
                allBooks.add(i.name)
            }
        })
    }

    // Init the Toolbar with functions!
    private fun initToolBar()
    {
        toolbar = rootView.findViewById(R.id.fragment_csv_import_toolbar)
        toolbar!!.setNavigationOnClickListener {
            returnToParentFragment()
        }
    }

    // Init the Buttons
    private fun initButton()
    {
        btnSave = rootView.findViewById(R.id.fragment_csv_btn_save)
        btnAbort = rootView.findViewById(R.id.fragment_csv_btn_abort)

        btnSave.setOnClickListener {
            // Check if the size of the imported list, is to big
            if(adapter.vocs.size <= 1000)
            {
                dialog.show(parentFragmentManager,"Loading To DataBase")
                // Check if Class holds the id of the book
                Log.d(TAG,"BookID = $bookID")
                if(bookID == -1L)
                {
                    dialog.dismiss()
                    val dialogSetBook = DialogSetBook(allBooks,allBookIDs)
                    dialogSetBook.show(childFragmentManager,"Set the Book ID")
                    dialogSetBook.setOnChooseListener(object:DialogSetBook.OnChooseListener{
                        override fun setOnChooseListener(id: Long) {
                            viewModel.setBookID(id)
                            viewModel.saveLocalVocToDataBase()
                            viewModel.getImportComplete().observe(viewLifecycleOwner, Observer {
                                dialog.dismiss()
                                returnToParentFragment()
                                Toast.makeText(requireContext(),"Import komplett",Toast.LENGTH_SHORT).show()
                            })
                        }

                    })
                }
                else
                {
                    viewModel.saveLocalVocToDataBase()
                    viewModel.getImportComplete().observe(viewLifecycleOwner, Observer {
                        dialog.dismiss()
                        returnToParentFragment()
                        Toast.makeText(requireContext(),"Import komplett",Toast.LENGTH_SHORT).show()
                    })
                }

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
        btnAbort.setOnClickListener {
            val dialogSetBook = DialogSetBook(allBooks,allBookIDs)
            dialogSetBook.show(childFragmentManager,"Set the Book ID")
            //returnToParentFragment()
            }
    }

    // Init the View to display and manipulate the vocs:
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

    // Try to Load the Data (From Arguments):
    private fun loadData()
    {

        try {
            var uri = requireArguments().getString("uri")
            Log.d("VocTrainer","Uri from Argument = $uri")
            viewModel.setNewData(uri!!.toUri())
            bookID = requireArguments().getLong("bookID",-1)
            viewModel.setBookID(bookID)
        } catch (e:Exception)
        {
            e.printStackTrace()
            Log.e("VocTrainer","Fehler beim Auslesen von intent",e)
        }
    }

    // Check for StoragePermission!
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



    // Methode aufrufen, um wieder zurück zu den VocFragments zu gelangen....
    private fun returnToParentFragment()
    {
        val backStack = childFragmentManager.backStackEntryCount
        if(backStack == 0)
        {

            findNavController().navigateUp()

        }
        else
        {
            val bundle = Bundle()
            bundle.putLong("bookId",bookID!!)
            findNavController().navigate(R.id.action_csvimport_vocfragment,bundle)
        }




    }

    // Undo the Delete Action
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



}