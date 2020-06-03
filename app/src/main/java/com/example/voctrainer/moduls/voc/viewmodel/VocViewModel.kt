package com.example.voctrainer.moduls.voc.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.voctrainer.backend.database.entities.Book
import com.example.voctrainer.backend.database.entities.Setting
import com.example.voctrainer.backend.database.entities.Test
import com.example.voctrainer.backend.database.entities.Voc
import com.example.voctrainer.backend.repository.MainRepository
import com.example.voctrainer.createCurrentTimeStamp
import com.example.voctrainer.moduls.voc.utils.TestResults
import com.example.voctrainer.moduls.voc.utils.TestStatistic
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

    // Vokabeln
    var vocs:MutableLiveData<List<Voc>> = MutableLiveData()

    // Test Stuff
    var tests = mainRep.getTest(bookId)
    var lastTwoTest = mainRep.getLastTest(bookId)

    // Settings
    var settings = mainRep.getSettings(bookId)


    // Extra Data
    var vocStatusValues:MutableLiveData<ArrayList<Int>> = MutableLiveData()


    // Lokale Live Daten:
    var liveBook:MutableLiveData<Book> = MutableLiveData()
    init {
        createVocStatusValues()
        uiScope.launch {
            withContext(Dispatchers.IO)
            {
                liveBook.postValue(mainRep.getOfflineBookById(bookId))
                vocs.postValue(mainRep.getOfflineVocs(bookId))
            }
        }

    }

    // Genaue Daten abfragen...
    fun createVocStatusValues()
    {
        uiScope.launch {
            withContext(Dispatchers.IO)
            {
                var values = arrayListOf(0,0,0)
                if(vocs.value == null)
                {
                    var localVoc = mainRep.getOfflineVocs(bookId)

                    for (i in localVoc)
                    {
                        values[i.status]++
                    }
                }
                else
                {
                    for (i in vocs.value!!)
                    {
                        values[i.status]++
                    }
                }

                vocStatusValues.postValue(values)
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
            createVocStatusValues()
            withContext(Dispatchers.IO)
            {
                vocs.postValue(mainRep.getOfflineVocs(bookId))
            }


        }
    }
    // Vokabeln löschen:
    fun onDeleteNewVoc(voc: Voc)
    {
        uiScope.launch {
            mainRep.deleteVoc(voc)
            mainRep.updateBookWithNewData(bookId)
            createVocStatusValues()
            withContext(Dispatchers.IO)
            {
                vocs.postValue(mainRep.getOfflineVocs(bookId))
            }
        }
    }
    // Vokabeln updaten:
    fun onUpdateVoc(voc: Voc)
    {
        uiScope.launch {
            mainRep.updateVoc(voc)
            createVocStatusValues()
            withContext(Dispatchers.IO)
            {
                vocs.postValue(mainRep.getOfflineVocs(bookId))
            }

        }
    }

    fun onFilterVocs(query:String)
    {
        uiScope.launch {
            withContext(Dispatchers.IO)
            {
                Log.d("VocTrainer","VocViewModel - bookId = $bookId")
                if(query.isNotBlank() || query.isNotEmpty())
                {
                    Log.d("VocTrainer","VocViewModel - query.isNotBlank() query = $query")
                    vocs.postValue(mainRep.getFilteredVocs(bookId,query))
                }
                else
                {
                    Log.d("VocTrainer","VocViewModel - query.isBlank() query = $query")
                    vocs.postValue(mainRep.getFilteredVocs(bookId,query))
                }

                for(i in vocs.value!!)
                {
                    Log.d("VocTrainer","VocViewModel - onFilterVocs vocs = ${i}")
                }

            }
        }

    }

    // VocPractise:
    fun onAddNewSetting(itemCount:Int,timeMode:Boolean,time:Long,practiseMode:Boolean,settingsMode:Int)
    {
        uiScope.launch {
            mainRep.insertNewSettings(Setting(0,bookId,itemCount,timeMode,time,practiseMode,settingsMode))
        }
    }
    fun onUpdateSetting(newSetting:Setting)
    {
        uiScope.launch {
            mainRep.updateSetting(newSetting)
        }
    }




    // TODO(): Kommt in das ViewModel vom Practise Modul!
    fun onAddNewTest(itemsIds:ArrayList<Long>,solutions:ArrayList<String>,results:Float)
    {
        uiScope.launch {
            /*val test =
            Log.e("VocTrainer","VocViewModel onAddNewTest() test = $test")*/
            mainRep.insertNewTest(Test(timeStamp = createCurrentTimeStamp(),bookId = bookId,itemIds = getItemIds(),solutions = getSolutions(),result = Random.nextInt(0,100).toFloat()))

        }
    }
    // TODO(): Kommt in das ViewModel vom Practise Modul!
    fun onDeleteTest(test:Test)
    {
        uiScope.launch {
            mainRep.deleteTest(test)
        }
    }

    // TODO() -> Wird entfernt wenn alles funkt
    private fun getItemIds():ArrayList<Long>
    {
        var results:ArrayList<Long> = ArrayList()
        for(i in 1..20)
        {
         results.add(i.toLong())
        }

        return results
    }
    // TODO() -> Wird entfernt wenn alles funkt
    private fun getSolutions():ArrayList<String>
    {
        var solutions:ArrayList<String> = ArrayList()
        for(i in 1..20)
        {
            solutions.add("Solution...")
        }
        return solutions

    }



    // VocHomeFragment:
    fun createTestResult(tests:List<Test>):TestResults
    {
        if(tests.size >= 2 && tests.isNotEmpty())
        {
            val progressResult = ((tests[0].result-tests[1].result)/tests[1].result)*100
            val progressCorrect = ((tests[0].itemsCorrect-tests[1].itemsCorrect)/tests[1].itemsCorrect.toFloat())*100
            val progressItemCount = ((tests[0].itemIds.size-tests[1].itemIds.size)/tests[1].itemIds.size.toFloat())*100
            val oldItemsFault = tests[1].itemIds.size - tests[1].itemsCorrect
            val newItemsFault = tests[0].itemIds.size - tests[0].itemsCorrect
            val progressFault = ((newItemsFault-oldItemsFault)/newItemsFault.toFloat())*100

            return TestResults(tests[0].result,
                tests[0].itemsCorrect,
                newItemsFault,
                tests[0].itemIds.size,
                progressResult,
                progressCorrect,
                progressFault,
                progressItemCount,
                tests.size)
        }
        else if(tests.size == 1)
        {

            val newItemsFault = tests[0].itemIds.size - tests[0].itemsCorrect

            return TestResults(tests[0].result,
                tests[0].itemsCorrect,
                newItemsFault,
                tests[0].itemIds.size,
                99f,
                99f,
                99f,
                99f,
                tests.size)
        }
        else
        {
            return TestResults(0f,0,0,0,0f,0f,0f,0f,0)
        }




    }

    // VocStatisticFragment
    // Function to get Statistics over all done Tests
    fun createTestStatistics(tests:List<Test>): TestStatistic
    {
        if(tests.isNullOrEmpty())
        {
            return TestStatistic(0,0f,0f,0f,0f)
        }
        else
        {
            var testCount = 0
            var result = 0f
            var itemCount = 0f
            var itemCorrect = 0f
            var itemFault = 0f
            for(i in tests)
            {
                testCount++
                result += i.result
                itemCount += i.solutions.size
                itemCorrect += i.itemsCorrect
                itemFault += (i.solutions.size - i.itemsCorrect)

            }

            val size = (tests.size.toFloat())
            result /= size
            itemCount /= size
            itemCorrect /= size
            itemFault /= size

            return TestStatistic(testCount,result,itemCount,itemCorrect,itemFault)
        }

    }


    // Get Mutable LiveData:
    fun getVocStatusValues():LiveData<ArrayList<Int>>
    {
        return vocStatusValues
    }

    fun getVocs():LiveData<List<Voc>>
    {
        return vocs
    }












    override fun onCleared() {
        super.onCleared()
        Log.i("VocViewModel","VocViewModel wurde gelöscht")
        viewModelJob.cancel()
    }


}
