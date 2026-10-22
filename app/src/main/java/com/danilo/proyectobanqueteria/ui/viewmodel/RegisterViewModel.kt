package com.danilo.proyectobanqueteria.ui.viewModel

import android.util.Log
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.danilo.proyectobanqueteria.model.RegisterModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.danilo.proyectobanqueteria.data.local.entity.UsuarioEntity
import com.danilo.proyectobanqueteria.data.local.entity.database.UserDatabase
import com.danilo.proyectobanqueteria.ui.natives.vibracion
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel(){

    var uiState by mutableStateOf(RegisterModel())
        private set

    fun onNameChanged(name: String){
        uiState = uiState.copy(name=name, errorMessage = null)
    }

    fun onEmailChanged(email: String){
        uiState = uiState.copy(email=email, errorMessage = null)
    }

    fun onPasswordChanged(password: String){
        uiState = uiState.copy(password=password, errorMessage = null)
    }

    fun onConfirmPasswordChanged(confirmPassword: String){
        uiState = uiState.copy(confirmPassword=confirmPassword, errorMessage = null)
    }

    fun onRegisterClick(context: Context, onSuccess:() -> Unit){
        val state = uiState

        if(state.name.isBlank() || state.email.isBlank() || state.password.isBlank()){
            uiState = uiState.copy(errorMessage = "Por favor completa todos los campos")
            vibracion(context)
            return
        }

        if(state.password != state.confirmPassword){
            uiState = uiState.copy(errorMessage = "Las contraseñas no coinciden")
            vibracion(context)
            return
        }

        viewModelScope.launch {
            val db = UserDatabase.getDatabase(context)
            val existingUser = db.UsuarioDao().getUserByEmail(state.email)

            if(existingUser!=null){
                uiState = uiState.copy(errorMessage = "El correo ya está registrado")
                vibracion(context)
            }else{
                val nuevoUser = UsuarioEntity(
                    nombre = state.name,
                    email = state.email,
                    contrasena = state.password
                )
                db.UsuarioDao().registerUser(nuevoUser)
                uiState = uiState.copy(errorMessage = null)
                onSuccess()
            }
        }
    }
}