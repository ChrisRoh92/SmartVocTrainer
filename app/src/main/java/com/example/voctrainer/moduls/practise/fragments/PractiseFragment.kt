package com.example.voctrainer.moduls.practise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.voctrainer.R
import com.example.voctrainer.moduls.standard.dialogs.DialogStandardAlert

class PractiseFragment():Fragment()
{

    // Allgemeine Variablen:
    private lateinit var rootView: View

    // View Elemente:
    // Buttons:
    private lateinit var btnOk:Button
    private lateinit var btnAbort:Button
    private lateinit var btnNext:ImageButton
    private lateinit var btnBack:ImageButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_practise, container, false)
        initButtons()
        return rootView
    }


    private fun initButtons()
    {
        btnOk = rootView.findViewById(R.id.fragment_practise_btn_ok)
        btnAbort = rootView.findViewById(R.id.fragment_practise_btn_abort)
        btnNext = rootView.findViewById(R.id.fragment_practise_btn_next)
        btnBack = rootView.findViewById(R.id.fragment_practise_btn_back)

        // Listener:
        btnOk.setOnClickListener { Toast.makeText(rootView.context,"Wird bewertet...",Toast.LENGTH_SHORT).show() }
        btnAbort.setOnClickListener {
            var dialog = DialogStandardAlert("Möchten Sie den Versuch wirklich abbrechen?","Dieser Vorgang kann nicht rückgängig gemacht werden")
            dialog.show(childFragmentManager,"")
            dialog.setOnDialogClickListener(object:DialogStandardAlert.OnDialogClickListener{
                override fun setOnDialogClickListener() {
                    findNavController().navigate(R.id.action_practise_result)
                }

            })
        }
        btnNext.setOnClickListener {  }
        btnBack.setOnClickListener {  }
    }

}