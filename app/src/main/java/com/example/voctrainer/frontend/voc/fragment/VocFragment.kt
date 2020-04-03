package com.example.voctrainer.frontend.voc.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2

import com.example.voctrainer.R
import com.example.voctrainer.frontend.voc.adapter.VocStateAdapter
import com.example.voctrainer.frontend.voc.dialogs.DialogImportVocData
import com.example.voctrainer.frontend.voc.dialogs.DialogNewVocData
import com.example.voctrainer.frontend.voc.viewmodel.VocViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class VocFragment : Fragment() {

   // Allgemeine Variablen:
    private lateinit var rootView:View

    // Stuff für den ViewPager2:
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter:VocStateAdapter
    private lateinit var tabLayout:TabLayout
    private val tabText:ArrayList<String> = arrayListOf("Home","Vokabeln","Üben","Statistik")
    private var currentPos = 0

    // Toolbar:
    private lateinit var toolbar: Toolbar



    private lateinit var viewModel: VocViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_voc, container, false)


        initViewPager2()
        initToolBar()



        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }



    // ViewPager2 initialisieren:
    private fun initViewPager2()
    {
        viewPager2 = rootView.findViewById(R.id.fragment_vocs_viewpager2)
        adapter = VocStateAdapter(this)
        viewPager2.adapter = adapter

        // TabLayout Stuff:
        tabLayout = rootView.findViewById(R.id.fragment_voc_tablayout)
        TabLayoutMediator(tabLayout,viewPager2){tab,position ->
            tab.text = tabText[position]

        }.attach()

        // Current Item:
        viewPager2.setPageTransformer { page, position ->
           if(viewPager2.currentItem != currentPos)
           {

               changeToolBarMenu(currentPos,viewPager2.currentItem)
               currentPos = viewPager2.currentItem
               //Toast.makeText(rootView.context,"Aktuelle Position: $currentPos",Toast.LENGTH_SHORT).show()

           }
        }




    }

    private fun initToolBar()
    {
        toolbar = rootView.findViewById(R.id.fragment_voc_toolbar)
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_voc_main)
        }
        toolbar.setOnMenuItemClickListener {
            if(it.itemId == R.id.menu_voc_import)
            {
                /*var bundle = bundleOf("source" to 1)
                findNavController().navigate(R.id.action_voc_setting,bundle)*/
                var dialog = DialogImportVocData()
                dialog.show(childFragmentManager,"")
                dialog.setOnDialogClickListener(object:DialogImportVocData.OnDialogClickListener{
                    override fun setOnDialogClickListener() {

                    }

                })

            }
            else if(it.itemId == R.id.menu_voc_data_search)
            {
                // Suchen von Stuff
            }
            true
        }


    }

    private fun changeToolBarMenu(oldPos:Int,newPos:Int)
    {
        if(newPos == 1)
        {
            // VocData Menu aufrufen...
            toolbar.menu.clear()
            toolbar.inflateMenu(R.menu.menu_voc_data)
        }
        else if(newPos != 1 && oldPos == 1)
        {
            toolbar.menu.clear()
            toolbar.inflateMenu(R.menu.menu_voc_toolbar)
        }
    }

}
