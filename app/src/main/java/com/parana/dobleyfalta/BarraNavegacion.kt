package com.parana.dobleyfalta

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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

    Box(modifier = Modifier.fillMaxSize()) {
        // Barra inferior
        NavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            containerColor = DarkBlue,
            tonalElevation = 4.dp
        ) {
            // 1️⃣ Noticias
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

            // 2️⃣ Jornadas
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

            // 3️⃣ Home (resaltado un poco más grande y en naranja por defecto)
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
                        tint = PrimaryOrange, // Siempre resaltado
                        modifier = Modifier.size(34.dp) // 👈 más grande que los demás
                    )
                },
                label = {
                    Text(
                        text = "Home",
                        color = PrimaryOrange,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )

            // 4️⃣ Login
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

            // 5️⃣ Menú (desplegable)
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
                .align(Alignment.BottomEnd),
            offset = DpOffset(x = (-16).dp, y = (-56).dp)
        ) {
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
                    navController.navigate("jornadas_por_liga_screen")
                    menuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Noticias", color = Color.White) },
                onClick = {
                    navController.navigate("noticias")
                    menuExpanded = false
                },
                modifier = Modifier.testTag("menuNoticias")
            )
            DropdownMenuItem(
                text = { Text("Tienda", color = Color.White) },
                onClick = {
                    navController.navigate("tienda")
                    menuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Mi Perfil", color = Color.White) },
                onClick = {
                    navController.navigate("miperfil")
                    menuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Admin", color = Color.White) },
                onClick = {
                    navController.navigate("admin")
                }
            )
        }
    }
}
