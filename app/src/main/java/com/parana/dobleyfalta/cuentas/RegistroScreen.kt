package com.parana.dobleyfalta.cuentas

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.R

@Composable
fun RegistroScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val focusManager = LocalFocusManager.current

    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var mostrarContraseña by remember { mutableStateOf(false) }

    var usuarioError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var contraseñaError by remember { mutableStateOf<String?>(null) }

    val usuariosExistentes = listOf("juan", "maria")
    val emailsExistentes = listOf("juan@mail.com", "maria@mail.com")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(32.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo_transparent),
            contentDescription = "Logo",
            tint = Color.Unspecified,
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = "Crear Cuenta",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 32.dp)
        )

        OutlinedTextField(
            value = usuario,
            onValueChange = {
                usuario = it
                usuarioError = null
            },
            label = { Text("Nombre de Usuario", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(12.dp),
            isError = usuarioError != null,
            supportingText = {
                usuarioError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            },
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
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = { Text("Email", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(12.dp),
            isError = emailError != null,
            supportingText = {
                emailError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            },
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
            value = contraseña,
            onValueChange = {
                contraseña = it
                contraseñaError = null
            },
            label = { Text("Contraseña", color = LightGrey) },
            visualTransformation = if (mostrarContraseña) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { mostrarContraseña = !mostrarContraseña }) {
                    Icon(
                        painter = painterResource(
                            id = if (mostrarContraseña) R.drawable.hidden
                            else R.drawable.eye_open
                        ),
                        contentDescription = "Mostrar/Ocultar",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(12.dp),
            isError = contraseñaError != null,
            supportingText = {
                contraseñaError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            },
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
            onClick = {
                usuarioError = null
                emailError = null
                contraseñaError = null

                var valido = true

                if (usuario.isBlank()) {
                    usuarioError = "El nombre de usuario es obligatorio"
                    valido = false
                } else if (usuariosExistentes.contains(usuario.trim())) {
                    usuarioError = "Ese nombre de usuario ya existe"
                    valido = false //Esta validacion debe hacerse en el back
                }

                if (email.isBlank()) {
                    emailError = "El email es obligatorio"
                    valido = false
                    //Si tiene un formato valido se niega entonces no entra al if
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError = "El email no tiene un formato válido"
                    valido = false
                } else if (emailsExistentes.contains(email.trim())) {
                    emailError = "Ese email ya está registrado" //Esta validacion debe hacerse en el back
                    valido = false
                }

                if (contraseña.isBlank()) {
                    contraseñaError = "La contraseña es obligatoria"
                    valido = false
                } else if (contraseña.length < 6) {
                    contraseñaError = "La contraseña debe tener al menos 6 caracteres"
                    valido = false
                }

                if (valido) {
                    navController.navigate("miperfil")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Text(text = "Crear Cuenta", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Text(
            text = "¿Ya tienes cuenta? Inicia sesión",
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { navController.navigate("login") },
            color = PrimaryOrange,
            fontSize = 14.sp,
            textDecoration = TextDecoration.Underline
        )
    }
}


//GLOSARIO

//android.util.Patterns.EMAIL_ADDRESS
//Es una constante que contiene una expresión regular (regex) predefinida por Android
//para detectar si un texto tiene formato de correo electrónico válido.

//matcher(email)
//Aplica esa expresión regular al valor de la variable email.

//.matches()
//Devuelve true si el email coincide completamente con el patrón de un email válido.

//isBlank()
//devuelve true si la cadena está vacía o solo tiene espacios en blanco

//supportingText = { ... }
//Es un espacio que aparece debajo del TextField.
//Se suele usar para poner mensajes de ayuda o de error.

//isError
//es un parámetro del OutlinedTextField.
//Si lo pones en true, el campo cambia el color del borde (usualmente se pone en rojo).

//colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
//Los botones en Compose permiten configurar sus colores.
//ButtonDefaults es un objeto de ayuda que trae valores por defecto para los Button.
//La función buttonColors() devuelve un objeto que define los colores para varios estados del botón (normal, presionado, deshabilitado, etc.).

//colors = ButtonDefaults.buttonColors(
//    containerColor = PrimaryOrange, // Fondo del botón
//    contentColor = Color.White,     // Color del texto o iconos dentro del botón
//    disabledContainerColor = Color.Gray, // Fondo si está deshabilitado
//    disabledContentColor = Color.DarkGray // Texto si está deshabilitado
//)
//No hace falta pasar todos los parámetros:
//vos solo sobreescribís lo que te interesa (containerColor en tu caso) y los demás quedan con los valores por defecto de Material Design.
//Un botón deshabilitado es aquel que no se puede presionar (por ejemplo, cuando el formulario está incompleto).


// elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
//La elevación es la “sombra” que proyecta el botón para dar sensación de profundidad.
//ButtonDefaults.buttonElevation() crea un objeto que indica cuánto se eleva el botón en distintos estados.
//Parámetros comunes:
//defaultElevation: cuando está en reposo.
//pressedElevation: cuando está siendo presionado.
//disabledElevation: cuando está deshabilitado.

//Ejemplo:
//elevation = ButtonDefaults.buttonElevation(
//    defaultElevation = 8.dp,   // sombra normal
//    pressedElevation = 2.dp,   // sombra más pequeña al presionar
//    disabledElevation = 0.dp   // sin sombra si está deshabilitado
//)