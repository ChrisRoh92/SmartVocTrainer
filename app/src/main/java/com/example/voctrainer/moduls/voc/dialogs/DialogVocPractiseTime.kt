package com.example.voctrainer.moduls.voc.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.voctrainer.R

class DialogVocPractiseTime(var hour:Int = 0,var minute:Int = 0,var second:Int = 0): DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var dialogView: View

    // View Elemente:
    private lateinit var pickerHour:NumberPicker
    private lateinit var pickerMinute:NumberPicker
    private lateinit var pickerSeconds:NumberPicker

    // Buttons
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button

    // Interface:
    private lateinit var mListener:OnTimeClickListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_voc_practise_time, container, false)

        initView()
        initButtons()
        return dialogView
    }

    private fun initView()
    {
        pickerHour = dialogView.findViewById(R.id.dialog_voc_practise_np_hour)
        pickerMinute = dialogView.findViewById(R.id.dialog_voc_practise_np_minute)
        pickerSeconds = dialogView.findViewById(R.id.dialog_voc_practise_np_second)

        pickerHour.wrapSelectorWheel = false;
        pickerMinute.wrapSelectorWheel = false;
        pickerSeconds.wrapSelectorWheel = false;

        pickerHour.maxValue = 99
        pickerMinute.maxValue = 59
        pickerSeconds.maxValue = 59

        pickerHour.minValue = 0
        pickerMinute.minValue = 0
        pickerSeconds.minValue = 0

        pickerHour.value = hour
        pickerMinute.value = minute
        pickerSeconds.value = second



    }


    private fun initButtons()
    {
        // Init Views:
        btnSave = dialogView.findViewById(R.id.dialog_voc_practise_btn_ok)
        btnAbort = dialogView.findViewById(R.id.dialog_voc_practise_btn_abort)

        btnSave.setOnClickListener {
            if((pickerHour.value+pickerMinute.value+pickerSeconds.value) == 0)
            {
                Toast.makeText(requireContext(),"Bitte eine Zeit auswählen",Toast.LENGTH_SHORT).show()
            }
            else
            {
                mListener?.setOnTimeClickListener(pickerHour.value,pickerMinute.value,pickerSeconds.value)
                dismiss()
            }
        }
        btnAbort.setOnClickListener { dismiss() }
    }

    interface OnTimeClickListener
    {
        fun setOnTimeClickListener(hour:Int = 0,minute:Int = 0,second:Int = 0)
    }

    fun setOnTimeClickListener(mListener:OnTimeClickListener)
    {
        this.mListener = mListener
    }

}