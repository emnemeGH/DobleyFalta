package com.parana.dobleyfalta

import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.animation.*
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.PopupProperties

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
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                modifier = Modifier.testTag("noticias-icon")
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
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                modifier = Modifier.testTag("jornadas-icon")
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
                selected = currentRoute == "tabla",
                onClick = {
                    navController.navigate("tabla") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_tabla),
                        contentDescription = "Tabla",
                        tint = if (currentRoute == "tabla") PrimaryOrange else Color.White,
                        modifier = Modifier
                            .size(28.dp)
                    )
                },
                label = {
                    Text(
                        text = "Tabla",
                        color = if (currentRoute == "tabla") PrimaryOrange else Color.White
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                modifier = Modifier.testTag("tabla-icon")
            )

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

        MenuLateral(
            menuExpanded = menuExpanded,
            onDismiss = { menuExpanded = false },
            navController = navController
        )

    }
}