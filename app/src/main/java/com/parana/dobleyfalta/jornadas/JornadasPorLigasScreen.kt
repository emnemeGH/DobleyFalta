package com.parana.dobleyfalta.jornadas.empleado

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.MainViewModel
import com.parana.dobleyfalta.R

// Definición de colores
val CardBackground = Color(0xFF1A375E)
val TextWhite = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JornadasPorLigaScreen(navController: NavController, mainViewModel: MainViewModel) {
    // Inicialización de los colores dentro de la función Composable
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val BlueEdit = colorResource(id = R.color.blue_edit)
    val RedDelete = colorResource(id = R.color.red_delete)

    var selectedLiga by remember { mutableStateOf<String?>(null) }
    val rol = mainViewModel.rolUsuario.value
    val ligas = mainViewModel.ligas

    // Estado para controlar el diálogo de eliminación de ligas
    var mostrarConfirmacionBorradoLiga by remember { mutableStateOf(false) }
    var ligaAEliminar by remember { mutableStateOf<String?>(null) }

    // Estado para controlar el diálogo de eliminación de jornadas
    var mostrarConfirmacionBorradoJornada by remember { mutableStateOf(false) }
    var jornadaAEliminar by remember { mutableStateOf<Int?>(null) }

    // Lista mutable para las jornadas para poder eliminarlas
    val jornadas = remember { mutableStateListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10) }


    Scaffold(
        containerColor = DarkBlue,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = selectedLiga ?: "Selecciona una Liga",
                        color = TextWhite
                    )
                },
                navigationIcon = {
                    if (selectedLiga != null) {
                        IconButton(
                            onClick = { selectedLiga = null },
                            modifier = Modifier.padding(0.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.back),
                                contentDescription = "Volver a ligas",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { navController.navigate("principal") },
                            modifier = Modifier.padding(0.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.back),
                                contentDescription = "Volver a principal",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                },
                actions = {
                    // Botón para crear liga o agregar jornada
                    if (rol == "empleado") {
                        val onClickAction = if (selectedLiga == null) {
                            { navController.navigate("crear_liga") }
                        } else {
                            { navController.navigate("crear_jornada") }
                        }
                        val contentDescription = if (selectedLiga == null) "Crear Liga" else "Agregar jornada"

                        IconButton(
                            onClick = onClickAction,
                            modifier = Modifier
                                .size(24.dp)
                                .background(PrimaryOrange, RoundedCornerShape(24.dp))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = contentDescription,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkBlue
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedLiga == null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ligas.forEach { liga ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedLiga = liga },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = CardBackground)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = liga,
                                    color = TextWhite,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )
                                // Botones de edición y eliminación para empleados
                                if (rol == "empleado") {
                                    IconButton(onClick = { navController.navigate("editar_liga") }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_edit),
                                            contentDescription = "Editar Liga",
                                            tint = BlueEdit,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            ligaAEliminar = liga
                                            mostrarConfirmacionBorradoLiga = true
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_delete),
                                            contentDescription = "Eliminar Liga",
                                            tint = RedDelete,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "Selecciona una jornada",
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(jornadas) { jornadaNumero: Int ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("jornadas_screen/$jornadaNumero")
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = CardBackground)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Jornada $jornadaNumero",
                                    color = TextWhite,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )
                                // Botones de edición y eliminación para empleados
                                if (rol == "empleado") {
                                    IconButton(onClick = { navController.navigate("editar_jornada") }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_edit),
                                            contentDescription = "Editar Jornada",
                                            tint = BlueEdit,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            jornadaAEliminar = jornadaNumero
                                            mostrarConfirmacionBorradoJornada = true
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_delete),
                                            contentDescription = "Eliminar Jornada",
                                            tint = RedDelete,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación de eliminación de Ligas
    if (mostrarConfirmacionBorradoLiga && ligaAEliminar != null) {
        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacionBorradoLiga = false
                ligaAEliminar = null
            },
            title = {
                Text("Confirmar eliminación", fontWeight = FontWeight.Bold, color = Color.White)
            },
            text = {
                Text("¿Seguro que quieres eliminar la liga ${ligaAEliminar}?", color = LightGrey)
            },
            confirmButton = {
                Button(
                    onClick = {
                        ligaAEliminar?.let { mainViewModel.eliminarLiga(it) }
                        mostrarConfirmacionBorradoLiga = false
                        ligaAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Borrar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacionBorradoLiga = false
                        ligaAEliminar = null
                    }) {
                    Text("Cancelar", color = PrimaryOrange)
                }
            },
            containerColor = CardBackground,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Diálogo de confirmación de eliminación de Jornadas
    if (mostrarConfirmacionBorradoJornada && jornadaAEliminar != null) {
        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacionBorradoJornada = false
                jornadaAEliminar = null
            },
            title = {
                Text("Confirmar eliminación", fontWeight = FontWeight.Bold, color = Color.White)
            },
            text = {
                Text("¿Seguro que quieres eliminar la jornada ${jornadaAEliminar}?", color = LightGrey)
            },
            confirmButton = {
                Button(
                    onClick = {
                        jornadaAEliminar?.let { jornadas.remove(it) }
                        mostrarConfirmacionBorradoJornada = false
                        jornadaAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Borrar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacionBorradoJornada = false
                        jornadaAEliminar = null
                    }) {
                    Text("Cancelar", color = PrimaryOrange)
                }
            },
            containerColor = CardBackground,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
