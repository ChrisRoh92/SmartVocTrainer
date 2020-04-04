package com.example.voctrainer.frontend.voc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R

class VocStatisticRecyclerViewAdapter(var content:ArrayList<String>):
    RecyclerView.Adapter<VocStatisticRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_voc_statistic,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }







    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var tvTitle: TextView = itemView.findViewById(R.id.item_voc_statistic_tv_title)
        var tvSubTitle: TextView = itemView.findViewById(R.id.item_voc_statistic_tv_subtitle)
        var tvValue: TextView = itemView.findViewById(R.id.item_voc_statistic_tv_value)
    }


}