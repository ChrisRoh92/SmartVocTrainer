package com.example.voctrainer.moduls.csv_handler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R

class CsvDialogSetBookRecyclerViewAdapter(var content:ArrayList<String>):RecyclerView.Adapter<CsvDialogSetBookRecyclerViewAdapter.ViewHolder>()
{

    // Interface:
    private lateinit var mListener:OnClickListener


    // Implemented Methods:
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_simple_1,parent,false)
        return ViewHolder(view,mListener)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = content[position]
        holder.tv.text = item
    }




    class ViewHolder(itemView: View, mListener:OnClickListener):RecyclerView.ViewHolder(itemView)
    {
        var tv:TextView = itemView.findViewById(R.id.item_tv)

        init {
            itemView.setOnClickListener {
                mListener?.setOnClickListener(adapterPosition)
            }
        }
    }

    interface OnClickListener
    {
        fun setOnClickListener(pos:Int)
    }

    fun setOnClickListener(mListener:OnClickListener)
    {
        this.mListener = mListener
    }


}