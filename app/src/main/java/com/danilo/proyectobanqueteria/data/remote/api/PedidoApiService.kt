package com.danilo.proyectobanqueteria.data.remote.api

import com.danilo.proyectobanqueteria.data.remote.dto.EventoDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PedidoApiService {

    @GET("api/eventos")
    suspend fun listarEventos(): List<EventoDTO>

    @GET("api/eventos/{id}")
    suspend fun obtenerEvento(@Path("id") id: Long): EventoDTO

    @GET("api/eventos/cliente/{clienteId}")
    suspend fun obtenerEventosPorCliente(@Path("clienteId") clienteId: Long): List<EventoDTO>

    @POST("api/eventos")
    suspend fun crearEvento(@Body evento: EventoDTO): EventoDTO
}