package com.example.voctrainer.moduls.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.backend.database.entities.Book
import kotlin.math.roundToInt

class MainRecyclerViewListAdapter:
    androidx.recyclerview.widget.ListAdapter<Book, MainRecyclerViewListAdapter.ViewHolder>(DIFF_CALLBACK)
{

    // Interface:
    private lateinit var mShowListener:OnAdapterShowButtonClick
    private lateinit var mPractiseListener:OnAdapterPractiseButtonClick
    private lateinit var mDeleteListener:OnAdapterDeleteButtonClick
    private lateinit var mShareListener:OnAdapterShareButtonClick


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_2,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = getItem(position)
        val progress = book.vocLearned


        holder.tvTitle.text = book.name
        holder.tvSubTitle.text = "Erstellt am: ${book.timeStamp}"

        // Progress:
        holder.tvProgress.text = "$progress %"

        holder.tvVocs.text = "${book.vocCount}"
        holder.tvVocsOpen.text = "${book.vocUnLearned}"
        holder.tvVocsLearned.text = "${book.vocLearned}"

        holder.itemView.setOnClickListener { mShowListener?.setOnAdapterShowButtonClick(getItem(holder.adapterPosition).id) }
        holder.itemView.setOnLongClickListener {
            mDeleteListener?.setOnAdapterDeleteButtonClick(getItem(holder.adapterPosition))
            true
        }







    }














    class ViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView)
    {
        // Alle Buttons
        // TextViews:
        var tvTitle: TextView = itemView.findViewById(R.id.item_tv_title)
        var tvSubTitle: TextView = itemView.findViewById(R.id.item_tv_subtitle)
        var tvProgress: TextView = itemView.findViewById(R.id.item_tv_progress)
        var tvVocsOpen: TextView = itemView.findViewById(R.id.item_tv_vocs_nlearned)
        var tvVocs: TextView = itemView.findViewById(R.id.item_tv_vocs)
        var tvVocsLearned: TextView = itemView.findViewById(R.id.item_tv_vocs_learned)



    }

    // ************************************************

    companion object
    {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Book>()
        {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.name == newItem.name
            }

        }
    }


    // *************************************************
    // Interface 1 - Öffnen
    interface OnAdapterShowButtonClick
    {
        fun setOnAdapterShowButtonClick(bookId:Long)
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
        fun setOnAdapterDeleteButtonClick(book:Book)
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