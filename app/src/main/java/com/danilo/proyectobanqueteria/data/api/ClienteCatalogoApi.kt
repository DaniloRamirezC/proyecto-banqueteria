package com.danilo.proyectobanqueteria.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/** Comunicación con los microservicios existentes de cliente (8082) y catálogo (8081). */
object ClienteCatalogoApi {
    private const val HOST = "10.0.2.2"
    private const val CLIENTES_URL = "http://$HOST:8082/api/clientes"
    private const val CATALOGO_URL = "http://$HOST:8081/api/catalogo"
    private const val PEDIDOS_URL = "http://$HOST:8083/api/pedidos"
    private const val ADMIN_URL = "http://$HOST:8084/api/administrador"
    private const val AUTENTICACION_URL = "http://$HOST:8085/api/auth"
    private const val RESUMEN_URL = "http://$HOST:8086/api/resumenes"

    data class MenuRemoto(
        val idMenu: Long,
        val nombre: String,
        val detalle: String,
        val precio: Int?,
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
                add(
                    MenuRemoto(
                        idMenu = item.optLong("idMenu"),
                        nombre = item.optString("nombre"),
                        detalle = item.optString("detalle"),
                        precio = if (item.isNull("precio")) null else item.optInt("precio")
                            .takeIf { it > 0.0 },
                        idCategoria = item.optLong("idCategoria", categoriaId)
                    )
                )
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

    suspend fun registrarPedido(
        nombre: String,
        rut: String,
        empresa: String,
        telefono: String,
        email: String,
        tipoEvento: String,
        pastel: String,
        fecha: String,
        horario: String,
        asistentes: Int,
        observaciones: String
    ) = withContext(Dispatchers.IO){
        val body = JSONObject().apply {
            put("nombreCliente", nombre.trim())
            put("rutCliente", rut.trim())
            put("empresa", empresa.ifBlank { "No aplica" })
            put("telefono", telefono.trim())
            put("email", email.trim())
            put("tipoEvento", tipoEvento)
            put("pastel", pastel)
            put("fechaEvento", fecha)
            put("horario", horario)
            put("asistentes", asistentes)
            put("observaciones", observaciones)
        }
        request(PEDIDOS_URL, "POST", body.toString())
    }

    suspend fun actualizarEstadoAdmin(
        eventoId: Int,
        estadoRevision: String
    )= withContext(Dispatchers.IO){
        val body = JSONObject().apply {
            put("estadoRevision", estadoRevision)
        }
        request("$ADMIN_URL/eventos/$eventoId/estado", "PUT", body.toString())
    }

    data class AuthResponse(
        val token: String,
        val email: String,
        val rol: String
    )

    suspend fun loginRemoto(email: String, contrasena: String): AuthResponse = withContext(
        Dispatchers.IO){
        val body = JSONObject().apply {
            put("correo", email.trim())
            put("contrasena",contrasena)
        }
        val response = request("$AUTENTICACION_URL/login", "POST", body.toString())
        val json = JSONObject(response)
        AuthResponse(
            token = json.optString("token"),
            email = json.optString("email", email.trim()),
            rol = json.optString("rol", "CLIENTE")
        )
    }

    suspend fun registrarUsuarioAuth(
        nombre: String,
        email: String,
        contrasena: String,
        rol: String
    )= withContext(Dispatchers.IO){
        val body = JSONObject().apply {
            put("nombreUsuario", nombre.trim())
            put("correo", email.trim())
            put("contrasena", contrasena) //
            put("nombreRol", rol)
        }
        request("$AUTENTICACION_URL/registro", "POST", body.toString())
    }

    data class ResumenPedidoRemoto(
        val idResumen: Long,
        val rutCliente: String,
        val nombreCliente: String,
        val tipoEvento: String,
        val totalEstimado: Int,
        val estadoPago: String
    )

    suspend fun obtenerResumenPorRut(rut: String): ResumenPedidoRemoto = withContext(Dispatchers.IO) {
        val response = request("$RESUMEN_URL/cliente/${rut.trim()}", "GET")
        val json = JSONObject(response)
        ResumenPedidoRemoto(
            idResumen = json.optLong("idResumen"),
            rutCliente = json.optString("rutCliente", rut.trim()),
            nombreCliente = json.optString("nombreCliente"),
            tipoEvento = json.optString("tipoEvento"),
            totalEstimado = json.optInt("totalEstimado", 0),
            estadoPago = json.optString("estadoPago", "Pendiente")
        )
    }
}