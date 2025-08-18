package com.parana.dobleyfalta

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String = "",
    onMenuClick: (String) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFA047) // Fondo naranja (#FFA047)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color(0xFF162938)
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* acción si quieres al clickear el logo */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.logo_transparent), // tu logo en drawable
                    contentDescription = "Logo"
                )
            }
        },
        actions = {
            IconButton(onClick = { menuExpanded = !menuExpanded }) {
                Icon(
                    painter = painterResource(id = R.drawable.menu_hamburguesa), // un ícono hamburguesa
                    contentDescription = "Menú"
                )
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Inicio") },
                    onClick = {
                        onMenuClick("home")
                        menuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Equipos") },
                    onClick = {
                        onMenuClick("equipos")
                        menuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Tabla de posiciones") },
                    onClick = {
                        onMenuClick("tabla")
                        menuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Noticias") },
                    onClick = {
                        onMenuClick("noticias")
                        menuExpanded = false
                    }
                )
            }
        }
    ) //parentesis del topbar
    } //llave del card
}
