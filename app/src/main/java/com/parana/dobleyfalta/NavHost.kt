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
import com.parana.dobleyfalta.equipos.DetallesEquiposScreen
import com.parana.dobleyfalta.equipos.EquiposScreen
import com.parana.dobleyfalta.equipos.empleado_equipos.CrearEquipoScreen
import com.parana.dobleyfalta.equipos.empleado_equipos.EditarEquipoScreen
import com.parana.dobleyfalta.jornadas.EditarPartidosScreen
import com.parana.dobleyfalta.noticias.DetalleNoticiasScreen
import com.parana.dobleyfalta.noticias.NoticiasScreen
import com.parana.dobleyfalta.jornadas.JornadasScreen
import com.parana.dobleyfalta.jornadas.Partido
import com.parana.dobleyfalta.jornadas.empleado.CrearJornadaScreen
import com.parana.dobleyfalta.jornadas.empleado.CrearLigaScreen
import com.parana.dobleyfalta.jornadas.empleado.CrearPartidosScreen
import com.parana.dobleyfalta.jornadas.empleado.EditarJornadaScreen
import com.parana.dobleyfalta.jornadas.empleado.EditarLigasScreen
import com.parana.dobleyfalta.jornadas.empleado.JornadasPorLigaScreen
import com.parana.dobleyfalta.noticias.empleado_noticia.CrearNoticiaScreen
import com.parana.dobleyfalta.noticias.empleado_noticia.EditarNoticiaScreen
import com.parana.dobleyfalta.tienda.TiendaScreen
import com.parana.dobleyfalta.carrito.CarritoScreen
import com.parana.dobleyfalta.tabla.TablaScreen
import com.parana.dobleyfalta.home.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    mainViewModel: MainViewModel
) {

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, mainViewModel)
        }
        composable("registro") {
            RegistroScreen(navController)
        }
        composable("principal") {
            PantallaPrincipal(navController, mainViewModel)
        }
        composable("home") {
            HomeScreen(navController, mainViewModel)
        }
        composable("equipos") {
            EquiposScreen(navController)
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
            JornadasPorLigaScreen(navController, mainViewModel)
        }

        composable("crear_liga") {
            CrearLigaScreen(navController, mainViewModel)
        }
        composable("editar_liga") {
            EditarLigasScreen(navController, mainViewModel)
        }

        composable("jornadas_screen/{jornadaId}") { backStackEntry ->
            val jornadaId = backStackEntry.arguments?.getString("jornadaId")?.toIntOrNull() ?: 1
            JornadasScreen(navController = navController, jornadaId = jornadaId)
        }

        composable("crear_jornada") {
            CrearJornadaScreen(navController, mainViewModel)
        }

        composable ("editar_jornada"){
            EditarJornadaScreen(navController, mainViewModel)
        }

        composable("crear_partido") {
            CrearPartidosScreen(navController, mainViewModel)
        }

        composable("editar_partido") { backStackEntry ->
            val partido = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Partido>("partido")

            partido?.let {
                EditarPartidosScreen(navController, mainViewModel, it)
            }
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
        composable("admin_editar_usuario") {
            AdminEditUserScreen(navController = navController)
        }
        composable("recuperar_contraseña") {
            RecuperarContraseñaScreen(navController = navController)
        }
        composable("editar_noticia") {
            EditarNoticiaScreen(navController = navController)
        }
        composable("crear_noticia") {
            CrearNoticiaScreen(navController = navController)
        }
        composable("tienda") {
            TiendaScreen(navController = navController)
        }
        /*composable("detalle_producto/{productId}") {
            backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId")
            DetalleProductoScreen(navController = navController, product = productoId ?:0)
        }*/
        composable("carrito") {
            CarritoScreen(navController = navController)
        }
        composable("crear_equipo") {
            CrearEquipoScreen(navController = navController)
        }
        composable("editar_equipo") {
            EditarEquipoScreen(navController = navController)
        }
        composable("tabla") {
            TablaScreen(navController = navController)
        }
    }
}