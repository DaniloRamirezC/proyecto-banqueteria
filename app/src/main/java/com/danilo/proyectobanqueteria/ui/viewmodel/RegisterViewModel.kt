package com.danilo.proyectobanqueteria.ui.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.danilo.proyectobanqueteria.model.RegisterModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

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

    fun onRegisterClick(onSuccess:() -> Unit){
        val state = uiState

        if(state.name.isBlank() || state.email.isBlank() || state.password.isBlank()){
            uiState = uiState.copy(errorMessage = "Por favor completa todos los campos")
            return
        }

        if(state.password != state.confirmPassword){
            uiState = uiState.copy(errorMessage = "Las contraseñas no coinciden")
            return
        }

        Log.d("RegisterVM", "Usuario registrado: ${state.name} (${state.email})")
        uiState = uiState.copy(errorMessage = null)
        onSuccess()

    }
}