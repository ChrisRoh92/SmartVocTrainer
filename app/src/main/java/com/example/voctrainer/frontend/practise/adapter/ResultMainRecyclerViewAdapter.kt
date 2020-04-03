package com.example.voctrainer.frontend.practise.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R

class ResultMainRecyclerViewAdapter(var values:ArrayList<String>,var changeValues:ArrayList<Int>):
    RecyclerView.Adapter<ResultMainRecyclerViewAdapter.ViewHolder>() {



    private var titles:ArrayList<String> = arrayListOf("Anzahl Korrekt","Anzahl Falsch","Erfolgsquote","Benötigte Zeit")



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result_details,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.tvTitle.text = titles[position]
        holder.tvValue.text = values[position]
        setChangeValue(changeValues[position],holder.imageChange,holder.tvChange)

    }

    private fun setChangeValue(value:Int,imageChange:ImageView,tvChange:TextView)
    {
        if(value > 0)
        {
            tvChange.text = "+$value %"
            imageChange.setImageResource(R.drawable.ic_arrow_upward_green_24dp)
        }
        else if(value < 0)
        {
            tvChange.text = "-$value %"
            imageChange.setImageResource(R.drawable.ic_arrow_downward_red_24dp)
        }
        else
        {
            tvChange.text = "$value %"
            imageChange.setImageResource(R.drawable.ic_arrow_back_black_24dp)

        }
    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvTitle: TextView = itemView.findViewById(R.id.item_result_details_tv_title)
        var tvValue: TextView = itemView.findViewById(R.id.item_result_details_tv_value)
        var tvChange: TextView = itemView.findViewById(R.id.item_result_details_tv_change)
        var imageChange: ImageView = itemView.findViewById(R.id.item_result_details_imageview_change)
    }
}