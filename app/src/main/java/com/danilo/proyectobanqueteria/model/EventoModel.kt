package com.danilo.proyectobanqueteria.model

import android.net.Uri
import kotlin.math.roundToInt

data class EventoModel(
    val nombre: String = "",
    val rut: String = "",
    val empresa: String = "",
    val telefono: String = "",
    val email: String = "",
    val tipoEvento: String = "",
    val pastel: String = "",
    val fecha: String = "",
    val horario: String = "",
    val asistentes: Int = 0,
    val observaciones: String = "",

    val estadoAbono: String = "Pendiente",
    val comprobante: Uri? = null,
    val fechaPago: String= ""
){
    val esPasteleria: Boolean
        get() = tipoEvento == "Pastelería"
    val precioPersona: Int = when(tipoEvento){
            "Cóctel" -> 14000
            "Celebración" -> 20000
            "Matrimonio" -> 35000
            "Empresarial" -> 18500
            "Coffee Break" -> 8500
            else -> 0
        }
    val totalEvento: Int = (precioPersona * 1.19 * asistentes).roundToInt()
    val abonoEvento: Int = totalEvento/2
}