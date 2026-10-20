package com.danilo.proyectobanqueteria.model

import kotlin.math.roundToInt

data class EventoModel(
    val nombre: String = "",
    val rut: String = "",
    val empresa: String = "",
    val telefono: String = "",
    val email: String = "",
    val tipoEvento: String = "",
    val fecha: String = "",
    val horario: String = "",
    val asistentes: Int = 0,
    val observaciones: String = ""
){
    val precioPersona: Int = when(tipoEvento){
            "Cóctel" -> 14000
            "Celebración" -> 20000
            "Matrimonio" -> 35000
            "Empresarial" -> 18500
            "Coffe Break" -> 8500
            else -> 0
        }
    val totalEvento: Int = (precioPersona * 1.19 * asistentes).roundToInt()
}