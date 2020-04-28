package com.example.voctrainer.moduls.voc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voctrainer.R
import com.example.voctrainer.backend.database.entities.Test
import com.example.voctrainer.moduls.voc.utils.TestResults

class VocHomeRecyclerViewAdapter(var testResult: TestResults):
    RecyclerView.Adapter<VocHomeRecyclerViewAdapter.ViewHolder>()
{
    private var titles:ArrayList<String> = ArrayList()
    private var values:ArrayList<String> = ArrayList()
    private var progress:ArrayList<Float> = ArrayList()
    init {
        initArrayList()

    }

    private fun initArrayList()
    {
        titles.clear()
        values.clear()
        progress.clear()

        titles.add("Quote")
        titles.add("Anzahl Richtig")
        titles.add("Anzahl Falsch")
        titles.add("Anzahl Geübter Vokabeln")

        values.add("${testResult.result} %")
        values.add("${testResult.itemsCorrect} Vokabeln")
        values.add("${testResult.itemsFault} Vokabeln")
        values.add("${testResult.itemCount} Vokabeln")

        progress.add(testResult.progressResult)
        progress.add(testResult.progressItemsCorrect)
        progress.add(testResult.progressItemsFault)
        progress.add(testResult.progressItemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result_details,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvMain.text = titles[position]
        holder.tvValue.text = values[position]
        changeImageView(holder.imageView,progress[position])
        holder.tvProgress.text = "%.2f".format(progress[position]) + " %"
    }

    private fun changeImageView(imageView:ImageView,progress:Float)
    {
        if(progress == 0f)
        {
            imageView.setImageResource(R.drawable.ic_arrow_back_black_24dp)
        }
        else if(progress > 0f)
        {
            imageView.setImageResource(R.drawable.ic_arrow_upward_green_24dp)
        }
        else if(progress < 0f)
        {
            imageView.setImageResource(R.drawable.ic_arrow_downward_red_24dp)
        }
    }

    fun updateContent(newTestResult:TestResults)
    {
        this.testResult = newTestResult
        initArrayList()
        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var tvMain:TextView = itemView.findViewById(R.id.item_result_details_tv_title)
        var tvValue:TextView = itemView.findViewById(R.id.item_result_details_tv_value)
        var tvProgress:TextView = itemView.findViewById(R.id.item_result_details_tv_change)
        var imageView:ImageView = itemView.findViewById(R.id.item_result_details_imageview_change)
    }


}