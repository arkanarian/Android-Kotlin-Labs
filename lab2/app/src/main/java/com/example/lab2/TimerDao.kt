package com.example.lab2

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface TimerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTimer(timer: Timer)

    @Update
    suspend fun updateTimer(timer: Timer)

    @Delete
    suspend fun deleteTimer(timer: Timer)

    @Query("SELECT * FROM TimerTable")
    fun getTimers(): Flow<List<Timer>>

    @Query("SELECT * FROM TimerTable WHERE id=:id")
    fun getTimerById(id: Int): Timer

    @Query("SELECT * FROM TimerTable WHERE duration LIKE :color")
    fun getAllTimersWithFavoriteColor(color: String?): List<Timer>
}