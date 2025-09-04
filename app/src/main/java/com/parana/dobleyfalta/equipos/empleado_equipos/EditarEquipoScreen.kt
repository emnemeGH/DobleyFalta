package com.parana.dobleyfalta.equipos.empleado_equipos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.R

@Composable
fun EditarEquipoScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val CardBackground = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val focusManager = LocalFocusManager.current

    // TODO: En una app real, usarías el equipoId para cargar los datos
    // del equipo desde tu fuente de datos (ViewModel, base de datos, etc.).
    // Por ahora, simulamos que cargamos un nombre.
    var nombreEquipo by remember { mutableStateOf("") }
    var nombreError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(32.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Botón de Volver ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.navigate("equipos") }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Volver a equipos",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Editar Equipo",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // --- Campo de Texto para el Nombre ---
        OutlinedTextField(
            value = nombreEquipo,
            onValueChange = {
                nombreEquipo = it
                nombreError = null
            },
            label = { Text("Nombre del equipo", color = LightGrey) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                unfocusedBorderColor = CardBackground,
                focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            isError = nombreError != null,
            supportingText = {
                nombreError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Botón de Guardar Cambios ---
        Button(
            onClick = {
                if (nombreEquipo.isBlank()) {
                    nombreError = "El nombre del equipo es obligatorio"
                } else {
                    // Lógica para actualizar el equipo en tu fuente de datos
                    navController.navigate("equipos")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}