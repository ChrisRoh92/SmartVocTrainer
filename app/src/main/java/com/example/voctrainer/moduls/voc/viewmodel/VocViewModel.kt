package com.example.voctrainer.moduls.voc.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.voctrainer.backend.database.entities.Book
import com.example.voctrainer.backend.database.entities.Test
import com.example.voctrainer.backend.database.entities.Voc
import com.example.voctrainer.backend.repository.MainRepository
import com.example.voctrainer.createCurrentTimeStamp
import com.example.voctrainer.moduls.voc.utils.TestResults
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
    var book = mainRep.getBookById(bookId)
    var vocs = mainRep.getVocs(bookId)
    var tests = mainRep.getTest(bookId)
    var lastTwoTest = mainRep.getLastTest(bookId)


    // Lokale Live Daten:
    var liveBook:MutableLiveData<Book> = MutableLiveData()
    init {
        uiScope.launch {
            withContext(Dispatchers.IO)
            {
                liveBook.postValue(mainRep.getOfflineBookById(bookId))
            }
        }


    }










    // Vokabeln einfügen:
    fun onAddNewVoc(vocNative:String,vocForeign:String)
    {
        uiScope.launch {

            val result = Random.nextInt(0,3)
            mainRep.insertNewVoc(Voc(0L,bookId,vocNative,vocForeign,result ,"-"))
            mainRep.updateBookWithNewData(bookId)
            /*if(liveBook.value != null)
            {
                val book = liveBook.value
                book!!.vocCount.plus(1)
                if(result < 2)
                    book!!.vocUnLearned.plus(1)
                else
                    book!!.vocLearned.plus(1)
                liveBook.postValue(book)
            }*/


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
            /*val test =
            Log.e("VocTrainer","VocViewModel onAddNewTest() test = $test")*/
            mainRep.insertNewTest(Test(timeStamp = createCurrentTimeStamp(),bookId = bookId,itemIds = getItemIds(),solutions = getSolutions(),result = Random.nextInt(0,100).toFloat()))

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


    // Erstellung Side Stuff:
    // VocHomeFragment:
    fun createTestResult(tests:List<Test>):TestResults
    {
        if(tests.size >= 2 && tests.isNotEmpty())
        {
            val progressResult = (tests[0].result-tests[1].result)/tests[1].result
            val progressCorrect = (tests[0].itemsCorrect-tests[1].itemsCorrect)/tests[1].itemsCorrect.toFloat()
            val progressItemCount = (tests[0].itemIds.size-tests[1].itemIds.size)/tests[1].itemIds.size.toFloat()
            val oldItemsFault = tests[1].itemIds.size - tests[1].itemsCorrect
            val newItemsFault = tests[0].itemIds.size - tests[0].itemsCorrect
            val progressFault = (newItemsFault-oldItemsFault)/newItemsFault.toFloat()

            return TestResults(tests[0].result,
                tests[0].itemsCorrect,
                newItemsFault,
                tests[0].itemIds.size,
                progressResult,
                progressCorrect,
                progressFault,
                progressItemCount)
        }
        else if(tests.size == 1)
        {

            val newItemsFault = tests[0].itemIds.size - tests[0].itemsCorrect

            return TestResults(tests[0].result,
                tests[0].itemsCorrect,
                newItemsFault,
                tests[0].itemIds.size,
                999f,
                999f,
                999f,
                999f)
        }
        else
        {
            return TestResults(0f,0,0,0,0f,0f,0f,0f)
        }




    }












    override fun onCleared() {
        super.onCleared()
        Log.i("VocViewModel","VocViewModel wurde gelöscht")
        viewModelJob.cancel()
    }


}
