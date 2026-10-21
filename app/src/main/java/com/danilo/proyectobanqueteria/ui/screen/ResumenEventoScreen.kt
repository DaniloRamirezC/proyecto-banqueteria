package com.danilo.proyectobanqueteria.ui.screen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danilo.proyectobanqueteria.ui.components.Footer
import com.danilo.proyectobanqueteria.ui.components.Header
import com.danilo.proyectobanqueteria.ui.natives.crearFoto
import com.danilo.proyectobanqueteria.ui.natives.vibracion
import com.danilo.proyectobanqueteria.ui.viewModel.EventoViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ResumenEventoScreen(
    viewModel: EventoViewModel,
    onNavigateToCatalogo: () -> Unit
) {
    val context = LocalContext.current
    val evento = viewModel.uiState
    val moneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))
    var fotoReg by remember { mutableStateOf<Uri?>(null) }

    val camaraUso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ){ exitoso: Boolean ->
        if (exitoso && fotoReg !=null){
            viewModel.onComprobanteSelec(fotoReg)
            vibracion(context)
        }
    }
    Scaffold(
        topBar = { Header(title = "Resumen de Cotización") },
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
                .imePadding()
        ) {
            Text(
                text = "Registro de evento",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFCB522E)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    DetalleEvento("Cliente: ", evento.nombre)
                    DetalleEvento("RUT: ", evento.rut)
                    DetalleEvento("Teléfono: ", evento.telefono)
                    DetalleEvento("Email: ", evento.email)
                    DetalleEvento("Empresa: ", evento.empresa)
                    DetalleEvento("Tipo de evento: ", evento.tipoEvento)
                    DetalleEvento("Fecha: ", evento.fecha)
                    DetalleEvento("Asistentes: ", "${evento.asistentes} personas")
                    DetalleEvento("Observaciones: ", evento.observaciones)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    DetalleEvento(
                        "Precio por persona",
                        moneda.format(evento.precioPersona)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Total estimado: ${moneda.format(evento.totalEvento)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFCB522E)
                    )
                    Text(
                        text = "Abono (50%): ${moneda.format(evento.abonoEvento)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    DetalleEvento("Estado abono: ", evento.estadoAbono)
                    if (evento.fechaPago.isNotBlank()) {
                        DetalleEvento("Fecha registro: ", evento.fechaPago)
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Comprobante de transferencia",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if(evento.comprobante == null){
                        Button(
                            onClick = {
                                val uri = crearFoto(context)
                                fotoReg = uri
                                camaraUso.launch(uri)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57E41)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Cámara")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tomar foto al comprobante", fontWeight = FontWeight.Bold)
                        }
                    }else{
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Comprobante capturado",
                                tint = Color(0xFF2E7D32)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Comprobante capturado y registrado",
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                val uri = crearFoto(context)
                                fotoReg = uri
                                camaraUso.launch(uri)
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Volver a tomar foto")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onNavigateToCatalogo() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57E41)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .shadow(5.dp, shape = CircleShape)
                    .height(48.dp)
            ) {
                Text(
                    text = "Volver al catálogo",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
@Composable
fun DetalleEvento(etiqueta: String, valor: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, fontWeight = FontWeight.SemiBold, color = Color.Gray)
        Text(text = valor, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}
@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun ResumenScreenPreview(){
    ResumenEventoScreen(
        viewModel = EventoViewModel(),
        onNavigateToCatalogo = {}
    )
}