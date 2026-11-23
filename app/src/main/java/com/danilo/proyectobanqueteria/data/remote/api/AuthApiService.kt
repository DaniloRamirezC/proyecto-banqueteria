package com.danilo.proyectobanqueteria.data.remote.api

import com.danilo.proyectobanqueteria.data.remote.dto.AuthResponseDTO
import com.danilo.proyectobanqueteria.data.remote.dto.LoginRequestDTO
import com.danilo.proyectobanqueteria.data.remote.dto.RegistroRequestDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/auth/registro")
    suspend fun registrar(@Body request: RegistroRequestDTO): AuthResponseDTO

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDTO): AuthResponseDTO
}