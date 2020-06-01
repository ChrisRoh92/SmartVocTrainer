package com.example.voctrainer.moduls.main.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SearchEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.voctrainer.R
import com.example.voctrainer.backend.database.entities.Book
import com.example.voctrainer.moduls.main.adapter.MainRecyclerViewAdapter
import com.example.voctrainer.moduls.main.adapter.MainRecyclerViewListAdapter
import com.example.voctrainer.moduls.main.dialogs.DialogImportVoc
import com.example.voctrainer.moduls.main.dialogs.DialogNewVoc
import com.example.voctrainer.moduls.main.viewmodel.MainViewModel
import com.example.voctrainer.moduls.standard.dialogs.DialogStandardAlert
import com.google.android.material.floatingactionbutton.FloatingActionButton




/*
****** Offene Tasks ******

- TODO() Import von Vokabelheften muss implementiert werden
- TODO() Teilen von Vokabelheften muss implementiert werden
- TODO() Direktes Üben mit bookId starten, muss noch implementiert werden
- TODO() Zwei mal zurück klicken, damit App beendet wird...
******************************************************************
 */

class MainFragment : Fragment(), SearchView.OnQueryTextListener {

    // Allgemeines Stuff:
    private lateinit var rootView:View

    // RecyclerView-Stuff:
    private lateinit var rv:RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter:MainRecyclerViewListAdapter

    // NavController
    private lateinit var navController: NavController

    // Toolbar
    private lateinit var toolbar: Toolbar

    // Fab Button
    private lateinit var fabAddVoc:FloatingActionButton

    // Status
    private var deleteBtnWasPushed = false



    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_main, container, false)
        initRecyclerView()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.books.observe(viewLifecycleOwner, Observer { books ->
            Log.d("VocTrainer","MainFragment Book List = $books")
            adapter.submitList(books)
            adapter.submitList(books) {
                /*layoutManager.smoothScrollToPosition(rv,null,0)*/
                if(!deleteBtnWasPushed)
                {
                    rv.scrollToPosition(0)
                }
                else
                {
                    deleteBtnWasPushed = false
                }

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
        adapter = MainRecyclerViewListAdapter()
        rv.layoutManager = layoutManager
        rv.adapter = adapter


        initAdapterListener()




    }

    private fun initAdapterListener()
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
    }



    private fun initToolBar()
    {
        toolbar = rootView.findViewById(R.id.fragment_main_toolbar)
        toolbar.inflateMenu(R.menu.menu_main_toolbar)
        toolbar.setNavigationOnClickListener {
            var bundle = bundleOf("source" to 0)
            navController.navigate(R.id.action_global_settings,bundle)
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
                var uriPath = data.data!!.path

                val bundle = Bundle()
                bundle.putString("uri",data.data.toString())
                navController.navigate(R.id.action_global_csv_import, bundle)

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

                }

            })
        }
    }

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
