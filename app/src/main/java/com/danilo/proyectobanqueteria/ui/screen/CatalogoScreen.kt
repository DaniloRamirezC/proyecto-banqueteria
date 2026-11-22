package com.danilo.proyectobanqueteria.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danilo.proyectobanqueteria.R
import com.danilo.proyectobanqueteria.data.ClienteCatalogoApi
import com.danilo.proyectobanqueteria.ui.components.Footer
import com.danilo.proyectobanqueteria.ui.components.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

private val terracota = Color(0xFFCB522E)
private val fondo = Color(0xFFE6C196)
private val crema = Color(0xFFFFF8EF)

@DrawableRes
private fun imagenParaMenu(nombre: String): Int {
    val n = nombre.lowercase()
    return when {
        "chocolate" in n -> R.drawable.torta_chocolate
        "frutilla" in n -> R.drawable.torta_frutilla
        "red velvet" in n -> R.drawable.torta_red_velvet
        "manjar" in n && "nuez" in n -> R.drawable.torta_manjar_nuez
        ("limón" in n || "limon" in n) && "merengue" in n -> R.drawable.torta_limon_merengue
        "tres leches" in n -> R.drawable.torta_tres_leches
        "coctel" in n || "cóctel" in n -> R.drawable.menu_coctel
        "celebr" in n -> R.drawable.menu_celebracion
        "matrimonio" in n -> R.drawable.menu_matrimonio
        "empres" in n -> R.drawable.menu_empresarial
        "coffee" in n || "break" in n -> R.drawable.menu_coffe_break
        else -> R.drawable.paolicias
    }
}

private fun precioChile(valor: Double): String =
    NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(valor)

@Composable
fun CatalogoScreen(
    onNavigateToCotizar: () -> Unit,
    onLogout: () -> Unit
) {
    var mostrarSalir by remember { mutableStateOf(false) }
    if (mostrarSalir) {
        AlertDialog(
            onDismissRequest = { mostrarSalir = false },
            containerColor = crema,
            title = { Text("Cerrar sesión", fontWeight = FontWeight.Bold, color = terracota) },
            text = { Text("¿Está seguro que desea cerrar sesión?", color = Color(0xFF50372B)) },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarSalir = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = terracota)
                ) { Text("Sí, salir", color = Color.White, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarSalir = false }) {
                    Text("Cancelar", color = Color(0xFF50372B), fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }
    var categoriaSeleccionada by remember { mutableStateOf(1L) }
    var menus by remember { mutableStateOf(emptyList<ClienteCatalogoApi.MenuRemoto>()) }
    var cargando by remember { mutableStateOf(false) }
    var recarga by remember { mutableStateOf(0) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(categoriaSeleccionada, recarga) {
        cargando = true
        error = null
        try {
            menus = withContext(Dispatchers.IO) {
                ClienteCatalogoApi.obtenerCatalogo(categoriaSeleccionada)
            }
        } catch (e: Exception) {
            menus = emptyList()
            error = "No se pudo cargar el catálogo. Comprueba que service-catalogo esté funcionando y vuelve a intentar."
        } finally {
            cargando = false
        }
    }

    // El catálogo conserva exactamente los cinco menús originales de Banquetería
    // y las seis tortas de 30 personas. Se filtran registros antiguos/duplicados.
    val ordenBanqueteria = listOf(
        "menú cóctel", "menú celebración", "menú matrimonio",
        "menú empresarial", "menú coffee break"
    )
    val ordenPasteleria = listOf(
        "torta de chocolate", "torta de frutilla", "torta red velvet",
        "torta manjar nuez", "torta de limón merengue", "torta tres leches"
    )
    val ordenPermitido = if (categoriaSeleccionada == 2L) ordenPasteleria else ordenBanqueteria
    val productosVisibles = menus
        .filter { menu -> ordenPermitido.contains(menu.nombre.trim().lowercase()) }
        .distinctBy { it.nombre.trim().lowercase() }
        .sortedBy { ordenPermitido.indexOf(it.nombre.trim().lowercase()) }

    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Header(title = "Catálogo de menús")
                IconButton(
                    onClick = { mostrarSalir = true },
                    modifier = Modifier.padding(end = 12.dp).statusBarsPadding()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Cerrar sesión",
                        tint = terracota
                    )
                }
            }
        },
        bottomBar = { Footer() },
        containerColor = fondo
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(fondo)
                .padding(horizontal = 16.dp, vertical = 12.dp)
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
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Menús y pastelería para acompañar tus momentos especiales. Elige una propuesta y solicita una cotización personalizada.",
                fontSize = 14.sp,
                color = Color(0xFF50372B)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { categoriaSeleccionada = 1L },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (categoriaSeleccionada == 1L) terracota else crema,
                        contentColor = if (categoriaSeleccionada == 1L) Color.White else terracota
                    )
                ) { Text("Banquetería", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = { categoriaSeleccionada = 2L },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (categoriaSeleccionada == 2L) terracota else crema,
                        contentColor = if (categoriaSeleccionada == 2L) Color.White else terracota
                    )
                ) { Text("Pastelería", fontWeight = FontWeight.Bold) }
            }
            Spacer(modifier = Modifier.height(10.dp))

            when {
                cargando -> Column(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = terracota)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Cargando catálogo desde el servidor...", color = Color(0xFF50372B))
                }
                error != null -> Column(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(error!!, color = Color(0xFF7A2418))
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { recarga++ },
                        colors = ButtonDefaults.buttonColors(containerColor = terracota)
                    ) { Text("Reintentar") }
                }
                productosVisibles.isEmpty() -> Column(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("No hay productos disponibles en esta categoría.", color = Color(0xFF50372B))
                }
                else -> LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productosVisibles, key = { it.idMenu }) { menu ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = crema),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = imagenParaMenu(menu.nombre)),
                                        contentDescription = menu.nombre,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp))
                                    )
                                    Column(modifier = Modifier.padding(start = 10.dp)) {
                                        Text(menu.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = terracota)
                                        if (menu.detalle.isNotBlank()) {
                                            Text(menu.detalle, fontSize = 13.sp, color = Color(0xFF50372B))
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                if (categoriaSeleccionada == 2L) {
                                    Text(
                                        text = "Tamaño único: 30 personas",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF50372B)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                                Text(
                                    text = menu.precio?.let { precioChile(it) } ?: "Precio no disponible",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = terracota
                                )
                                if (categoriaSeleccionada == 1L) {
                                    Text("por persona (IVA incluido)", fontSize = 12.sp, color = Color(0xFF50372B))
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = onNavigateToCotizar,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = terracota, contentColor = Color.White)
                                ) { Text("Solicitar cotización", fontWeight = FontWeight.Bold) }
                            }
                        }
                    }
                    item {
                        Text(
                            text = if (categoriaSeleccionada == 2L) {
                                "Pastelería: seis tortas disponibles, todas para 30 personas."
                            } else {
                                "Precios por persona con IVA incluido. Valores recuperados del catálogo original de Paolicias."
                            },
                            fontSize = 12.sp,
                            color = Color(0xFF50372B),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogoScreenPreview() {
    CatalogoScreen(onNavigateToCotizar = {}, onLogout = {})
}
