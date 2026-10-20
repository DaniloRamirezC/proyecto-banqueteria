package com.danilo.proyectobanqueteria.ui.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.danilo.proyectobanqueteria.model.LoginModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class LoginViewModel : ViewModel(){
    var uiState by mutableStateOf(LoginModel())
        private set

    fun onEmailChanged(email:String){
        uiState = uiState.copy(email = email)
    }

    fun onPasswordChanged(password:String){
        uiState = uiState.copy(password = password)
    }

    fun onLoginClick(){
        val email = uiState.email
        val password = uiState.password

        println("Iniciando sesión con Email: $email y contraseña: $password")
    }
}