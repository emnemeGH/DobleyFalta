package com.parana.dobleyfalta.jornadas.JornadasPorLigaScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.parana.dobleyfalta.retrofit.models.ligas.LigaModel
import com.parana.dobleyfalta.retrofit.viewmodels.ligas.LigasViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

val DarkBlue = Color(0xFF102B4E)
val PrimaryOrange = Color(0xFFFF6600)
val DarkGrey = Color(0xFF1A375E)
val LightGrey = Color(0xFFA0B3C4)
val RedDelete = Color(0xFFE53935) // Nuevo color para el botón de borrar

fun formatearFecha(fecha: String?): String {
    if (fecha.isNullOrEmpty()) return "-"
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = parser.parse(fecha.substring(0, 10))
        formatter.format(date!!)
    } catch (e: Exception) {
        fecha
    }
}

@Composable
fun JornadasPorLigaScreen(navController: NavController) {

    val ligasViewModel: LigasViewModel = viewModel()
    val ligas by ligasViewModel.ligas.collectAsState()
    val loading by ligasViewModel.loading.collectAsState()
    val error by ligasViewModel.error.collectAsState()

    var selectedLiga by remember { mutableStateOf<LigaModel?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // 🚨 ESTADOS PARA EL DIÁLOGO DE CONFIRMACIÓN DE ELIMINACIÓN
    var mostrarConfirmacionBorrado by remember { mutableStateOf(false) }
    var ligaAEliminar by remember { mutableStateOf<LigaModel?>(null) }
    var mostrarDialogoError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        ligasViewModel.cargarLigas()
    }

    // 🚨 EFECTO PARA MOSTRAR EL DIÁLOGO DE ERROR DEL VIEWMODEL
    LaunchedEffect(error) {
        if (error != null && error!!.isNotEmpty()) {
            mostrarDialogoError = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
    ) {
        // --- TÍTULO Y BOTÓN DE CREAR LIGA ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Ligas",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1.5f)
            )

            Button(
                onClick = { navController.navigate("crear_liga") },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear Nueva Liga",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
        }
        // --- FIN TÍTULO Y BOTÓN ---

        // Contenido principal: Lista de ligas, carga o error
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            when {
                loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = PrimaryOrange
                    )
                }

                // 🚨 SE MODIFICA PARA QUE EL ERROR SEA MOSTRADO EN EL DIÁLOGO DE ABAJO
                error != null && !mostrarDialogoError -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error al cargar las ligas.",
                            color = Color.White
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { ligasViewModel.cargarLigas() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryOrange,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }

                selectedLiga == null -> {
                    // Muestra la lista de Ligas para selección
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        items(ligas) { liga ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = DarkGrey),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Texto (clickable para ver jornadas)
                                    Text(
                                        text = liga.nombre,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { selectedLiga = liga }
                                    )

                                    // Botones de acción (Editar y Borrar)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(-10.dp)
                                    ) {
                                        // 1. Botón Editar
                                        IconButton(
                                            onClick = {
                                                navController.navigate("editar_liga/${liga.idLiga}")
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Editar Liga",
                                                tint = LightGrey,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }

                                        // 2. Botón Borrar 🚨 CAMBIO AQUÍ
                                        IconButton(
                                            onClick = {
                                                ligaAEliminar = liga // Prepara la liga para borrar
                                                mostrarConfirmacionBorrado = true // Muestra el diálogo
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Eliminar Liga",
                                                tint = Color.Red,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                else -> {
                    // Muestra las Jornadas de la liga seleccionada
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // ... Contenido de jornadas (Sin cambios) ...
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(selectedLiga?.jornadas ?: emptyList()) { jornada ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val ligaId = selectedLiga?.idLiga ?: 0
                                            navController.navigate("jornadas_screen/$ligaId/${jornada.numero}")
                                        },
                                    colors = CardDefaults.cardColors(containerColor = DarkGrey),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(70.dp)
                                            .padding(horizontal = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Jornada Nº ${jornada.numero}",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 18.sp
                                        )

                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = formatearFecha(jornada.fechaInicio.toString()),
                                                color = LightGrey,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = formatearFecha(jornada.fechaFin.toString()),
                                                color = LightGrey,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { selectedLiga = null },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = PrimaryOrange
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "VOLVER A LIGAS",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
    // Añadir el Snackbar fuera del Column principal para que flote
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)
    )

    // 🚨 NUEVO: Diálogo de confirmación de eliminación de Liga
    if (mostrarConfirmacionBorrado && ligaAEliminar != null) {
        val liga = ligaAEliminar!!
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionBorrado = false },
            title = {
                Text(
                    "Confirmar Eliminación",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = { Text("¿Estás seguro de que quieres eliminar la liga **${liga.nombre}**?", color = LightGrey) },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarConfirmacionBorrado = false
                        ligaAEliminar = null // Limpiar estado temporalmente

                        // 🚨 LLAMADA A ELIMINAR LIGA CON MANEJO DE ÉXITO Y ERROR
                        ligasViewModel.eliminarLiga(
                            id = liga.idLiga,
                            onSuccess = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Liga '${liga.nombre}' eliminada con éxito.",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                            // El error se maneja automáticamente con el LaunchedEffect de arriba.
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedDelete)
                ) { Text("Borrar") }
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

    // 🚨 NUEVO: Diálogo para mostrar errores de la operación de eliminación (o carga)
    if (mostrarDialogoError) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoError = false
                ligasViewModel.clearError() // Asegúrate de tener esta función en tu ViewModel
            },
            title = {
                Text(
                    "Error al realizar la operación",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = { Text(error ?: "Error desconocido.", color = LightGrey) },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogoError = false
                        ligasViewModel.clearError()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                ) { Text("Aceptar") }
            },
            containerColor = DarkGrey,
            shape = RoundedCornerShape(16.dp)
        )
    }
}