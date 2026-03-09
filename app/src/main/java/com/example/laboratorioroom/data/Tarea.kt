package com.example.laboratorioroom.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tarea(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val descripcion: String
)