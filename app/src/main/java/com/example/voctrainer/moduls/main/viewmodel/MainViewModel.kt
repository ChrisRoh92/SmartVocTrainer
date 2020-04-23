package com.example.voctrainer.moduls.main.viewmodel

import android.app.Application
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
    var books: LiveData<List<Book>> = mainRep.getBooks()



    fun onAddBook(name:String)
    {
        uiScope.launch {
            mainRep.insertNewBook(Book(0,name,createCurrentTimeStamp(),0,0,0))
        }
    }
    fun onUpdateBook(book:Book)
    {
        uiScope.launch {
            mainRep.updateBook(book)
        }
    }
    fun onDeleteBook(book:Book)
    {
        uiScope.launch {
            mainRep.deleteBook(book)
        }
    }













}
