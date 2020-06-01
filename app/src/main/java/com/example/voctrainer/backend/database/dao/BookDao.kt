package com.example.voctrainer.backend.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.voctrainer.backend.database.entities.Book
import com.example.voctrainer.backend.database.entities.BookWithVocs

@Dao
interface BookDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: Book)

    @Insert
    fun insertAll(books:List<Book>)

    @Delete
    fun delete(book: Book)

    @Delete
    fun deleteAll(books:List<Book>)

    @Update
    fun update(book: Book)

    @Update
    fun updateAll(books:List<Book>)

    @Query("SELECT * FROM Book ORDER BY id DESC")
    fun getBooks():LiveData<List<Book>>

    //
    //@Query("SELECT * FROM Book WHERE name LIKE :input ORDER BY id DESC")
    @Query("SELECT * FROM Book WHERE name LIKE '%' || :input || '%' ORDER BY id DESC")
    fun getFilteredBooks(input:String):List<Book>

    // Offline - no LiveData:
    @Query("SELECT * FROM Book ORDER BY id DESC")
    fun getOfflineBooks():List<Book>


    @Query("SELECT * FROM Book WHERE name LIKE :item")
    fun getAllBooksSearched(item:String):LiveData<List<Book>>



    @Transaction
    @Query("SELECT * FROM Book ORDER BY id ASC")
    fun getBooksWithVocs():LiveData<List<BookWithVocs>>

    @Query("SELECT * FROM Book WHERE id == :bookId")
    fun getBookById(bookId:Long):LiveData<Book>

    @Query("SELECT * FROM Book WHERE id == :bookId")
    fun getOfflineBookById(bookId:Long):Book








}