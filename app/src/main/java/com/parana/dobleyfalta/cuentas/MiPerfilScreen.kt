package com.parana.dobleyfalta.cuentas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import com.parana.dobleyfalta.R

@Composable
fun ProfileScreen(navController: NavController) {
    var mostrarConfirmacionBorrado by remember { mutableStateOf(false) }

    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val RedColor = colorResource(id = R.color.red_delete)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            //SpaceBetween
            //El primer hijo va al inicio (izquierda)
            //El último hijo va al final (derecha).
            //Si hay más de dos hijos, el espacio se reparte entre los elementos (pero no antes del primero ni después del último).
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mi Perfil",
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, PrimaryOrange, CircleShape)
                    .padding(15.dp)
            )
            Spacer(modifier = Modifier.width(25.dp))
            Column {
                Text(
                    "Usuario Registrado",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    "usuario@example.com",
                    color = LightGrey,
                    fontSize = 14.sp
                )
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .background(Color.Blue, RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("Usuario", color = Color.White, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = DarkGrey),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Equipo Favorito", color = Color.White, fontWeight = FontWeight.Bold)
                Text(
                    "Suscríbete para recibir notificaciones de tu equipo.",
                    color = LightGrey,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 12.dp, top = 2.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2C405A), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_transparent),
                        contentDescription = "Team Logo",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        "Club Estudiantes",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(end = 55.dp)
                    )
                    TextButton(onClick = { navController.navigate("login") }) {
                        Text("Cambiar", color = PrimaryOrange, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = DarkGrey),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Configuración de la cuenta",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                OpcionPerfil("Editar Perfil") { navController.navigate("editar_perfil") }

                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                OpcionPerfil("Cambiar Contraseña") { navController.navigate("cambiar_contraseña") }

                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )

                OpcionPerfilPeligro(
                    text = "Cerrar Sesión",
                    icon = painterResource(id = R.drawable.logout),
                    onClick = { navController.navigate("login") }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )

                OpcionPerfilPeligro(
                    text = "Borrar Cuenta",
                    icon = painterResource(id = R.drawable.ic_delete),
                    onClick = {
                        mostrarConfirmacionBorrado = true
                    }
                )
            }
        }
    }

    if (mostrarConfirmacionBorrado) {
    AlertDialog(
        onDismissRequest = {
            mostrarConfirmacionBorrado = false
        },
        title = {
            Text("Confirmar eliminación", fontWeight = FontWeight.Bold, color = Color.White)
        },
        text = {
            Text(stringResource(R.string.delete), color = LightGrey)
        },
        confirmButton = {
            Button(
                onClick = {
                    mostrarConfirmacionBorrado = false
                    navController.navigate("login")
                },
                colors = ButtonDefaults.buttonColors(containerColor = RedColor)
            ) {
                Text("Borrar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    mostrarConfirmacionBorrado = false
                }
            ) {
                Text("Cancelar", color = PrimaryOrange)
            }
        },
        containerColor = DarkGrey,
        shape = RoundedCornerShape(16.dp)
    )
}
}

@Composable
//onClick es un parámetro de tipo función:
//() -> Unit significa:
//() → no recibe parámetros.
//Unit __ no devuelve nada (es como void en otros lenguajes).
fun OpcionPerfil(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            //clickable() hace que el row reaccione a clicks
            //Recibe un bloque de código { en este caso es la funcion que le pasemos por parametro}
            // que se ejecuta cuando el usuario toca ese componente
            .clickable (
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ){ onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = Color.White, fontSize = 14.sp)
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
fun OpcionPerfilPeligro(text: String, icon: Painter, onClick: () -> Unit) {
    val RedColor = colorResource(id = R.color.red_delete)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = text,
            tint = RedColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, color = RedColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}


//GLOSARIO

//AlertDialog(...)
//Este es un diálogo de alerta, un componente que muestra un mensaje emergente en la pantalla
//normalmente con uno o varios botones.

//onDismissRequest = { ... }
//Esta es una lambda que se ejecuta cuando el usuario intenta cerrar el diálogo
//sin hacer clic explícitamente en un botón del mismo (por ejemplo, tocando fuera del cuadro de diálogo
// o presionando el botón "atrás").

//.clip(CircleShape)
//El modifier .clip(...) recorta el contenido de un Composable según una forma geométrica (shape).
//(todo lo que “dibuja” algo en la pantalla es un Composable)
//En este caso, CircleShape → un círculo perfecto.