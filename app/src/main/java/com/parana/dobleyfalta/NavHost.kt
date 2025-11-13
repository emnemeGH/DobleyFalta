package com.parana.dobleyfalta

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.parana.dobleyfalta.noticias.DetalleNoticiasScreen
import com.parana.dobleyfalta.noticias.NoticiasScreen
import com.parana.dobleyfalta.jornadas.empleado.CrearJornadaScreen
import com.parana.dobleyfalta.jornadas.empleado.CrearLigaScreen
import com.parana.dobleyfalta.jornadas.empleado.EditarLigasScreen
import com.parana.dobleyfalta.noticias.empleado_noticia.CrearNoticiaScreen
import com.parana.dobleyfalta.noticias.empleado_noticia.EditarNoticiaScreen
import com.parana.dobleyfalta.tienda.TiendaScreen
import com.parana.dobleyfalta.carrito.CarritoScreen
//import com.parana.dobleyfalta.tabla.TablaScreen
import com.parana.dobleyfalta.home.HomeScreen
import com.parana.dobleyfalta.jornadas.EditarJornadaScreen
import com.parana.dobleyfalta.jornadas.JornadasPorLigaScreen.JornadasPorLigaScreen
import com.parana.dobleyfalta.jornadas.JornadasScreen
import com.parana.dobleyfalta.jornadas.empleado.MarcadorPartidoScreen
import com.parana.dobleyfalta.ui.screens.CrearPartidoScreen
import com.parana.dobleyfalta.retrofit.models.auth.Rol

@Composable
fun AppNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    mainViewModel: MainViewModel
) {

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context.applicationContext) }
    val rolUsuario: Rol? = sessionManager.getRolUsuario()
    var startDestination = ""

    if(rolUsuario == Rol.Administrador) {
        startDestination = "admin"
    } else {
        startDestination = "home"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("registro") {
            RegistroScreen(navController)
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
            JornadasPorLigaScreen(navController)
        }

        composable("crear_liga") {
            CrearLigaScreen(navController)
        }
        composable(
            // 1. Definimos la ruta con el argumento. ¡Importante el tipo!
            route = "editar_liga/{idLiga}",
            arguments = listOf(
                navArgument("idLiga") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            // 2. Extraemos el argumento
            val idLiga = backStackEntry.arguments?.getInt("idLiga") ?: 0

            // 3. Llamamos al Composable con el argumento extraído
            EditarLigasScreen(
                navController = navController,
                idLiga = idLiga // Suponiendo que has renombrado el mainViewModel por ligasViewModel o usas Hilt/Koin
            )
        }

        composable(
            route = "jornadas_screen/{ligaId}/{jornadaNumero}",
            arguments = listOf(
                navArgument("ligaId") { type = NavType.IntType },
                navArgument("jornadaNumero") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val ligaId = backStackEntry.arguments?.getInt("ligaId") ?: 0

            // ✅ CORREGIDO: Usar "jornadaNumero" que es el argumento definido en la ruta
            val jornadaNumero = backStackEntry.arguments?.getInt("jornadaNumero") ?: 1

            JornadasScreen(
                navController = navController,
                ligaId = ligaId,
                // Le pasamos el número de jornada, NO el ID, a la pantalla
                jornadaId = jornadaNumero // Se llama "jornadaId" en el Composable, pero es el número. Es mejor refactorizarlo a "jornadaNumero" si es posible.
            )
        }




        composable("crear_jornada") {
            CrearJornadaScreen(navController, mainViewModel)
        }

        composable(
            // 1. Define la ruta que acepta el argumento 'id'
            route = "editar_jornada/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType } // 2. Especifica que es un entero
            )
        ) { backStackEntry ->

            // 3. Extrae el argumento. El '0' es un valor por defecto si falla (aunque con NavType.IntType no debería ser null)
            val idJornada = backStackEntry.arguments?.getInt("id") ?: 0

            // 4. Llama a la pantalla correctamente, pasándole el ID.
            // NOTA: El MainViewModel ya no es necesario aquí.
            EditarJornadaScreen(navController = navController, idJornada = idJornada)
        }

        composable(
            route = "crear_partido/{jornadaId}",
            arguments = listOf(navArgument("jornadaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val jornadaId = backStackEntry.arguments?.getInt("jornadaId") ?: 0
            CrearPartidoScreen(navController = navController, jornadaId = jornadaId)
        }


//        composable("editar_partido") { backStackEntry ->
//            val partido = navController.previousBackStackEntry
//                ?.savedStateHandle
//                ?.get<Partido>("partido")
//
//            partido?.let {
//                EditarPartidosScreen(navController, mainViewModel, it)
//            }
//        }
        composable(
            route = "marcador_partido/{idPartido}",
            arguments = listOf(navArgument("idPartido") { type = NavType.IntType })
        ) { backStackEntry ->
            val idPartido = backStackEntry.arguments?.getInt("idPartido") ?: 0
            MarcadorPartidoScreen(navController, idPartido = idPartido) // ⬅️ ¡Aquí está el cambio!
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
        composable("admin_crear_usuario") {
            CreateUserScreen(navController = navController)
        }
        composable(
            "admin_editar_usuario/{idUsuario}",
            arguments = listOf(navArgument("idUsuario") { type = NavType.IntType })
        ) { backStackEntry ->
            val idUsuario = backStackEntry.arguments?.getInt("idUsuario")
            AdminEditUserScreen(navController = navController, idUsuario = idUsuario ?: 0)
        }
        composable("recuperar_contraseña") {
            RecuperarContraseñaScreen(navController = navController)
        }
        composable(
            "editar_noticia/{idNoticia}",
            arguments = listOf(navArgument("idNoticia") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("idNoticia") ?: 0
            EditarNoticiaScreen(navController = navController, noticiaId = id)
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
        composable(
            "editar_equipo/{idEquipo}",
            arguments = listOf(navArgument("idEquipo") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("idEquipo") ?: 0
            EditarEquipoScreen(navController = navController, idEquipo = id)
        }
//        composable("tabla") {
//            TablaScreen(navController = navController)
//        }
    }
}