package com.example.voctrainer.moduls.default_elements.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R

class Simple2RecyclerViewAdapter(var main:ArrayList<String>,var sub:ArrayList<String>):RecyclerView.Adapter<Simple2RecyclerViewAdapter.ViewHolder>()
{

    // Interface:
    private lateinit var mListener: OnItemClickListener
    private lateinit var mLongListener: OnItemLongClickListener



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_simple_2,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return main.size


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvMain.text = main[position]
        holder.tvSub.text = sub[position]
        holder.itemView.setOnClickListener { mListener?.setOnItemClickListener(holder.adapterPosition) }
    }


    fun updateSubContent(sub:ArrayList<String>)
    {
        this.sub = sub
        notifyDataSetChanged()
    }

    fun updateMainContent(main:ArrayList<String>)
    {
        this.main = main
        notifyDataSetChanged()
    }



    inner class ViewHolder(
        itemView:View) : RecyclerView.ViewHolder(itemView)
    {
        var tvMain:TextView = itemView.findViewById(R.id.item_tv_main)
        var tvSub:TextView = itemView.findViewById(R.id.item_tv_sub)

        init {
            tvMain.textSize = 18f
        }




    }


    // Interface:
    interface OnItemClickListener
    {
        fun setOnItemClickListener(pos:Int)
    }

    fun setOnItemClickListener(mListener: OnItemClickListener)
    {
        this.mListener = mListener
    }

    interface OnItemLongClickListener
    {
        fun setOnItemLongClickListener(pos:Int)
    }

    fun setOnItemLongClickListener(mLongListener: OnItemLongClickListener)
    {
        this.mLongListener = mLongListener
    }




}