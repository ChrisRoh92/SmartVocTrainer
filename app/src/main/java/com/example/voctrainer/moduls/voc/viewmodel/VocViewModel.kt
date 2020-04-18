package com.example.voctrainer.moduls.voc.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.voctrainer.backend.database.database.BookDatabase
import com.example.voctrainer.backend.database.database.VocDataBase
import com.example.voctrainer.backend.database.entities.Voc
import kotlinx.coroutines.*

class VocViewModel(private val bookId:Long,application: Application) : AndroidViewModel(application)
{
    private var db: VocDataBase = VocDataBase.getInstance(application)
    private var vocDao = db.vocDao()

    // Coroutine Stuff:
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    // Alle Vokabeln:
    var vocs = vocDao.getVocs(bookId)

    init {
        Log.i("VocViewModel","VocViewModel wurde erstellt")
    }





    // Vokabeln einfügen:
    fun onAddNewVoc(vocNative:String,vocForeign:String)
    {
        uiScope.launch {

            insert(Voc(0L,bookId,vocNative,vocForeign,0,"k.A."))
        }
    }

    // Vokabeln löschen:
    fun onDeleteNewVoc(voc: Voc)
    {
        uiScope.launch {
            delete(voc)
        }
    }

    // Vokabeln updaten:
    fun onUpdateVoc(voc: Voc)
    {
        uiScope.launch {
            update(voc)
        }
    }


    private suspend fun insert(voc:Voc)
    {
        withContext(Dispatchers.IO)
        {

            vocDao.insert(voc)
        }
    }

    private suspend fun insertAll(vocs:List<Voc>)
    {
        withContext(Dispatchers.IO)
        {
            vocDao.insertAll(vocs)
        }
    }

    private suspend fun delete(voc:Voc)
    {
        withContext(Dispatchers.IO)
        {
            vocDao.delete(voc)
        }
    }

    private suspend fun deleteAll(vocs:List<Voc>)
    {
        withContext(Dispatchers.IO)
        {
            vocDao.deleteAll(vocs)
        }
    }

    private suspend fun update(voc:Voc)
    {
        withContext(Dispatchers.IO)
        {
            vocDao.update(voc)
        }
    }

    private suspend fun updateAll(vocs:List<Voc>)
    {
        withContext(Dispatchers.IO)
        {
            vocDao.updateAll(vocs)
        }
    }




    override fun onCleared() {
        super.onCleared()
        Log.i("VocViewModel","VocViewModel wurde gelöscht")
        viewModelJob.cancel()
    }


}
