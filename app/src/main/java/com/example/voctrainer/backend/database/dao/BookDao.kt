package com.example.voctrainer.backend.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.voctrainer.backend.database.entities.Book

@Dao
interface BookDao
{

    @Insert
    fun insert(book: Book)

    @Delete
    fun deleteBook(book: Book)

    @Update
    fun updateBook(book: Book)

    @Query("SELECT * FROM Book")
    fun getAllBooks():LiveData<List<Book>>

    @Query("SELECT * FROM Book WHERE id == :bookId")
    fun getBookById(bookId:Long):LiveData<Book>


}