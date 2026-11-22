package com.danilo.proyectobanqueteria.ui.screen

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danilo.proyectobanqueteria.model.EventoModel
import com.danilo.proyectobanqueteria.ui.components.Footer
import com.danilo.proyectobanqueteria.ui.components.Header
import java.io.File
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AdminScreen(
    rutBusqueda: String,
    reservas: List<EventoModel>,
    onRutChanged: (String) -> Unit,
    onBuscarPorRut: () -> Unit,
    onAprueboPago: (Int) -> Unit,
    onRechazoPago: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val moneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))

    Scaffold(
        topBar = { Header(title = "Panel Administrador") },
        bottomBar = { Footer() }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE6C196))
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reservas (${reservas.size})",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFCB522E)
                )

                OutlinedButton(
                    onClick = onLogout,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFFCB522E)
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Cerrar sesión"
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Cerrar sesión",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = rutBusqueda,
                        onValueChange = onRutChanged,
                        label = { Text("Buscar por RUT") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFCB522E),
                            focusedLabelColor = Color(0xFFCB522E)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onBuscarPorRut,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57E41)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Buscar reservas", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (reservas.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay reservas registradas para mostrar",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                reservas.forEachIndexed { index, evento ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Reserva #${index + 1} · ${evento.tipoEvento.ifBlank { "Evento" }}",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFCB522E)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            DetalleEvento("Cliente: ", evento.nombre)
                            DetalleEvento("RUT: ", evento.rut)
                            if (evento.empresa.isNotBlank()) {
                                DetalleEvento("Empresa: ", evento.empresa)
                            }
                            if (evento.telefono.isNotBlank()) {
                                DetalleEvento("Teléfono: ", evento.telefono)
                            }
                            if (evento.email.isNotBlank()) {
                                DetalleEvento("Email: ", evento.email)
                            }
                            DetalleEvento("Fecha: ", evento.fecha)
                            if (evento.horario.isNotBlank()) {
                                DetalleEvento("Horario: ", evento.horario)
                            }
                            DetalleEvento("Asistentes: ", "${evento.asistentes} personas")
                            if (evento.observaciones.isNotBlank()) {
                                DetalleEvento("Observaciones: ", evento.observaciones)
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                            Text(
                                text = "Total estimado: ${moneda.format(evento.totalEvento)}",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFCB522E)
                            )
                            Text(
                                text = "Abono (50%): ${moneda.format(evento.abonoEvento)}",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF2E7D32)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            DetalleEvento("Estado abono: ", evento.estadoAbono)
                            if (evento.fechaPago.isNotBlank()) {
                                DetalleEvento("Fecha registro: ", evento.fechaPago)
                            }
                            DetalleEvento("Estado revisión: ", evento.estadoRevision)

                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                            val estimacion = evento.estimacionOperativa
                            Text(
                                text = "Estimación Operativa",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFCB522E)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            DetalleEvento("Tramo: ", estimacion.tramo)
                            DetalleEvento("Garzones requeridos: ", "${estimacion.garzon}")
                            DetalleEvento("Ayudantes de cocina: ", "${estimacion.ayudanteCocina}")
                            DetalleEvento("Total staff: ", "${estimacion.totalEquipo} personas")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Insumos: ${estimacion.detalleInsumo}",
                                fontSize = 13.sp,
                                color = Color(0xFF50372B)
                            )

                            if(evento.comprobantePath.isNotBlank()){
                                val archivo = remember(evento.comprobantePath) {
                                    File(evento.comprobantePath)
                                }
                                if(archivo.exists()){
                                    val bitmap = remember(evento.comprobantePath) {
                                        BitmapFactory.decodeFile(archivo.absolutePath)?.asImageBitmap()
                                    }
                                    if(bitmap!=null){
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = "Comprobante adjunto:",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF503372B)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Image(
                                            bitmap = bitmap,
                                            contentDescription = "Comprobante de transferencia",
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(200.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0xFFF5F5F5))
                                        )
                                    }
                                }
                            }else{
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Sin comprobante adjunto aún",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = {onAprueboPago(evento.id)},
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Aprobar", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                Button(
                                    onClick = {onRechazoPago(evento.id)},
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ){
                                    Text("Rechazar", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminScreenPreview() {
    AdminScreen(
        rutBusqueda = "18.456.789-2",
        reservas = listOf(
            EventoModel(
                nombre = "Camila Rojas",
                rut = "18.456.789-2",
                empresa = "Eventos Sur",
                telefono = "+56 9 8765 4321",
                email = "camila@ejemplo.cl",
                tipoEvento = "Matrimonio",
                fecha = "12/12/2026",
                horario = "19:00",
                asistentes = 80,
                estadoAbono = "Pagado en 50%",
                fechaPago = "21/11/2026"
            )
        ),
        onRutChanged = {},
        onBuscarPorRut = {},
        onAprueboPago = {},
        onRechazoPago = {},
        onLogout = {}
    )
}