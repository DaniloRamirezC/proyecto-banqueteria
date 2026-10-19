package com.danilo.proyectobanqueteria.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danilo.proyectobanqueteria.ui.components.Footer
import com.danilo.proyectobanqueteria.ui.components.Header

private data class MenuBanqueteria(
    val emoji: String,
    val nombre: String,
    val descripcion: String,
    val incluye: List<String>
)

private val menus = listOf(
    MenuBanqueteria("🥂", "Menú Cóctel", "Ideal para recepciones, inauguraciones y celebraciones de pie.", listOf("Canapés variados", "Mini sándwiches", "Empanaditas de cóctel", "Bocaditos dulces y bebidas a definir")),
    MenuBanqueteria("🎉", "Menú Celebración", "Una alternativa para cumpleaños, aniversarios y reuniones familiares.", listOf("Cóctel de bienvenida", "Plato principal con acompañamiento", "Ensaladas surtidas", "Postre individual")),
    MenuBanqueteria("💍", "Menú Matrimonio", "Una propuesta especial para compartir un día inolvidable.", listOf("Recepción de invitados", "Entrada o cóctel", "Plato principal a elección", "Mesa dulce y servicio de bebidas a cotizar")),
    MenuBanqueteria("🍽️", "Menú Empresarial", "Pensado para almuerzos corporativos, cenas y eventos de empresa.", listOf("Entrada o ensalada", "Plato principal", "Postre", "Opciones de café y bebidas")),
    MenuBanqueteria("☕", "Menú Coffee Break", "Perfecto para reuniones, capacitaciones y jornadas de trabajo.", listOf("Café y té", "Jugos o agua", "Mini pastelería", "Opciones dulces y saladas"))
)

private val terracota = Color(0xFFCB522E)
private val fondo = Color(0xFFE6C196)
private val crema = Color(0xFFFFF8EF)

@Composable
fun CatalogoScreen(onCotizar: () -> Unit) {
    Scaffold(
        topBar = { Header(title = "Catálogo de menús") },
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
            Text(
                text = "Paolicias Banquetería",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = terracota
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Menús para acompañar tus momentos especiales. Elige una propuesta y solicita una cotización personalizada.",
                fontSize = 14.sp,
                color = Color(0xFF50372B)
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(menus) { menu ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = crema),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = menu.emoji, fontSize = 30.sp)
                                Column(modifier = Modifier.padding(start = 10.dp)) {
                                    Text(menu.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = terracota)
                                    Text(menu.descripcion, fontSize = 13.sp, color = Color(0xFF50372B))
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Este menú puede incluir:", fontWeight = FontWeight.SemiBold, color = Color(0xFF50372B))
                            menu.incluye.forEach { item ->
                                Text("•  $item", fontSize = 14.sp, color = Color(0xFF50372B), modifier = Modifier.padding(top = 3.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onCotizar,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = terracota, contentColor = Color.White)
                            ) {
                                Text("Solicitar cotización", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                item {
                    Text(
                        text = "Los menús son referenciales. La propuesta final se adapta al tipo de evento, cantidad de invitados y preferencias del cliente. No se muestran precios: solicita tu cotización.",
                        fontSize = 12.sp,
                        color = Color(0xFF50372B),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogoScreenPreview() {
    CatalogoScreen(onCotizar = {})
}
