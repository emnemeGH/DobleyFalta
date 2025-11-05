package com.parana.dobleyfalta.adminpantallas

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.retrofit.viewmodels.admin.AdminEditarUsuarioViewModel
import kotlin.text.isBlank

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditUserScreen(navController: NavController, idUsuario: Int) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val focusManager = LocalFocusManager.current

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var mostrarContraseña by remember { mutableStateOf(false) }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var correoError by remember { mutableStateOf<String?>(null) }
    var contraseñaError by remember { mutableStateOf<String?>(null) }

    val roles = listOf("Registrado", "Empleado")
    var expanded by remember { mutableStateOf(false) }
    var rolSeleccionado by remember { mutableStateOf(roles[0]) }

    val viewModel: AdminEditarUsuarioViewModel = viewModel()
    val usuario by viewModel.usuario.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(idUsuario) {
        viewModel.cargarUsuario(idUsuario)
    }

    LaunchedEffect(usuario) {
        usuario?.let {
            nombre = it.nombre
            correo = it.correo
            rolSeleccionado = it.rol.name
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

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Editar Usuario",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

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
            isError = nombreError != null,
            supportingText = {
                nombreError?.let {
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
            value = correo,
            onValueChange = {
                correo = it
                correoError = null
            },
            label = { Text("Correo electrónico", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            isError = correoError != null,
            supportingText = {
                correoError?.let {
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            isError = contraseñaError != null,
            supportingText = {
                contraseñaError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            },
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

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            OutlinedTextField(
                value = rolSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Rol", color = LightGrey) },
                modifier = Modifier
                    .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkGrey,
                    unfocusedContainerColor = DarkGrey,
                    unfocusedBorderColor = DarkGrey,
                    focusedBorderColor = PrimaryOrange,
                    cursorColor = PrimaryOrange,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.Gray,
                    disabledContainerColor = DarkGrey,
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                roles.forEach { rol ->
                    DropdownMenuItem(
                        text = { Text(rol) },
                        onClick = {
                            rolSeleccionado = rol
                            expanded = false
                        },
                    )
                }
            }
        }

        Button(
            onClick = {

                var valido = true

                if (nombre.isBlank()) {
                    nombreError = "El nombre es obligatorio"
                    valido = false
                }

                if (correo.isBlank()) {
                    correoError = "El email es obligatorio"
                    valido = false
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    correoError = "El formato del email no es válido"
                    valido = false
                }

                if (contraseña.length < 6 && contraseña.length >= 1) {
                    contraseñaError = "La contraseña debe tener al menos 6 caracteres"
                    valido = false
                }

                if (valido) {
                    viewModel.actualizarUsuario(
                        id = idUsuario,
                        nombre = nombre,
                        correo = correo,
                        nuevaContrasena = contraseña,
                        rol = rolSeleccionado
                    ) {
                        navController.navigate("admin")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}


