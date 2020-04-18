package com.example.voctrainer.moduls.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.example.voctrainer.backend.database.database.BookDatabase
import com.example.voctrainer.backend.database.entities.Book
import com.example.voctrainer.createCurrentTimeStamp
import kotlinx.coroutines.*

class MainViewModel(application: Application) : AndroidViewModel(application)
{

    private var db:BookDatabase =
        Room.databaseBuilder(application.applicationContext,BookDatabase::class.java,"book_datebase").build()
    private var bookDao = db.bookDao()

    // Coroutine Stuff:
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    // Alle Bücher:
    var books = bookDao.getAllBooks()


    fun onAddBook(name: String)
    {
        uiScope.launch {
            insert(Book(0,name,createCurrentTimeStamp()))
        }
    }

    fun onUpdateBook(book:Book)
    {
        uiScope.launch {
            update(book)
        }
    }

    fun onDeleteBook(book:Book)
    {
        uiScope.launch {
            delete(book)
        }
    }




    private suspend fun insert(book: Book)
    {
        withContext(Dispatchers.IO)
        {
            bookDao.insert(book)
        }
    }

    private suspend fun update(book: Book)
    {
        withContext(Dispatchers.IO)
        {
            bookDao.updateBook(book)
        }
    }

    private suspend fun delete(book:Book)
    {
        withContext(Dispatchers.IO)
        {
            bookDao.deleteBook(book)
        }
    }






}
