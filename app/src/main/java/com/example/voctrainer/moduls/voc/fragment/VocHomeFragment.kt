package com.example.voctrainer.moduls.voc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.voctrainer.R

class VocHomeFragment: Fragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_voc_home, container, false)

        return rootView
    }
}