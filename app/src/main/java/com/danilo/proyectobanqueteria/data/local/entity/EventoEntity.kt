package com.danilo.proyectobanqueteria.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val rut: String,
    val empresa: String,
    val telefono: String,
    val email: String,
    val tipoEvento: String,
    val fecha: String,
    val horario: String,
    val asistentes: Int,
    val observaciones: String
)
