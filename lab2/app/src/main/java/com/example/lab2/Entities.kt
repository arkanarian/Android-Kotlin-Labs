package com.example.lab2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "TimerTable")
data class Timer (
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "color")
    var color: String,

    @ColumnInfo(name = "duration")
    var duration: Int,

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

)
//    : Serializable {
//    @PrimaryKey(autoGenerate = true)
//    var id: Int? = null
//}


@Entity(
    tableName = "PhaseTable",
    foreignKeys = [ForeignKey(
        entity = Timer::class,
        parentColumns = ["id"],
        childColumns = ["timerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Phase (

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "duration")
    var duration: Int,

    @ColumnInfo(name = "duration_rest")
    var duration_rest: Int,

    @ColumnInfo(name = "repetitions")
    var repetitions: Int,

    @ColumnInfo(name = "timerId")
    var timerId: Int?,

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)