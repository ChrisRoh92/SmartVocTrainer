package com.example.voctrainer.backend.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.voctrainer.backend.database.entities.BookWithSettings

@Dao
interface SettingsDao
{



    @Transaction
    @Query("SELECT * FROM book")
    fun getBookWithSettings(): List<BookWithSettings>
}