package com.example.voctrainer.moduls.csv_handler

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.example.voctrainer.BuildConfig
import com.example.voctrainer.backend.database.entities.Voc
import com.example.voctrainer.moduls.main.helper.LocalVoc
import kotlinx.coroutines.runBlocking
import java.io.*
import java.lang.StringBuilder

object CSVImportProcessor
{




    fun requestFileToArrayList(context: Context,uri: Uri):ArrayList<String> {

        var content:ArrayList<String> = ArrayList()




        val contentResolver = context.applicationContext.contentResolver
        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        // Check for the freshest data.
        //contentResolver.takePersistableUriPermission(uri, takeFlags)
        try {



            contentResolver.openInputStream(uri)?.use {inputstream ->
                BufferedReader(InputStreamReader(inputstream)).use { reader ->
                    var line:String? = reader.readLine()
                    while (line != null)
                    {
                        content.add(line)
                        line = reader.readLine()
                    }
                }
            }
        } catch (e:IOException)
        {
            Log.d("VocTrainer","IOException: ",e)
        }


        return content
    }

    fun createLocalVocsFromStrings(rawData:ArrayList<String>):ArrayList<LocalVoc>
    {
        var voc:ArrayList<LocalVoc> = ArrayList()
        for(i in rawData)
        {
            val splitRawData = i.split(";")
            voc.add(LocalVoc(splitRawData[0],splitRawData[1]))
        }

        return voc
    }

    fun createVocListFromLocalVoc(localVocs:ArrayList<LocalVoc>, bookID:Long):List<Voc>
    {
        var values:ArrayList<Voc> = ArrayList()
        for(i in localVocs)
        {
            values.add(Voc(0L,bookID,i.native,i.foreign,0 ,"-"))
        }

        return values

    }

}