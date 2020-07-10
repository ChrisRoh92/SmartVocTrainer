package com.example.voctrainer.moduls.csv_handler.viewholder

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.voctrainer.backend.database.entities.Voc
import com.example.voctrainer.backend.repository.MainRepository
import com.example.voctrainer.moduls.csv_handler.CSVImportProcessor.createLocalVocsFromStrings
import com.example.voctrainer.moduls.csv_handler.CSVImportProcessor.createVocListFromLocalVoc

import com.example.voctrainer.moduls.csv_handler.CSVImportProcessor.requestFileToArrayList
import com.example.voctrainer.moduls.main.helper.LocalVoc
import kotlinx.coroutines.*
import kotlin.random.Random

class CSVViewModel(application: Application) : AndroidViewModel(application)
{
    // Coroutine Stuff:
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    // Repository
    private var bookId:Long = -1
    private val mainRep = MainRepository(context = application.applicationContext)

    // Daten für abgerufene Daten aus CSV
    private var rawData:ArrayList<String> = ArrayList()
    private var content: MutableLiveData<ArrayList<LocalVoc>> = MutableLiveData()

    private var importComplete:MutableLiveData<Boolean> = MutableLiveData()

    fun setBookID(id:Long)
    {
        this.bookId = id
    }

    fun setNewData(uri: Uri)
    {

        uiScope.launch {
            withContext(Dispatchers.IO)
            {
                rawData = requestFileToArrayList(getApplication(),uri)!!
                content.postValue(createLocalVocsFromStrings(rawData))
            }
        }

    }

    fun deleteLocalVoc(position:Int)
    {
        uiScope.launch {
            withContext(Dispatchers.IO)
            {
                val localContent = content.value
                localContent!!.removeAt(position)
                content.postValue(localContent)
            }
        }
    }

    fun undoDeleteLocalVoc(voc:LocalVoc,position: Int)
    {
        uiScope.launch {
            withContext(Dispatchers.IO)
            {
                val localContent = content.value
                localContent!!.add(position,voc)
                content.postValue(localContent)
            }
        }
    }

    fun updateLocalVoc(voc:LocalVoc,position: Int)
    {
        uiScope.launch {
            withContext(Dispatchers.IO)
            {
                val localContent = content.value
                localContent!!.removeAt(position)
                localContent!!.add(position,voc)
                content.postValue(localContent)
            }
        }
    }



    fun saveLocalVocToDataBase(id:Long = bookId)
    {
        uiScope.launch {
            withContext(Dispatchers.IO)
            {
                val localContent = content.value
                mainRep.insertNewVocs(createVocListFromLocalVoc(localContent!!,bookId))
                mainRep.updateBookWithNewData(id)
                importComplete.postValue(true)
            }
        }
    }

    fun getContentData(): LiveData<ArrayList<LocalVoc>>
    {
        return content
    }

    fun getImportComplete():LiveData<Boolean>
    {
        return importComplete
    }


}