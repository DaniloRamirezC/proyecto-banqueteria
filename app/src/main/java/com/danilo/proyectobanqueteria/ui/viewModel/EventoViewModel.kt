package com.danilo.proyectobanqueteria.ui.viewModel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilo.proyectobanqueteria.data.local.entity.EventoEntity
import com.danilo.proyectobanqueteria.data.local.entity.database.EventDatabase
import com.danilo.proyectobanqueteria.model.EventoModel
import com.danilo.proyectobanqueteria.ui.natives.vibracion
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventoViewModel : ViewModel() {

    var uiState by mutableStateOf(EventoModel())

    var errorMensaje by mutableStateOf<String?>(null)
        private set
    var errorAsistentes by mutableStateOf(false)
        private set

    var rutBusqueda by mutableStateOf("")
        private set
    var listaReservas by mutableStateOf<List<EventoModel>>(emptyList())
        private set

    fun onNombreChanged(nombre: String) {
        uiState = uiState.copy(nombre = nombre)
        errorMensaje = null
    }

    fun onRutChanged(rut: String) {
        uiState = uiState.copy(rut = rut)
        errorMensaje = null
    }

    fun onEmpresaChanged(empresa: String) {
        uiState = uiState.copy(empresa = empresa)
    }

    fun onTelefonoChanged(telefono: String) {
        uiState = uiState.copy(telefono = telefono)
        errorMensaje = null
    }

    fun onEmailChanged(email: String) {
        uiState = uiState.copy(email = email)
        errorMensaje = null
    }

    fun onTipoEventoChanged(tipoEvento: String) {
        uiState = uiState.copy(tipoEvento = tipoEvento)
        errorMensaje = null
    }

    fun onfechaChanged(fecha: String) {
        uiState = uiState.copy(fecha = fecha)
        errorMensaje = null
    }

    fun onHorarioChanged(horario: String) {
        uiState = uiState.copy(horario = horario)
    }

    fun onAsistentes(asistentes: String) {
        val canti = asistentes.toIntOrNull() ?: 0
        uiState = uiState.copy(asistentes = canti)
        if (errorAsistentes) {
            errorAsistentes = false
        }
        errorMensaje = null
    }

    fun onObservacionesChanged(observaciones: String) {
        uiState = uiState.copy(observaciones = observaciones)
    }

    fun onComprobanteSelec(uri: Uri?) {
        if (uri != null) {
            val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(Date())
            uiState = uiState.copy(
                comprobante = uri,
                estadoAbono = "Pagado en 50%",
                fechaPago = fechaActual
            )
        }
    }

    fun onRutBusquedaChanged(rut: String) {
        rutBusqueda = rut
        errorMensaje = null
    }

    fun buscarReservasPorRut(context: Context, rut: String = rutBusqueda) {
        val rutLimpio = rut.trim()
        if (rutLimpio.isBlank()) {
            listaReservas = emptyList()
            errorMensaje = "Por favor, ingresa un RUT"
            return
        }

        viewModelScope.launch {
            val db = EventDatabase.getEventDatabase(context)
            val entidad: EventoEntity? = db.EventoDao().getEventByRut(rutLimpio)
            if (entidad != null) {
                errorMensaje = null
                listaReservas = listOf(
                    EventoModel(
                        nombre = entidad.nombre,
                        rut = entidad.rut,
                        empresa = entidad.empresa,
                        telefono = entidad.telefono,
                        email = entidad.email,
                        tipoEvento = entidad.tipoEvento,
                        fecha = entidad.fecha,
                        horario = entidad.horario,
                        asistentes = entidad.asistentes,
                        observaciones = entidad.observaciones
                    )
                )
            } else {
                listaReservas = emptyList()
                errorMensaje = "No se encontró ninguna reserva con ese RUT"
            }
        }
    }

    fun limpiarBusquedaAdmin() {
        rutBusqueda = ""
        listaReservas = emptyList()
        errorMensaje = null
    }

    fun onConfirmarClick(context: Context, onSuccess: () -> Unit) {
        val state = uiState
        when {
            state.nombre.isBlank() -> {
                errorMensaje = "Por favor, ingresa el nombre"
                vibracion(context)
                return
            }
            state.rut.isBlank() -> {
                errorMensaje = "Por favor, ingresa el Rut"
                vibracion(context)
                return
            }
            state.tipoEvento.isBlank() -> {
                errorMensaje = "Por favor, selecciona el tipo de evento"
                vibracion(context)
                return
            }
            state.telefono.isBlank() -> {
                errorMensaje = "Por favor, ingrese su teléfono"
                vibracion(context)
                return
            }
            state.fecha.isBlank() -> {
                errorMensaje = "Por favor, selecciona una fecha"
                vibracion(context)
                return
            }
            state.horario.isBlank() -> {
                errorMensaje = "Por favor, selecciona un horario"
                vibracion(context)
                return
            }
            state.asistentes !in 20..100 -> {
                errorAsistentes = true
                errorMensaje = "Cantidad de asistentes debe ser entre 20 y 100"
                vibracion(context)
                return
            }
            else -> {
                errorAsistentes = false
                if (state.empresa.isBlank()) {
                    uiState = uiState.copy(empresa = "No aplica")
                }
            }
        }

        viewModelScope.launch {
            val db = EventDatabase.getEventDatabase(context)
            val rutExistente = db.EventoDao().getEventByRut(state.rut)

            if (rutExistente != null) {
                errorMensaje = "Este Rut ya existe"
                vibracion(context)
            } else {
                val nuevoEvent = EventoEntity(
                    nombre = state.nombre,
                    rut = state.rut,
                    empresa = state.empresa.ifBlank { "No aplica" },
                    telefono = state.telefono,
                    email = state.email,
                    tipoEvento = state.tipoEvento,
                    fecha = state.fecha,
                    horario = state.horario,
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