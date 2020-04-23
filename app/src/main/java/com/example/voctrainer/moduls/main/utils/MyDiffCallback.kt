package com.example.voctrainer.moduls.main.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.voctrainer.backend.database.entities.Book
import kotlin.math.sign

class MyDiffCallback(val newContent:ArrayList<Book>,val oldContent:ArrayList<Book>): DiffUtil.Callback()
{
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newContent[newItemPosition].id == oldContent[oldItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldContent.size
    }

    override fun getNewListSize(): Int {
        return newContent.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newContent[newItemPosition] == oldContent[oldItemPosition]
    }
}