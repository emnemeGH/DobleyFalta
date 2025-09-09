package com.parana.dobleyfalta.equipos.empleado_equipos

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.R

@Composable
fun EditarEquipoScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val focusManager = LocalFocusManager.current

    var nombreEquipo by remember { mutableStateOf("Club Atlético Paracao") }
    var ciudad by remember { mutableStateOf("Paraná") }
    var direccion by remember { mutableStateOf("Av. de los Constituyentes 123") }
    var logoUrl by remember { mutableStateOf("https://example.com/logo.png") }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var ciudadError by remember { mutableStateOf<String?>(null) }
    var direccionError by remember { mutableStateOf<String?>(null) }
    var logoUrlError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Volver a equipos",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Editar Equipo",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        CampoDeTexto(
            valor = nombreEquipo,
            alCambiarValor = {
                nombreEquipo = it
                nombreError = null
            },
            etiqueta = "Nombre del equipo",
            error = nombreError
        )

        CampoDeTexto(
            valor = ciudad,
            alCambiarValor = {
                ciudad = it
                ciudadError = null
            },
            etiqueta = "Ciudad",
            error = ciudadError
        )

        CampoDeTexto(
            valor = direccion,
            alCambiarValor = {
                direccion = it
                direccionError = null
            },
            etiqueta = "Dirección",
            error = direccionError
        )

        CampoDeTexto(
            valor = logoUrl,
            alCambiarValor = {
                logoUrl = it
                logoUrlError = null
            },
            etiqueta = "URL del Logo",
            error = logoUrlError
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                nombreError = null
                ciudadError = null
                direccionError = null
                logoUrlError = null
                var esValido = true

                if (nombreEquipo.isBlank()) {
                    nombreError = "El nombre es obligatorio"
                    esValido = false
                }
                if (ciudad.isBlank()) {
                    ciudadError = "La ciudad es obligatoria"
                    esValido = false
                }
                if (direccion.isBlank()) {
                    direccionError = "La dirección es obligatoria"
                    esValido = false
                }
                if (logoUrl.isBlank()) {
                    logoUrlError = "La URL del logo es obligatoria"
                    esValido = false
                }

                if (esValido) {
                    navController.popBackStack()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun CampoDeTexto(
    valor: String,
    alCambiarValor: (String) -> Unit,
    etiqueta: String,
    error: String?
) {
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val CardBackground = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)

    OutlinedTextField(
        value = valor,
        onValueChange = alCambiarValor,
        label = { Text(etiqueta, color = LightGrey) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CardBackground,
            unfocusedContainerColor = CardBackground,
            unfocusedBorderColor = CardBackground,
            focusedBorderColor = PrimaryOrange,
            cursorColor = PrimaryOrange,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
        ),
        isError = error != null,
        supportingText = {
            error?.let {
                Text(it, color = Color.Red, fontSize = 12.sp)
            }
        },
        singleLine = true
    )
}