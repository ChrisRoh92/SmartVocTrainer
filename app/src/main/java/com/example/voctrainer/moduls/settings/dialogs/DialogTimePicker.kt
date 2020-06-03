package com.example.voctrainer.moduls.settings.dialogs

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.voctrainer.R

class DialogTimePicker(var hour:Int, var minute:Int):DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var dialogView: View

    // View Elemente:
    private lateinit var btnOk:Button
    private lateinit var btnAbort:Button

    // TimePicker:
    private lateinit var picker:TimePicker

    // Interface:
    private lateinit var mListener:OnTimePickerDialogClick


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_settings_timepicker, container, false)

        initViews()

        return dialogView
    }



    private fun initViews()
    {
        // Init Buttons
        btnOk = dialogView.findViewById(R.id.dialog_timepicker_btn_ok)
        btnAbort = dialogView.findViewById(R.id.dialog_timepicker_btn_abort)

        // Init Button Listener:
        btnOk.setOnClickListener {
            if(mListener!=null)
            {
                var mHour = 0
                var mMinute = 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mHour = picker.hour
                    mMinute = picker.minute
                }
                else
                {
                    mHour = picker.currentHour
                    mMinute = picker.currentMinute
                }
                mListener.setOnTimePickerDialogClick(mHour,mMinute)
                dismiss()
            }
        }
        btnAbort.setOnClickListener { dismiss() }

        // TimePicker:
        picker = dialogView.findViewById(R.id.dialog_timepicker_picker)
        picker.setIs24HourView(true)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            picker.hour = hour
            picker.minute = minute
        }
        else
        {
            picker.currentHour = hour
            picker.currentMinute = minute
        }
    }


    interface OnTimePickerDialogClick
    {
        fun setOnTimePickerDialogClick(hour:Int,minute:Int)
    }

    fun setOnTimePickerDialogClick(mListener:OnTimePickerDialogClick)
    {
        this.mListener = mListener
    }



}