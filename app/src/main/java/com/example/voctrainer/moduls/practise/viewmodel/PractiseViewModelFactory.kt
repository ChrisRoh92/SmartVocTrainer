package com.example.voctrainer.moduls.practise.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.voctrainer.moduls.voc.viewmodel.VocViewModel
import java.lang.IllegalArgumentException

class PractiseViewModelFactory(private val bookId:Long,private val settingsID:Long, private val application: Application): ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if(modelClass.isAssignableFrom(PractiseViewModel::class.java))
        {
            return PractiseViewModel(bookId,settingsID,application) as T
        }
        else
        {
            throw IllegalArgumentException("Unkown ViewModel Class")
        }


    }

}