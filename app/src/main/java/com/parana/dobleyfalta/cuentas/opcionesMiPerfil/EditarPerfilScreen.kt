package com.parana.dobleyfalta.cuentas.opcionesMiPerfil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.SessionManager
import com.parana.dobleyfalta.retrofit.viewmodels.miperfil.PerfilViewModel

@Composable
fun EditProfileScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context.applicationContext) }

    val idUsuario = sessionManager.getIdUsuario()

    val viewModel: PerfilViewModel = viewModel()

    val usuario by viewModel.usuario.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var correoError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        idUsuario?.let { viewModel.cargarUsuario(it) }
    }

    LaunchedEffect(usuario) {
        usuario?.let {
            nombre = it.nombre
            correo = it.correo
        }
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

    if (error != null) {
        correoError = error
    }

    Column(
        modifier = Modifier
            // El Column ocupa todo el fillMaxSize() con el fondo DarkBlue
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
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(0.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Volver",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = "Editar Perfil",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        if (error != null && usuario == null) {
            Text(
                text = error ?: "Error al cargar el perfil",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                nombreError = null
            },
            label = { Text("Nombre de Usuario", color = LightGrey) },
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
            isError = nombreError != null,
            supportingText = {
                nombreError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }
            },
            singleLine = true
        )

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                correoError = null
                viewModel.clearError()
            },
            label = { Text("Correo", color = LightGrey) },
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
            isError = correoError != null,
            supportingText = {
                correoError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }
            },
            singleLine = true
        )

        Button(
            onClick = {
                nombreError = null
                correoError = null

                var valido = true

                if (nombre.isBlank()) {
                    nombreError = "El nombre de usuario es obligatorio"
                    valido = false
                }

                if (correo.isBlank()) {
                    correoError = "El correo es obligatorio"
                    valido = false
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    correoError = "El formato del correo no es válido"
                    valido = false
                }

                if (valido && idUsuario != null) {
                    viewModel.actualizarUsuario(idUsuario, nombre, correo, null) {

                        viewModel.usuario.value?.let { usuarioActualizado ->
                            sessionManager.saveUsuario(usuarioActualizado)
                        }

                        navController.navigate("miperfil")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
