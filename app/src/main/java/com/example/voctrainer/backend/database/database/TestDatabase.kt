package com.example.voctrainer.backend.database.database

import android.content.Context
import androidx.room.*
import com.example.voctrainer.backend.database.converter.TestConverter
import com.example.voctrainer.backend.database.dao.TestDao
import com.example.voctrainer.backend.database.entities.Test


@Database(entities = [Test::class],version = 1)
@TypeConverters(TestConverter::class)
abstract class TestDatabase:RoomDatabase()
{
    abstract fun testDao():TestDao


    companion object
    {
        private val INSTANCE:TestDatabase? = null

        fun getInstance(context: Context):TestDatabase
        {
            synchronized(this)
            {
                var instance = INSTANCE
                if(instance == null)
                {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TestDatabase::class.java,
                        "test_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }


        }
    }

}

