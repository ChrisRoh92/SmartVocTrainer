package com.example.voctrainer.moduls.csv_handler.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.voctrainer.R
import com.example.voctrainer.moduls.main.helper.LocalVoc
import com.google.android.material.textfield.TextInputLayout

class DialogChangeImportItem(var localVoc: LocalVoc):DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var dialogView: View

    // TextInputLayout:
    private lateinit var etNative:androidx.appcompat.widget.AppCompatEditText
    private lateinit var etForein:androidx.appcompat.widget.AppCompatEditText
    // ImageButtons:
    private lateinit var btnNative:ImageButton
    private lateinit var btnForeign:ImageButton
    // Buttons:
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button

    // Interface:
    private lateinit var mListener:OnDialogSaveClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_csvimport_change_item, container, false)

        // Initialisieren:
        initViewElements()


        return dialogView
    }

    private fun initViewElements()
    {
        etNative = dialogView.findViewById(R.id.dialog_csv_itemchange_et_native)
        etForein = dialogView.findViewById(R.id.dialog_csv_itemchange_et_foreign)

        etNative.setText(localVoc.native)
        etForein.setText(localVoc.foreign)

        // Init ImageButtons:
        btnNative = dialogView.findViewById(R.id.dialog_csv_itemchange_btn_delete_native)
        btnForeign = dialogView.findViewById(R.id.dialog_csv_itemchange_btn_delete_foreign)

        btnNative.setOnClickListener { deleteContentFromTextInputLayout(0) }
        btnForeign.setOnClickListener { deleteContentFromTextInputLayout(1) }

        // Normal Buttons:
        btnSave = dialogView.findViewById(R.id.dialog_csv_itemchange_btn_save_finish)
        btnAbort = dialogView.findViewById(R.id.dialog_csv_itemchange_btn_abort)

        btnSave.setOnClickListener { startSaveProcedure() }
        btnAbort.setOnClickListener { dismiss() }

    }

    private fun startSaveProcedure()
    {
        if(etNative.text.toString().isNotEmpty() && etForein.text.toString().isNotEmpty())
        {
            val newVoc = LocalVoc(etNative.text.toString(),etForein.text.toString())
            mListener?.setOnDialogSaveClickListener(newVoc)
            dismiss()
        }
        else
        {
            if (etNative.text.toString().isEmpty() && etForein.text.toString().isEmpty())
            {
                Toast.makeText(requireContext(),"Felder dürfen nicht leer bleiben!",Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(etNative.text))
            {
                Toast.makeText(requireContext(),"Muttersprachen Vokabel darf nicht leer sein",Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(requireContext(),"Fremdsprachen Vokabel darf nicht leer sein",Toast.LENGTH_SHORT).show()
            }

        }
    }


    private fun deleteContentFromTextInputLayout(pos:Int)
    {
        if(pos == 0)
        {
            etNative.text!!.clear()
        }
        else
        {
            etForein.text!!.clear()
        }
    }

    interface OnDialogSaveClickListener
    {
        fun setOnDialogSaveClickListener(newVoc:LocalVoc)
    }

    fun setOnDialogSaveClickListener(mListener:OnDialogSaveClickListener)
    {
        this.mListener = mListener
    }
}