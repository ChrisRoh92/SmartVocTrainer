package com.example.voctrainer.moduls.settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.voctrainer.R

class FragmentSettings: Fragment()
{

    // Allgemeine Variablen:
    private lateinit var rootView:View

    // Toolbar:
    private lateinit var toolBar:Toolbar








    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_settings, container, false)

        initToolBar()

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_voc_settings_container,
                PreferenceFragmentSettings()
            )
            .commit()

        return rootView
    }

    private fun initToolBar()
    {
        toolBar = rootView.findViewById(R.id.fragment_settings_toolbar)
        toolBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_settings_main)
        }
    }
}