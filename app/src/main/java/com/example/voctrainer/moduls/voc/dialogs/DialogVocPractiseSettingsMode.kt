package com.example.voctrainer.moduls.voc.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.example.voctrainer.R

class DialogVocPractiseSettingsMode(var settingsMode:Int = 0): DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var dialogView: View

    // View Elemente:
    private lateinit var rbtnGroup:RadioGroup

    // Buttons:
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button

    // Array with RadioButton Id:
    private val rbtnID:Array<Int> = arrayOf(
        R.id.dialog_voc_practise_rbtn_1,
        R.id.dialog_voc_practise_rbtn_2,
        R.id.dialog_voc_practise_rbtn_3,
        R.id.dialog_voc_practise_rbtn_4)

    // Interface:
    private lateinit var mListener:OnSettingsModeClickListener




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_voc_practise_settingsmode, container, false)

        initViews()
        initButtons()


        return dialogView
    }

    private fun initViews()
    {
        rbtnGroup = dialogView.findViewById(R.id.dialog_voc_practise_rbtn_group)
        rbtnGroup.check(rbtnID[settingsMode])

    }

    private fun initButtons()
    {
        btnSave = dialogView.findViewById(R.id.dialog_voc_practise_btn_ok)
        btnAbort = dialogView.findViewById(R.id.dialog_voc_practise_btn_abort)

        btnSave.setOnClickListener {
            mListener?.setOnSettingsModeClickListener(rbtnID.indexOf(rbtnGroup.checkedRadioButtonId))
            dismiss()
        }
        btnAbort.setOnClickListener { dismiss() }
    }

    interface OnSettingsModeClickListener
    {
        fun setOnSettingsModeClickListener(settingsMode:Int = 0)
    }

    fun setOnSettingsModeClickListener(mListener:OnSettingsModeClickListener)
    {
        this.mListener = mListener
    }


}