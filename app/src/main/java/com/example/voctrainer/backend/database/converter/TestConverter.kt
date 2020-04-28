package com.example.voctrainer.backend.database.converter

import android.util.Log
import androidx.room.TypeConverter
import java.lang.Exception


class TestConverter
{
    @TypeConverter
    fun longArraylistToString(values:ArrayList<Long>):String
    {
        return values.joinToString(";")
    }

    @TypeConverter
    fun stringToLongArrayList(item:String):ArrayList<Long>
    {
        var values = item.split(";")
        var items:ArrayList<Long> = ArrayList()
        for(i in values)
        {
            try {
                items.add(i.toLong())
            }catch (e:Exception)
            {
                Log.e("VocTrainer",e.printStackTrace().toString())
                e.printStackTrace()
            }
        }

        return items
    }



    @TypeConverter
    fun stringArraylistToString(values:ArrayList<String>):String
    {
        return values.joinToString(";")
    }

    @TypeConverter
    fun stringToStringArrayList(item:String):ArrayList<String>
    {
        var values = item.split(";")
        var items:ArrayList<String> = ArrayList()
        for(i in values)
        {
            try {
                items.add(i)
            }catch (e:Exception)
            {
                Log.e("VocTrainer",e.printStackTrace().toString())
                e.printStackTrace()
            }
        }

        return items
    }
}