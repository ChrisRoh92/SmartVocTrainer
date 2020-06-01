package com.example.voctrainer.backend.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.voctrainer.backend.database.database.BookDatabase
import com.example.voctrainer.backend.database.database.SettingsDatabase
import com.example.voctrainer.backend.database.database.TestDatabase
import com.example.voctrainer.backend.database.database.VocDataBase
import com.example.voctrainer.backend.database.entities.Book
import com.example.voctrainer.backend.database.entities.Setting
import com.example.voctrainer.backend.database.entities.Test
import com.example.voctrainer.backend.database.entities.Voc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

/*
Offene Punkte und Ideen zu dieser Klasse:
- SubRepositories einrichten, sonst wird die Klass zu unübersichtlich...



*/


class MainRepository(val context: Context):CoroutineScope
{
    // Coroutines:
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    // Datenbanken:
    private val dbBook = BookDatabase.getInstance(context)
    private val dbVoc = VocDataBase.getInstance(context)
    private val dbSettings = SettingsDatabase.getInstance(context)
    private val dbTest = TestDatabase.getInstance(context)

    // Daos davon:
    private val bookDao = dbBook.bookDao()
    private val vocDao = dbVoc.vocDao()
    private val settingsDao = dbSettings.settingsDao()
    private val testDao = dbTest.testDao()

    // ********************************************************************
    // Get LiveData Listen...:
    fun getBooks():LiveData<List<Book>>
    {

        return bookDao.getBooks()
    }
    fun getFilteredBooks(input:String):List<Book>
    {

        return bookDao.getFilteredBooks(input)
    }
    // Der Rest braucht die bookID, da die Settings, Vocs usw, nur von einem Book ausgegeben werden sollen
    fun getSettings(bookId:Long):LiveData<List<Setting>>
    {
        return settingsDao.getSettings(bookId)
    }
    fun getVocs(bookId:Long):LiveData<List<Voc>>
    {
        return vocDao.getVocs(bookId)
    }
    fun getTest(bookId:Long):LiveData<List<Test>>
    {
        return testDao.getTests(bookId)
    }

    // Einzelne Objekte anhand der ID erhalten:
    fun getBookById(bookId: Long):LiveData<Book>
    {
        return bookDao.getBookById(bookId)
    }

    fun getVocById(bookId: Long,vocId:Long):LiveData<Voc>
    {
        return vocDao.getVocById(vocId,bookId)
    }

    fun getSettingsById(bookId: Long,settingsId:Long):LiveData<Setting>
    {
        return settingsDao.getSettingsByID(settingsId,bookId)
    }

    fun getTestById(bookId: Long,testId:Long):LiveData<Test>
    {
        return testDao.getTestByID(testId,bookId)
    }

    // Die Letzten Bücher bekommen...
    /*fun getLastBook():LiveData<Book>
    {

    }

    fun getLastVoc(bookId: Long):LiveData<Voc>
    {

    }

    fun getLastSettings(bookId: Long):LiveData<Setting>
    {

    }*/

    fun getLastTest(bookId: Long):LiveData<List<Test>>
    {
        return testDao.getLastTest(bookId)
    }


    // Offline - Listen geben:
    fun getOfflineBooks():List<Book>
    {
        return bookDao.getOfflineBooks()
    }

    fun getOfflineVocs(bookId: Long):List<Voc>
    {
        return vocDao.getOfflineVocs(bookId)
    }

    fun getFilteredVocs(bookId: Long,query:String):List<Voc>
    {
        return vocDao.getFilteredVocs(bookId,query)
    }

    fun getOfflineSettings(bookId: Long):List<Setting>
    {
        return settingsDao.getOfflineSettings(bookId)
    }

    fun getOfflineTests(bookId: Long):List<Test>
    {
        return testDao.getOfflineTests(bookId)
    }

    // Einzelne Objekte anhand der ID erhalten (Kein LiveData):
    fun getOfflineBookById(bookId: Long):Book
    {
        return bookDao.getOfflineBookById(bookId)
    }

    fun getOfflineVocById(bookId: Long,vocId:Long):Voc
    {
        return vocDao.getOfflineVocById(vocId,bookId)
    }

    fun getOfflineSettingsById(bookId: Long,settingsId:Long):Setting
    {
        return settingsDao.getOfflineSettingsByID(settingsId,bookId)
    }

    fun getOfflineTestById(bookId: Long,testId:Long):Test
    {
        return testDao.getOfflineTestByID(testId,bookId)
    }


    // ********************************************************************
    // Einzelnen Eintrag eintragen:
    suspend fun insertNewBook(book:Book)
    {
        withContext(Dispatchers.IO)
        {
            bookDao.insert(book)
        }
    }
    suspend fun insertNewVoc(voc:Voc)
    {
        withContext(Dispatchers.IO)
        {
            vocDao.insert(voc)
        }
    }
    suspend fun insertNewSettings(setting:Setting)
    {
        withContext(Dispatchers.IO)
        {
            settingsDao.insert(setting)
        }
    }
    suspend fun insertNewTest(test:Test)
    {
        withContext(Dispatchers.IO)
        {
            testDao.insert(test)
        }
    }

    // Einzelnen Eintrag eintragen:
    suspend fun insertNewBooks(books:List<Book>)
    {
        withContext(Dispatchers.IO)
        {
            bookDao.insertAll(books)
        }
    }
    suspend fun insertNewVocs(vocs:List<Voc>)
    {
        withContext(Dispatchers.IO)
        {
            vocDao.insertAll(vocs)
        }
    }
    suspend fun insertNewSettingss(settings:List<Setting>)
    {
        withContext(Dispatchers.IO)
        {
            settingsDao.insertAll(settings)
        }
    }
    suspend fun insertNewTests(tests:List<Test>)
    {
        withContext(Dispatchers.IO)
        {
            testDao.insertAll(tests)
        }
    }


    // ********************************************************************
    // Einzelen Eintrag entfernen:
    suspend fun deleteBook(book:Book)
    {
        withContext(Dispatchers.IO)
        {
            bookDao.delete(book)
        }
    }
    suspend fun deleteVoc(voc:Voc)
    {
        withContext(Dispatchers.IO)
        {
            vocDao.delete(voc)
        }
    }
    suspend fun deleteSetting(setting:Setting)
    {
        withContext(Dispatchers.IO)
        {
            settingsDao.delete(setting)
        }
    }
    suspend fun deleteTest(test:Test)
    {
        withContext(Dispatchers.IO)
        {
            testDao.delete(test)
        }
    }
    // Einzelen Eintrag entfernen:
    suspend fun deleteBooks(books:List<Book>)
    {
        withContext(Dispatchers.IO)
        {
            bookDao.deleteAll(books)
        }
    }
    suspend fun deleteVocs(vocs:List<Voc>)
    {
        withContext(Dispatchers.IO)
        {
            vocDao.deleteAll(vocs)
        }
    }
    suspend fun deleteSettings(settings:List<Setting>)
    {
        withContext(Dispatchers.IO)
        {
            settingsDao.deleteAll(settings)
        }
    }
    suspend fun deleteTests(tests:List<Test>)
    {
        withContext(Dispatchers.IO)
        {
            testDao.deleteAll(tests)
        }
    }

    // ********************************************************************
    // Einzelen Eintrag entfernen:
    suspend fun updateBook(book:Book)
    {
        withContext(Dispatchers.IO)
        {
            bookDao.update(book)
        }
    }
    suspend fun updateVoc(voc:Voc)
    {
        withContext(Dispatchers.IO)
        {
            vocDao.insert(voc)
        }
    }
    suspend fun updateSetting(setting:Setting)
    {
        withContext(Dispatchers.IO)
        {
            settingsDao.update(setting)
        }
    }
    suspend fun updateTest(test:Test)
    {
        withContext(Dispatchers.IO)
        {
            testDao.update(test)
        }
    }
    // Einzelen Eintrag entfernen:
    suspend fun updateBooks(books:List<Book>)
    {
        withContext(Dispatchers.IO)
        {
            bookDao.updateAll(books)
        }
    }
    suspend fun updateVocs(vocs:List<Voc>)
    {
        withContext(Dispatchers.IO)
        {
            vocDao.updateAll(vocs)
        }
    }
    suspend fun updateSettings(settings:List<Setting>)
    {
        withContext(Dispatchers.IO)
        {
            settingsDao.updateAll(settings)
        }
    }
    suspend fun updateTests(tests:List<Test>)
    {
        withContext(Dispatchers.IO)
        {
            testDao.updateAll(tests)
        }
    }

    // ********************************************************************
    // Andere Methoden
    suspend fun updateBookWithNewData(bookId:Long)
    {
        withContext(Dispatchers.IO)
        {

            runBlocking {
                try {
                    val book = getOfflineBookById(bookId)
                    val vocs = getOfflineVocs(bookId = book.id)
                    book.vocCount = vocs.size
                    book.vocUnLearned = 0
                    book.vocLearned = 0
                    for(i in vocs)
                    {
                        if(i.status < 2)
                        {
                            book.vocUnLearned++
                        }
                        else if (i.status == 2)
                        {
                            book.vocLearned++
                        }
                    }

                    updateBook(book)


                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }


        }

    }



}