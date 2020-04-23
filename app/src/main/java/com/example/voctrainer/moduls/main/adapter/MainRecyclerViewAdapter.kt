package com.example.voctrainer.moduls.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.backend.database.entities.Book
import com.example.voctrainer.backend.database.entities.BookWithVocs
import com.example.voctrainer.moduls.main.utils.MyDiffCallback
import com.example.voctrainer.moduls.main.utils.getBookProgress
import com.example.voctrainer.moduls.main.utils.getBookStatus
import kotlin.math.roundToInt

class MainRecyclerViewAdapter(var content:ArrayList<Book>):
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
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val book = content[holder.adapterPosition]
        val progress = getProgress(book.vocCount,book.vocLearned)
        Log.e("VocTrainer","MainRecyclerViewAdapter.kt onBindViewHolder progress = $progress")

        holder.tvTitle.text = book.name
        holder.tvSubTitle.text = book.timeStamp

        // Progress:
        holder.tvProgress.text = "$progress %"

        holder.tvVocs.text = "${book.vocCount}"
        holder.tvVocsOpen.text = "${book.vocUnLearned}"
        holder.tvVocsLearned.text = "${book.vocLearned}"
        holder.pbProgress.progress = progress


        holder.btnShow.setOnClickListener {
            if(mShowListener!=null)
            {
                mShowListener.setOnAdapterShowButtonClick(book.id)
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
                mDeleteListener.setOnAdapterDeleteButtonClick(book)
            }
        }

        holder.btnShare.setOnClickListener {
            if(mShareListener!=null)
            {
                mShareListener.setOnAdapterShareButtonClick(holder.adapterPosition)
            }
        }
    }

    fun updateContent(newContent:ArrayList<Book>)
    {
        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(content,newContent))
        diffResult.dispatchUpdatesTo(this)
        /*content = newContent
        notifyDataSetChanged()*/


    }

    fun getProgress(vocCount:Int,vocLearned:Int):Int
    {
        return if(vocCount == 0)
        {
            0
        }
        else
        {
            ((vocLearned.toFloat()/vocCount)*100).roundToInt()
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
        // ProgressBar
        var pbProgress:ProgressBar = itemView.findViewById(R.id.item_voc_sk)

        init {
            pbProgress.max = 100

        }


    }

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