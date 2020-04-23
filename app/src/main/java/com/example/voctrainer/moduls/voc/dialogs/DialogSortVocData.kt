package com.example.voctrainer.moduls.voc.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.example.voctrainer.R

class DialogSortVocData():DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var dialogView: View
    // Beispieldaten...


    // View Elemente:
    private lateinit var btnChange:ImageButton
    private lateinit var btnOk: Button
    private lateinit var btnAbort: Button

    //RadioButton
    private lateinit var rbtnGroup:RadioGroup
    private var rbtnList:ArrayList<RadioButton> = ArrayList()
    private val rbtnIDs:ArrayList<Int> = arrayListOf(
        R.id.dialog_voc_datas_sorting_rbtn_alphabet,
        R.id.dialog_voc_datas_sorting_rbtn_learning_status,
        R.id.dialog_voc_datas_sorting_rbtn_daycount,
        R.id.dialog_voc_datas_sorting_rbtn_date_insert
    )
    private var direction = true
    private var position = 0





    // Interface:
    private lateinit var mListener: OnDialogClickListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_voc_datas_sort, container, false)

        // Initialisieren:
        initRadioButtons()
        initViewElements()

        return dialogView
    }


    private fun initRadioButtons()
    {
        rbtnGroup = dialogView.findViewById(R.id.dialog_voc_datas_sorting_radiogroup)
        for(i in rbtnIDs)
        {
            rbtnList.add(dialogView.findViewById(i))
        }
        rbtnList[0].isChecked = true
        setRadioTexts()
    }

    private fun setRadioTexts()
    {
        if(direction)
        {
            // Aufsteigend
            rbtnList[0].text = "Alphabetisch (A-Z)"
            rbtnList[1].text = "Lernzustand (Nicht gelernt -> Gelernt)"
            rbtnList[2].text = "Anzahl Tage letztes mal geübt (Aufsteigend)"
            rbtnList[3].text = "Datum Eingetragen (Aufsteigend)"
        }
        else
        {
            rbtnList[0].text = "Alphabetisch (Z-A)"
            rbtnList[1].text = "Lernzustand (Gelernt -> Nicht gelernt)"
            rbtnList[2].text = "Anzahl Tage letztes mal geübt (Absteigend)"
            rbtnList[3].text = "Datum Eingetragen (Absteigend)"
        }
    }

    private fun initViewElements()
    {
        btnChange = dialogView.findViewById(R.id.dialog_voc_datas_sorting_btn_change)
        btnOk = dialogView.findViewById(R.id.dialog_voc_datas_sorting_btn_ok)
        btnAbort = dialogView.findViewById(R.id.dialog_voc_datas_sorting_btn_abort)

        // Click Listener
        btnChange.setOnClickListener {

            direction = !direction
            setRadioTexts()
        }
        btnOk.setOnClickListener {
            if(mListener!=null)
            {
                position = rbtnGroup.indexOfChild(dialogView.findViewById(rbtnGroup.checkedRadioButtonId))
                mListener.setOnDialogClickListener(position,direction)
                dismiss()
            }
        }
        btnAbort.setOnClickListener {
            dismiss()
        }
    }



    interface OnDialogClickListener
    {
        fun setOnDialogClickListener(pos:Int,direction:Boolean)
    }

    fun setOnDialogClickListener(mListener:OnDialogClickListener)
    {
        this.mListener = mListener
    }
}