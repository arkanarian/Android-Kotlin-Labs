package com.example.lab2

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface PhaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPhase(phase: Phase)

    @Update
    suspend fun updatePhase(phase: Phase)

    @Delete
    suspend fun deletePhase(phase: Phase)

    @Query("DELETE FROM PhaseTable WHERE timerId=:id")
    suspend fun deletePhasesByTimerId(id: Int?)

    @Query("SELECT * FROM PhaseTable")
    fun getPhases(): Flow<List<Phase>>

    @Query("SELECT * FROM PhaseTable WHERE timerId=:id")
    fun getPhasesByTimerId(id: Int?): Flow<List<Phase>>

    @Query("SELECT * FROM PhaseTable WHERE id=:id")
    suspend fun getPhaseById(id: Int): Phase
}