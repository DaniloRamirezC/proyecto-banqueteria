package com.danilo.proyectobanqueteria.data

import com.danilo.proyectobanqueteria.data.api.ClienteCatalogoApi
import com.danilo.proyectobanqueteria.data.remote.RetrofitClient
import com.danilo.proyectobanqueteria.data.remote.dto.AuthResponseDTO
import com.danilo.proyectobanqueteria.data.remote.dto.ClienteDTO
import com.danilo.proyectobanqueteria.data.remote.dto.ComprobanteDTO
import com.danilo.proyectobanqueteria.data.remote.dto.DetalleEventoDTO
import com.danilo.proyectobanqueteria.data.remote.dto.EstimacionOperativaDTO
import com.danilo.proyectobanqueteria.data.remote.dto.EventoDTO
import com.danilo.proyectobanqueteria.data.remote.dto.LoginRequestDTO
import com.danilo.proyectobanqueteria.data.remote.dto.RegistroRequestDTO
import com.danilo.proyectobanqueteria.data.remote.dto.ResumenDTO
import com.danilo.proyectobanqueteria.data.remote.dto.ResumenRefDTO
import com.danilo.proyectobanqueteria.data.remote.dto.ValidacionPagoDTO
import retrofit2.HttpException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object BanqueteriaApi {

    suspend fun obtenerCatalogo(categoriaId: Long): List<ClienteCatalogoApi.MenuRemoto>{
        return try {
            RetrofitClient.catalogoApi.obtenerPorCategoria(categoriaId).map { dto ->
                ClienteCatalogoApi.MenuRemoto(
                    idMenu = dto.idMenu,
                    nombre = dto.nombre,
                    detalle = dto.detalle,
                    precio = dto.precio.toInt().takeIf { it > 0 },
                    idCategoria = dto.idCategoria
                )
            }
        }catch (e: HttpException){
            throw mapHttpException(e)
        }
    }

    suspend fun registrarCliente(
        nombre: String,
        rut: String,
        empresa: String,
        telefono: String,
        email: String
    ): ClienteDTO {
        return try {
            RetrofitClient.clienteApi.crearCliente(
                ClienteDTO(
                    nombre = nombre.trim(),
                    rut = rut.trim(),
                    empresa = empresa.trim().ifBlank { null },
                    telefono = telefono.trim(),
                    email = email.trim()
                )
            )
        }catch (e: HttpException){
            throw mapHttpException(e)
        }
    }

    suspend fun obtenerClientePorRut(rut: String): ClienteDTO?{
        return try {
            RetrofitClient.clienteApi.listarClientes()
                .firstOrNull{it.rut.equals(rut.trim(), ignoreCase = true)}
        }catch (e: HttpException){
            throw mapHttpException(e)
        }
    }

    suspend fun login(correo: String, contrasena: String): AuthResponseDTO{
        return try{
            RetrofitClient.authApi.login(
                LoginRequestDTO(correo = correo.trim(), contrasena = contrasena)
            )
        }catch (e: HttpException){
            throw mapHttpException(e)
        }
    }

    suspend fun registrarUsuario(
        nombreUsuario: String,
        correo: String,
        contrasena: String,
        esAdmin: Boolean
    ): AuthResponseDTO{
        return try{
            RetrofitClient.authApi.registrar(
                RegistroRequestDTO(
                    nombreUsuario = nombreUsuario.trim(),
                    correo = correo.trim(),
                    contrasena = contrasena,
                    nombreRol = if(esAdmin) "ADMIN" else "CLIENTE"
                )
            )
        }catch (e: HttpException){
            throw mapHttpException(e)
        }
    }

    suspend fun crearEvento(
        tipoEvento: String,
        fecha: String,
        horario: String,
        cantiPersonas: Int,
        observaciones: String,
        clienteId: Long,
        detalles: List<DetalleEventoDTO> = emptyList()
    ): EventoDTO{
        return try{
            val fechaIso = convertirFechaIso(fecha)
            RetrofitClient.pedidoApi.crearEvento(
                EventoDTO(
                    tipoEvento = tipoEvento,
                    fecha = fechaIso,
                    horario = horario,
                    cantiPersonas = if(cantiPersonas<=0) 1 else cantiPersonas,
                    observaciones = observaciones.ifBlank { null },
                    clienteId = clienteId,
                    detalles = detalles
                )
            )
        }catch (e: HttpException){
            throw mapHttpException(e)
        }
    }

    suspend fun listarEventoPorCliente(clienteId: Long): List<EventoDTO>{
        return try{
            RetrofitClient.pedidoApi.obtenerEventosPorCliente(clienteId)
        }catch (e: HttpException){
            throw mapHttpException(e)
        }
    }

    suspend fun crearResumen(
        idEvento: Long,
        precio: Int,
        cantiPersonas: Int
    ): ResumenDTO{
        return try{
            RetrofitClient.resumenApi.crearResumen(
                ResumenDTO(
                    idEvento = idEvento,
                    precio = precio,
                    cantiPersonas = if(cantiPersonas<=0) 1 else cantiPersonas
                )
            )
        }catch (e: HttpException){
            throw mapHttpException(e)
        }
    }

    suspend fun registrarComprobante(
        idResumen: Long,
        estadoAbono: String,
        urlComprobante: String
    ): ComprobanteDTO{
        return try{
            RetrofitClient.resumenApi.registrarComprobante(
                ComprobanteDTO(
                    resumen = ResumenRefDTO(idResumen = idResumen),
                    estadoAbono = estadoAbono,
                    urlComprobante = urlComprobante
                )
            )
        }catch (e: HttpException){
            throw mapHttpException(e)
        }
    }

    suspend fun obtenerEstimacion(
        cantiAsistentes: Int,
        tipoEvento: String
    ): EstimacionOperativaDTO{
        return try{
            RetrofitClient.adminApi.obtenerEstimacion(
                asistentes = cantiAsistentes,
                tipoEvento = tipoEvento
            )
        }catch (e: HttpException){
            throw mapHttpException(e)
        }
    }

    suspend fun validarComprobante(
        idAdmin: Long,
        idComprobante: Long,
        idEvento: Long,
        aprobado: Boolean,
        observacion: String = ""
    ): ValidacionPagoDTO{
        return try{
            RetrofitClient.adminApi.validarComprobante(
                idAdmin = idAdmin,
                validacion = ValidacionPagoDTO(
                    idComprobante = idComprobante,
                    idEvento = idEvento,
                    estadoValidacion = aprobado,
                    observacionAdmin = observacion.ifBlank { null }
                )
            )
        }catch (e: HttpException){
            throw mapHttpException(e)
        }
    }

    private fun convertirFechaIso(fecha: String): String{
        return try{
            val entrada = DateTimeFormatter.ofPattern("d/M/yyyy")
            val salida = DateTimeFormatter.ISO_LOCAL_DATE
            LocalDate.parse(fecha.trim(), entrada).format(salida)
        }catch (_: Exception){
            fecha.trim()
        }
    }

    private fun mapHttpException(e: HttpException): IllegalStateException{
        val errorBody = e.response()?.errorBody()?.string().orEmpty()
        val detalle = errorBody.ifBlank { "HTTP ${e.code()}" }
        return IllegalStateException("El servidor respondió ${e.code()}: $detalle")
    }

}