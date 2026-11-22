package com.danilo.proyectobanqueteria.model

data class RegisterModel(
    val name: String = "",
    val rut: String = "",
    val company: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val errorMessage: String? = null
)
