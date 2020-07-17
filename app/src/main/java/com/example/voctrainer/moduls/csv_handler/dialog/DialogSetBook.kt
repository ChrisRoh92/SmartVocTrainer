package com.example.voctrainer.moduls.csv_handler.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.moduls.csv_handler.adapter.CsvDialogSetBookRecyclerViewAdapter

class DialogSetBook(var books:ArrayList<String>, var bookID:ArrayList<Long>): DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var dialogView: View
    private lateinit var mListener: OnChooseListener

    // RecyclerView:
    private lateinit var rv:RecyclerView
    private lateinit var adapter:CsvDialogSetBookRecyclerViewAdapter
    private lateinit var layoutManager:LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_csvimport_setbook, container, false)

        // Initialisieren:
        // Initialisieren:
        rv = dialogView.findViewById(R.id.dialog_csvimport_setbook_rv)
        layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        adapter = CsvDialogSetBookRecyclerViewAdapter(books)
        rv.layoutManager = layoutManager
        rv.setHasFixedSize(true)
        rv.adapter = adapter
        rv.addItemDecoration(DividerItemDecoration(requireContext(),layoutManager.orientation))

        adapter.setOnClickListener(object: CsvDialogSetBookRecyclerViewAdapter.OnClickListener{
            override fun setOnClickListener(pos: Int) {
                mListener?.setOnChooseListener(bookID[pos])
                dismiss()
            }

        })



        return dialogView
    }

    // Interface:
    interface OnChooseListener
    {
        fun setOnChooseListener(id:Long)
    }

    fun setOnChooseListener(mListener:OnChooseListener)
    {
        this.mListener = mListener
    }


}