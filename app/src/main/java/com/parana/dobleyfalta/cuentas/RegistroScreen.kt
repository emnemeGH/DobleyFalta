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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.retrofit.viewmodels.RegistroViewModel

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

    val viewModel: RegistroViewModel = viewModel()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    if (loading) {
        Dialog(onDismissRequest = {}) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryOrange)
            }
        }
    }

    if (error != null) {
        contraseñaError = error
    }

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
                viewModel.clearError()
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

                usuarioError = validarCampoNoVacio(usuario, "Usuario")
                emailError = validarCampoNoVacio(email, "Email")
                contraseñaError = validarCampoNoVacio(contraseña, "Contraseña")

                //Hay que hacer esta estructura de ifs, si no, si la primer validacion da error pero la segunda no
                //se sobreeescriben los errores
                if (emailError == null) {
                    emailError = validarEmail(email)
                }

                if (contraseñaError == null) {
                    contraseñaError = validarLongitudContraseña(contraseña)
                }

                val valido = usuarioError == null && emailError == null && contraseñaError == null

                if (valido) {
                    viewModel.registrarUsuario(usuario, email, contraseña) {
                        navController.navigate("login")
                    }
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
/**
 * Valida que un campo de texto no esté vacío.
 * @param valor El contenido del campo.
 * @param nombreCampo El nombre del campo para mostrar en el mensaje.
 * @return String con mensaje de error si está vacío, o null si está completo.
 */
fun validarCampoNoVacio(valor: String, nombreCampo: String): String? {
    //Devuelve si valor esta vacio el string, si no devuelve null
    return if (valor.isBlank()) "$nombreCampo es obligatorio" else null
}

/**
 * Valida que un email tenga formato válido.
 * @param email El email a validar.
 * @return String con mensaje de error si no es válido, o null si es válido.
 */
fun validarEmail(email: String): String? {
    //Si tiene un formato valido se niega entonces no entra al if
    return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        "El email no tiene un formato correcto"
    } else null
}

/**
 * Valida que la contraseña cumpla con una longitud mínima.
 * @param contraseña La contraseña a validar.
 * @param minLength Longitud mínima requerida (default 6).
 * @return String con mensaje de error si no cumple la longitud mínima, o null si es válida.
 */
fun validarLongitudContraseña(contraseña: String): String? {
    return if (contraseña.length < 6) {
        "La contraseña debe tener al menos 6 caracteres"
    } else null
}

