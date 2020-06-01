package com.example.voctrainer.moduls.csv_handler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R

class CSVImportRecyclerViewAdapter(var nativeVoc:ArrayList<String>,var foreignVoc:ArrayList<String>):RecyclerView.Adapter<CSVImportRecyclerViewAdapter.ViewHolder>()
{



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_csv_import,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return nativeVoc.size
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvMain.text = nativeVoc[position]

    }



    fun updateContent(newContent:ArrayList<String>)
    {

        nativeVoc = newContent
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var tvMain:TextView = itemView.findViewById(R.id.item_title)
        var tvSub:TextView = itemView.findViewById(R.id.item_subtitle)
        var rbtn:RadioButton = itemView.findViewById(R.id.item_rbtn)

    }



}