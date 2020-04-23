package com.example.voctrainer.backend.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true) val id:Long,
    var name:String,
    var timeStamp:String,
    var vocCount:Int,
    var vocLearned:Int,
    var vocUnLearned:Int)


data class BookWithVocs(
    @Embedded val book:Book,
    @Relation(
        parentColumn = "id",
        entity = Voc::class,
        entityColumn = "bookId"
    )
    var vocs:List<Voc>
)