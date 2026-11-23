package com.danilo.proyectobanqueteria.data.remote.api

import com.danilo.proyectobanqueteria.data.remote.dto.CategoriaDTO
import com.danilo.proyectobanqueteria.data.remote.dto.MenuDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface CatalogoApiService {
    @GET("api/catalogo")
    suspend fun listarMenus(): List<MenuDTO>

    @GET("api/catalogo/{id}")
    suspend fun obtenerMenu(@Path("id") id: Long): MenuDTO

    @GET("api/catalogo/categoria/{idCategoria}")
    suspend fun obtenerPorCategoria(@Path("idCategoria") idCategoria: Long): List<MenuDTO>

    @GET("api/categorias")
    suspend fun listarCategorias(): List<CategoriaDTO>
}