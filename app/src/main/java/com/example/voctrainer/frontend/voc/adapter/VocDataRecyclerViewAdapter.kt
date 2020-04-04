package com.example.voctrainer.frontend.voc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R

class VocDataRecyclerViewAdapter(var content:ArrayList<String>):
    RecyclerView.Adapter<VocDataRecyclerViewAdapter.ViewHolder>() {



    // Interface
    private lateinit var mListener:OnItemClickListener



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_voc_datas_content,parent,false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            if(mListener!=null)
            {
                mListener.setOnItemClickListener(holder.adapterPosition)
            }
        }

    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvTitle:TextView = itemView.findViewById(R.id.item_voc_content_tv_title)
        var tvSubtitle:TextView = itemView.findViewById(R.id.item_voc_content_tv_title)
        var tvDate:TextView = itemView.findViewById(R.id.item_voc_content_tv_title)
        var image:ImageView = itemView.findViewById(R.id.item_voc_content_image)
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