package com.danilo.proyectobanqueteria.data.remote.api

import com.danilo.proyectobanqueteria.data.remote.dto.ComprobanteDTO
import com.danilo.proyectobanqueteria.data.remote.dto.ResumenDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ResumenApiService {

    @GET("api/resumenes")
    suspend fun listarResumenes(): List<ResumenDTO>

    @GET("api/resumenes/{id}")
    suspend fun obtenerResumen(@Path("id") id: Long): ResumenDTO

    @GET("api/resumenes/evento/{idEvento}")
    suspend fun listarResumenPorEvento(@Path("idEvento") idEvento: Long): List<ResumenDTO>

    @POST("api/resumenes")
    suspend fun crearResumen(@Body resumen: ResumenDTO): ResumenDTO

    @GET("api/comprobantes")
    suspend fun listarComprobante(): List<ComprobanteDTO>

    @POST("api/comprobantes")
    suspend fun registrarComprobante(@Body comprobante: ComprobanteDTO): ComprobanteDTO

    @PATCH("api/comprobantes/{id}/estado")
    suspend fun actualizarEstadoAbono(
        @Path("id") id: Long,
        @Body body: Map<String, String>
    ): ComprobanteDTO
}