package com.example.voctrainer.moduls.voc.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.voctrainer.backend.database.database.BookDatabase
import com.example.voctrainer.backend.database.database.VocDataBase
import com.example.voctrainer.backend.database.entities.Book
import com.example.voctrainer.backend.database.entities.Test
import com.example.voctrainer.backend.database.entities.Voc
import com.example.voctrainer.backend.repository.MainRepository
import kotlinx.coroutines.*
import kotlin.random.Random

class VocViewModel(private val bookId:Long,application: Application) : AndroidViewModel(application)
{
    // Repository
    private val mainRep = MainRepository(context = application.applicationContext)

    // Coroutine Stuff:
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    // Alle Vokabeln:
    var vocs = mainRep.getVocs(bookId)
    var tests = mainRep.getTest(bookId)










    // Vokabeln einfügen:
    fun onAddNewVoc(vocNative:String,vocForeign:String)
    {
        uiScope.launch {

            mainRep.insertNewVoc(Voc(0L,bookId,vocNative,vocForeign, 0,"-"))
            mainRep.updateBookWithNewData(bookId)
        }
    }
    // Vokabeln löschen:
    fun onDeleteNewVoc(voc: Voc)
    {
        uiScope.launch {
            mainRep.deleteVoc(voc)
            mainRep.updateBookWithNewData(bookId)
        }
    }
    // Vokabeln updaten:
    fun onUpdateVoc(voc: Voc)
    {
        uiScope.launch {
            mainRep.updateVoc(voc)
            vocs = mainRep.getVocs(bookId)

        }
    }

    fun onAddNewTest()
    {
        uiScope.launch {
            val test = Test(0,bookId, arrayListOf(1,2,3,4,5,6), arrayListOf("ad","ad","ad","ad","ad","ad"), 34,77f,0)
            mainRep.insertNewTest(test)
        }
    }

    private fun getItemIds():ArrayList<Long>
    {
        var results:ArrayList<Long> = ArrayList()
        for(i in 1..20)
        {
         results.add(i.toLong())
        }

        return results
    }

    private fun getSolutions():ArrayList<String>
    {
        var solutions:ArrayList<String> = ArrayList()
        for(i in 1..20)
        {
            solutions.add("Solution...")
        }
        return solutions

    }











    override fun onCleared() {
        super.onCleared()
        Log.i("VocViewModel","VocViewModel wurde gelöscht")
        viewModelJob.cancel()
    }


}
