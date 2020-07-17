package com.example.voctrainer.moduls.practise.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.voctrainer.backend.database.entities.Setting
import com.example.voctrainer.backend.database.entities.Test
import com.example.voctrainer.backend.database.entities.Voc
import com.example.voctrainer.backend.repository.MainRepository
import com.example.voctrainer.checkForLearnStatus
import com.example.voctrainer.createCurrentTimeStamp
import kotlinx.coroutines.*
import kotlin.math.roundToInt

class PractiseViewModel(private val bookId:Long,private val settingsID:Long,application: Application) : AndroidViewModel(application)
{
    // Repository
    private val mainRep = MainRepository(context = application.applicationContext)

    // Coroutine Stuff:
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    // Settings:
    private var currentSetting = mainRep.getSettingsById(bookId,settingsID)

    // Voks:
    private var vocList =  mainRep.getVocs(bookId)

    // List for Vok:
    private var testVocList:MutableLiveData<List<Voc>> = MutableLiveData()

    // Control Variables:
    private var readyForResult = mainRep.getTest(bookId)

    // TestVocList:
    private var newTestVocList:MutableLiveData<List<Voc>> = MutableLiveData()




    fun createVocSet()
    {
        uiScope.launch {
            var localList:ArrayList<Voc> = ArrayList()
            for(i in vocList!!.value!!)
            {
                if(checkForLearnStatus(i.status) <= currentSetting!!.value!!.settingsMode)
                {
                    localList.add(i)
                }
            }
            localList.shuffle()
            var newList:ArrayList<Voc> = ArrayList()
            // Prüfen ob genug Vokabeln da sind, (Nutzer wurde darüber davor informiert), und ggfs. umstellen auf die Anzahl der verfügbaren Voks
            var itemCount = if(currentSetting!!.value!!.itemCount > localList.size) localList.size else currentSetting!!.value!!.itemCount
            for(i in 0 until itemCount)
            {
                newList.add(localList[i])
            }


            /**/

            testVocList.postValue(newList)
        }


    }

    fun createNewTest(solutions:ArrayList<String>,itemsCorrect:ArrayList<Int>)
    {

        uiScope.launch {
            withContext(Dispatchers.IO)
            {

                fun createItemIdList():ArrayList<Long>
                {
                    var values = ArrayList<Long>()
                    for (i in testVocList!!.value!!)
                    {
                        values.add(i.id)
                    }

                    return values
                }
                fun calcCorrectValue():Float
                {
                    return (((itemsCorrect.sum().toFloat())/testVocList.value!!.size)*100).roundToInt().toFloat()
                }

                val idList = createItemIdList()
                updateVocs(idList,itemsCorrect)

                mainRep.insertNewTest(Test(0, createCurrentTimeStamp(),bookId,idList,solutions,itemsCorrect.sum(),calcCorrectValue(),settingsID))

            }
        }
    }

    private suspend fun updateVocs(IDs:ArrayList<Long>,itemsCorrect:ArrayList<Int>)
    {
        withContext(Dispatchers.IO)
        {
            var vocs:ArrayList<Voc> = ArrayList()
            for((index,i) in IDs.withIndex())
            {
                var voc = mainRep.getOfflineVocById(bookId,i)
                if(itemsCorrect[index] == 1)
                {
                    voc.status += 1
                }
                else
                {
                    voc.status = 0
                }
                voc.lastPractiseDate = createCurrentTimeStamp(1)
                vocs.add(voc)
            }
            // Diese Vokabeln jetzt aktualisieren:
            for(i in vocs)
            {
                mainRep.updateVoc(i)
            }
            mainRep.updateBookWithNewData(bookId)


        }

    }

    fun createTestVocListFromIDs(vocIDs:ArrayList<Long>)
    {
        uiScope.launch {
            withContext(Dispatchers.IO)
            {
                var values:ArrayList<Voc> = ArrayList()
                for(i in vocIDs)
                {
                    values.add(mainRep.getOfflineVocById(bookId,i))
                }
                newTestVocList.postValue(values)
            }
        }

    }



    fun getTestVocList():LiveData<List<Voc>> = testVocList

    fun getCurrentSetting():LiveData<Setting> = currentSetting

    fun getVocList():LiveData<List<Voc>> = vocList

    fun getReadyForResult():LiveData<List<Test>> = readyForResult

    fun getNewTestVocList():LiveData<List<Voc>> = newTestVocList


}