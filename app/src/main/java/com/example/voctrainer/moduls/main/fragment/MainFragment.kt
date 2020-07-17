package com.example.voctrainer.moduls.main.fragment

import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.LAYER_TYPE_HARDWARE
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

import com.example.voctrainer.R
import com.example.voctrainer.backend.database.entities.Book
import com.example.voctrainer.moduls.main.adapter.MainRecyclerViewAdapter
import com.example.voctrainer.moduls.main.adapter.MainRecyclerViewListAdapter
import com.example.voctrainer.moduls.main.dialogs.DialogImportVoc
import com.example.voctrainer.moduls.main.dialogs.DialogNewVoc
import com.example.voctrainer.moduls.main.viewmodel.MainViewModel
import com.example.voctrainer.moduls.standard.dialogs.DialogStandardAlert
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.random.Random


/*
****** Offene Tasks ******

- TODO() Import von Vokabelheften muss implementiert werden
- TODO() Teilen von Vokabelheften muss implementiert werden
- TODO() Direktes Üben mit bookId starten, muss noch implementiert werden
- TODO() Alle zugehörigen Daten eines Vokabelheftes mitlöschen (Alle Vokabeln, Settings etc.)
* *
*

******************************************************************
 */

class MainFragment : Fragment(), SearchView.OnQueryTextListener {

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

    // Status
    private var adapterAction = 0
    private var deletePosition = 0



    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_main_2, container, false)
        initRecyclerView()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.books.observe(viewLifecycleOwner, Observer { books ->
            adapter.updateContent(ArrayList(books),deletePosition,adapterAction)
            if (adapterAction == 2)
            {
                //rv.smoothScrollToPosition(0)

                rv.post {

                    rv.smoothScrollToPosition(0) }
            }


        })


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
        adapter = MainRecyclerViewAdapter(ArrayList())
        rv.layoutManager = layoutManager
        rv.adapter = adapter
        rv.setHasFixedSize(true)

        //rv.addItemDecoration(DividerItemDecoration(rv.context,layoutManager.orientation))

        //initAdapterListener()

        // Vokabelheft öffnen
        adapter.setOnAdapterShowButtonClick(object:MainRecyclerViewAdapter.OnAdapterShowButtonClick{
            override fun setOnAdapterShowButtonClick(bookId: Long) {
                var bundle = Bundle()
                //Log.d("VocLearner","MainFragment - initAdapterister - adapter.setOnAdapterShowButtonClick: bookdId = $bookId")
                bundle.putLong("bookId",bookId)
                navController.navigate(R.id.action_main_voc,bundle)
            }


        })

        // Vokabelheft löschen
        adapter.setOnAdapterDeleteClick(object:MainRecyclerViewAdapter.OnAdapterDeleteButtonClick{
            override fun setOnAdapterDeleteButtonClick(book: Book,position: Int) {
                // Vokabelheft wird endgültig gelöscht
                // Vorgang kann nicht rückgängig gemacht werden
                var dialog = DialogStandardAlert("Vokabelheft wird endgültig gelöscht!","Vorgang kann nicht rückgängig gemacht werden")
                dialog.show(childFragmentManager,"")
                dialog.setOnDialogClickListener(object:DialogStandardAlert.OnDialogClickListener{
                    override fun setOnDialogClickListener() {
                        viewModel.onDeleteBook(book)
                        adapterAction = 1
                        deletePosition = position
                    }

                })

            }

        })




    }

    /*private fun initAdapterListener()
    {
        // Vokabelheft öffnen
        adapter.setOnAdapterShowButtonClick(object:MainRecyclerViewListAdapter.OnAdapterShowButtonClick{
            override fun setOnAdapterShowButtonClick(bookId: Long) {
                var bundle = Bundle()
                //Log.d("VocLearner","MainFragment - initAdapterister - adapter.setOnAdapterShowButtonClick: bookdId = $bookId")
                bundle.putLong("bookId",bookId)
                navController.navigate(R.id.action_main_voc,bundle)
            }


        })

        // Schnelle Übung starten
        adapter.setOnAdapterPractiseButtonClick(object:MainRecyclerViewListAdapter.OnAdapterPractiseButtonClick{
            override fun setOnAdapterPractiseButtonClick(pos: Int) {
                var bundle = bundleOf("position" to pos)
                navController.navigate(R.id.action_global_nested_practise,bundle)
            }


        })

        // Vokabelheft löschen
        adapter.setOnAdapterDeleteButtonClick(object:MainRecyclerViewListAdapter.OnAdapterDeleteButtonClick{
            override fun setOnAdapterDeleteButtonClick(book: Book) {
                // Vokabelheft wird endgültig gelöscht
                // Vorgang kann nicht rückgängig gemacht werden
                var dialog = DialogStandardAlert("Vokabelheft wird endgültig gelöscht!","Vorgang kann nicht rückgängig gemacht werden")
                dialog.show(childFragmentManager,"")
                dialog.setOnDialogClickListener(object:DialogStandardAlert.OnDialogClickListener{
                    override fun setOnDialogClickListener() {
                        viewModel.onDeleteBook(book)
                        deleteBtnWasPushed = true
                    }

                })

            }

        })

        // Vokabelheft teilen:
        adapter.setOnAdapterShareButtonClick(object:MainRecyclerViewListAdapter.OnAdapterShareButtonClick{
            override fun setOnAdapterShareButtonClick(pos: Int) {
                Toast.makeText(rootView.context,"Vokabelheft wird geteilt...",Toast.LENGTH_SHORT).show()
            }

        })
    }*/



    private fun initToolBar()
    {
        toolbar = rootView.findViewById(R.id.fragment_main_toolbar)
        toolbar.title = "Vokabelhefte"
        toolbar.inflateMenu(R.menu.menu_main_toolbar)
        toolbar.setNavigationOnClickListener {
            navController.navigate(R.id.action_main_settings)
        }

        val searchItem = toolbar.menu.findItem(R.id.menu_main_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        toolbar.setOnMenuItemClickListener {
            Log.d("VocTrainer","MainFragment - toolbar.setOnMenuItemClickListener = ${it.itemId!!}")
            if(it.itemId == R.id.menu_main_import)
            {

                var dialog = DialogImportVoc()
                dialog.show(childFragmentManager,"")
                dialog.setOnDialogClickListener(object:DialogImportVoc.OnDialogClickListener{
                    override fun setOnDialogClickListener()
                    {
                        var intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "*/*"
                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true)

                        val requestCode = 123
                        startActivityForResult(intent,requestCode)
                    }

                })
            }

            true
        }



    }

    // Und hier aus import, das Ganze auch entgegen nehmen...
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            if(data?.type == "text/comma-separated-values")
            {
                var uriPath = data.data!!

                /*val bundle = Bundle()
                bundle.putString("uri",uriPath.toString())
                navController.navigate(R.id.action_global_csv_import, bundle)*/

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
                    viewModel.onAddBook(name)
                    adapterAction = 2

                }

            })
        }
    }

    // From SearchVIew
    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.onFilterBookList(query!!)
        Log.d("VocTrainer","MainFragment - onQueryTextSubmit query = ${query!!}")
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(TextUtils.isEmpty(newText))
        {
            viewModel.onFilterBookList("")
        }
        else
        {
            Log.d("VocTrainer","MainFragment - onQueryTextChange query = ${newText!!}")
            viewModel.onFilterBookList(newText!!)
        }
        return true
    }

}
