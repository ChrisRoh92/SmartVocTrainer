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
import java.lang.Exception

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
        Log.e("VocDatasFragment","bookId = $bookId")
        Log.e("VocDatasFragment","application = ${activity!!.application}")
        try {
            vocViewModelFactory = VocViewModelFactory(bookId!!,activity!!.application)
            vocViewModel = ViewModelProvider(this,vocViewModelFactory).get(VocViewModel::class.java)
            startObserver()
        } catch (e:Exception)
        {
            e.printStackTrace()

        }



    }

    private fun startObserver()
    {
        vocViewModel.vocs.observe(viewLifecycleOwner, Observer { vocs ->
            adapter.updateContent(ArrayList(vocs))
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
                    Log.e("VocDatasFragment","vocNative = $vocNative")
                    Log.e("VocDatasFragment","vocForeign = $vocForeign")
                    vocViewModel.onAddNewVoc("vocNative","vocForeign")
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
                        vocViewModel.onUpdateVoc(voc)
                    }

                })
            }

        })

    }






}