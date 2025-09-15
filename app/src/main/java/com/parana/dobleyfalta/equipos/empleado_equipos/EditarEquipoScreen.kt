package com.parana.dobleyfalta.equipos.empleado_equipos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
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
    val LightGrey = Color(0xFFA0B3C4)
    val CardBackground = Color(0xFF1A375E)
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var nombreEquipo by remember { mutableStateOf("Club Atlético Paracao") }
    var ciudad by remember { mutableStateOf("Paraná") }
    var direccion by remember { mutableStateOf("Av. de los Constituyentes 123") }
    var logoUrl by remember { mutableStateOf("https://example.com/logo.png") }
    var descripcion by remember { mutableStateOf("El Paraná Rowing Club es un club deportivo ubicado en la Ciudad de Paraná, capital de la provincia de Entre Ríos, en Argentina.Fue fundado en 1917 con el fin de practicar remo y natación, pero luego amplió sus actividades a otros deportes como rugby, básquet, hockey Sobre césped, pelota paleta, vóley, esgrima y tenis, entre otros.") }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var ciudadError by remember { mutableStateOf<String?>(null) }
    var direccionError by remember { mutableStateOf<String?>(null) }
    var logoUrlError by remember { mutableStateOf<String?>(null) }
    var descripcionError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .verticalScroll(scrollState)
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

        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                descripcion = it
                descripcionError = null
            },
            label = { Text("Descripción", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp, max = 200.dp)
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
            isError = descripcionError != null,
            supportingText = {
                descripcionError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            }
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
                if (descripcion.isBlank()) {
                    descripcionError = "La descripción es obligatoria"
                    esValido = false
                }

                if (esValido) {
                    navController.popBackStack()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("botonGuardar"),
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
    error: String?,
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
            .padding(bottom = 16.dp)
            .testTag("campoEditarEquipo"),
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

//rememberScrollState()
// es un Composable que te permite recordar y manejar el estado de scroll (posición de desplazamiento)
// para componentes que usan scroll manual (no lazy), por ejemplo Column, Row o OutlinedTextField
//
// Sintaxis Básica:
// val scrollState = rememberScrollState()
//
// Uso típico:
// Column(
//     modifier = Modifier.verticalScroll(scrollState) // asocia el estado al scroll
// ) {
//     // Contenido que puede superar el alto de la pantalla
// }
//
// Notas:
// - scrollState guarda la posición actual del scroll, así que si el composable se recompone
//   no pierde en qué lugar estaba el usuario.
// - Lo necesitás siempre que uses verticalScroll() o horizontalScroll()
// - Si no lo usás, el contenido no va a scrollear aunque lo envuelvas en verticalScroll()
// - Es parecido a remember { mutableStateOf() } porque recuerda el valor a través de recomposiciones,
//   pero en vez de guardar datos, guarda la posición del scroll.
//Otra definicion
//Crea un estado de desplazamiento que Compose recuerda mientras la composición viva.
//Este estado contiene información de dónde está actualmente el scroll.
//Si no usaras remember, Compose crearía un nuevo estado cada vez que la pantalla se redibuja,
// y el scroll volvería a la posición inicial constantemente.

//.verticalScroll() y .horizontalScroll()
// son Modifiers que habilitan el desplazamiento (scroll) en el eje vertical u horizontal
// para composables que pueden expandirse más allá del espacio disponible.
// Se usan junto con un ScrollState (generalmente rememberScrollState()).
//
// Sintaxis Básica:
// val scrollState = rememberScrollState()
//
// Column(
//     modifier = Modifier.verticalScroll(scrollState) // permite scrollear verticalmente
// ) {
//     // Contenido largo que exceda el tamaño visible
// }
//
// Row(
//     modifier = Modifier.horizontalScroll(scrollState) // permite scrollear horizontalmente
// ) {
//     // Contenido ancho que exceda el tamaño visible
// }
//
// Notas:
// - A diferencia de LazyColumn o LazyRow, estos no son "lazy": todo el contenido se dibuja de golpe.
// - Requieren pasar un ScrollState, si no, no funcionará el scroll.
// - Ideal para formularios o pantallas con pocos elementos que pueden crecer en tamaño.
// - Si el contenido no excede el tamaño del contenedor, no se genera scroll (no hace nada).
