package com.example.voctrainer.moduls.csv_handler.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.voctrainer.R

class DialogImportError(): DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var dialogView: View

    // View Elemente:
    private lateinit var btnNewImport:Button
    private lateinit var btnAbort:Button


    // Interface:
    private lateinit var mListener: OnDialogClickListener
    private lateinit var mDismissListener:OnDismissDialogClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_csvimport_error, container, false)

        // Initialisieren:
        initViewElements()


        return dialogView
    }

    private fun initViewElements()
    {
        btnNewImport = dialogView.findViewById(R.id.dialog_csvimport_error_btn_new)
        btnAbort = dialogView.findViewById(R.id.dialog_csvimport_error_btn_abort)

        // Click Listener:
        btnNewImport.setOnClickListener {
            if(mListener!=null)
            {
                mListener.setOnDialogClickListener()
            }
        }

        btnAbort.setOnClickListener {
            if(mDismissListener!=null)
            {
                mDismissListener.setOnDismissDialogClickListener()
            }
        }
    }



    // Interface:
    interface OnDialogClickListener
    {
        fun setOnDialogClickListener()
    }

    fun setOnDialogClickListener(mListener:OnDialogClickListener)
    {
        this.mListener = mListener
    }

    // Interface zum Abrechen des ImportVorgangs:
    interface OnDismissDialogClickListener
    {
        fun setOnDismissDialogClickListener()
    }

    fun setOnDismissDialogClickListener(mDismissListener:OnDismissDialogClickListener)
    {
        this.mDismissListener = mDismissListener
    }
}