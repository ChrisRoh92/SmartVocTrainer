package com.example.voctrainer.backend.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true) val id:Long,
    var name:String,
    var timeStamp:String)