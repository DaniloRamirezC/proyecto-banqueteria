package com.danilo.proyectobanqueteria.data.local.entity.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.danilo.proyectobanqueteria.data.local.entity.EventoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventoDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerEvent(event: EventoEntity)

    @Query("SELECT * FROM events WHERE rut = :rut LIMIT 1")
    suspend fun getEventByRut(rut: String): EventoEntity?

    @Query("SELECT * FROM events ORDER BY id DESC")
    fun getAllEvents(): Flow<List<EventoEntity>>

    @Query("UPDATE events SET estadoRevision =:nuevoEstado WHERE id =:eventoId")
    suspend fun actualizarEstadoRevision(eventoId: Int, nuevoEstado: String)

    @Query("UPDATE events SET comprobantePath =:path, estadoAbono = :estadoAbono, fechaPago =:fechaPago, estadoRevision ='Pendiente' WHERE rut =:rut")
    suspend fun actualizarComprobantePorRut(
        rut: String,
        path: String,
        estadoAbono: String,
        fechaPago: String
    )

}