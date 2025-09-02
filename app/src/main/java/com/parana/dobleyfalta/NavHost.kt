package com.parana.dobleyfalta

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.parana.dobleyfalta.adminpantallas.AdminEditUserScreen
import com.parana.dobleyfalta.adminpantallas.AdminScreen
import com.parana.dobleyfalta.adminpantallas.CreateUserScreen
import com.parana.dobleyfalta.cuentas.LoginScreen
import com.parana.dobleyfalta.cuentas.ProfileScreen
import com.parana.dobleyfalta.cuentas.RecuperarContraseñaScreen
import com.parana.dobleyfalta.cuentas.RegistroScreen
import com.parana.dobleyfalta.cuentas.opcionesMiPerfil.ChangePasswordScreen
import com.parana.dobleyfalta.cuentas.opcionesMiPerfil.EditProfileScreen
import com.parana.dobleyfalta.equipos.EquiposListScreen
import com.parana.dobleyfalta.equipos.DetallesEquiposScreen
import com.parana.dobleyfalta.jornadas.JornadasPorLigaScreen
import com.parana.dobleyfalta.noticias.DetalleNoticiasScreen
import com.parana.dobleyfalta.noticias.NoticiasScreen
import com.parana.dobleyfalta.jornadas.JornadasScreen
import com.parana.dobleyfalta.noticias.empleado_noticia.CrearNoticiaScreen
import com.parana.dobleyfalta.noticias.empleado_noticia.EditarNoticiaScreen

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
        composable("jornadas_por_liga_screen") {
            JornadasPorLigaScreen(navController)
        }

        composable("jornadas_screen/{jornadaId}") { backStackEntry ->
            val jornadaId = backStackEntry.arguments?.getString("jornadaId")?.toIntOrNull() ?: 1
            JornadasScreen(navController = navController, jornadaId = jornadaId)
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
        composable("admin") {
            AdminScreen(navController = navController)
        }
        composable("crear_usuario") {
            CreateUserScreen(navController = navController)
        }
        composable ("admin_editar_usuario") {
            AdminEditUserScreen(navController = navController)
        }
        composable("recuperar_contraseña") {
            RecuperarContraseñaScreen(navController = navController)
        }
        composable("editar_noticia") {
            EditarNoticiaScreen(navController = navController)
        }
        composable("agregar_noticia") {
            CrearNoticiaScreen(navController = navController)
        }
    }
}