package com.example.voctrainer.moduls.standard.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.voctrainer.R

class DialogStandardAlert(var main:String, var sub:String,var btnMain:String ="ok", var btnSub:String ="Abbrechen"):DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var dialogView: View

    // View Elemente:
    private lateinit var btnOk: Button
    private lateinit var btnAbort: Button
    // TextViews...
    private lateinit var tvMain:TextView
    private lateinit var tvSub:TextView


    // Interface:
    private lateinit var mListener:OnDialogClickListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_standard_alert, container, false)

        // Initialisieren:
        initViewElements()

        return dialogView
    }


    private fun initViewElements()
    {
        // Buttons
        // Init. Buttons:
        btnOk = dialogView.findViewById(R.id.dialog_standard_alert_btn_ok)
        btnAbort = dialogView.findViewById(R.id.dialog_standard_alert_btn_abort)
        btnOk.text = btnMain
        btnAbort.text = btnSub
        // Click_Listener:
        btnOk.setOnClickListener {

            if(mListener!=null)
            {
                mListener.setOnDialogClickListener()
                dismiss()
            }
        }
        btnAbort.setOnClickListener {
            dismiss()
        }

        //TextViews:
        //Init. TextViews
        tvMain = dialogView.findViewById(R.id.dialog_standard_alert_tv_main)
        tvSub = dialogView.findViewById(R.id.dialog_standard_alert_tv_sub)
        // Set Text to TextViews
        tvMain.text = main
        tvSub.text = sub

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


