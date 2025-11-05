package com.parana.dobleyfalta

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MenuLateral(
    menuExpanded: Boolean,
    onDismiss: () -> Unit,
    navController: NavController
) {
    if (menuExpanded) {
        Popup(
            onDismissRequest = onDismiss
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .clickable { onDismiss() }
                )
                    Box(
                        modifier = Modifier
                            .width(170.dp)
                            .align(Alignment.CenterEnd)
                            .background(
                                DarkGrey,
                                RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(18.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            MenuItem(painterResource(id = R.drawable.icon_team), "Equipos") {
                                navController.navigate("equipos")
                                onDismiss()
                            }
                            MenuItem(Icons.Default.ShoppingCart, "Tienda") {
                                navController.navigate("tienda")
                                onDismiss()
                            }
                            MenuItem(painterResource(id = R.drawable.icon_tabla), "Tabla") {
                                navController.navigate("tabla")
                                onDismiss()
                            }
                            MenuItem(Icons.Default.Person, "Mi Perfil") {
                                navController.navigate("miperfil")
                                onDismiss()
                            }
                            MenuItem(Icons.Default.Person, "AdminNo") {
                                navController.navigate("admin")
                                onDismiss()
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItem(icon: Painter, label: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, color = Color.White, fontSize = 18.sp)
    }
}


@Composable
fun MenuItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, color = Color.White, fontSize = 18.sp)
    }
}
