package com.danilo.proyectobanqueteria.ui.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danilo.proyectobanqueteria.R
import com.danilo.proyectobanqueteria.ui.components.Footer
import com.danilo.proyectobanqueteria.ui.components.Header

@Composable
fun BanqueteriaScreen(
    onNavigateToLogin: ()-> Unit,
){
    val context = LocalContext.current
    Scaffold(
        topBar ={ Header(title = "Paolicias.Sweet App") },
        bottomBar ={ Footer() }
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
                horizontalAlignment =Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.paolicias),
                    contentDescription = "Logo de Paolicias",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(240.dp)
                        .shadow(elevation = 12.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .border(4.dp, Color(0xFFF57E41),CircleShape)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {onNavigateToLogin()
                        Toast.makeText(context,"Bienvenido usuario", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(56.dp)
                        .shadow(8.dp, shape = CircleShape),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFCB522E),
                        contentColor = Color.White
                    ),
                    border = BorderStroke(2.dp, Color.White),
                    shape = CircleShape
                ) {
                    Text(
                        text = "Ingresar",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun BanqueteriaScreenPreview(){
    BanqueteriaScreen{}
}