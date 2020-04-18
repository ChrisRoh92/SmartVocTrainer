package com.example.voctrainer.moduls.voc.dialogs

import android.app.Notification
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.DialogFragment
import com.example.voctrainer.R
import com.example.voctrainer.moduls.standard.dialogs.DialogStandardAlert
import com.google.android.material.snackbar.Snackbar

class DialogNewVocData: DialogFragment(), View.OnClickListener {

    // Allgemeine Variablen:
    private lateinit var dialogView: View
    private var dialogFragment = this

    // View Elemente:
    // Buttons:
    private lateinit var btnSaveNext:Button
    private lateinit var btnSaveFinish:Button
    private lateinit var btnAbort:Button
    // ImageButton:
    private lateinit var btnDeleteNative:ImageButton
    private lateinit var btnDeleteForeign:ImageButton
    // EditText:
    private lateinit var etNative:EditText
    private lateinit var etForeign:EditText

    // Interface:
    private lateinit var mListener:OnDialogClickListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_voc_datas_new_vocs, container, false)

        // Initialisieren:
        initViewElements()



        return dialogView
    }


    private fun initViewElements()
    {
        // Buttons:
        btnSaveNext = dialogView.findViewById(R.id.dialog_voc_newvocdata_btn_save_next)
        btnSaveFinish = dialogView.findViewById(R.id.dialog_voc_newvocdata_btn_save_finish)
        btnAbort = dialogView.findViewById(R.id.dialog_voc_newvocdata_btn_abort)
        // ImageButton:
        btnDeleteNative = dialogView.findViewById(R.id.dialog_voc_newvocdata_btn_delete_native)
        btnDeleteForeign = dialogView.findViewById(R.id.dialog_voc_newvocdata_btn_delete_foreign)
        // Button Click Listeners:
        btnSaveNext.setOnClickListener(this)
        btnSaveFinish.setOnClickListener(this)
        btnAbort.setOnClickListener(this)
        btnDeleteNative.setOnClickListener(this)
        btnDeleteForeign.setOnClickListener(this)


        // EditTexts:
        etNative = dialogView.findViewById(R.id.dialog_voc_newvocdata_et_native)
        etForeign = dialogView.findViewById(R.id.dialog_voc_newvocdata_et_foreign)

    }

    private fun saveData(finish:Boolean)
    {
        if(finish)
        {
            if(mListener!=null && checkForContent())
            {
                mListener.setOnDialogClickListener(etNative.text.toString(),etForeign.text.toString())
                clearEditText(etNative)
                clearEditText(etForeign)
                dismiss()
            }
            else if(!checkForContent())
            {
                //Toast.makeText(dialogView.context,"Bitte beide Felder ausfüllen!",Toast.LENGTH_SHORT).show()
                Snackbar.make(dialogView,"Bitte beide Felder ausfüllen!",Snackbar.LENGTH_SHORT).show()

            }



        }
        else
        {
            if(mListener!=null && checkForContent())
            {
                mListener.setOnDialogClickListener(etNative.text.toString(),etForeign.text.toString())
                clearEditText(etNative)
                clearEditText(etForeign)
            }
            else if(!checkForContent())
            {
                Snackbar.make(dialogView,"Bitte beide Felder ausfüllen!",Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun dismissDialog()
    {
        var dialog = DialogStandardAlert("Möchtest du die Eingabe wirklich beenden?","Etwaige Eingaben gehen verloren!","Ja","Nein")
        dialog.show(childFragmentManager,"")
        dialog.setOnDialogClickListener(object:DialogStandardAlert.OnDialogClickListener{
            override fun setOnDialogClickListener() {
                dismiss()
            }

        })

    }

    private fun clearEditText(et:EditText)
    {
        if(!et.text.isNullOrEmpty())
        {
            et.text.clear()
        }

    }


    private fun checkForContent():Boolean
    {
        return (!etNative.text.isNullOrEmpty() && !etForeign.text.isNullOrEmpty())
    }



    interface OnDialogClickListener
    {
        fun setOnDialogClickListener(vocNative:String,vocForeign:String)
    }

    fun setOnDialogClickListener(mListener:OnDialogClickListener)
    {
        this.mListener = mListener
    }

    override fun onClick(v: View?) {
        when(v)
        {
            btnSaveNext -> saveData(false)
            btnSaveFinish -> saveData(true)
            btnAbort -> dismissDialog()
            btnDeleteNative -> clearEditText(etNative)
            btnDeleteForeign -> clearEditText(etForeign)
        }

    }
}