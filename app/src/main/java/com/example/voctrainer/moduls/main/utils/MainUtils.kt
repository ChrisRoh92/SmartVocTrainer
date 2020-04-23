package com.example.voctrainer.moduls.main.utils

import android.util.Log
import com.example.voctrainer.backend.database.entities.Book
import com.example.voctrainer.backend.database.entities.Voc
import kotlin.math.roundToInt


fun getBookStatus(vocs:ArrayList<Voc>):BookStatus
{
    Log.e("VocTrainer","MainUtils.kt - getBookStatus vocs = $vocs")
    val vocCount = vocs.size
    var vocLearned = 0
    var vocUnlearned = 0
    for(i in vocs)
    {
        if(i.status == 0 || i.status == 1)
        {
            vocUnlearned++
        }
        else if(i.status == 2)
        {
            vocLearned++

        }
    }

    var progress =
        if (vocCount == 0)
        {
        0
        }
        else
        {
            (vocLearned.toFloat()/vocCount).roundToInt() * 100
        }

    return BookStatus(vocCount,vocLearned,vocUnlearned,progress)

}

fun getBookProgress(book: Book):Int
{
    Log.e("VocTrainer" ,"MainUtils.kt - getBookProgress() book = $book")
    return if (book.vocCount == 0)
        {
            0
        }
        else
        {
            ((book.vocLearned.toFloat()/book.vocCount)* 100).roundToInt()
            Log.e("VocTrainer" ,"MainUtils.kt - getBookProgress() book = ${((book.vocLearned.toFloat()/book.vocCount)* 100).roundToInt()}")
        }
}