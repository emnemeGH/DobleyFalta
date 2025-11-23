package com.parana.dobleyfalta.jornadas.JornadasPorLigaScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.parana.dobleyfalta.SessionManager
import com.parana.dobleyfalta.retrofit.models.auth.Rol
import com.parana.dobleyfalta.retrofit.models.ligas.LigaModel
import com.parana.dobleyfalta.retrofit.viewmodels.ligas.LigasViewModel
import com.parana.dobleyfalta.retrofit.models.jornadas.JornadaModel
import com.parana.dobleyfalta.retrofit.viewmodels.JornadasViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

val DarkBlue = Color(0xFF102B4E)
val PrimaryOrange = Color(0xFFFF6600)
val DarkGrey = Color(0xFF1A375E)
val LightGrey = Color(0xFFA0B3C4)
val RedDelete = Color(0xFFE53935)

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
    val jornadasViewModel: JornadasViewModel = viewModel()

    val ligas by ligasViewModel.ligas.collectAsState()
    val loading by ligasViewModel.loading.collectAsState()
    val errorLiga by ligasViewModel.error.collectAsState()
    val errorJornada by jornadasViewModel.error.collectAsState()

    var selectedLiga by remember { mutableStateOf<LigaModel?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var mostrarConfirmacionBorradoLiga by remember { mutableStateOf(false) }
    var ligaAEliminar by remember { mutableStateOf<LigaModel?>(null) }
    var mostrarConfirmacionBorradoJornada by remember { mutableStateOf(false) }
    var jornadaAEliminar by remember { mutableStateOf<JornadaModel?>(null) }
    var mostrarDialogoError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val rolUsuario = sessionManager.getRolUsuario()

    LaunchedEffect(Unit) {
        ligasViewModel.cargarLigas()
    }

    LaunchedEffect(errorLiga, errorJornada) {
        val currentError = errorLiga ?: errorJornada
        if (!currentError.isNullOrEmpty()) {
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (selectedLiga != null) {
                IconButton(
                    onClick = { selectedLiga = null },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver a Ligas",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(36.dp))
            }
            Text(
                text = selectedLiga?.nombre ?: "Ligas",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
            )

            if (rolUsuario == Rol.Empleado) {
                if (selectedLiga == null) {
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
                } else {
                    val idLigaValido = selectedLiga?.idLiga ?: 0
                    val isButtonEnabled = idLigaValido > 0
                    Button(
                        onClick = {
                            val idLiga = selectedLiga!!.idLiga
                            navController.navigate("crear_jornada/$idLiga")
                        },
                        enabled = isButtonEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryOrange,
                            disabledContainerColor = DarkGrey
                        ),
                        shape = RoundedCornerShape(24.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Crear Nueva Jornada",
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.size(36.dp))
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            when {
                loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = PrimaryOrange
                    )
                }

                (errorLiga != null || errorJornada != null) && !mostrarDialogoError -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error al cargar/operar. Intenta recargar las ligas.",
                            color = Color.White
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = {
                                ligasViewModel.cargarLigas()
                                ligasViewModel.clearError()
                                jornadasViewModel.clearError()
                            },
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
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        items(ligas) { liga ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
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
                                    Text(
                                        text = liga.nombre,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                selectedLiga = liga
                                                jornadasViewModel.cargarJornadasDeLiga(liga.idLiga)
                                            }
                                    )

                                    if (rolUsuario == Rol.Empleado) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(-10.dp)
                                        ) {
                                            IconButton(
                                                onClick = { navController.navigate("editar_liga/${liga.idLiga}") }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Editar Liga",
                                                    tint = LightGrey,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }

                                            IconButton(
                                                onClick = {
                                                    ligaAEliminar = liga
                                                    mostrarConfirmacionBorradoLiga = true
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Eliminar Liga",
                                                    tint = RedDelete,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(selectedLiga?.jornadas ?: emptyList()) { jornada ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
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
                                            fontSize = 18.sp,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable {
                                                    val ligaId = selectedLiga?.idLiga ?: 0
                                                    navController.navigate("jornadas_screen/$ligaId/${jornada.numero}")
                                                }
                                        )

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.End
                                        ) {
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

                                            Spacer(modifier = Modifier.width(8.dp))

                                            if (rolUsuario == Rol.Empleado) {
                                                IconButton(
                                                    onClick = { navController.navigate("editar_jornada/${jornada.idJornada}/${selectedLiga?.idLiga}") },
                                                    modifier = Modifier.size(36.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = "Editar Jornada",
                                                        tint = LightGrey,
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                }

                                                IconButton(
                                                    onClick = {
                                                        jornadaAEliminar = jornada
                                                        mostrarConfirmacionBorradoJornada = true
                                                    },
                                                    modifier = Modifier.size(36.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Eliminar Jornada",
                                                        tint = RedDelete,
                                                        modifier = Modifier.size(24.dp)
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
            }
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.BottomCenter)
    )

    if (mostrarConfirmacionBorradoLiga && ligaAEliminar != null) {
        val liga = ligaAEliminar!!
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionBorradoLiga = false },
            title = { Text("Confirmar Eliminación de Liga", fontWeight = FontWeight.Bold, color = Color.White) },
            text = { Text("¿Seguro de eliminar la liga **${liga.nombre}**? Esto borrará todas sus jornadas y partidos.", color = LightGrey) },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarConfirmacionBorradoLiga = false
                        ligaAEliminar = null
                        ligasViewModel.eliminarLiga(liga.idLiga) {
                            scope.launch { snackbarHostState.showSnackbar("Liga '${liga.nombre}' eliminada.") }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedDelete)
                ) { Text("Borrar") }
            },
            dismissButton = { TextButton(onClick = { mostrarConfirmacionBorradoLiga = false }) { Text("Cancelar", color = PrimaryOrange) } },
            containerColor = DarkGrey,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (mostrarConfirmacionBorradoJornada && jornadaAEliminar != null) {
        val jornada = jornadaAEliminar!!
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionBorradoJornada = false },
            title = { Text("Confirmar Eliminación de Jornada", fontWeight = FontWeight.Bold, color = Color.White) },
            text = { Text("¿Seguro de eliminar la Jornada Nº ${jornada.numero}? Esto eliminará todos los partidos.", color = LightGrey) },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarConfirmacionBorradoJornada = false
                        jornadaAEliminar = null
                        jornadasViewModel.eliminarJornada(jornada.idJornada) {
                            ligasViewModel.cargarLigas()
                            scope.launch { snackbarHostState.showSnackbar("Jornada Nº ${jornada.numero} eliminada.") }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedDelete)
                ) { Text("Borrar") }
            },
            dismissButton = { TextButton(onClick = { mostrarConfirmacionBorradoJornada = false }) { Text("Cancelar", color = PrimaryOrange) } },
            containerColor = DarkGrey,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (mostrarDialogoError) {
        val errorMsg = errorLiga ?: errorJornada ?: "Error desconocido."
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoError = false
                ligasViewModel.clearError()
                jornadasViewModel.clearError()
            },
            title = { Text("Error al realizar la operación", fontWeight = FontWeight.Bold, color = Color.White) },
            text = { Text(errorMsg, color = LightGrey) },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogoError = false
                        ligasViewModel.clearError()
                        jornadasViewModel.clearError()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                ) { Text("Aceptar") }
            },
            containerColor = DarkGrey,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
