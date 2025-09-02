package com.parana.dobleyfalta.noticias.empleado_noticia

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import com.parana.dobleyfalta.R

@Composable
fun EditarNoticiaScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val focusManager = LocalFocusManager.current

    var tituloNoticia by remember { mutableStateOf("") }
    var contenidoNoticia by remember { mutableStateOf("") }

    var tituloError by remember { mutableStateOf<String?>(null) }
    var contenidoError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(32.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { navController.navigate("noticias") },
                modifier = Modifier.padding(0.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Volver a noticias",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Editar Noticia",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = tituloNoticia,
            onValueChange = {
                tituloNoticia = it
                tituloError = null
            },
            label = { Text("Título de la noticia", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey,
                unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey,
                focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            isError = tituloError != null,
            supportingText = {
                tituloError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            }
        )

        OutlinedTextField(
            value = contenidoNoticia,
            onValueChange = {
                contenidoNoticia = it
                contenidoError = null
            },
            label = { Text("Contenido", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp, max = 300.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey,
                unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey,
                focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            isError = contenidoError != null,
            supportingText = {
                contenidoError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            }
        )

        Button(
            onClick = {  },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkGrey),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Cambiar Imagen", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = {
                tituloError = null
                contenidoError = null

                if (tituloNoticia.isBlank()) {
                    tituloError = "El título es obligatorio"
                }
                if (contenidoNoticia.isBlank()) {
                    contenidoError = "El contenido es obligatorio"
                }

                if (tituloError == null && contenidoError == null) {
                    navController.navigate("noticias")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
