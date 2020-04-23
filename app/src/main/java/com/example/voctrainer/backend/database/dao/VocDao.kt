package com.example.voctrainer.backend.database.dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*


import com.example.voctrainer.backend.database.entities.Voc

@Dao
interface VocDao
{

    // ************   Einfügen   ************
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(voc:Voc)


    @Insert
    fun insertAll(vocs:List<Voc>)


    // ************   Updaten   ************
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(voc:Voc)

    @Update
    fun updateAll(vocs:List<Voc>)


    // ************   Löschen   ************
    @Delete
    fun delete(voc:Voc)

    @Delete
    fun deleteAll(vocs:List<Voc>)

    @Query("DELETE FROM Voc WHERE bookId == :bookId")
    fun clear(bookId: Long)


    // ************   Abfragen   ************

    @Query("SELECT * FROM Voc WHERE bookId == :bookId")
    fun getVocs(bookId:Long):LiveData<List<Voc>>

    @Query("SELECT * FROM Voc WHERE bookId == :bookId")
    fun getOfflineVocs(bookId:Long):List<Voc>

    @Query("SELECT * FROM Voc WHERE id == :vocId AND bookId == :bookId")
    fun getVocById(vocId:Long,bookId: Long): LiveData<Voc>

    @Query("SELECT * FROM Voc WHERE id == :vocId AND bookId == :bookId")
    fun getOfflineVocById(vocId:Long,bookId: Long): Voc

}