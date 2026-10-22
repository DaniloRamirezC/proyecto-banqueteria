package com.danilo.proyectobanqueteria.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Footer(
    copyrightText: String = "©2026 Nibeda Systems. Todos los derechos reservados",
    backgroundColor: Color = Color(0xFFEEE4D4),
    textColor: Color = Color.Black
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .navigationBarsPadding()
            .defaultMinSize(minHeight = 44.dp)
            .padding(8.dp,12.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = copyrightText,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    }
}