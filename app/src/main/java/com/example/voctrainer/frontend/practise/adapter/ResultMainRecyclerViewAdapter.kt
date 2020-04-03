package com.example.voctrainer.frontend.practise.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R

class ResultMainRecyclerViewAdapter(var size:Int):
    RecyclerView.Adapter<ResultMainRecyclerViewAdapter.ViewHolder>() {






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result_details,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

    }
}