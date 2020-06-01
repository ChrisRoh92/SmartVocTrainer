package com.example.voctrainer.moduls.csv_handler

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.runBlocking
import java.io.FileNotFoundException
import java.io.InputStream

class CSVImportProcessor(var context: Context, var uri: Uri)
{




    fun requestFileToArrayList():ArrayList<String>? {

        var content:ArrayList<String> = ArrayList()
        runBlocking {
            var inputStream: InputStream? = null

            try {
                inputStream = context.contentResolver.openInputStream(uri)
                inputStream!!.bufferedReader().forEachLine { content.add(it) }
            } catch (e: FileNotFoundException)
            {
                e.printStackTrace()
                Log.d("CSVExample","Sorry, File not found")
            }
        }


        return content
    }

}