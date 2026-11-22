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
import kotlin.String

class EventoViewModel: ViewModel() {

    var uiState by mutableStateOf(EventoModel())

    var errorMensaje by mutableStateOf<String?>(null)
        private set
    var errorAsistentes by mutableStateOf(false)
        private set
    var rutBusqueda by mutableStateOf("")
        private set
    var listaReservas by mutableStateOf<List<EventoModel>>(emptyList())
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
        val esPasteleria = tipoEvento == "Pastelería"
        uiState = uiState.copy(
            tipoEvento = tipoEvento,
            pastel = if(esPasteleria) uiState.pastel else "",
            asistentes = if(esPasteleria) 0 else uiState.asistentes,
            horario = if(esPasteleria && uiState.horario.startsWith("Noche")) "" else uiState.horario
        )
        errorAsistentes = false
        errorMensaje = null
    }
    fun onPastelChanged(pastel: String){
        uiState = uiState.copy(pastel = pastel)
        errorMensaje = null
    }
    fun onfechaChanged(fecha: String){
        uiState = uiState.copy(fecha = fecha)
        errorMensaje = null
    }
    fun onHorarioChanged(horario: String){
        uiState = uiState.copy(horario = horario)
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
    fun onComprobanteSelec(context: Context,uri: Uri?){
        if(uri!=null){
            val fechaActual = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm",
                java.util.Locale.getDefault()).format(java.util.Date())

            val nuevoEstadoAbono = if (uiState.esPasteleria) "Completado" else "Pagado en 50%"

            uiState = uiState.copy(
                comprobante = uri,
                estadoAbono = nuevoEstadoAbono,
                fechaPago = fechaActual
            )

            viewModelScope.launch {
                val rutaGuardada = com.danilo.proyectobanqueteria.ui.natives.guardarComprobante(context,uri) ?: ""

                if(rutaGuardada.isNotEmpty()){
                    val db = EventDatabase.getEventDatabase(context)
                    db.EventoDao().actualizarComprobantePorRut(
                        rut = uiState.rut,
                        path = rutaGuardada,
                        estadoAbono =nuevoEstadoAbono,
                        fechaPago = fechaActual
                    )
                }
            }
        }
    }
    fun onConfirmarClick(context: Context, onSuccess: ()-> Unit){
        val state = uiState
        when{
            state.nombre.isBlank() -> {
                errorMensaje = "Por favor, ingresa el nombre"
                vibracion(context)
                return
            }
            state.rut.isBlank() ->{
                errorMensaje = "Por favor, ingresa el Rut"
                vibracion(context)
                return
            }
            state.tipoEvento.isBlank() ->{
                errorMensaje = "Por favor, selecciona el tipo de evento"
                vibracion(context)
                return
            }
            state.esPasteleria && state.pastel.isBlank() ->{
                errorMensaje = "Por favor, selecciona un pastel del catálogo"
                vibracion(context)
                return
            }
            state.telefono.isBlank() ->{
                errorMensaje = "Por favor, ingrese su teléfono"
                vibracion(context)
                return
            }
            state.fecha.isBlank() ->{
                errorMensaje = "Por favor, selecciona una fecha"
                vibracion(context)
                return
            }
            state.horario.isBlank()->{
                errorMensaje = "Por favor, selecciona un horario"
                vibracion(context)
                return
            }
            !state.esPasteleria &&(state.asistentes !in 10..100 || state.asistentes %10 !=0) ->{
                errorAsistentes = true
                errorMensaje = "Por favor, selecciona la cantidad de asistentes"
                vibracion(context)
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
                vibracion(context)
            }else{
                val nuevoEvent = EventoEntity(
                    nombre = state.nombre,
                    rut = state.rut,
                    empresa = state.empresa.ifBlank { "No aplica" },
                    telefono = state.telefono,
                    email = state.email,
                    tipoEvento = state.tipoEvento,
                    pastel = if(state.esPasteleria) state.pastel else "",
                    fecha = state.fecha,
                    horario = state.horario,
                    asistentes = state.asistentes,
                    observaciones = state.observaciones,
                    comprobantePath = "",
                    estadoAbono = state.estadoAbono,
                    fechaPago = state.fechaPago,
                    estadoRevision = "Pendiente"
                )
                db.EventoDao().registerEvent(nuevoEvent)
                errorMensaje = null
                onSuccess()
            }
        }
    }
    fun onRutBusquedaChanged(rut: String) {
        rutBusqueda = rut
        errorMensaje = null
    }

    fun cargarReservas(context: Context){
        viewModelScope.launch {
            val db = EventDatabase.getEventDatabase(context)
            db.EventoDao().getAllEvents().collect { entities ->
                val resultado = mutableListOf<EventoModel>()
                for (i in entities.indices){
                    resultado.add(mapearEntidadModel(entities[i]))
                }
                listaReservas = resultado
            }
        }
    }

    fun buscarReservasPorRut(context: Context, rut: String = rutBusqueda) {
        val rutLimpio = rut.trim()
        if (rutLimpio.isBlank()) {
            cargarReservas(context)
            return
        }

        viewModelScope.launch {
            val db = EventDatabase.getEventDatabase(context)
            val entidad: EventoEntity? = db.EventoDao().getEventByRut(rutLimpio)
            if (entidad != null) {
                errorMensaje = null
                listaReservas = listOf(mapearEntidadModel(entidad))
            } else {
                listaReservas = emptyList()
                errorMensaje = "No se encontró ninguna reserva con ese RUT"
            }
        }
    }

    fun cambiarEstadoRevision(context: Context, eventoId: Int, estadoNuevo: String){
        viewModelScope.launch {
            val db = EventDatabase.getEventDatabase(context)
            db.EventoDao().actualizarEstadoRevision(eventoId,estadoNuevo)

            val actualizado = mutableListOf<EventoModel>()
            for (i in listaReservas.indices){
                val item = listaReservas[i]
                if(item.id == eventoId){
                    actualizado.add(item.copy(estadoRevision = estadoNuevo))
                }else{
                    actualizado.add(item)
                }
            }
            listaReservas = actualizado
        }
    }

    fun limpiarBusquedaAdmin() {
        rutBusqueda = ""
        listaReservas = emptyList()
        errorMensaje = null
    }

    private fun mapearEntidadModel(entidad: EventoEntity): EventoModel{
        return EventoModel(
            id = entidad.id,
            nombre = entidad.nombre,
            rut = entidad.rut,
            empresa = entidad.empresa,
            telefono = entidad.telefono,
            email = entidad.email,
            tipoEvento = entidad.tipoEvento,
            pastel = entidad.pastel,
            asistentes = entidad.asistentes,
            observaciones = entidad.observaciones,
            comprobantePath = entidad.comprobantePath,
            estadoAbono = entidad.estadoAbono,
            fechaPago = entidad.fechaPago,
            estadoRevision = entidad.estadoRevision
        )
    }
}