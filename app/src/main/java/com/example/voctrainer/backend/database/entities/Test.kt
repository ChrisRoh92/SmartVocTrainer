package com.example.voctrainer.backend.database.entities


import androidx.room.Entity
import androidx.room.PrimaryKey


// solutions: Vom Nutzer eingegebene Lösungen
// itemsCorrect: Anzahl Korrekt (Damit das nicht jedesmal neu gerechnet werden muss)
// result: Richtig in Prozent
// settingsId: Verwendete Test Einstellung ID)
// itemsIds: IDs der verwendeten Vocs...

@Entity
data class Test(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0L,

    var timeStamp:String = "",

    var bookId:Long = 0L,

    var itemIds:ArrayList<Long> = ArrayList(),

    var solutions:ArrayList<String> = ArrayList(),

    var itemsCorrect:Int = 2,

    var result:Float = 44f,

    var settingsID:Long = 0L
)



/*
data class BookWithTests(
    @Embedded val book:Book,
    @Relation(
        parentColumn = "id",
        entity = Test::class,
        entityColumn = "bookId"
    )
    val tests:List<Test>
)*/
