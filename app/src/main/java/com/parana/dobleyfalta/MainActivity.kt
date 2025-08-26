package com.parana.dobleyfalta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Surface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Usa una Surface en lugar de un tema específico para un ejemplo genérico.
            Surface(color = MaterialTheme.colorScheme.background) {
                MainScreenWithBottomNav()
            }
        }
    }
}

@Composable
fun MainScreenWithBottomNav() {
    val navController = rememberNavController()

    // Obtiene la ruta actual para saber qué ícono de la barra debe estar seleccionado
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        // Coloca la barra de navegación inferior en el slot de bottomBar
        bottomBar = {
            AppBottomNavigationBar(navController = navController, currentRoute = currentRoute ?: "")
        }
    ) { innerPadding ->
        // Coloca el NavHost en el slot de content y pasa el padding a los composables hijos
        AppNavHost(navController = navController, innerPadding = innerPadding)
    }
}

@Composable
fun PantallaPrincipal(navController: androidx.navigation.NavController) {
    androidx.compose.material3.Button(
        onClick = { navController.navigate("equipos") },
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Ver Equipos")
    }
}