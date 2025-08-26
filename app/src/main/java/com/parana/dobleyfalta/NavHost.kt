package com.parana.dobleyfalta

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.parana.dobleyfalta.cuentas.LoginScreen
import com.parana.dobleyfalta.cuentas.ProfileScreen
import com.parana.dobleyfalta.cuentas.RegistroScreen
import com.parana.dobleyfalta.cuentas.opcionesMiPerfil.ChangePasswordScreen
import com.parana.dobleyfalta.cuentas.opcionesMiPerfil.EditProfileScreen
import com.parana.dobleyfalta.equipos.EquiposListScreen
import com.parana.dobleyfalta.equipos.DetallesEquiposScreen
import com.parana.dobleyfalta.noticias.DetalleNoticiasScreen
import com.parana.dobleyfalta.noticias.NoticiasScreen
import com.parana.dobleyfalta.partidos.JornadasScreen

@Composable
fun AppNavHost(navController: NavHostController, innerPadding: PaddingValues) {

    NavHost(navController = navController, startDestination = "equipos") {
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
        composable("miperfil") {
            ProfileScreen(navController)
        }
        composable("cambiar_contraseña") {
            ChangePasswordScreen(navController)
        }
        composable("editar_perfil") {
            EditProfileScreen(navController)
        }
        composable("detalles/{equipoId}") { backStackEntry ->
            val equipoId = backStackEntry.arguments?.getString("equipoId")?.toInt()
            if (equipoId != null) {
                DetallesEquiposScreen(navController, equipoId)
            }
        }
        composable("jornadas") {
            JornadasScreen(navController)
        }
        composable("noticias") {
            NoticiasScreen(navController = navController)
        }
        composable(
            "detalle_noticia/{noticiaId}",
            arguments = listOf(navArgument("noticiaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val noticiaId = backStackEntry.arguments?.getInt("noticiaId")
            DetalleNoticiasScreen(navController = navController, noticiaId = noticiaId ?: 0)
        }
    }
}