package com.danilo.proyectobanqueteria.ui.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilo.proyectobanqueteria.data.local.entity.EventoEntity
import com.danilo.proyectobanqueteria.data.local.entity.database.EventDatabase
import com.danilo.proyectobanqueteria.model.EventoModel
import kotlinx.coroutines.launch
import kotlin.String

class EventoViewModel: ViewModel() {

    var uiState by mutableStateOf(EventoModel())
        private set
    var errorMensaje by mutableStateOf<String?>(null)
        private set
    var errorAsistentes by mutableStateOf(false)
        private set
    fun onNombreChanged(nombre: String){
        uiState = uiState.copy(nombre = nombre)
        errorMensaje = null
    }
    fun onRutChanged(rut: String){
        uiState = uiState.copy(rut = rut)
        errorMensaje = null
    }
    fun onEmpresaChanged(empresa: String){
        uiState = uiState.copy(empresa = empresa)
    }
    fun onTelefonoChanged(telefono: String){
        uiState = uiState.copy(telefono = telefono)
        errorMensaje = null
    }
    fun onEmailChanged(email: String){
        uiState = uiState.copy(email = email)
        errorMensaje = null
    }
    fun onTipoEventoChanged(tipoEvento: String){
        uiState = uiState.copy(tipoEvento = tipoEvento)
        errorMensaje = null
    }
    fun onfechaChanged(fecha: String){
        uiState = uiState.copy(fecha = fecha)
        errorMensaje = null
    }
    fun onAsistentes(asistentes: String){
        val canti = asistentes.toIntOrNull()?:0
        uiState = uiState.copy(asistentes = canti)
        if (errorAsistentes){
            errorAsistentes = false
        }
        errorMensaje = null
    }
    fun onObservacionesChanged(observaciones: String){
        uiState = uiState.copy(observaciones = observaciones)
    }
    fun onConfirmarClick(context: Context, onSuccess: ()-> Unit){
        val state = uiState
        when{
            state.nombre.isBlank() -> {
                errorMensaje = "Por favor, ingresa el nombre"
                return
            }
            state.rut.isBlank() ->{
                errorMensaje = "Por favor, ingresa el Rut"
                return
            }
            state.tipoEvento.isBlank() ->{
                errorMensaje = "Por favor, selecciona el tipo de evento"
                return
            }
            state.telefono.isBlank() ->{
                errorMensaje = "Por favor, ingrese su teléfono"
                return
            }
            state.fecha.isBlank() ->{
                errorMensaje = "Por favor, selecciona una fecha"
                return
            }
            state.asistentes !in 20..100->{
                errorAsistentes = true
                errorMensaje = "Cantidad de asistentes debe ser entre 20 y 100"
                return
            }else ->{
                errorAsistentes = false
                if (state.empresa.isBlank()){
                    uiState = uiState.copy(empresa = "No aplica")
                }
            }
        }
        viewModelScope.launch {
            val db = EventDatabase.getEventDatabase(context)
            val rutExistente = db.EventoDao().getEventByRut(state.rut)

            if(rutExistente!=null){
                errorMensaje = "Este Rut ya existe"
            }else{
                val nuevoEvent = EventoEntity(
                    nombre = state.nombre,
                    rut = state.rut,
                    empresa = state.empresa.ifBlank { "No aplica" },
                    telefono = state.telefono,
                    email = state.email,
                    tipoEvento = state.tipoEvento,
                    fecha = state.fecha,
                    asistentes = state.asistentes,
                    observaciones = state.observaciones
                )
                db.EventoDao().registerEvent(nuevoEvent)
                errorMensaje = null
                onSuccess()
            }

        }
    }
}