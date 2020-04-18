package com.example.voctrainer.moduls.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R

class SettingsRecyclerViewAdapter(var titles:ArrayList<String>,var subTitles:ArrayList<String>):
    RecyclerView.Adapter<SettingsRecyclerViewAdapter.ViewHolder>() {

    // Interface:
    private lateinit var mListener:OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_settings_standard,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = titles[position]
        holder.tvSubTitle.text = subTitles[position]
        holder.itemView.setOnClickListener {
            if(mListener!=null)
            {
                mListener.setOnItemClickListener(holder.adapterPosition)
            }
        }
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var tvTitle:TextView = itemView.findViewById(R.id.item_settings_tv_title)
        var tvSubTitle:TextView = itemView.findViewById(R.id.item_settings_tv_subtitle)
    }

    interface OnItemClickListener
    {
        fun setOnItemClickListener(pos:Int)
    }

    fun setOnItemClickListener(mListener:OnItemClickListener)
    {
        this.mListener = mListener
    }



}