package com.example.voctrainer.moduls.voc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R

class VocStatisticRecyclerViewAdapter(var content:ArrayList<Float>):
    RecyclerView.Adapter<VocStatisticRecyclerViewAdapter.ViewHolder>() {

    val title:ArrayList<String> = arrayListOf("Anzahl Test","Ø Abgefragte Vokabeln","Ø Erfolgsquote","Ø Anzahl Korrekt","Ø Anzahl Falsch")
    val subTitle:ArrayList<String> = arrayListOf("Wie viele Tests hast du schon absolviert",
        "Wie viele Vokabeln übst du im Schnitt",
        "Wie viel Prozent korrekte Eingaben hast du",
        "Wie hoch ist die Zahl korrekter Vokabeln",
        "Wie hoch ist die Zahl falscher Vokabeln")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_voc_statistic,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = title[position]
        holder.tvSubTitle.text = subTitle[position]
        if(position == 0)
        {
            holder.tvValue.text = "${content[position].toInt()}"
        }
        else if (position ==2)
        {
            holder.tvValue.text = "%.2f".format(content[position])+" %"
        }
        else
        {
            holder.tvValue.text = "%.2f".format(content[position])
        }



    }

    fun updateContent(content:ArrayList<Float>)
    {
        this.content = content
        notifyDataSetChanged()
    }







    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var tvTitle: TextView = itemView.findViewById(R.id.item_voc_statistic_tv_title)
        var tvSubTitle: TextView = itemView.findViewById(R.id.item_voc_statistic_tv_subtitle)
        var tvValue: TextView = itemView.findViewById(R.id.item_voc_statistic_tv_value)
    }


}