package com.danilo.proyectobanqueteria.data.remote

import com.danilo.proyectobanqueteria.data.remote.api.AdminApiService
import com.danilo.proyectobanqueteria.data.remote.api.AuthApiService
import com.danilo.proyectobanqueteria.data.remote.api.CatalogoApiService
import com.danilo.proyectobanqueteria.data.remote.api.ClienteApiService
import com.danilo.proyectobanqueteria.data.remote.api.PedidoApiService
import com.danilo.proyectobanqueteria.data.remote.api.ResumenApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val HOST = "184.197.196.174"

    private const val URL_CATALOGO = "http://$HOST:8081/"
    private const val URL_CLIENTE  = "http://$HOST:8082/"
    private const val URL_PEDIDO   = "http://$HOST:8083/"
    private const val URL_ADMIN    = "http://$HOST:8084/"
    private const val URL_AUTH     = "http://$HOST:8085/"
    private const val URL_RESUMEN  = "http://$HOST:8086/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .writeTimeout(3, TimeUnit.SECONDS)
        .build()

    private fun buildRetrofit(baseUrl: String): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val catalogoApi: CatalogoApiService by lazy {
        buildRetrofit(URL_CATALOGO).create(CatalogoApiService::class.java)
    }

    val clienteApi: ClienteApiService by lazy {
        buildRetrofit(URL_CLIENTE).create(ClienteApiService::class.java)
    }

    val pedidoApi: PedidoApiService by lazy {
        buildRetrofit(URL_PEDIDO).create(PedidoApiService::class.java)
    }

    val adminApi: AdminApiService by lazy {
        buildRetrofit(URL_ADMIN).create(AdminApiService::class.java)
    }

    val authApi: AuthApiService by lazy {
        buildRetrofit(URL_AUTH).create(AuthApiService::class.java)
    }

    val resumenApi: ResumenApiService by lazy {
        buildRetrofit(URL_RESUMEN).create(ResumenApiService::class.java)
    }

}