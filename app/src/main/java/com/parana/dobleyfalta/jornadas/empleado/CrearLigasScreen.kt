package com.parana.dobleyfalta.jornadas.empleado

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.DarkBlue
import com.parana.dobleyfalta.MainViewModel
import com.parana.dobleyfalta.PrimaryOrange
import com.parana.dobleyfalta.R

val DarkGrey = Color(0xFF1A375E)
val LightGrey = Color(0xFFA0B3C4)

@Composable
fun CrearLigaScreen(navController: NavController, mainViewModel: MainViewModel) {
    var nombreLiga by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { navController.navigate("jornadas_por_liga_screen") },
            modifier = Modifier
                .padding(0.dp)
                .align(Alignment.Start)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Volver a ligas",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
        // Título de la pantalla con el mismo estilo que el Login
        Text(
            text = "Crear Liga",
            fontSize = 32.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 32.dp)
        )

        OutlinedTextField(
            value = nombreLiga,
            onValueChange = {
                nombreLiga = it
                error = false
            },
            label = { Text("Nombre de la liga", color = LightGrey) },
            isError = error,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey,
                unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey,
                focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        if (error) {
            Text(
                text = "El nombre no puede estar vacío",
                color = PrimaryOrange,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nombreLiga.isBlank()) {
                    error = true
                } else {
                    mainViewModel.agregarLiga(nombreLiga) // Guarda en el ViewModel
                    navController.popBackStack()          // Vuelve a JornadasPorLigaScreen
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
        ) {
            Text("Guardar Liga", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
