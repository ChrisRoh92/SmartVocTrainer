package com.example.voctrainer.moduls.voc.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R

class VocDataSortRecyclerViewAdapter(var titles:ArrayList<String>,var subTitles:ArrayList<String>,var initPos:Int):
    RecyclerView.Adapter<VocDataSortRecyclerViewAdapter.ViewHolder>() {

    // Interface:
    private lateinit var mListener:OnItemClickListener
    private var checked:ArrayList<Boolean> = ArrayList()


    init {
        for((index,i) in titles.withIndex())
        {
            checked.add(index == initPos)
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_voc_datas_sorting,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitles.text = titles[position]
        holder.tvSubTitles.text = subTitles[position]
        holder.itemView.setOnClickListener {
            if(mListener!=null)
            {
                mListener.setOnItemClickListener(holder.adapterPosition)
                setSwitchOnPosition(holder.adapterPosition)

            }

        }
        holder.switch.isChecked = checked[position]
        holder.switch.setOnClickListener {
            mListener.setOnItemClickListener(holder.adapterPosition)
            setSwitchOnPosition(holder.adapterPosition)
        }
    }

    private fun setSwitchOnPosition(pos:Int)
    {
        for(i in 0.until(checked.size))
        {
            checked[i] = (i == pos)

        }
        initPos = pos
        notifyDataSetChanged()
    }

    fun setDirectionOfSorting(direction:Boolean)
    {
        if(direction)
        {
            subTitles = arrayListOf("A-Z","Nicht gelernt - Gelernt","Aufsteigend","Aufsteigend")
        }
        else
        {
            subTitles = arrayListOf("Z-A","Gelernt - Nicht gelernt","Absteigend","Absteigend")
        }
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var tvTitles:TextView = itemView.findViewById(R.id.item_voc_datas_sorting_tv_title)
        var tvSubTitles:TextView = itemView.findViewById(R.id.item_voc_datas_sorting_tv_subtitle)
        var switch:Switch = itemView.findViewById(R.id.item_voc_datas_sorting_switch)
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