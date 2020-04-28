package com.example.voctrainer.moduls.voc.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.moduls.voc.adapter.VocPractiseResultRecyclerViewAdapter
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModel
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception



/*
****** Offene Tasks ******
- TODO() Es müssen noch Übungseinstellungen definiert werden, die dargestellt werden müssen
- TODO() Sortieren und Filtern von Tests muss noch implementiert werden... (Alles in Dao)
******************************************************************
 */
class VocPractiseFragment: Fragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView: View

    // View Elemente:
    private lateinit var fabStart:FloatingActionButton

    // RecyclerView:
    private lateinit var rvResults:RecyclerView
    private lateinit var managerResult:LinearLayoutManager
    private lateinit var adapterResult:VocPractiseResultRecyclerViewAdapter

    // ViewModel:
    private lateinit var vocViewModel: VocViewModel
    private lateinit var vocViewModelFactory: VocViewModelFactory
    private var bookId:Long? = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bookId = arguments?.getLong("bookId",0)
        try {
            vocViewModelFactory = VocViewModelFactory(bookId!!,activity!!.application)
            vocViewModel = ViewModelProvider(requireParentFragment(),vocViewModelFactory).get(VocViewModel::class.java)
            startObserver()
        } catch (e: Exception)
        {
            e.printStackTrace()

        }



    }

    private fun startObserver()
    {
        vocViewModel.tests.observe(viewLifecycleOwner, Observer { tests ->
            adapterResult.setNewContent(ArrayList(tests))
            Log.e("VocTrainer","$tests")
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_voc_practise, container, false)

        initViews()
        initRecyclerView()
        return rootView
    }

    private fun initViews()
    {
        // FloatingActionButton:
        fabStart = rootView.findViewById(R.id.fragment_voc_practise_fab)
        fabStart.setOnClickListener {
            vocViewModel.onAddNewTest()
            //findNavController().navigate(R.id.action_global_nested_practise)
        }
    }

    private fun initRecyclerView()
    {
        rvResults = rootView.findViewById(R.id.fragment_voc_practise_rv_result)
        managerResult = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapterResult = VocPractiseResultRecyclerViewAdapter(ArrayList())
        rvResults.layoutManager = managerResult
        rvResults.adapter = adapterResult
        adapterResult.setOnItemClickListener(object:VocPractiseResultRecyclerViewAdapter.OnItemClickListener{
            override fun setOnItemClickListener(pos: Int) {
                findNavController().navigate(R.id.action_global_nested_practise)
            }

        })

    }
}