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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.parana.dobleyfalta.ui.theme.DobleYFaltaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            DobleYFaltaTheme {
                MainScreenWithBottomNav(mainViewModel)
            }
        }
    }
}

@Composable
fun MainScreenWithBottomNav(mainViewModel: MainViewModel) {
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
        AppNavHost(
            navController = navController,
            innerPadding = innerPadding,
            mainViewModel = mainViewModel
        )
    }
}

// Scaffold
// Es un "contenedor base" en Jetpack Compose para estructurar la UI de una pantalla.
// Permite colocar elementos comunes de una app, como:
// - topBar → barra superior (ej: título, botones de acción)
// - bottomBar → barra inferior de navegación
// - floatingActionButton → botón flotante
// - drawerContent → menú lateral deslizable
// - content → el contenido principal de la pantalla

// Scaffold se encarga de:
// Posicionar correctamente todos estos elementos en la pantalla.
// Calcular automáticamente el padding para que el contenido principal
// no quede tapado por la barra superior o inferior (ese padding se pasa como innerPadding).