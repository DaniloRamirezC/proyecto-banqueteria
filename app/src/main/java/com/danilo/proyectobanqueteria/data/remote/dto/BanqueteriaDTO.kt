package com.danilo.proyectobanqueteria.data.remote.dto

import com.google.gson.annotations.SerializedName

//Catalogo (8081)
data class MenuDTO(
    val idMenu: Long = 0,
    val nombre: String = "",
    val detalle: String = "",
    val precio: Int = 0,
    val idCategoria: Long = 0
)

data class CategoriaDTO(
    val idCategoria: Long = 0,
    val nombreCategoria: String = ""
)

//Cliente (8082)
data class ClienteDTO(
    val idCliente: Long? = null,
    val nombre: String,
    val rut: String,
    val empresa: String?=null,
    val telefono: String,
    val email: String,
)

//Pedido (8083)
data class DetalleEventoDTO(
    val detalleEvento: Long? = null,
    val nombreItem: String,
    val precio: Int,
    val menuId: Long,
)

data class EventoDTO(
    val idEvento: Long? = null,
    val tipoEvento: String,
    val fecha: String,
    val horario: String,
    val cantiPersonas: Int,
    val observaciones: String? = null,
    val clienteId: Long,
    val detalles: List<DetalleEventoDTO> = emptyList()
)

//Administrador (8084)
data class AdministradorDTO(
    val idAdmin: Long? = null,
    val nombreCompleto: String,
    val cargo: String,
    val usuarioId: Long
)

data class EstimacionOperativaDTO(
    val tipoEvento: String,
    val cantiAsistentes: Int,
    val garzones: Int,
    val ayudantesCocina: Int,
    val totalPersonal: Int,
    val tramoInsumos: String,
    val detalleInsumos: String
)

data class ValidacionPagoDTO(
    val idValidacion: Long?= null,
    val idAdmin: Long?= null,
    val idComprobante: Long? = null,
    val idEvento: Long?= null,
    val estadoValidacion: Boolean,
    val observacionAdmin: String? = null,
    val fechaRevision: String? = null
)

//Autenticación (8085)
data class LoginRequestDTO(
    val correo: String,
    val contrasena: String
)

data class RegistroRequestDTO(
    val nombreUsuario: String,
    val correo: String,
    val contrasena: String,
    val nombreRol: String
)

data class AuthResponseDTO(
    val token: String,
    val idUsuario: Long,
    val nombreUsuario: String,
    val correo: String,
    val rol: String
)

//Resumen (8086)
data class ResumenDTO(
    val idResumen: Long? = null,
    @SerializedName("id_evento") val idEvento: Long,
    @SerializedName("precio_persona") val precio: Int,
    @SerializedName("canti_personas") val cantiPersonas: Int,
    val subtotal: Int?= null,
    val iva: Int?=null,
    val total: Int?=null,
    @SerializedName("monto_abono") val montoAbono: Int?=null
)

data class ResumenRefDTO(
    val idResumen: Long
)

data class ComprobanteDTO(
    @SerializedName("id_comprobante") val idComprobante: Long?=null,
    val resumen: ResumenRefDTO,
    val total: Int?=null,
    val abono: Int?=null,
    val estadoAbono: String?=null,
    @SerializedName("url_comprobante") val urlComprobante: String?=null,
    @SerializedName("fecha_registro") val fechaRegistro: String?=null
)

