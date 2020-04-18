package com.example.voctrainer.backend.database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.voctrainer.backend.database.dao.VocDao
import com.example.voctrainer.backend.database.entities.Voc

@Database(entities = [Voc::class],version = 1,exportSchema = false)
abstract class VocDataBase:RoomDatabase()
{
    abstract fun vocDao():VocDao

    companion object{

        @Volatile
        private var INSTANCE:VocDataBase? = null

        fun getInstance(context: Context):VocDataBase
        {
            synchronized(this)
            {
                var instance = INSTANCE
                if(instance == null)
                {
                    instance = Room.databaseBuilder(context.applicationContext,
                        VocDataBase::class.java,
                        "Voc_Database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }

    }

}