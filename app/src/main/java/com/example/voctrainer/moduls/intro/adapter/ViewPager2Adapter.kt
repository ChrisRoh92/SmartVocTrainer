package com.example.voctrainer.moduls.intro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R

class ViewPager2Adapter(var title:ArrayList<String>,var subTitle:ArrayList<String>,var images:ArrayList<Int>):RecyclerView.Adapter<ViewPager2Adapter.ViewHolder>()
{

    var backgroundColor = arrayListOf<Int>(R.drawable.background_blue_gradient,R.drawable.background_red_gradient,R.drawable.background_green_gradient)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_intro_content,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return title.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.tvMain.text = title[position]
        holder.tvSub.text = subTitle[position]
        holder.imageView.setImageResource(images[position])
        holder.layout.setBackgroundResource(backgroundColor[position])

    }





    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var imageView:ImageView = itemView.findViewById(R.id.fragment_intro_content_imageview)
        var tvMain: TextView = itemView.findViewById(R.id.fragment_intro_content_tv_main)
        var tvSub: TextView = itemView.findViewById(R.id.fragment_intro_content_tv_sub)
        var layout:ConstraintLayout = itemView.findViewById(R.id.fragment_intro_content_clayout)

    }
}