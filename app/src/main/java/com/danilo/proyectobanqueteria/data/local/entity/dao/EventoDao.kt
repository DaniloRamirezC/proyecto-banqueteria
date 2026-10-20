package com.danilo.proyectobanqueteria.data.local.entity.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.danilo.proyectobanqueteria.data.local.entity.EventoEntity

@Dao
interface EventoDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerEvent(event: EventoEntity)

    @Query("SELECT * FROM events WHERE rut = :rut LIMIT 1")
    suspend fun getEventByRut(rut: String): EventoEntity?

}