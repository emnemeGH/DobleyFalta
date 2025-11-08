package com.parana.dobleyfalta.adminpantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.SessionManager
import com.parana.dobleyfalta.retrofit.models.auth.Usuario
import com.parana.dobleyfalta.retrofit.repositories.UsuariosRepository
import com.parana.dobleyfalta.retrofit.viewmodels.admin.AdminUsuariosViewModel
import kotlinx.coroutines.launch


@Composable
fun AdminScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val RedDelete = colorResource(id = R.color.red_delete)

    val viewModel: AdminUsuariosViewModel = viewModel()

    val usuarios by viewModel.usuarios.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var usuarioABorrar by remember { mutableStateOf<Usuario?>(null) }
    var mostrarConfirmacionBorrado by remember { mutableStateOf(false) }

    val repository = remember { UsuariosRepository() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context.applicationContext) }

    LaunchedEffect(Unit) {
        viewModel.cargarUsuarios()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Panel de Admin",
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                TextButton(
                    onClick = {
                        sessionManager.clearSession()

                        navController.navigate("login") {
                            popUpTo(0)
                        }
                    }
                ) {
                    Text("Cerrar sesión", color = PrimaryOrange, fontSize = 14.sp)
                }
            }

            Button(
                onClick = { navController.navigate("admin_crear_usuario") },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Crear Usuario",
                    modifier = Modifier.size(18.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("Crear Empleado", color = Color.White)
            }
        }

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryOrange)
            }
        } else if (error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(error ?: "", color = Color.Red)
            }
        } else {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkGrey),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Lista de Usuarios",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(usuarios) { usuario ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF2C405A), RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                //Las propiedades de alineación (horizontalArrangement, verticalAlignment, etc.) afectan solo a los hijos directos del contenedor.
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                //Hay solo 2 Rows que son hijos del Row padre
                                //De esta forma el primer row queda a la izquierda y el segundo a la derecha
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFA0B3C4)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            //Text() espera un String, no acepta Char
                                            text = usuario.nombre.first().toString(),
                                            color = DarkBlue,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Column(modifier = Modifier.padding(start = 12.dp)) {
                                        Text(
                                            usuario.nombre,
                                            color = Color.White,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            usuario.correo,
                                            color = LightGrey,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            usuario.rol.name,
                                            color = LightGrey,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(top = 1.dp)
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(
                                        onClick = { navController.navigate("admin_editar_usuario/${usuario.idUsuario}") },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_edit),
                                            contentDescription = "Editar",
                                            tint = colorResource(id = R.color.blue_edit),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            usuarioABorrar = usuario
                                            mostrarConfirmacionBorrado = true
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_delete),
                                            contentDescription = "Eliminar",
                                            tint = RedDelete,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (mostrarConfirmacionBorrado) {
                        AlertDialog(
                            onDismissRequest = { mostrarConfirmacionBorrado = false },
                            title = {
                                Text("Confirmar eliminación", fontWeight = FontWeight.Bold, color = Color.White)
                            },
                            text = {
                                Text("¿Seguro que quieres eliminar este usuario?", color = LightGrey)
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        mostrarConfirmacionBorrado = false

                                        usuarioABorrar?.let { usuario ->
                                            scope.launch {
                                                try {
                                                    val exito = repository.eliminarUsuario(usuario.idUsuario)
                                                    if (exito) {
                                                        viewModel.cargarUsuarios()
                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                }
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                ) {
                                    Text("Borrar")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { mostrarConfirmacionBorrado = false }) {
                                    Text("Cancelar", color = PrimaryOrange)
                                }
                            },
                            containerColor = DarkGrey,
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }
            }
        }
    }
}