package com.example.laboratorioroom.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TareaDao {
    @Insert
    suspend fun insertar(tarea: Tarea)

    @Query("SELECT * FROM Tarea")
    suspend fun obtenerTareas(): List<Tarea>
}