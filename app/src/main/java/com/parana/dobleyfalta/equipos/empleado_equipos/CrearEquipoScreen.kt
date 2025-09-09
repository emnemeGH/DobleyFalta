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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.R

@Composable
fun CrearEquipoScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val CardBackground = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val focusManager = LocalFocusManager.current

    var nombreEquipo by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var logoUrl by remember { mutableStateOf("") }

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
            text = "Crear Equipo",
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
            Text("Guardar Equipo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun CampoDeTexto(
    valor: String,
   //Cada vez que el usuario escribe, OutlinedTextField llama a onValueChange con el texto actualizado.
    //it vendria a ser el string que se pasa por parametro
    alCambiarValor: (String) -> Unit,
    etiqueta: String,
    error: String?
) {
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val CardBackground = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)

    OutlinedTextField(
        value = valor,
        //Para que Compose sepa qué hacer cada vez que cambie el texto, necesita ejecutar la función más tarde,
        // no en el momento en que definís el composable.
        //Si escribieras valor = it ahí, se intentaría ejecutar inmediatamente
        // (cuando it ni siquiera existe todavía).
        //Ademas no se puede modificar el valor de los parametros desde aca.
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

// popBackStack()
// Es un método del NavController que elimina (pop) la pantalla actual de la pila de navegación
// y vuelve a la pantalla anterior.
// Sintaxis básica:
// navController.popBackStack()
// Ejemplo:
// Pantalla A -> Pantalla B -> Pantalla C
// Si estando en C se ejecuta navController.popBackStack(),
// se elimina C y se vuelve a B.

//Qué hace OutlinedTextField
//OutlinedTextField(
//    value = ciudad,
//    onValueChange = { nuevoTexto -> ciudad = nuevoTexto }
//)
//value → el texto que se va a mostrar en la UI.
//onValueChange → una función que se ejecuta cuando el usuario escribe algo.
//Compose no devuelve nada, solo llama a la función que vos le pasaste.
//Esa función recibe como parámetro el nuevo texto.

//Uso de it
//Si la lambda (funcion anonima {}) tiene un solo parámetro, Kotlin permite usar it en vez de declarar un nombre:
//OutlinedTextField(
//    value = ciudad,
//    onValueChange = {
//        ciudad = it       // 'it' es el nuevo valor que escribió el usuario
//    }
//)
//Aca it representa el texto que entró en el campo.
//No lo devuelve OutlinedTextField, simplemente lo pasa como argumento a la lambda que vos definiste.
//OutlinedTextField no devuelve nada, su trabajo es llamar a tu lambda cada vez que cambie el texto.
//Lo que vos hagas dentro de la lambda (ej: actualizar ciudad) es tu responsabilidad.