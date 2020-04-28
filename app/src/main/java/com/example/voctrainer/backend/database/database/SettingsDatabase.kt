package com.example.voctrainer.backend.database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.voctrainer.backend.database.dao.SettingsDao
import com.example.voctrainer.backend.database.entities.Setting

@Database(entities = [Setting::class],version = 1)
abstract class SettingsDatabase:RoomDatabase()
{
    abstract fun settingsDao():SettingsDao

    companion object
    {
        @Volatile
        private var INSTANCE:SettingsDatabase? = null
        fun getInstance(context: Context):SettingsDatabase
        {
            synchronized(this)
            {
                var instance = INSTANCE
                if(instance == null)
                {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SettingsDatabase::class.java,
                        "settings_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }
}