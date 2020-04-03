package com.example.voctrainer.frontend.voc.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import com.example.voctrainer.R

class DialogImportVocData():DialogFragment()
{

    // Allgemeine Variablen:
    private lateinit var dialogView: View

    // View Elemente:
    private lateinit var btnImport: Button
    private lateinit var btnAbort: Button
    private lateinit var content: FrameLayout   // Klicken zum Import und anschließend dort alles anzeigen



    // Interface:
    private lateinit var mListener: OnDialogClickListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_voc_import_vocdata, container, false)

        // Initialisieren:
        initViewElements()

        return dialogView
    }


    private fun initViewElements()
    {
        btnImport = dialogView.findViewById(R.id.dialog_voc_import_btn_import)
        btnAbort = dialogView.findViewById(R.id.dialog_voc_import_btn_abort)
        content = dialogView.findViewById(R.id.dialog_voc_import_framelayout)


        // Button Click_Listener:
        btnAbort.setOnClickListener {
            dismiss()
        }

        btnImport.setOnClickListener {

            if(mListener!=null)
            {
                mListener.setOnDialogClickListener()
                dismiss()
            }
        }

        // FrameLayout:
        content.setOnClickListener {

        }
    }

    interface OnDialogClickListener
    {
        fun setOnDialogClickListener()
    }

    fun setOnDialogClickListener(mListener:OnDialogClickListener)
    {
        this.mListener = mListener
    }
}