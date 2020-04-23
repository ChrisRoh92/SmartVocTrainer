package com.example.voctrainer.backend.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

// solutions: Vom Nutzer eingegebene Lösungen
// itemsCorrect: Anzahl Korrekt (Damit das nicht jedesmal neu gerechnet werden muss)
// result: Richtig in Prozent
// settingsId: Verwendete Test Einstellung ID)
// itemsIds: IDs der verwendeten Vocs...

@Entity
data class Test(
    @PrimaryKey(autoGenerate = true) val id:Long,
    val bookId:Long,
    var itemIds:ArrayList<Long>,
    var solutions:ArrayList<String>,
    var itemsCorrect:Int,
    var result:Float,
    var settingsID:Long)



data class BookWithTests(
    @Embedded val book:Book,
    @Relation(
        parentColumn = "id",
        entity = Test::class,
        entityColumn = "bookId"
    )
    val tests:List<Test>
)