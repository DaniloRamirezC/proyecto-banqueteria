package com.danilo.proyectobanqueteria.ui.viewModel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.danilo.proyectobanqueteria.model.LoginModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.danilo.proyectobanqueteria.data.local.entity.database.UserDatabase
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel(){
    var uiState by mutableStateOf(LoginModel())
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    fun onEmailChanged(email:String){
        uiState = uiState.copy(email = email)
        errorMessage = null
    }

    fun onPasswordChanged(password:String){
        uiState = uiState.copy(password = password)
        errorMessage = null
    }

    fun onLoginClick(context: Context, onSuccess: ()-> Unit){
        val email = uiState.email
        val password = uiState.password

        if(email.isBlank() || password.isBlank()){
            errorMessage = "Ingresa correo y contraseña"
            return
        }
        viewModelScope.launch{
            val db = UserDatabase.getDatabase(context)
            val user = db.UsuarioDao().getUserByEmail(email)

            if(user!=null && user.contrasena==password){
                errorMessage = null
                onSuccess()
            }else{
                errorMessage = "Correo o contraseña incorrectos"
            }
        }
    }
}