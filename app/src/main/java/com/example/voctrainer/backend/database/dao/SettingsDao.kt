package com.example.voctrainer.backend.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.voctrainer.backend.database.entities.Setting

@Dao
interface SettingsDao
{

    @Insert
    fun insert(setting: Setting)

    @Insert
    fun insertAll(settings:List<Setting>)

    @Delete
    fun delete(setting: Setting)

    @Delete
    fun deleteAll(settings: List<Setting>)

    @Update
    fun update(setting: Setting)

    @Update
    fun updateAll(settings: List<Setting>)

    // Liste erhalten:
    @Query("SELECT * FROM setting WHERE ID == :bookID")
    fun getSettings(bookID:Long):LiveData<List<Setting>>

    @Query("SELECT * FROM setting WHERE ID == :bookID")
    fun getOfflineSettings(bookID:Long):List<Setting>

    // Settings by ID:
    @Query("SELECT * FROM setting WHERE id == :settingsID AND bookId == :bookId")
    fun getSettingsByID(settingsID:Long,bookId: Long):LiveData<Setting>

    // Offline Settings by ID:
    @Query("SELECT * FROM setting WHERE id == :settingsID AND bookId == :bookId")
    fun getOfflineSettingsByID(settingsID:Long,bookId: Long):Setting

    @Query("SELECT * FROM setting WHERE bookId == :bookId ORDER BY id DESC LIMIT 1 ")
    fun getLastSetting(bookId: Long):Setting



}