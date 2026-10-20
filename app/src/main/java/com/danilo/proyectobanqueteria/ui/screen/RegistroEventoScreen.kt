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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.danilo.proyectobanqueteria.ui.viewModel.EventoViewModel
import java.util.Calendar

@Composable
fun RegistroEventoScreen(
    viewModel: EventoViewModel = viewModel(),
    onNavigateToResumen: ()-> Unit
){
    val context = LocalContext.current
    val state = viewModel.uiState

    var desplegar by remember { mutableStateOf(false) }
    val listarEventos = listOf("Cóctel","Celebración","Matrimonio","Empresarial","Coffee Break","Otro")

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

    Scaffold(
        topBar = { Header(title = "Registrar evento") },
        bottomBar = { Footer() }
    ) {
        innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFE6C196)),
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment =Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp,16.dp)
                    .verticalScroll(rememberScrollState())
            ){
                Text(
                    text = "Nombre",
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = state.nombre,
                    onValueChange = {viewModel.onNombreChanged(it)},
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE2E2E2),
                        unfocusedContainerColor = Color(0xFFE2E2E2),
                        focusedBorderColor = Color(0xFFF57E41),
                        unfocusedBorderColor = Color(0xFFF57E41)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "RUT",
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = state.rut,
                    onValueChange = {viewModel.onRutChanged(it)},
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {Text("12345678-9", color = Color.Gray)},
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE2E2E2),
                        unfocusedContainerColor = Color(0xFFE2E2E2),
                        focusedBorderColor = Color(0xFFF57E41),
                        unfocusedBorderColor = Color(0xFFF57E41)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Empresa",
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = state.empresa,
                    onValueChange = {viewModel.onEmpresaChanged(it)},
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {Text("Opcional", color = Color.Gray)},
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE2E2E2),
                        unfocusedContainerColor = Color(0xFFE2E2E2),
                        focusedBorderColor = Color(0xFFF57E41),
                        unfocusedBorderColor = Color(0xFFF57E41)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Teléfono",
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = state.telefono,
                    onValueChange = {viewModel.onTelefonoChanged(it)},
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {Text("+5691234567", color = Color.Gray)},
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE2E2E2),
                        unfocusedContainerColor = Color(0xFFE2E2E2),
                        focusedBorderColor = Color(0xFFF57E41),
                        unfocusedBorderColor = Color(0xFFF57E41)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Email",
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = state.email,
                    onValueChange = {viewModel.onEmailChanged(it)},
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {Text("example@email.com", color = Color.Gray)},
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE2E2E2),
                        unfocusedContainerColor = Color(0xFFE2E2E2),
                        focusedBorderColor = Color(0xFFF57E41),
                        unfocusedBorderColor = Color(0xFFF57E41)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Tipo de evento",
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    OutlinedTextField(
                        value = state.tipoEvento,
                        onValueChange = {},
                        singleLine = true,
                        readOnly = true,
                        trailingIcon = { Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Seleccionar opciones",
                            tint = Color(0xFFF57E41)
                        ) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFE2E2E2),
                            unfocusedContainerColor = Color(0xFFE2E2E2),
                            focusedBorderColor = Color(0xFFF57E41),
                            unfocusedBorderColor = Color(0xFFF57E41)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable{desplegar = true}
                    )
                    DropdownMenu(
                        expanded = desplegar,
                        onDismissRequest = {desplegar = false}
                    ) {
                        listarEventos.forEach { opcion ->
                            DropdownMenuItem(
                                text = {Text(opcion)},
                                onClick = {
                                    viewModel.onTipoEventoChanged(opcion)
                                    desplegar = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Seleccionar fecha",
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = state.fecha,
                    onValueChange = {},
                    singleLine = true,
                    readOnly = true,
                    placeholder = { Text("DD/MM/AAAA", color = Color.Gray) },
                    trailingIcon = {
                        IconButton(onClick = {seleccionarFecha.show()}) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Abrir calendario",
                                tint = Color(0xFFF57E41)
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE2E2E2),
                        unfocusedContainerColor = Color(0xFFE2E2E2),
                        focusedBorderColor = Color(0xFFF57E41),
                        unfocusedBorderColor = Color(0xFFF57E41)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {seleccionarFecha.show()}
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Cantidad de asistentes",
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = if(state.asistentes==0) "" else state.asistentes.toString(),
                    onValueChange = {viewModel.onAsistentes(it)},
                    singleLine = true,
                    isError = viewModel.errorAsistentes,
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {Text("80", color = Color.Gray)},
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE2E2E2),
                        unfocusedContainerColor = Color(0xFFE2E2E2),
                        focusedBorderColor = Color(0xFFF57E41),
                        unfocusedBorderColor = Color(0xFFF57E41),
                        errorBorderColor = Color(0xFFB00020),
                        errorContainerColor = Color(0xFFFCE8E6)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                AnimatedVisibility(
                    visible = viewModel.errorAsistentes,
                    enter = fadeIn(animationSpec = tween(300)) + expandVertically(),
                    exit = fadeOut(animationSpec = tween(200)) + shrinkVertically()
                ) {
                    Text(
                        text = "Debe ingresar entre 20 y 100 asistentes",
                        color = Color(0xFF8B0000),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp,4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Observaciones",
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = state.observaciones,
                    onValueChange = {viewModel.onObservacionesChanged(it)},
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE2E2E2),
                        unfocusedContainerColor = Color(0xFFE2E2E2),
                        focusedBorderColor = Color(0xFFF57E41),
                        unfocusedBorderColor = Color(0xFFF57E41)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        viewModel.onConfirmarClick(
                            onSuccess = {Toast.makeText(context,"Evento ingresado con éxito",Toast.LENGTH_SHORT).show()
                                onNavigateToResumen()},
                            onError = {mensajeError ->
                                Toast.makeText(context,mensajeError, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
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
                        text = "Confirmar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFFE6C196))
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57E41)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(text = "Inicio",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp)
                }

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57E41)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(text = "Catálogo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp)
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun RegistroEventoScreenPreview(){
    RegistroEventoScreen{}
}