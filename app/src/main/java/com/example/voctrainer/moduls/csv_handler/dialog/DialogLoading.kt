package com.example.voctrainer.moduls.csv_handler.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.voctrainer.R

class DialogLoading: DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var dialogView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_csvimport_loading, container, false)

        // Initialisieren:




        return dialogView
    }
}