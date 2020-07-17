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
    private lateinit var mDeleteListener:OnAdapterDeleteButtonClick








    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_2,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = content[position]
        holder.tvTitle.text = book.name
        holder.tvSubTitle.text = "Erstellt am: ${book.timeStamp}"

        // Progress:
        holder.tvProgress.text = "${computeProgress(book.vocLearned,book.vocCount)} %"

        holder.tvVocs.text = "${book.vocCount}"
        holder.tvVocsOpen.text = "${book.vocUnLearned}"
        holder.tvVocsLearned.text = "${book.vocLearned}"

        holder.itemView.setOnClickListener { mShowListener?.setOnAdapterShowButtonClick(content[holder.adapterPosition].id) }
        holder.itemView.setOnLongClickListener {
            mDeleteListener?.setOnAdapterDeleteButtonClick(content[holder.adapterPosition],holder.adapterPosition)
            true
        }
    }



    fun updateContent(newContent:ArrayList<Book>,position: Int,action:Int)
    {
//        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(content,newContent))
//        diffResult.dispatchUpdatesTo(this)
        content = newContent
        if(action == 0)
        {
            notifyDataSetChanged()
        }
        else if(action == 1)
        {
            notifyItemRemoved(position)
        }
        else if (action == 2)
        {
            notifyItemInserted(0)
        }

    }

    private fun computeProgress(learned:Int,total:Int):Int
    {


        return if(total > 0) ((learned.toFloat()/total)*100).roundToInt() else 0
    }






    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        /*// Alle Buttons
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
*/

        // Alle Buttons
        // TextViews:
        var tvTitle: TextView = itemView.findViewById(R.id.item_tv_title)
        var tvSubTitle: TextView = itemView.findViewById(R.id.item_tv_subtitle)
        var tvProgress: TextView = itemView.findViewById(R.id.item_tv_progress)
        var tvVocsOpen: TextView = itemView.findViewById(R.id.item_tv_vocs_nlearned)
        var tvVocs: TextView = itemView.findViewById(R.id.item_tv_vocs)
        var tvVocsLearned: TextView = itemView.findViewById(R.id.item_tv_vocs_learned)


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

    // Interface 2 - Delete
    interface OnAdapterDeleteButtonClick
    {
        fun setOnAdapterDeleteButtonClick(book:Book,position: Int)
    }

    fun setOnAdapterDeleteClick(mDeleteListener: OnAdapterDeleteButtonClick)
    {
        this.mDeleteListener = mDeleteListener
    }




}