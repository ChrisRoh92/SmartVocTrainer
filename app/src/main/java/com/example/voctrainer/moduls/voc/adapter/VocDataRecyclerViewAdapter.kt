package com.example.voctrainer.moduls.voc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.backend.database.entities.Voc
import com.example.voctrainer.moduls.voc.utils.VocDataDiffUtil

class VocDataRecyclerViewAdapter(var content:ArrayList<Voc>):
    RecyclerView.Adapter<VocDataRecyclerViewAdapter.ViewHolder>() {

    // Interface
    private lateinit var mListener:OnItemClickListener
    private lateinit var mLongListener:OnItemLongClickListener

    // ImageList
    private val imageList:ArrayList<Int> = arrayListOf(R.drawable.ic_close_black_24dp,R.drawable.ic_refresh_black_24dp,R.drawable.ic_check_black_24dp)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_voc_datas_content,parent,false)
        return ViewHolder(view,this,mListener,mLongListener)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var voc = content[position]
        holder.tvTitle.text = voc.native2
        holder.tvSubtitle.text = voc.foreign
        holder.tvDate.text = voc.lastPractiseDate
        holder.image.setImageResource(imageList[voc.status])


    }

    // Später mit DiffUtil oder sowas...
    fun updateContent(newContent:ArrayList<Voc>,justContent:Boolean)
    {
        if(!justContent)
        {
            val diffCallback = VocDataDiffUtil(content,newContent)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            content.clear()
            content.addAll(newContent)
            diffResult.dispatchUpdatesTo(this)
        }
        else
        {
            content = newContent
            notifyDataSetChanged()
        }
    }



    class ViewHolder(itemView: View,adapter:VocDataRecyclerViewAdapter,mListener:OnItemClickListener,mLongListener:OnItemLongClickListener) : RecyclerView.ViewHolder(itemView)
    {
        var tvTitle:TextView = itemView.findViewById(R.id.item_voc_content_tv_title)
        var tvSubtitle:TextView = itemView.findViewById(R.id.item_voc_content_tv_subtitle)
        var tvDate:TextView = itemView.findViewById(R.id.item_voc_content_tv_date)
        var image:ImageView = itemView.findViewById(R.id.item_voc_content_image)

        init {
            itemView.setOnClickListener {
                mListener?.setOnItemClickListener(adapter.content[adapterPosition])
            }

            itemView.setOnLongClickListener {
                mLongListener?.setOnItemLongClickListener(adapter.content[adapterPosition])
                true
            }
        }
    }

    interface OnItemClickListener
    {
        fun setOnItemClickListener(voc:Voc)
    }
    fun setOnItemClickListener(mListener:OnItemClickListener)
    {
        this.mListener = mListener
    }


    interface OnItemLongClickListener
    {
        fun setOnItemLongClickListener(voc:Voc)
    }
    fun setOnItemLongClickListener(mLongListener:OnItemLongClickListener)
    {
        this.mLongListener = mLongListener
    }


}