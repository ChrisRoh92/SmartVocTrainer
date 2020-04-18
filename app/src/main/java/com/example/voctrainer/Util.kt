package com.example.voctrainer

import java.text.SimpleDateFormat
import java.util.*


fun createCurrentTimeStamp():String
{
    var date = Date()
    val formatter = SimpleDateFormat("dd.MM.yyyy - HH.mm.ss")

    return formatter.format(date)
}

