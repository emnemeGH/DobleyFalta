package com.parana.dobleyfalta

import androidx.compose.foundation.background
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
            .fillMaxWidth()
            .background(Color(0xFFFFA047)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFA047), // Fondo naranja (#FFA047)
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
                        painter = painterResource(id = R.drawable.logo_transparent),
                        contentDescription = "Logo",
                        tint = Color.Unspecified
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
                        text = { Text("Login") },
                        onClick = {
                            onMenuClick("login")
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
                        text = { Text("Jornadas") },
                        onClick = {
                            onMenuClick("jornadas")
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
