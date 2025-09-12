package com.parana.dobleyfalta

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

val DarkBlue = Color(0xFF102B4E)
val PrimaryOrange = Color(0xFFFF6600)
val DarkGrey = Color(0xFF1A375E)

@Composable
fun AppBottomNavigationBar(
    navController: NavController,
    currentRoute: String
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        // Barra inferior
        NavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            containerColor = DarkBlue,
//            tonalElevation = 4.dp -> esto no hace nada conrix
        ) {
            // Noticias
            NavigationBarItem(
                selected = currentRoute == "noticias",
                onClick = {
                    navController.navigate("noticias") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_noticias),
                        contentDescription = "Noticias",
                        tint = if (currentRoute == "noticias") PrimaryOrange else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                },
                label = {
                    Text(
                        text = "Noticias",
                        color = if (currentRoute == "noticias") PrimaryOrange else Color.White
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )

            // Jornadas
            NavigationBarItem(
                selected = currentRoute == "jornadas_por_liga_screen",
                onClick = {
                    navController.navigate("jornadas_por_liga_screen") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_basketball),
                        contentDescription = "Jornadas",
                        tint = if (currentRoute == "jornadas_por_liga_screen") PrimaryOrange else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                },
                label = {
                    Text(
                        text = "Jornadas",
                        color = if (currentRoute == "jornadas_por_liga_screen") PrimaryOrange else Color.White
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )

            // Home
            NavigationBarItem(
                selected = currentRoute == "home",
                onClick = {
                    navController.navigate("home") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_home),
                        contentDescription = "Home",
                        tint = if (currentRoute == "home") PrimaryOrange else Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                },
                label = {
                    Text(
                        text = "Home",
                        color = if (currentRoute == "home") PrimaryOrange else Color.White
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )

            // Login
            NavigationBarItem(
                selected = currentRoute == "login",
                onClick = {
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "Login",
                        tint = if (currentRoute == "login") PrimaryOrange else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                },
                label = {
                    Text(
                        text = "Login",
                        color = if (currentRoute == "login") PrimaryOrange else Color.White
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )

            // Menú (desplegable)
            NavigationBarItem(
                selected = false,
                onClick = { menuExpanded = true },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.menu_hamburguesa),
                        contentDescription = "Menú",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                },
                label = { Text("Menú", color = Color.White) },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                modifier = Modifier.testTag("menu")
            )
        }

        // Menú desplegable alineado a la derecha
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            modifier = Modifier
                .background(DarkGrey)
                .align(Alignment.BottomEnd)
                .padding(start = 8.dp, end = 16.dp),
            offset = DpOffset(x = (295).dp, y = (0).dp)
        ) {
            DropdownMenuItem(
                text = { Text("Equipos", color = Color.White, fontSize = 18.sp) },
                onClick = {
                    navController.navigate("equipos")
                    menuExpanded = false
                },
                modifier = Modifier.testTag("menuEquipos")
            )
            DropdownMenuItem(
                text = { Text("Jornadas", color = Color.White, fontSize = 18.sp) },
                onClick = {
                    navController.navigate("jornadas_por_liga_screen")
                    menuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Noticias", color = Color.White, fontSize = 18.sp) },
                onClick = {
                    navController.navigate("noticias")
                    menuExpanded = false
                },
                modifier = Modifier.testTag("menuNoticias")
            )
            DropdownMenuItem(
                text = { Text("Tienda", color = Color.White, fontSize = 18.sp) },
                onClick = {
                    navController.navigate("tienda")
                    menuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Tabla", color = Color.White, fontSize = 18.sp) },
                onClick = {
                    navController.navigate("tabla")
                    menuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Mi Perfil", color = Color.White, fontSize = 18.sp) },
                onClick = {
                    navController.navigate("miperfil")
                    menuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Admin", color = Color.White, fontSize = 18.sp) },
                onClick = {
                    navController.navigate("admin")
                }
            )
        }
    }
}

// DropdownMenu
// Es un menú desplegable en Compose que se muestra encima de la UI principal.
// Se usa para mostrar opciones adicionales que aparecen al hacer clic en un botón o icono.
// Propiedades principales:
// - expanded: Boolean → indica si el menú está abierto (true) o cerrado (false).
// - onDismissRequest: () -> Unit → función que se ejecuta cuando se quiere cerrar el menú
// (ej: tocar afuera).
// - x → mueve el menú horizontalmente (positivo a la derecha, negativo a la izquierda)
// - y → mueve el menú verticalmente (positivo hacia abajo, negativo hacia arriba)
// - offset: DpOffset → desplaza el menú respecto a su posición por defecto (horizontal y vertical).

// popUpTo(navController.graph.startDestinationId)
// Sirve para controlar qué pantallas del stack de navegación se deben eliminar al navegar a otra pantalla.

// NavGraph
// Es el "mapa de navegación" que define todas las pantallas disponibles en la app y cuál es la inicial.
// Ejemplo:
// NavHost(navController, startDestination = "login") { ... }
// Aquí el startDestination = "login", es decir, la primera pantalla que se muestra.

// Stack de navegación
// Es la "pila de pantallas" que el usuario ha visitado hasta ahora.
// Por ejemplo, si el usuario va de login → registro → equipos, el stack queda así:
// [login, registro, equipos]

// graph.startDestinationId
// Es el ID de la pantalla inicial de el NavGraph. En nuestro caso es "login".

// popUpTo(navController.graph.startDestinationId)
// Significa: "al navegar a esta nueva pantalla, elimina todas las pantallas que están por encima de
// la inicial en el stack".
// Ejemplo usando tu stack anterior:
// stack antes: [login, registro, equipos]
// stack después de navController.navigate("noticias") { popUpTo(navController.graph.startDestinationId) }
// stack resultante: [login, noticias]
// Las pantallas intermedias (registro y equipos) se eliminan, pero la inicial (login) queda.

// launchSingleTop = true
// Esta opción evita que se cree otra instancia de la misma pantalla si ya está en el tope del stack.
// Por ejemplo, si ya estamos en "noticias" y tocamos el ícono de Noticias otra vez,
// no se volverá a crear una nueva "noticias" encima, simplemente se mantiene la actual.

// selected = currentRoute == "noticias"
// selected es un parámetro de NavigationBarItem que indica si este ítem está "activo" o "seleccionado".
// currentRoute es la ruta actual de navegación (un String que dice en qué pantalla estamos).
// currentRoute == "noticias" es una comparación que devuelve true o false:
// - true → significa que estamos en la pantalla "noticias", entonces este ítem se pinta como seleccionado.
// - false → significa que no estamos en "noticias", entonces se pinta como normal (no seleccionado).

// NavigationBarItem
// Es un ítem individual dentro de la NavigationBar.
// Cada NavigationBarItem suele tener:
//   - un icono (icon)
//   - un texto (label)
//   - un estado de selección (selected)
//   - un evento de click (onClick) para navegar.
// Sirve para construir cada opción de la barra inferior (por ejemplo: Home, Noticias, Perfil).

// currentRoute: String
// Es un parámetro de tipo String que representa la ruta actual de navegación.
// En Jetpack Compose Navigation, cada pantalla tiene una "route" (ej: "login", "principal", "noticias").
// Con este String podemos saber en qué pantalla estamos en este momento.
// En la barra de navegación, sirve para marcar cuál icono debe aparecer seleccionado.
// Ejemplo: si currentRoute = "noticias", el ícono de Noticias se pinta como activo.

// colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
// Esto sirve para personalizar los colores de un NavigationBarItem.
// NavigationBarItemDefaults.colors(...) devuelve un objeto con todos los colores que usa el item:
// - indicatorColor → color del "indicador" que aparece detrás del item seleccionado (la especie de resaltado)
// - iconColor → color del icono (no se cambió aquí, se usa el valor por defecto)
// - labelColor → color del texto (no se cambió aquí, se usa el valor por defecto)
// En este caso, se pone indicatorColor = Color.Transparent
// - Significa que no queremos que aparezca el fondo o resaltado detrás del item seleccionado
// - El item seguirá cambiando de color de texto o icono según el parámetro `selected`,
//   pero no habrá un "cuadro" detrás del ícono.