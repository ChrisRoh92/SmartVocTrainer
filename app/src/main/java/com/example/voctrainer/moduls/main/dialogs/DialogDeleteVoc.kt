package com.example.voctrainer.moduls.main.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.voctrainer.R

class DialogDeleteVoc:DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var dialogView: View

    // View Elemente:
    private lateinit var btnDelete: Button
    private lateinit var btnAbort: Button


    // Interface:
    private lateinit var mListener:OnDialogClickListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_main_delete_voc, container, false)

        // Initialisieren:
        initViewElements()

        return dialogView
    }


    private fun initViewElements()
    {
        btnDelete = dialogView.findViewById(R.id.dialog_delete_voc_btn_delete)
        btnAbort = dialogView.findViewById(R.id.dialog_delete_voc_btn_abort)


        // Click_Listener:
        btnAbort.setOnClickListener {
            dismiss()
        }
        btnDelete.setOnClickListener {

                if(mListener!=null)
                {
                    mListener.setOnDialogClickListener()
                    dismiss()
                }
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