package com.danilo.proyectobanqueteria.data.remote.api

import com.danilo.proyectobanqueteria.data.remote.dto.AdministradorDTO
import com.danilo.proyectobanqueteria.data.remote.dto.EstimacionOperativaDTO
import com.danilo.proyectobanqueteria.data.remote.dto.ValidacionPagoDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AdminApiService {

    @GET("api/administrador")
    suspend fun listarAdministradores(): List<AdministradorDTO>

    @GET("api/administrador/usuario/{usuarioId}")
    suspend fun obtenerPorIdUsuario(@Path("usuarioId") usuarioId: Long): AdministradorDTO

    @GET("api/administrador/estimacion-operativa")
    suspend fun obtenerEstimacion(
        @Query("asistentes") asistentes: Int,
        @Query("tipoEvento") tipoEvento: String = "Banquetería"
    ): EstimacionOperativaDTO

    @POST("api/administrador/{idAdmin}/validar-comprobante")
    suspend fun validarComprobante(
        @Path("idAdmin") idAdmin: Long,
        @Body validacion: ValidacionPagoDTO
    ): ValidacionPagoDTO

    @GET("api/administrador/{idAdmin}/validaciones")
    suspend fun listarValidaciones(@Path("idAdmin") idAdmin: Long): List<ValidacionPagoDTO>
}