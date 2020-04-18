package com.example.voctrainer.backend.database.dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.voctrainer.backend.database.entities.BookWithSettings

import com.example.voctrainer.backend.database.entities.Voc

@Dao
interface VocDao
{

    // ************   Einfügen   ************
    @Insert
    fun insert(voc:Voc)


    @Insert
    fun insertAll(vocs:List<Voc>)


    // ************   Updaten   ************
    @Update
    fun update(voc:Voc)

    @Update
    fun updateAll(vocs:List<Voc>)


    // ************   Löschen   ************
    @Delete
    fun delete(voc:Voc)

    @Delete
    fun deleteAll(vocs:List<Voc>)

    @Query("DELETE FROM voc_table WHERE bookId == :bookId")
    fun clear(bookId: Long)


    // ************   Abfragen   ************

    @Query("SELECT * FROM voc_table WHERE bookId == :bookId")
    fun getVocs(bookId:Long):LiveData<List<Voc>>

    @Query("SELECT * FROM voc_table WHERE id == :vocId AND bookId == :bookId")
    fun getVocById(vocId:Long,bookId: Long): LiveData<Voc>

}