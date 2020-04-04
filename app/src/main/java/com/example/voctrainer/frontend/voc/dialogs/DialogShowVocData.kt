package com.example.voctrainer.frontend.voc.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.voctrainer.R
import com.example.voctrainer.frontend.standard.dialogs.DialogStandardAlert

class DialogShowVocData(var nativ:String,var foreign:String,var status:String, var date:String):DialogFragment(), View.OnClickListener {

    // Allgemeine Variablen:
    private lateinit var dialogView: View

    // View Elemente:
    // Buttons:
    private lateinit var btnSaveFinish:Button
    private lateinit var btnAbort:Button
    // ImageButton:
    private lateinit var btnDeleteNative: ImageButton
    private lateinit var btnDeleteForeign: ImageButton
    // EditText:
    private lateinit var etNative: EditText
    private lateinit var etForeign: EditText

    // TextView:
    private lateinit var tvStatus:TextView
    private lateinit var tvDate:TextView

    // Interface:
    private lateinit var mListener:OnDialogClickListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_voc_datas_show_vocdata, container, false)

        // Initialisieren:
        initViewElements()

        return dialogView
    }


    private fun initViewElements()
    {
        // Buttons:
        btnSaveFinish = dialogView.findViewById(R.id.dialog_voc_showvocdata_btn_save_finish)
        btnAbort = dialogView.findViewById(R.id.dialog_voc_showvocdata_btn_abort)
        // ImageButton:
        btnDeleteNative = dialogView.findViewById(R.id.dialog_voc_showvocdata_btn_delete_native)
        btnDeleteForeign = dialogView.findViewById(R.id.dialog_voc_showvocdata_btn_delete_foreign)
        // Button Click Listeners:
        btnSaveFinish.setOnClickListener(this)
        btnAbort.setOnClickListener(this)
        btnDeleteNative.setOnClickListener(this)
        btnDeleteForeign.setOnClickListener(this)


        // EditTexts:
        etNative = dialogView.findViewById(R.id.dialog_voc_showvocdata_et_native)
        etForeign = dialogView.findViewById(R.id.dialog_voc_showvocdata_et_foreign)

        // TextViews:
        tvStatus = dialogView.findViewById(R.id.dialog_voc_showvocdata_tv_status)
        tvDate = dialogView.findViewById(R.id.dialog_voc_showvocdata_tv_date)

        // Set Values:
        etNative.setText(nativ)
        etForeign.setText(foreign)
        tvStatus.text = status
        tvDate.text = date

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
                Toast.makeText(dialogView.context,"Bitte beide Felder ausfüllen!", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(dialogView.context,"Bitte beide Felder ausfüllen!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dismissDialog()
    {
        var dialog = DialogStandardAlert("Möchtest du die Eingabe wirklich beenden?","Etwaige Eingaben gehen verloren!","Ja","Nein")
        dialog.show(childFragmentManager,"")
        dialog.setOnDialogClickListener(object: DialogStandardAlert.OnDialogClickListener{
            override fun setOnDialogClickListener() {
                dismiss()
            }

        })
    }

    private fun clearEditText(et: EditText)
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
            btnSaveFinish -> saveData(true)
            btnAbort -> dismissDialog()
            btnDeleteNative -> clearEditText(etNative)
            btnDeleteForeign -> clearEditText(etForeign)
        }

    }
}