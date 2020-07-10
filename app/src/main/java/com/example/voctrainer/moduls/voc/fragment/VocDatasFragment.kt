package com.example.voctrainer.moduls.voc.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.backend.database.entities.Voc
import com.example.voctrainer.moduls.voc.adapter.VocDataRecyclerViewAdapter
import com.example.voctrainer.moduls.voc.dialogs.DialogNewVocData
import com.example.voctrainer.moduls.voc.dialogs.DialogShowVocData
import com.example.voctrainer.moduls.voc.dialogs.DialogSortVocData
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModel
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception



/*
****** Offene Tasks ******
- TODO() Für OnLongClick ist noch keine Lösung gefunden (Vielleicht ein ganz neues Fragment als Notlösung?)
  Soll Verwendung für folgende Features sein:
        - Löschen von einer oder mehreren Vokabeln
        - Vokabeln auswählen für einen Test
        - Status von einer oder mehreren Vokabeln ändern
        - Export von ausgewählten Vokabeln als .csv
        - ...
-TODO() Sortieren ist nicht implementiert
-TODO() Filtern ist nicht implementiert
-TODO() Lernstatus wird noch als Zahl angezeigt....
-TODO() SubTitle zu lang beim DialogShowVocData
-TODO() !!! Import und Export von Vokabeln als .csv !!!
******************************************************************
 */
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
    private lateinit var btnFilter:ImageButton
    private lateinit var btnSort:ImageButton

    // ViewModel:
    private lateinit var vocViewModel:VocViewModel
    private lateinit var vocViewModelFactory: VocViewModelFactory
    private var bookId:Long? = 0

    // Workaround
    private var justContent = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_voc_data, container, false)

        initViews()
        initRecyclerView()





        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bookId = arguments?.getLong("bookId",0)
        try {
            vocViewModelFactory = VocViewModelFactory(bookId!!,requireActivity().application)
            vocViewModel = ViewModelProvider(requireParentFragment(),vocViewModelFactory).get(VocViewModel::class.java)
            startObserver()
        } catch (e:Exception)
        {
            e.printStackTrace()

        }



    }

    private fun startObserver()
    {
        vocViewModel.getVocs().observe(viewLifecycleOwner, Observer { vocs ->
            adapter.updateContent(ArrayList(vocs),justContent)
            rv.scrollToPosition(0)
            justContent = false


        })


    }

    private fun initViews()
    {
        fabAdd = rootView.findViewById(R.id.fragment_voc_datas_fab)
        fabAdd.setOnClickListener {

            var dialog = DialogNewVocData()
            dialog.show(childFragmentManager,"")
            dialog.setOnDialogClickListener(object:DialogNewVocData.OnDialogClickListener{
                override fun setOnDialogClickListener(vocNative: String, vocForeign: String) {
                    vocViewModel.onAddNewVoc(vocNative,vocForeign)
                }

            })
        }

        btnFilter = rootView.findViewById(R.id.fragment_voc_data_btn_filter)
        btnSort = rootView.findViewById(R.id.fragment_voc_data_btn_sort)

        btnFilter.setOnClickListener {  }
        btnSort.setOnClickListener {
            var dialog = DialogSortVocData()
            dialog.show(childFragmentManager,"")
            dialog.setOnDialogClickListener(object:DialogSortVocData.OnDialogClickListener{
                override fun setOnDialogClickListener(pos: Int, direction: Boolean) {
                    Log.d("VocLearner","VocDatasFragment - initViews pos = $pos direction = $direction")
                }

            })

        }
    }


    private fun initRecyclerView()
    {
        rv = rootView.findViewById(R.id.fragment_voc_data_rv)
        layoutManager = LinearLayoutManager(rootView.context, RecyclerView.VERTICAL,false)
        adapter = VocDataRecyclerViewAdapter(ArrayList())
        rv.layoutManager = layoutManager
        rv.adapter = adapter
        rv.setHasFixedSize(true)

        // Anzeigen von einer Vokabeln sowie ggfs. Updaten...
        adapter.setOnItemClickListener(object:VocDataRecyclerViewAdapter.OnItemClickListener{
            override fun setOnItemClickListener(voc: Voc) {
                Log.e("VocDatasFragment","voc = $voc")
                var dialog = DialogShowVocData(voc)
                dialog.show(childFragmentManager,"")
                dialog.setOnDialogClickListener(object:DialogShowVocData.OnDialogClickListener{
                    override fun setOnDialogClickListener(vocNative: String, vocForeign: String) {
                        voc.native2 = vocNative
                        voc.foreign = vocForeign
                        justContent = true
                        vocViewModel.onUpdateVoc(voc)
                    }

                })
            }

        })

        adapter.setOnItemLongClickListener(object:VocDataRecyclerViewAdapter.OnItemLongClickListener{
            override fun setOnItemLongClickListener(voc: Voc) {
                vocViewModel.onDeleteNewVoc(voc)
            }

        })

    }






}