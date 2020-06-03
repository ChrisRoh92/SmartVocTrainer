package com.example.voctrainer.moduls.voc.dialogs

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.voctrainer.R

class DialogVocPractiseItemCount(var itemCount:Int): DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var dialogView: View


    // View Elemente:
    private lateinit var rbtnGroup:RadioGroup
    private lateinit var etItemCount:EditText
    private lateinit var divider:View
    private lateinit var tvEt:TextView

    // Buttons:
    private lateinit var btnOk: Button
    private lateinit var btnAbort: Button

    // Interface:
    private lateinit var mListener:OnItemCountClickListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_voc_practise_itemcount, container, false)
        initViews()
        initButtons()
        return dialogView
    }

    private fun initViews()
    {
        // Visible Stuff gone, not gone etc....
        etItemCount = dialogView.findViewById(R.id.dialog_voc_practise_et_itemcount)
        divider = dialogView.findViewById(R.id.dialog_voc_practise_divider)
        tvEt = dialogView.findViewById(R.id.dialog_voc_practise_tv_et_itemcount)

        fun setVisibilty(status:Boolean)
        {
            if(etItemCount.visibility == View.VISIBLE && !status)
            {
                if(etItemCount.visibility != View.GONE)
                {
                    etItemCount.visibility = View.GONE
                    divider.visibility = View.GONE
                    tvEt.visibility = View.GONE
                }

            }
            else if(etItemCount.visibility == View.GONE && status )
            {
                etItemCount.visibility = View.VISIBLE
                divider.visibility = View.VISIBLE
                tvEt.visibility = View.VISIBLE
            }

        }

        rbtnGroup = dialogView.findViewById(R.id.dialog_rbtn_group)
        rbtnGroup.setOnCheckedChangeListener { group, checkedId ->
            setVisibilty(checkedId == R.id.dialog_voc_practise_rbtn_custom)

        }

        rbtnGroup.check(getIdByItemCount(itemCount))

    }

    // Wird aufgerufen, um die richtige Menge auszuwählen
    private fun getItemCount(id:Int):Int
    {
        when(id) {
            R.id.dialog_voc_practise_rbtn_10 -> return 10
            R.id.dialog_voc_practise_rbtn_20 -> return 20
            R.id.dialog_voc_practise_rbtn_30 -> return 30
            R.id.dialog_voc_practise_rbtn_50 -> return 50
            R.id.dialog_voc_practise_rbtn_all -> return -1
            else -> {
                if(etItemCount.text.toString().isNotEmpty())
                {
                    return etItemCount.text.toString().toInt()
                }
                else
                {
                    return 0
                }


            }
        }
    }

    // Wird am Anfang aufgerufen, damit direkt der richtige RadioButton ausgewählt ist
    private fun getIdByItemCount(itemCount:Int):Int
    {
        return when(itemCount) {
            10 -> R.id.dialog_voc_practise_rbtn_10
            20 -> R.id.dialog_voc_practise_rbtn_20
            30 -> R.id.dialog_voc_practise_rbtn_30
            50 -> R.id.dialog_voc_practise_rbtn_50
            -1 -> R.id.dialog_voc_practise_rbtn_all
            else -> {
                R.id.dialog_voc_practise_rbtn_custom
            }
        }
    }

    // Buttons initialisieren und Listener einführen
    private fun initButtons()
    {
        btnOk = dialogView.findViewById(R.id.dialog_voc_practise_btn_ok)
        btnAbort = dialogView.findViewById(R.id.dialog_voc_practise_btn_abort)

        // Listener:
        btnOk.setOnClickListener {
            // Wenn
            var itemCount = getItemCount(rbtnGroup.checkedRadioButtonId)
            if(itemCount != 0)
            {
                mListener?.setOnItemCountClickListener(itemCount)
                dismiss()
            }
            else
            {
                Toast.makeText(requireContext(),"Bitte eine Eingabe tätigen bzw. eine Zahl größer 0 wählen!", Toast.LENGTH_SHORT).show()
            }

        }

        btnAbort.setOnClickListener { dismiss() }

    }


    // Interface
    interface OnItemCountClickListener
    {
        fun setOnItemCountClickListener(itemCount:Int)
    }

    fun setOnItemCountClickListener(mListener:OnItemCountClickListener)
    {
        this.mListener = mListener
    }

}