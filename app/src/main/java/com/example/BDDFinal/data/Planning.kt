package com.example.BDDFinal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plannings")
data class Planning(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val slot1: String,
    val slot2: String,
    val slot3: String,
    val slot4: String,
    val date: String? = null
)
