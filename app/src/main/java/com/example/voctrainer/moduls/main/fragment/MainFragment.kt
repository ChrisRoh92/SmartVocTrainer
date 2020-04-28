package com.example.voctrainer.moduls.main.fragment

import android.os.Bundle
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
- TODO() Suche nach Vokabelheften ist nicht implementiert....
- TODO() Import von Vokabelheften muss implementiert werden
- TODO() Teilen von Vokabelheften muss implementiert werden
- TODO() Direktes Üben mit bookId starten, muss noch implementiert werden
- TODO() Zwei mal zurück klicken, damit App beendet wird...
******************************************************************
 */

class MainFragment : Fragment() {

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
        viewModel.books!!.observe(viewLifecycleOwner, Observer { books ->
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
        toolbar.setNavigationOnClickListener {
            var bundle = bundleOf("source" to 0)
            navController.navigate(R.id.action_global_settings,bundle)
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
                    viewModel.onAddBook(name)

                }

            })
        }
    }

}
