package com.example.voctrainer.moduls.voc.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import java.lang.IllegalArgumentException

class VocViewModelFactory(private val bookId:Long, private val application: Application): ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if(modelClass.isAssignableFrom(VocViewModel::class.java))
        {
            return VocViewModel(bookId,application) as T
        }
        else
        {
            throw IllegalArgumentException("Unkown ViewModel Class")
        }


    }

}