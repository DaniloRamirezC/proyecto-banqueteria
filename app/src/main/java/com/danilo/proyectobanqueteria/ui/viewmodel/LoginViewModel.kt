package com.danilo.proyectobanqueteria.ui.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilo.proyectobanqueteria.data.BanqueteriaApi
import com.danilo.proyectobanqueteria.data.local.entity.UsuarioEntity
import com.danilo.proyectobanqueteria.data.local.entity.database.UserDatabase
import com.danilo.proyectobanqueteria.model.LoginModel
import com.danilo.proyectobanqueteria.ui.natives.vibracion
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var tokenJwt by mutableStateOf<String?>(null)
        private set
    var uiState by mutableStateOf(LoginModel())
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var usuarioActual by mutableStateOf<UsuarioEntity?>(null)
        private set
    fun onEmailChanged(email: String) {
        uiState = uiState.copy(email = email)
        errorMessage = null
    }
    fun onPasswordChanged(password: String) {
        uiState = uiState.copy(password = password)
        errorMessage = null
    }
    fun esAdmin(): Boolean {
        val rol = usuarioActual?.rol?.trim().orEmpty()
        return rol.equals("administrador", ignoreCase = true) ||
                rol.equals("admin", ignoreCase = true)
    }
    fun onLoginClick(context: Context, onSuccess: () -> Unit) {
        val email = uiState.email.trim()
        val password = uiState.password
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Ingresa correo y contraseña"
            vibracion(context)
            return
        }
        viewModelScope.launch {
            try {
                val respuesta = BanqueteriaApi.login(email,password)
                tokenJwt = respuesta.token

                val userRemoto = UsuarioEntity(
                    id = respuesta.idUsuario?.toInt() ?: 0,
                    nombre = respuesta.nombreUsuario,
                    email = respuesta.correo,
                    contrasena = password,
                    rol = respuesta.rol
                )
                usuarioActual = userRemoto

                val db = UserDatabase.getDatabase(context)
                if(db.UsuarioDao().getUserByEmail(email) == null){
                    db.UsuarioDao().registerUser(userRemoto)
                }

                errorMessage = null
                onSuccess()

            }catch (e: Exception){
                usuarioActual = null
                tokenJwt = null
                val mensaje = e.message.orEmpty()
                errorMessage = when{
                    mensaje.contains("401") || mensaje.contains("403") || mensaje.contains("404") ->
                        "Correo o contraseña incorrectos"
                    mensaje.contains("Unable to resolve", true) || mensaje.contains("failed to connect", true) || mensaje.contains("Connection refused", true) ->
                        "No se pudo conectar con el servicio de autenticación"
                    else -> "Error al iniciar sesión: ${mensaje.take(150)}"
                }
                vibracion(context)
            }
        }
    }
}