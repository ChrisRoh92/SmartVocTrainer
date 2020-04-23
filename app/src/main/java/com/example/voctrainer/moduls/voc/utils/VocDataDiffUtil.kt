package com.example.voctrainer.moduls.voc.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.voctrainer.backend.database.entities.Voc

class VocDataDiffUtil(private val oldList:ArrayList<Voc>,private val newList:ArrayList<Voc>):DiffUtil.Callback()
{


    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].id == newList[newItemPosition].id)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        val oldItem = oldList[oldItemPosition]
        val newItem = oldList[oldItemPosition]
        //return (oldItem.native2 == newItem.native2) && (oldItem.foreign == newItem.foreign)
        return oldItem == newItem

    }


}