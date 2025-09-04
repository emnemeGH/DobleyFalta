package com.parana.dobleyfalta.jornadas.empleado

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.parana.dobleyfalta.MainViewModel

@Composable
fun CrearLigaScreen(navController: NavController, mainViewModel: MainViewModel) {
    var nombreLiga by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Liga", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombreLiga,
            onValueChange = {
                nombreLiga = it
                error = false
            },
            label = { Text("Nombre de la liga") },
            isError = error,
            modifier = Modifier.fillMaxWidth()
        )

        if (error) {
            Text(
                text = "El nombre no puede estar vacío",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nombreLiga.isBlank()) {
                    error = true
                } else {
                    println("⚽ Liga creada: $nombreLiga")
                    navController.popBackStack() // vuelve a la lista de ligas
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Liga")
        }
    }
}