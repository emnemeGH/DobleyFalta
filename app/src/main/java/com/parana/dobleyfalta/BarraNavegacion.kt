package com.parana.dobleyfalta

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

val DarkBlue = Color(0xFF102B4E)
val PrimaryOrange = Color(0xFFFF6600)
val DarkGrey = Color(0xFF1A375E)

@Composable
fun AppBottomNavigationBar(
    navController: NavHostController,
    currentRoute: String
) {
    var menuExpanded by remember { mutableStateOf(false) }

    NavigationBar(
        modifier = Modifier,
        containerColor = DarkBlue,
        tonalElevation = 4.dp
    ) {
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
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {
                Text(
                    text = "Noticias",
                    color = if (currentRoute == "noticias") PrimaryOrange else Color.White
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )

        // Ícono de Partidos/Jornadas
        NavigationBarItem(
            selected = currentRoute == "jornadas",
            onClick = {
                navController.navigate("jornadas") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_basketball),
                    contentDescription = "Jornadas",
                    tint = if (currentRoute == "jornadas") PrimaryOrange else Color.White,
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {
                Text(
                    text = "Jornadas",
                    color = if (currentRoute == "jornadas") PrimaryOrange else Color.White
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )

//        // Ícono de Tabla de Posiciones
//        NavigationBarItem(
//            selected = currentRoute == "clasificacion_screen",
//            onClick = {
//                navController.navigate("clasificacion_screen") {
//                    popUpTo(navController.graph.startDestinationId)
//                    launchSingleTop = true
//                }
//            },
//            icon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.icono_clasificacion),
//                    contentDescription = "Clasificación",
//                    tint = if (currentRoute == "clasificacion_screen") PrimaryOrange else Color.White
//                )
//            },
//            label = {
//                Text(
//                    text = "Clasificación",
//                    color = if (currentRoute == "clasificacion_screen") PrimaryOrange else Color.White
//                )
//            },
//            colors = NavigationBarItemDefaults.colors(
//                indicatorColor = Color.Transparent
//            )
//        )

        // Ícono de Cuenta/Login
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
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {
                Text(
                    text = "Login",
                    color = if (currentRoute == "login") PrimaryOrange else Color.White
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )

        // Ítem de menú desplegable
        NavigationBarItem(
            selected = false,
            onClick = { menuExpanded = true }, // Al hacer clic, se expande el menú
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.menu_hamburguesa),
                    contentDescription = "Menú",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {
                Text(
                    text = "Menú",
                    color = Color.White
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )
    }

    // El menú desplegable se muestra fuera del NavigationBar
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { menuExpanded = false },
        modifier = Modifier.background(DarkGrey)
    ) {
        // Los ítems del menú se navegan a través del navController
        DropdownMenuItem(
            text = { Text("Login", color = Color.White) },
            onClick = {
                navController.navigate("login")
                menuExpanded = false
            }
        )
        DropdownMenuItem(
            text = { Text("Equipos", color = Color.White) },
            onClick = {
                navController.navigate("equipos")
                menuExpanded = false
            }
        )
        DropdownMenuItem(
            text = { Text("Jornadas", color = Color.White) },
            onClick = {
                navController.navigate("jornadas")
                menuExpanded = false
            }
        )
        DropdownMenuItem(
            text = { Text("Noticias", color = Color.White) },
            onClick = {
                navController.navigate("noticias")
                menuExpanded = false
            }
        )
    }
}