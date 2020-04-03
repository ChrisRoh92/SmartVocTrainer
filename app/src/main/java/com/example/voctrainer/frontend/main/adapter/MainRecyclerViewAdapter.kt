package com.example.voctrainer.frontend.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R

class MainRecyclerViewAdapter(var content:Int):
    RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {


    // Interface:
    private lateinit var mShowListener:OnAdapterShowButtonClick
    private lateinit var mPractiseListener:OnAdapterPractiseButtonClick
    private lateinit var mDeleteListener:OnAdapterDeleteButtonClick
    private lateinit var mShareListener:OnAdapterShareButtonClick







    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_voc,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return content
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.btnShow.setOnClickListener {
            if(mShowListener!=null)
            {
                mShowListener.setOnAdapterShowButtonClick(holder.adapterPosition)
            }
        }

        holder.btnPractise.setOnClickListener {
            if(mPractiseListener!=null)
            {
                mPractiseListener.setOnAdapterPractiseButtonClick(holder.adapterPosition)
            }
        }

        holder.btnDelete.setOnClickListener {
            if(mDeleteListener!=null)
            {
                mDeleteListener.setOnAdapterDeleteButtonClick(holder.adapterPosition)
            }
        }

        holder.btnShare.setOnClickListener {
            if(mShareListener!=null)
            {
                mShareListener.setOnAdapterShareButtonClick(holder.adapterPosition)
            }
        }
    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        // Alle Buttons
        var btnShow: Button = itemView.findViewById(R.id.item_voc_btn_show)
        var btnPractise:Button = itemView.findViewById(R.id.item_voc_btn_practise)
        var btnDelete:Button = itemView.findViewById(R.id.item_voc_btn_delete)
        var btnShare:ImageButton = itemView.findViewById(R.id.item_voc_btn_share)
        // TextViews:
        var tvTitle: TextView = itemView.findViewById(R.id.item_voc_tv_title)
        var tvSubTitle: TextView = itemView.findViewById(R.id.item_voc_tv_subtitle)
        var tvProgress: TextView = itemView.findViewById(R.id.item_voc_tv_progress)
        var tvVocsOpen: TextView = itemView.findViewById(R.id.item_voc_tv_voc_offen)
        var tvVocs: TextView = itemView.findViewById(R.id.item_voc_tv_voc_anzahl)
        var tvVocsLearned: TextView = itemView.findViewById(R.id.item_voc_tv_voc_gelernt)
        // SeekBar
        var pbProgress:ProgressBar = itemView.findViewById(R.id.item_voc_sk)


    }

    // Interface 1 - Öffnen
    interface OnAdapterShowButtonClick
    {
        fun setOnAdapterShowButtonClick(pos:Int)
    }

    fun setOnAdapterShowButtonClick(mShowListener:OnAdapterShowButtonClick)
    {
        this.mShowListener = mShowListener
    }

    // Interface 2 - Schnelle Übung
    interface OnAdapterPractiseButtonClick
    {
        fun setOnAdapterPractiseButtonClick(pos:Int)
    }

    fun setOnAdapterPractiseButtonClick(mPractiseListener:OnAdapterPractiseButtonClick)
    {
        this.mPractiseListener = mPractiseListener
    }

    // Interface 3 - Delete
    interface OnAdapterDeleteButtonClick
    {
        fun setOnAdapterDeleteButtonClick(pos:Int)
    }

    fun setOnAdapterDeleteButtonClick(mDeleteListener:OnAdapterDeleteButtonClick)
    {
        this.mDeleteListener = mDeleteListener
    }

    // Interface 4 - Share
    interface OnAdapterShareButtonClick
    {
        fun setOnAdapterShareButtonClick(pos:Int)
    }

    fun setOnAdapterShareButtonClick(mShareListener:OnAdapterShareButtonClick)
    {
        this.mShareListener = mShareListener
    }
}