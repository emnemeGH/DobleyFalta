package com.parana.dobleyfalta.cuentas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
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
import com.parana.dobleyfalta.R
import androidx.navigation.NavController
import com.parana.dobleyfalta.SessionManager
import com.parana.dobleyfalta.retrofit.models.auth.Rol
import com.parana.dobleyfalta.retrofit.viewmodels.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val focusManager = LocalFocusManager.current

    var v_email by remember { mutableStateOf("") }
    var v_contraseña by remember { mutableStateOf("") }
    var mostrarContraseña by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var contraseñaError by remember { mutableStateOf<String?>(null) }
    var mostrarRecuperar by remember { mutableStateOf(false) }

    val viewModel: LoginViewModel = viewModel()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context.applicationContext) }

    if(error != null) {
        contraseñaError = error
    }

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
            text = "Doble y Falta App",
            fontSize = 32.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 32.dp)
        )
        OutlinedTextField(
            value = v_email,
            onValueChange = {
                v_email = it
                emailError = null
            },
            label = { Text("Email", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .testTag("emailInput"),
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
            isError = emailError != null,
            supportingText = {
                emailError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            },
        )
        Spacer(
            modifier = Modifier.height(5.dp)
        )
        OutlinedTextField(
            value = v_contraseña,
            onValueChange = {
                v_contraseña = it
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
                .fillMaxWidth(),
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
            isError = contraseñaError != null,
            supportingText = {
                contraseñaError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }

            },
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Button(
            onClick = {
                emailError = null
                contraseñaError = null

                when {
                    v_email.isBlank() -> emailError = "El email es obligatorio"
                    v_contraseña.isBlank() -> contraseñaError = "La contraseña es obligatoria"
                    else -> {
                        viewModel.login(v_email, v_contraseña) {

                            val rol = sessionManager.getRolUsuario()

                            if (rol == Rol.Administrador) {
                                navController.navigate("admin")
                            } else {
                                navController.navigate("home")
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("loginBoton"),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
        ) {
            Text("Iniciar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Text(
            text = "¿No tienes cuenta? Regístrate aquí",
            color = PrimaryOrange,
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { navController.navigate("registro") }
        )

        LaunchedEffect(contraseñaError) {
            if (contraseñaError != null) {
                delay(1000)
                mostrarRecuperar = true
            }
        }

        if (mostrarRecuperar) {
            Text(
                text = "Recuperar Contraseña",
                color = Color.White,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable {
                        navController.navigate("recuperar_contraseña")
                    },
                textDecoration = TextDecoration.Underline
            )
        }

    }
}


