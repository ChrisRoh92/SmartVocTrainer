package com.example.voctrainer.backend.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.voctrainer.backend.database.entities.Test

@Dao
interface TestDao
{

    @Insert
    fun insert(test:Test)

    @Insert
    fun insertAll(tests:List<Test>)

    @Delete
    fun delete(test:Test)

    @Delete
    fun deleteAll(tests:List<Test>)

    @Update
    fun update(test:Test)

    @Update
    fun updateAll(tests:List<Test>)

    @Query("SELECT * FROM test WHERE ID == :bookID")
    fun getTests(bookID:Long): LiveData<List<Test>>

    @Query("SELECT * FROM test WHERE ID == :bookID")
    fun getOfflineTests(bookID:Long): List<Test>

    @Query("SELECT * FROM test WHERE id == :testID AND bookId == :bookId")
    fun getTestByID(testID:Long,bookId: Long): LiveData<Test>

    @Query("SELECT * FROM test WHERE ID == :testID AND bookId == :bookId")
    fun getOfflineTestByID(testID:Long,bookId: Long):Test
}