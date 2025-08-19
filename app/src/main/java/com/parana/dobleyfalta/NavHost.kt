package com.parana.dobleyfalta

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.parana.dobleyfalta.cuentas.LoginScreen
import com.parana.dobleyfalta.cuentas.RegistroScreen
import com.parana.dobleyfalta.equipos.EquiposListScreen
import com.parana.dobleyfalta.equipos.DetallesEquiposScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("registro") {
            RegistroScreen(navController)
        }
        composable("principal") {
            PantallaPrincipal(navController)
        }
        composable("equipos") {
            EquiposListScreen(navController)
        }
        composable("detalles/{equipoId}") { backStackEntry ->
            val equipoId = backStackEntry.arguments?.getString("equipoId")?.toInt()
            if (equipoId != null) {
                DetallesEquiposScreen(navController, equipoId)
            }
        }
    }
}
