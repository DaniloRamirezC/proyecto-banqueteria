package com.danilo.proyectobanqueteria.ui.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilo.proyectobanqueteria.data.BanqueteriaApi
import com.danilo.proyectobanqueteria.data.api.ClienteCatalogoApi
import com.danilo.proyectobanqueteria.data.local.entity.UsuarioEntity
import com.danilo.proyectobanqueteria.data.local.entity.database.UserDatabase
import com.danilo.proyectobanqueteria.model.RegisterModel
import com.danilo.proyectobanqueteria.ui.natives.vibracion
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    var uiState by mutableStateOf(RegisterModel())
        private set
    var isLoading by mutableStateOf(false)
        private set

    fun onNameChanged(value: String) { uiState = uiState.copy(name = value, errorMessage = null) }
    fun onRutChanged(value: String) { uiState = uiState.copy(rut = value, errorMessage = null) }
    fun onEmpresaChanged(value: String) { uiState = uiState.copy(empresa = value, errorMessage = null) }
    fun onTelefonoChanged(value: String) { uiState = uiState.copy(telefono = value, errorMessage = null) }
    fun onEmailChanged(value: String) { uiState = uiState.copy(email = value, errorMessage = null) }
    fun onPasswordChanged(value: String) { uiState = uiState.copy(password = value, errorMessage = null) }
    fun onConfirmPasswordChanged(value: String) { uiState = uiState.copy(confirmPassword = value, errorMessage = null) }

    fun onRegisterClick(context: Context, onSuccess: () -> Unit) {
        val state = uiState
        val email = state.email.trim()
        if (state.name.isBlank() || email.isBlank() || state.password.isBlank()) {
            uiState = uiState.copy(errorMessage = "Por favor completa todos los campos")
            vibracion(context)
            return
        }
        val esAdmin = email.endsWith("@paolicias.cl", ignoreCase = true)
        if (!esAdmin && (state.rut.isBlank() || state.telefono.isBlank())) {
            uiState = uiState.copy(errorMessage = "Completa tu RUT y teléfono para registrar tus datos de cliente")
            vibracion(context)
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            uiState = uiState.copy(errorMessage = "Ingresa un correo electrónico válido")
            vibracion(context)
            return
        }
        if (state.password != state.confirmPassword) {
            uiState = uiState.copy(errorMessage = "Las contraseñas no coinciden")
            vibracion(context)
            return
        }
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            try {
                val db = UserDatabase.getDatabase(context)
                val existingUser = db.UsuarioDao().getUserByEmail(email)
                if (existingUser != null) {
                    uiState = uiState.copy(errorMessage = "El correo ya está registrado")
                    vibracion(context)
                    return@launch
                }

                BanqueteriaApi.registrarUsuario(
                    nombreUsuario = state.name,
                    correo = email,
                    contrasena = state.password,
                    esAdmin = esAdmin
                )
                // Las cuentas CLIENTE crean primero su perfil en el microservicio.
                // Las cuentas ADMIN siguen usando el registro local ya existente.
                if (!esAdmin) {
                    ClienteCatalogoApi.registrarCliente(
                        nombre = state.name,
                        rut = state.rut,
                        empresa = state.empresa,
                        telefono = state.telefono,
                        email = email
                    )
                }
                val nuevoUser = UsuarioEntity(
                    nombre = state.name.trim(),
                    email = email,
                    contrasena = state.password,
                    rol = if (esAdmin) "ADMIN" else "CLIENTE"
                )
                db.UsuarioDao().registerUser(nuevoUser)
                uiState = uiState.copy(errorMessage = null)
                onSuccess()
            } catch (e: Exception) {
                val message = e.message.orEmpty()
                uiState = uiState.copy(errorMessage = when {
                    message.contains("Unable to resolve", true) || message.contains("failed to connect", true) || message.contains("Connection refused", true) ->
                        "No se pudo conectar con el servidor. Verifica que los microservicios estén encendidos."
                    message.contains("duplicate", true) || message.contains("unique", true) || message.contains("409", true) ->
                        "El RUT o correo ya está registrado en el sistema."
                    else -> "No se pudo registrar el cliente: ${message.take(180)}"
                })
                vibracion(context)
            } finally {
                isLoading = false
            }
        }
    }
}
