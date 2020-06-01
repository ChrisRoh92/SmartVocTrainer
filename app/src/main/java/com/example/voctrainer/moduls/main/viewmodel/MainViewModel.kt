package com.example.voctrainer.moduls.main.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.voctrainer.backend.database.dao.VocDao
import com.example.voctrainer.backend.database.database.BookDatabase
import com.example.voctrainer.backend.database.database.VocDataBase
import com.example.voctrainer.backend.database.entities.Book
import com.example.voctrainer.backend.database.entities.BookWithVocs
import com.example.voctrainer.backend.repository.MainRepository
import com.example.voctrainer.createCurrentTimeStamp
import kotlinx.coroutines.*

class MainViewModel(application: Application) : AndroidViewModel(application)
{

    // Repository
    private val mainRep = MainRepository(context = application.applicationContext)

    // Coroutine Stuff:
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    // Alle Bücher:
    var books:MutableLiveData<List<Book>> = MutableLiveData()

    init {
        uiScope.launch {
            withContext(Dispatchers.IO)
            {
                books.postValue(mainRep.getOfflineBooks())
            }

        }
    }





    fun onAddBook(name:String)
    {
        uiScope.launch {
            mainRep.insertNewBook(Book(0,name,createCurrentTimeStamp(),0,0,0))
            withContext(Dispatchers.IO)
            {
                books.postValue(mainRep.getOfflineBooks())
            }
        }
    }
    fun onUpdateBook(book:Book)
    {
        uiScope.launch {
            mainRep.updateBook(book)
            withContext(Dispatchers.IO)
            {
                books.postValue(mainRep.getOfflineBooks())
            }
        }
    }
    fun onDeleteBook(book:Book)
    {
        uiScope.launch {
            mainRep.deleteBook(book)
            withContext(Dispatchers.IO)
            {
                books.postValue(mainRep.getOfflineBooks())
            }
        }
    }

    fun onFilterBookList(input:String)
    {
        uiScope.launch {
            withContext(Dispatchers.IO)
            {
                books.postValue(mainRep.getFilteredBooks(input))
            }


        }
        Log.d("VocTrainer","MainViewModel mainRep.getBooks(input) = ${books.value}")

    }

    fun getBooks():LiveData<List<Book>>
    {
        return books
    }















}
