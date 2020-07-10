package com.example.voctrainer.moduls.csv_handler.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.moduls.main.helper.LocalVoc

class CSVImportRecyclerViewAdapter(var vocs:ArrayList<LocalVoc>):RecyclerView.Adapter<CSVImportRecyclerViewAdapter.ViewHolder>()
{

    // Interface:
    private lateinit var mClickListener:OnItemClickListener
    private lateinit var mLongClickListener:OnItemLongClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_csv_import,parent,false)
        return ViewHolder(view,mClickListener,mLongClickListener,vocs)
    }

    override fun getItemCount(): Int {
        return vocs.size
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voc = vocs[position]
        holder.tvMain.text = voc.native
        holder.tvSub.text = voc.foreign


    }



    fun updateContent(newContent:ArrayList<LocalVoc>,deleteAction:Boolean = false,position: Int = -1, undoAction:Boolean = false)
    {

        vocs = newContent
        if(deleteAction && position != -1)
        {
            notifyItemRemoved(position)
        }
        else if(undoAction && position != -1)
        {
            Log.d("VocTrainer","Undo Action wurde ausgeführt (CSVImportRecyclerViewAdapter) @Positon $position")
            notifyItemInserted(position)
        }
        else if(position != -1)
        {
            notifyItemChanged(position)
        }
        else
        {
            notifyDataSetChanged()
        }



    }


    class ViewHolder(itemView: View,
                     mClickListener: OnItemClickListener,
                     mLongClickListener: OnItemLongClickListener,
                     content:ArrayList<LocalVoc>
    ):RecyclerView.ViewHolder(itemView)
    {
        var tvMain:TextView = itemView.findViewById(R.id.item_title)
        var tvSub:TextView = itemView.findViewById(R.id.item_subtitle)

        init {
            itemView.setOnClickListener { mClickListener?.setOnItemClickListener(content[adapterPosition],adapterPosition) }
            itemView.setOnLongClickListener {
                mLongClickListener?.setOnItemLongClickListener(content[adapterPosition],adapterPosition)
                true}
        }
    }

    // Interface für den normalen Klick:
    interface OnItemClickListener
    {
        fun setOnItemClickListener(voc:LocalVoc,position: Int)
    }

    fun setOnItemClickListener(mClickListener:OnItemClickListener)
    {
        this.mClickListener = mClickListener
    }

    // Interface für den langen Klick:
    interface OnItemLongClickListener
    {
        fun setOnItemLongClickListener(voc:LocalVoc,position: Int)
    }

    fun setOnItemLongClickListener(mLongClickListener:OnItemLongClickListener)
    {
        this.mLongClickListener = mLongClickListener
    }



}