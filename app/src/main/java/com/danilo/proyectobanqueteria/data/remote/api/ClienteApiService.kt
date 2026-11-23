package com.danilo.proyectobanqueteria.data.remote.api

import com.danilo.proyectobanqueteria.data.remote.dto.ClienteDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ClienteApiService {

    @GET("api/clientes")
    suspend fun listarClientes(): List<ClienteDTO>

    @GET("api/clientes/{id}")
    suspend fun obtenerCliente(@Path("id") id: Long): ClienteDTO

    @POST("api/clientes")
    suspend fun crearCliente(@Body cliente: ClienteDTO): ClienteDTO

    @PUT("api/clientes/{id}")
    suspend fun actualizarCliente(@Path("id") id: Long, @Body cliente: ClienteDTO): ClienteDTO

    @DELETE("api/clientes/{id}")
    suspend fun eliminarCliente(@Path("id") id: Long): Response<Unit>
}