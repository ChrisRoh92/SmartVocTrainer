package com.example.voctrainer.backend.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

// bookId: Id von dem VocBook
// itemCount: Anzahl von zu abfragenden Vokabeln
// TimeMode: false --> Ohne Timer ... true --> Mit Timer
// time: Zeit für Timer in sekunden
// practiseMod = 0 --> direct evaluation .... praciseMod = 1 --> end evaluation
// settingsMode = 0: Nur ungeübte, 1: Ungeübte und gelernte

@Entity
data class Setting(@PrimaryKey(autoGenerate = true) val id:Long,
                   val bookId:Long,
                   val itemCount:Int,
                   val timeMode:Boolean = false,
                   val time:Long = 0L,
                   val practiseMod:Int = 0,
                   val settingsMode:Int = 0)


/*
data class BookWithSettings(
    @Embedded val book:Book,
    @Relation(
        parentColumn = "id",
        entity = Settings::class,
        entityColumn = "bookId"
    )
    val settings:List<Settings>
)*/
