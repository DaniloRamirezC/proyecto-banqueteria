package com.danilo.proyectobanqueteria.ui.screen

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danilo.proyectobanqueteria.R
import com.danilo.proyectobanqueteria.ui.components.Footer
import com.danilo.proyectobanqueteria.ui.components.Header
import com.danilo.proyectobanqueteria.ui.natives.vibracion
import com.danilo.proyectobanqueteria.ui.viewModel.EventoViewModel
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

private val terracota = Color(0xFFCB522E)
private val fondo = Color(0xFFE6C196)
private val crema = Color(0xFFFFF8EF)
private val textoCafe = Color(0xFF50372B)

@Composable
fun RegistroEventoScreen(
    viewModel: EventoViewModel = viewModel(),
    onNavigateToResumen: () -> Unit,
    onNavigateToCatalogo: () -> Unit
){
    val context = LocalContext.current
    val state = viewModel.uiState

    var desplegar by remember { mutableStateOf(false) }
    var desplegarH by remember { mutableStateOf(false) }
    val listarEventos = listOf("Cóctel","Celebración","Matrimonio","Empresarial","Coffee Break","Otro")
    val listarHorarios = listOf("Mañana (09.00 - 13.00)","Tarde (14.00 - 19.00)","Noche (20.00 - 02.00)")

    val moneda = remember { NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))}

    val seleccionarFecha = remember { val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            {_,year,month,dayOfMonth ->
                viewModel.onfechaChanged("$dayOfMonth/${month+1}/$year")
            },
            anio,mes,dia)
    }
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = terracota,
        unfocusedBorderColor = Color(0xFFD5B895),
        focusedLabelColor = terracota,
        unfocusedLabelColor = textoCafe,
        cursorColor = terracota
    )

    Scaffold(
        topBar = { Header(title = "Registrar evento") },
        bottomBar = { Footer() },
        containerColor = fondo
    ) {
        innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(fondo)
                .verticalScroll(rememberScrollState())
                .padding(16.dp,12.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.paolicias),
                contentDescription = "Logo de Paolicias",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .shadow(elevation = 12.dp, shape = CircleShape)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFFF57E41), CircleShape)
            )
            Text(
                text = "Completa los datos de tu evento para calcular la cotización y reservar fecha",
                fontSize = 14.sp,
                color = textoCafe
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = crema),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Datos del cliente",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = terracota
                    )
                    OutlinedTextField(
                        value = state.nombre,
                        onValueChange = { viewModel.onNombreChanged(it) },
                        label = { Text("Nombre completo") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.rut,
                        onValueChange = { viewModel.onRutChanged(it) },
                        label = { Text("RUT") },
                        placeholder = { Text("12345678-9", color = Color.Gray) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.empresa,
                        onValueChange = { viewModel.onEmpresaChanged(it) },
                        label = { Text("Empresa (opcional)") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.telefono,
                        onValueChange = { viewModel.onTelefonoChanged(it) },
                        label = { Text("Teléfono") },
                        placeholder = { Text("+56911223344", color = Color.Gray) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = { viewModel.onEmailChanged(it) },
                        label = { Text("Email") },
                        placeholder = { Text("ejemplo@email.com", color = Color.Gray) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = crema),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Detalles del evento",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = terracota
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = state.tipoEvento,
                            onValueChange = {},
                            label = { Text("Tipo de evento / Menú") },
                            singleLine = true,
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Seleccionar opciones",
                                    tint = terracota
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = textFieldColors,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { desplegar = true }
                        )
                        DropdownMenu(
                            expanded = desplegar,
                            onDismissRequest = { desplegar = false },
                            containerColor = crema
                        ) {
                            listarEventos.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion, color = textoCafe) },
                                    onClick = {
                                        viewModel.onTipoEventoChanged(opcion)
                                        desplegar = false
                                    }
                                )
                            }
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = state.fecha,
                            onValueChange = {},
                            label = { Text("Fecha del evento") },
                            placeholder = { Text("DD/MM/AAAA", color = Color.Gray) },
                            singleLine = true,
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { seleccionarFecha.show() }) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Abrir calendario",
                                        tint = terracota
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = textFieldColors,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { seleccionarFecha.show() }
                        )
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = state.horario,
                            onValueChange = {},
                            label = { Text("Horario") },
                            singleLine = true,
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Seleccionar horarios",
                                    tint = terracota
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = textFieldColors,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { desplegarH = true }
                        )
                        DropdownMenu(
                            expanded = desplegarH,
                            onDismissRequest = { desplegarH = false },
                            containerColor = crema
                        ) {
                            listarHorarios.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion, color = textoCafe) },
                                    onClick = {
                                        viewModel.onHorarioChanged(opcion)
                                        desplegarH = false
                                    }
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = if (state.asistentes == 0) "" else state.asistentes.toString(),
                        onValueChange = { viewModel.onAsistentes(it) },
                        label = { Text("Cantidad de asistentes (20 a 100)") },
                        placeholder = { Text("Ej: 80", color = Color.Gray) },
                        singleLine = true,
                        isError = viewModel.errorAsistentes,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    AnimatedVisibility(
                        visible = viewModel.errorAsistentes,
                        enter = fadeIn(animationSpec = tween(300)) + expandVertically(),
                        exit = fadeOut(animationSpec = tween(200)) + shrinkVertically()
                    ) {
                        Text(
                            text = "Debe ingresar entre 20 y 100 asistentes",
                            color = Color(0xFFB00020),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedTextField(
                        value = state.observaciones,
                        onValueChange = { viewModel.onObservacionesChanged(it) },
                        label = { Text("Observaciones") },
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3,
                        maxLines = 5,
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            AnimatedVisibility(
                visible = state.tipoEvento.isNotBlank() && state.asistentes > 0,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = crema),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Estimación referencial",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = terracota
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Valor por persona:", fontSize = 14.sp, color = textoCafe)
                            Text(
                                "$${moneda.format(state.precioPersona)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textoCafe
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Total estimado (+IVA):",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = textoCafe
                            )
                            Text(
                                "$${moneda.format(state.totalEvento)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = terracota
                            )
                        }
                        HorizontalDivider(color = Color(0xFFE6C196), thickness = 1.dp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Abono inicial (50%):", fontSize = 14.sp, color = textoCafe)
                            Text(
                                "$${moneda.format(state.totalEvento)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textoCafe
                            )
                        }
                    }
                }
            }
            Button(
                onClick = {
                    viewModel.onConfirmarClick(
                        context = context,
                        onSuccess = {
                            Toast.makeText(
                                context,
                                "Evento registrado con éxito",
                                Toast.LENGTH_SHORT
                            ).show()
                            onNavigateToResumen()
                        }
                    )
                    viewModel.errorMensaje?.let { mensaje ->
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = terracota,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Confirmar evento",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            OutlinedButton(
                onClick = onNavigateToCatalogo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(2.dp, terracota),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = crema,
                    contentColor = terracota
                )
            ) {
                Text(
                    text = "Volver al Catálogo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
@Preview(showBackground = true)
@Composable
fun RegistroEventoScreenPreview(){
    RegistroEventoScreen(
        onNavigateToResumen = {},
        onNavigateToCatalogo = {}
    )
}