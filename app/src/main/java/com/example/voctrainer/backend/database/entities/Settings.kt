package com.example.voctrainer.backend.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

// TimeMode: false --> Ohne Timer ... true --> Mit Timer
// time: Zeit für Timer in sekunden
// practiseMod = 0 --> direct evaluation .... praciseMod = 1 --> end evaluation

@Entity
data class Settings(@PrimaryKey(autoGenerate = true) val id:Long,
                    val bookId:Long,
                    val itemCount:Int,
                    val timeMode:Boolean = false,
                    val time:Long = 0L,
                    val practiseMod:Int = 0)


data class BookWithSettings(
    @Embedded val book:Book,
    @Relation(
        parentColumn = "id",
        entity = Settings::class,
        entityColumn = "bookId"
    )
    val settings:List<Settings>
)