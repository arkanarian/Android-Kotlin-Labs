package com.example.lab2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface TimerDao {
    @Insert
    fun insertAll(vararg people: Timer?)

    @Delete
    fun delete(person: Timer?)

    @get:Query("SELECT * FROM TimerTable")
    val allPeople: List<Any?>?

    @Query("SELECT * FROM TimerTable WHERE duration LIKE :color")
    fun getAllPeopleWithFavoriteColor(color: String?): List<Timer?>?
}