package com.danilo.proyectobanqueteria.ui.screen

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danilo.proyectobanqueteria.ui.components.Footer
import com.danilo.proyectobanqueteria.ui.components.Header
import com.danilo.proyectobanqueteria.ui.viewModel.EventoViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ResumenEventoScreen(
    viewModel: EventoViewModel = viewModel(),
    onNavigateToCatalogo: () -> Unit
) {
    val evento = viewModel.uiState
    val moneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))

    Scaffold(
        topBar = { Header(title = "Resumen de Cotización") },
        bottomBar = { Footer() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFE6C196)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE6C196))
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
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
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
                Button(
                    onClick = { onNavigateToCatalogo() },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(48.dp)
                        .shadow(8.dp, shape = CircleShape)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFCB522E),
                        contentColor = Color.White
                    ),
                    border = BorderStroke(2.dp, Color.White),
                    shape = CircleShape,
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
@Preview(showBackground = true)
@Composable
fun ResumenScreenPreview(){
    ResumenEventoScreen(onNavigateToCatalogo = {})
}