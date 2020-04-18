package com.example.voctrainer.moduls.main.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.voctrainer.R

class DialogNewVoc:DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var dialogView:View

    // View Elemente:
    private lateinit var btnOk:Button
    private lateinit var btnAbort:Button
    private lateinit var etName:EditText

    // Interface:
    private lateinit var mListener:OnDialogClickListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,R.style.FullScreenDialog)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_main_new_voc, container, false)

        // Initialisieren:
        initViewElements()

        return dialogView
    }


    private fun initViewElements()
    {
        btnOk = dialogView.findViewById(R.id.dialog_newvoc_btn_ok)
        btnAbort = dialogView.findViewById(R.id.dialog_newvoc_btn_abort)
        etName = dialogView.findViewById(R.id.dialog_newvoc_et)

        // Click_Listener:
        btnAbort.setOnClickListener {
            dismiss()
        }
        btnOk.setOnClickListener {
            if(etName.text.toString().isNullOrEmpty())
            {
                // Bitten einen Wert einzutragen
                Toast.makeText(dialogView.context,"Bitte einen Namen eintragen",Toast.LENGTH_SHORT).show()
            }
            else
            {
                if(mListener!=null)
                {
                    mListener.setOnDialogClickListener(etName.text.toString())
                    dismiss()
                }
            }
        }

    }

    interface OnDialogClickListener
    {
        fun setOnDialogClickListener(name:String)
    }

    fun setOnDialogClickListener(mListener:OnDialogClickListener)
    {
        this.mListener = mListener
    }
}