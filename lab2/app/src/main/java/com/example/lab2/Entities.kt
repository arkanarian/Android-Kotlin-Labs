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

    @ColumnInfo(name = "duration_rest")
    var duration_rest: Int,

    @ColumnInfo(name = "repetitions")
    var repetitions: Int,

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

)
//    : Serializable {
//    @PrimaryKey(autoGenerate = true)
//    var id: Int? = null
//}


//@Entity(
//    foreignKeys = [ForeignKey(
//        entity = Timer::class,
//        parentColumns = "name",
//        childColumns = "timerId"
//    )]
//)
//class Phase {
//    @PrimaryKey(autoGenerate = true)
//    var id: Int? = null
//
//    @ColumnInfo(name = "name")
//    var name: String? = null
//
//    @ColumnInfo(name = "breed")
//    var breed: String? = null
//    var timerId : String? = null
//}