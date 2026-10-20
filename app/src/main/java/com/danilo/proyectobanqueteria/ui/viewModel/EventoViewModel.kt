package com.danilo.proyectobanqueteria.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.danilo.proyectobanqueteria.model.EventoModel

class EventoViewModel: ViewModel() {

    var uiState by mutableStateOf(EventoModel())
        private set
    var errorAsistentes by mutableStateOf(false)
        private set

    fun onNombreChanged(nombre: String){
        uiState = uiState.copy(nombre = nombre)
    }
    fun onRutChanged(rut: String){
        uiState = uiState.copy(rut = rut)
    }
    fun onEmpresaChanged(empresa: String){
        uiState = uiState.copy(empresa = empresa)
    }
    fun onTelefonoChanged(telefono: String){
        uiState = uiState.copy(telefono = telefono)
    }
    fun onEmailChanged(email: String){
        uiState = uiState.copy(email = email)
    }
    fun onTipoEventoChanged(tipoEvento: String){
        uiState = uiState.copy(tipoEvento = tipoEvento)
    }
    fun onfechaChanged(fecha: String){
        uiState = uiState.copy(fecha = fecha)
    }
    fun onAsistentes(asistentes: String){
        val canti = asistentes.toIntOrNull()?:0
        uiState = uiState.copy(asistentes = canti)
        if (errorAsistentes){
            errorAsistentes = false
        }
    }
    fun onObservacionesChanged(observaciones: String){
        uiState = uiState.copy(observaciones = observaciones)
    }
    fun onConfirmarClick(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        when{
            uiState.nombre.isBlank() ->onError("Por favor, ingresa el nombre")
            uiState.rut.isBlank() ->onError("Por favor, ingresa el Rut")
            uiState.tipoEvento.isBlank() ->onError("Por favor, selecciona el tipo de evento")
            uiState.telefono.isBlank() ->onError("Por favor, ingrese su teléfono")
            uiState.fecha.isBlank() ->onError("Por favor, selecciona una fecha")
            uiState.asistentes !in 21..<101 ->{
                errorAsistentes = true
                onError("Cantidad de asistentes debe ser entre 20 y 100")
            }else ->{
                errorAsistentes = false
                if (uiState.empresa.isBlank()){
                    uiState = uiState.copy(empresa = "No aplica")
                }
            println("Evento registrado con éxito: $uiState")
            println("Total a pagar: ${uiState.totalEvento}")
            onSuccess()
            }
        }

    }
}