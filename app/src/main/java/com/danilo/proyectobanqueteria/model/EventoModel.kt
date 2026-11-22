package com.danilo.proyectobanqueteria.model

import android.net.Uri
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

data class EstimacionOperativa(
    val garzon: Int,
    val ayudanteCocina: Int,
    val totalEquipo: Int,
    val tramo: String,
    val detalleInsumo: String
)
data class EventoModel(
    val id: Int = 0,
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
    val comprobantePath: String = "",
    val fechaPago: String= "",
    val estadoRevision: String = "Pendiente"
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

    val estimacionOperativa: EstimacionOperativa
        get() {
            val cantiAsistentes = asistentes.coerceIn(10,100)
            val garzon = if(esPasteleria) 0 else max(2, ceil(cantiAsistentes/15.0).toInt())
            val ayudanteCocina = max(1,ceil(cantiAsistentes/25.0).toInt())
            val totalEquipo = garzon + ayudanteCocina

            val tramo: String
            val detalleInsumo: String

            if(cantiAsistentes<=20){
                tramo = "Tramo pequeño (hasta 20 personas)"
                detalleInsumo = if (esPasteleria) {
                    "Cajas de transporte térmico, bases de torta y bandejas descartables para 20 porciones."
                }else{
                    "1 set compacto de vajilla/cristalería (20 asistentes), 2 manteles principales y una estación de café"
                }
            }else if(cantiAsistentes<=50){
                tramo = "Tramo mediano (21 a 50 personas)"
                detalleInsumo = if(esPasteleria) {
                    "Embalaje reforzado de pastelería y 50 kits de platos/cubiertos de postre."
                }else {
                    "3 sets de vajilla/cristalería (50 asistentes), 5 manteles de salón, 2 chafer buffeteras y estación doble de bar/café"
                }
            }else{
                tramo = "Tramo evento completo (51 a 100 personas)"
                detalleInsumo = if(esPasteleria) {
                    "Logística refrigerada de pastelería y 100 kits de servicio."
                }else {
                    "6 sets completos de vajilla/cristalería (100 asistentes), 10 manteles, 4 chafer buffeteras y estación doble de bar/café"
                }
            }
            return EstimacionOperativa(
                garzon = garzon,
                ayudanteCocina = ayudanteCocina,
                totalEquipo = totalEquipo,
                tramo = tramo,
                detalleInsumo = detalleInsumo
            )
        }
}