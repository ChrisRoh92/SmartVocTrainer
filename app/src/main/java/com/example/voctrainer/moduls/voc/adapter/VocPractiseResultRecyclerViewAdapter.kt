package com.example.voctrainer.moduls.voc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import kotlin.random.Random

class VocPractiseResultRecyclerViewAdapter(var content:ArrayList<String>):
    RecyclerView.Adapter<VocPractiseResultRecyclerViewAdapter.ViewHolder>() {


    // Interface:
    private lateinit var mListener:OnItemClickListener





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_voc_practise_verlauf,parent,false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        setIcon(Random.nextBoolean(),holder.imageCircle,holder.imageIcon)
        holder.itemView.setOnClickListener {
            mListener.setOnItemClickListener(holder.adapterPosition)
        }

    }


    private fun setIcon(status:Boolean,circle:ImageView,icon:ImageView)
    {
        if(status)
        {
            // Wenn true, dann bestanden
            circle.setImageResource(R.drawable.custom_circle_green)
            icon.setImageResource(R.drawable.ic_check_black_24dp)
        }
        else
        {
            // Sonst eben nicht bestanden
            circle.setImageResource(R.drawable.custom_circle_red)
            icon.setImageResource(R.drawable.ic_close_white_24dp)

        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        // TextView:
        var tvDate:TextView = itemView.findViewById(R.id.item_voc_practise_result_tv_date)
        var tvTime:TextView = itemView.findViewById(R.id.item_voc_practise_result_tv_time)
        var tvVocs:TextView = itemView.findViewById(R.id.item_voc_practise_result_tv_vocs)
        var tvVocsCorrect:TextView = itemView.findViewById(R.id.item_voc_practise_result_tv_vocs_correct)
        var tvResultat:TextView = itemView.findViewById(R.id.item_voc_practise_result_tv_result)
        // ImageView:
        var imageCircle:ImageView = itemView.findViewById(R.id.item_voc_practise_result_image_circle)
        var imageIcon:ImageView = itemView.findViewById(R.id.item_voc_practise_result_image_icon)
        // ImageButton:
        var btnRepeat:ImageButton = itemView.findViewById(R.id.item_voc_practise_result_btn_repeat)
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