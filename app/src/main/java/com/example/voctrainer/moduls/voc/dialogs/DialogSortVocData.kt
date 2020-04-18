package com.example.voctrainer.moduls.voc.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.moduls.voc.adapter.VocDataSortRecyclerViewAdapter

class DialogSortVocData():DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var dialogView: View
    // Beispieldaten...
    private var direction = false
    private var position = 0

    // View Elemente:
    private lateinit var btnChange:ImageButton
    private lateinit var btnOk: Button
    private lateinit var btnAbort: Button

    // RecyclerView
    private lateinit var rv:RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: VocDataSortRecyclerViewAdapter
    private lateinit var content: FrameLayout   // Klicken zum Import und anschließend dort alles anzeigen



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
        initRecyclerView()
        initViewElements()

        return dialogView
    }


    private fun initViewElements()
    {
        btnChange = dialogView.findViewById(R.id.dialog_voc_datas_sorting_btn_change)
        btnOk = dialogView.findViewById(R.id.dialog_voc_datas_sorting_btn_ok)
        btnAbort = dialogView.findViewById(R.id.dialog_voc_datas_sorting_btn_abort)

        // Click Listener
        btnChange.setOnClickListener {
            adapter.setDirectionOfSorting(!direction)
            direction = !direction
        }
        btnOk.setOnClickListener {
            if(mListener!=null)
            {
                mListener.setOnDialogClickListener(adapter.initPos,direction)
                dismiss()
            }
        }
        btnAbort.setOnClickListener {
            dismiss()
        }
    }

    private fun initRecyclerView()
    {
        rv = dialogView.findViewById(R.id.dialog_voc_datas_sorting_rv)
        layoutManager = LinearLayoutManager(dialogView.context,RecyclerView.VERTICAL,false)
        adapter = VocDataSortRecyclerViewAdapter(arrayListOf("Alphabetisch","Lernzustand","Anzahl Tage letztes mal Geübt","Datum Eingetragen"),
            arrayListOf("A-Z","Nicht gelernt - Gelernt","Aufsteigend","Aufsteigend"),position)
        rv.layoutManager = layoutManager
        rv.adapter = adapter
        adapter.setOnItemClickListener(object:VocDataSortRecyclerViewAdapter.OnItemClickListener{
            override fun setOnItemClickListener(pos: Int) {
                position = pos
            }

        })
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