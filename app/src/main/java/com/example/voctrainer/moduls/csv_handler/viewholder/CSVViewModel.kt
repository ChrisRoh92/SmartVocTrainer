package com.example.voctrainer.moduls.csv_handler.viewholder

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.voctrainer.moduls.csv_handler.CSVImportProcessor
import kotlinx.coroutines.*

class CSVViewModel(application: Application) : AndroidViewModel(application)
{
    // Coroutine Stuff:
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    // Daten für abgerufene Daten aus CSV
    private var content: MutableLiveData<ArrayList<String>> = MutableLiveData()


    fun setNewData(uri: Uri)
    {
//        uiScope.launch {
//
//
//        }

        runBlocking {
            val job = launch(Dispatchers.Default)
            {
                content.postValue(CSVImportProcessor(getApplication(),uri).requestFileToArrayList()!!)
            }
        }
    }


    fun getContentData(): LiveData<ArrayList<String>>
    {
        return content
    }
}