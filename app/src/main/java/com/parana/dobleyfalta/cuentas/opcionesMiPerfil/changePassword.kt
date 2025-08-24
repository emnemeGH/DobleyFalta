package com.parana.dobleyfalta.cuentas.opcionesMiPerfil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.parana.dobleyfalta.R

@Composable
fun ChangePasswordScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val focusManager = LocalFocusManager.current

    var contraseña by remember { mutableStateOf("") }
    var new_contraseña by remember { mutableStateOf("") }
    var verif_contraseña by remember { mutableStateOf("") }

//Crea un objeto especial que guarda un valor (false en este caso).
//La diferencia con una variable normal es que cuando ese valor cambia, Jetpack Compose redibuja automáticamente la UI donde se usa.
//Es decir: si mostrarContraseña cambia de false a true, Compose se da cuenta y actualiza el OutlinedTextField para mostrar la contraseña.
    var mostrarContraseña by remember { mutableStateOf(false) }
    var mostrarContraseñaVerif by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(32.dp)
            .padding(top = 90.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Cambiar Contraseña",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = contraseña,
            onValueChange = { contraseña = it },
            label = { Text("Contraseña Actual", color = LightGrey) },
            visualTransformation = PasswordVisualTransformation(),
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
            )
        )

        OutlinedTextField(
            value = new_contraseña,
            onValueChange = { new_contraseña = it },
            label = { Text("Nueva Contraseña", color = LightGrey) },
            visualTransformation = if (mostrarContraseña) VisualTransformation.None
            else PasswordVisualTransformation(),
            //trailingIcon Es un icono que aparece al final del campo, a la derecha del texto
            trailingIcon = {
                IconButton(onClick = { mostrarContraseña = !mostrarContraseña }) {
                    Icon(
                        painter = painterResource(id = R.drawable.eyebrow),
                        contentDescription = "Mostrar/Ocultar",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }
            },
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
            )
        )

        OutlinedTextField(
            value = verif_contraseña,
            onValueChange = {verif_contraseña = it },
            label = { Text("Confirmar Nueva Contraseña", color = LightGrey) },
            visualTransformation = if (mostrarContraseñaVerif) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { mostrarContraseñaVerif = !mostrarContraseñaVerif }) {
                    Icon(
                        painter = painterResource(id = R.drawable.eyebrow),
                        contentDescription = "Mostrar/Ocultar",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }
            },
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
            )
        )

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Text("Actualizar Contraseña", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

//GLOSARIO

//var contraseña by remember { mutableStateOf("") }

    //mutableStateOf("")
    //Crea un objeto especial de tipo MutableState<String> (Observable).
    //Contiene un valor (value) que empieza como "".
    //Cuando el usuario escribe, cambia el value y Compose redibuja la UI automáticamente con lo nuevo
    // (podes ver en el input lo que vas escribiendo).

    //remember { ... }
    //remember = “acordate de este valor mientras la pantalla siga viva”.
    //Si no lo usás, cada vez que Compose redibuja, la variable vuelve a estar vacía.

    //by
    //Cada variable en Kotlin tiene implícitamente dos cosas:
    //get → qué pasa cuando lees el valor (val x = contraseña)
    //set → qué pasa cuando escribís un valor (contraseña = "hola")
    //Normalmente Kotlin hace esto automáticamente, pero podés personalizarlo con el by.
    //ya no le decís a Kotlin “guardá el valor en tu propia caja”.
    //Le decís: “cada vez que leas o escribas esta variable, usa el objeto mutableStateOf("") para manejarlo”.
    //Entonces:
    //get contraseña → automáticamente hace mutableState.value
    //set contraseña = "hola" → automáticamente hace mutableState.value = "hola"
    //El by conecta los get/set de la variable con los get/set del objeto.
    //Basciamente Ahorra tener que poner contraseña.value para acceder al valor.
    //Con by, contraseña escrito solo, significa contraseña.value

//value = contraseña
//Este es el valor que se muestra en el campo de texto.
//Cada vez que Compose dibuja la pantalla, mira value para saber qué poner dentro del TextField.

//onValueChange = { contraseña = it }
//Esto es lo que pasa cuando el usuario escribe algo en el TextField.
//it es el nuevo texto que el usuario tipeó.
//La línea { contraseña = it } actualiza la variable contraseña con lo nuevo que el usuario tipeo.
//Asi al escrbir en el textField se cambia la variable contraseña que con el mutableState redibuja
//el textField para simplemente ver lo que se esta escribiendo

//PasswordVisualTransformation()
//Hace que se vea con puntitos en vez de verse la contraseña