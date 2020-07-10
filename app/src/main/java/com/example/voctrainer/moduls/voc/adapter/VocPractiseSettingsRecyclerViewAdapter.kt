package com.example.voctrainer.moduls.voc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R

class VocPractiseSettingsRecyclerViewAdapter(var statusTime:Boolean = false, var statusBewertung:Boolean = false):RecyclerView.Adapter<VocPractiseSettingsRecyclerViewAdapter.ViewHolder>() {


    // Data:
    private var titles:Array<String> = arrayOf(
        "Anzahl Vokabeln",
        "Mit Zeit Beschränkung",
        "Zeit Beschränkung",
        "Übungsstatus Vokabeln",
        "Bewertung am Ende")

    private var subTitles:Array<String> = arrayOf("Wie viele Vokabeln sollen abgefragt werden?",
        "Zeitbeschränkung aktivieren",
        "Zeit für Beschränkung festlegen",
        "Welche Vokabeln möchtest du üben?",
        "Eingaben werden erst am Schluss bewertet")

    // Interface:
    private lateinit var mListener:OnItemClickListener


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_voc_practise_setting,parent,false)
        return ViewHolder(view,mListener)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun onBindViewHolder(holder: ViewHolder,position: Int)
    {
        holder.tvTitle.text = titles[position]
        holder.tvSubTitle.text = subTitles[position]
        if(position == 1)
        {
            holder.switch.visibility = View.VISIBLE
            holder.switch.isChecked = statusTime
        }
        else if (position == 4)
        {
            holder.switch.visibility = View.VISIBLE
            holder.switch.isChecked = statusBewertung
        }
    }

    class ViewHolder(itemView: View,mListener:OnItemClickListener):RecyclerView.ViewHolder(itemView)
    {
        var tvTitle:TextView = itemView.findViewById(R.id.item_tv_title)
        var tvSubTitle:TextView = itemView.findViewById(R.id.item_tv_subtitle)
        var switch: Switch = itemView.findViewById(R.id.item_switch)

        init {
            itemView.setOnClickListener {
                if(adapterPosition == 1 || adapterPosition == 4)
                    switch.isChecked = !switch.isChecked
                mListener?.setOnItemClickListener(adapterPosition)
            }
            switch.setOnCheckedChangeListener { buttonView, isChecked ->
                mListener?.setOnItemClickListener(adapterPosition)
            }
        }

    }

    interface OnItemClickListener
    {
        fun setOnItemClickListener(position: Int)
    }

    fun setOnItemClickListener(mListener:OnItemClickListener)
    {
        this.mListener = mListener
    }
}