package com.example.voctrainer.backend.database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.voctrainer.backend.database.dao.BookDao
import com.example.voctrainer.backend.database.entities.Book
import com.example.voctrainer.backend.database.entities.Voc

@Database(entities = arrayOf(Book::class, Voc::class), version = 1,exportSchema = false)
abstract class BookDatabase:RoomDatabase()
{
    abstract fun bookDao(): BookDao

    companion object{

        @Volatile
        private var INSTANCE:BookDatabase? = null

        fun getInstance(context: Context):BookDatabase{
            synchronized(this)
            {
                var instance = INSTANCE
                if(instance == null)
                {
                    instance = Room.databaseBuilder(context.applicationContext,
                        BookDatabase::class.java,
                        "book_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }

                return instance
            }

        }


    }
}