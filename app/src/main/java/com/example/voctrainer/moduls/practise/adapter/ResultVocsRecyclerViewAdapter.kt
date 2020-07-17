package com.example.voctrainer.moduls.practise.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R

class ResultVocsRecyclerViewAdapter(var question:ArrayList<String>,var answer:ArrayList<String>,var solution:ArrayList<String> ):
    RecyclerView.Adapter<ResultVocsRecyclerViewAdapter.ViewHolder>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result_vocs,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return question.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.tvMain.text = answer[position]
        holder.tvSub.text = question[position]
        setImage(holder.imageView,position)

    }

    private fun setImage(i:ImageView, pos:Int)
    {
        if(checkForResult(pos))
        {
            i.setImageResource(R.drawable.ic_check_black_24dp)
        }
        else
        {
            i.setImageResource(R.drawable.ic_close_black_24dp)
        }
    }

    private fun checkForResult(pos:Int):Boolean
    {
        return answer[pos] == solution[pos]
    }

    fun updateContent(question:ArrayList<String>,answer:ArrayList<String>,solution:ArrayList<String>)
    {
        this.question = question
        this.answer = answer
        this.solution = solution
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvMain:TextView = itemView.findViewById(R.id.item_tv_main)
        var tvSub:TextView = itemView.findViewById(R.id.item_tv_sub)
        var imageView:ImageView = itemView.findViewById(R.id.item_image)
    }
}