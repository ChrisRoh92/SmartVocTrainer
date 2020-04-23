package com.example.voctrainer.backend.database.entities

import androidx.room.*


// status -->   0: Nicht geübt; 1: in Übung; 2: Geübt

@Entity
data class Voc(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0L,

    @ColumnInfo(name = "bookId")
    var bookId:Long = -1L,

    @ColumnInfo(name = "native2")
    var native2:String = "",

    @ColumnInfo(name = "foreign")
    var foreign:String = "",

    @ColumnInfo(name = "status")
    var status:Int = 0,

    @ColumnInfo(name = "lastPractiseDate")
    var lastPractiseDate:String = ""



               )





