package com.example.BDDFinal.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlanningDao {
    @Insert
    suspend fun insertPlanning(planning: Planning): Long

    @Update
    suspend fun updatePlanning(planning: Planning)

    @Query("SELECT * FROM plannings WHERE userId = :userId")
    suspend fun getPlanningForUser(userId: Int): List<Planning>
}
