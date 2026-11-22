package com.danilo.proyectobanqueteria.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/** Comunicación con los microservicios existentes de cliente (8082) y catálogo (8081). */
object ClienteCatalogoApi {
    // Emulador Android: usar adb reverse para redirigir estos puertos al computador.
    private const val CLIENTES_URL = "http://127.0.0.1:8082/api/clientes"
    private const val CATALOGO_URL = "http://127.0.0.1:8081/api/catalogo"

    data class MenuRemoto(
        val idMenu: Long,
        val nombre: String,
        val detalle: String,
        val precio: Double?,
        val idCategoria: Long
    )

    suspend fun registrarCliente(
        nombre: String, rut: String, empresa: String, telefono: String, email: String
    ) = withContext(Dispatchers.IO) {
        val body = JSONObject().apply {
            put("nombre", nombre.trim())
            put("rut", rut.trim())
            put("empresa", empresa.trim().ifBlank { JSONObject.NULL })
            put("telefono", telefono.trim())
            put("email", email.trim())
        }
        request(CLIENTES_URL, "POST", body.toString())
    }

    suspend fun obtenerCatalogo(categoriaId: Long): List<MenuRemoto> = withContext(Dispatchers.IO) {
        val response = request("$CATALOGO_URL/categoria/$categoriaId", "GET")
        val json = JSONArray(response)
        buildList {
            for (i in 0 until json.length()) {
                val item = json.getJSONObject(i)
                add(MenuRemoto(
                    idMenu = item.optLong("idMenu"),
                    nombre = item.optString("nombre"),
                    detalle = item.optString("detalle"),
                    precio = if (item.isNull("precio")) null else item.optDouble("precio").takeIf { it > 0.0 },
                    idCategoria = item.optLong("idCategoria", categoriaId)
                ))
            }
        }
    }

    private fun request(url: String, method: String, body: String? = null): String {
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 10000
            readTimeout = 10000
            setRequestProperty("Accept", "application/json")
            if (body != null) {
                doOutput = true
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
            }
        }
        try {
            if (body != null) connection.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val response = stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
            if (code !in 200..299) {
                val detail = response.takeIf { it.isNotBlank() } ?: "HTTP $code"
                throw IllegalStateException("El servidor respondió $code: $detail")
            }
            return response
        } finally {
            connection.disconnect()
        }
    }
}
