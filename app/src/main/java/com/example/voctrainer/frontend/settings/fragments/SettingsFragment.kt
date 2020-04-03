package com.example.voctrainer.frontend.settings.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar

import androidx.navigation.fragment.findNavController

import com.example.voctrainer.R


class SettingsFragment : Fragment() {

    // Allgemeine Variablen:
    private lateinit var rootView:View
    private var source = 0 // 0 von Main und 1 von Voc

    // Toolbar
    private lateinit var toolbar: Toolbar



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false)
        source = arguments?.getInt("source")!!
        initToolBar()



        return rootView
    }


    private fun initToolBar()
    {
        toolbar = rootView.findViewById(R.id.fragment_settings_toolbar)
        toolbar.setNavigationOnClickListener {
            if(source == 0)
            {
                findNavController().navigate(R.id.action_setting_main)
            }
            else
            {
                findNavController().navigate(R.id.action_setting_voc)
            }


        }

    }


}
