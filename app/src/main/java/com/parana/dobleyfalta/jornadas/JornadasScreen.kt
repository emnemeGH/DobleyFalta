package com.parana.dobleyfalta.jornadas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.parana.dobleyfalta.retrofit.viewmodels.JornadasViewModel
import com.parana.dobleyfalta.retrofit.viewmodels.partidos.PartidosViewModel
import com.parana.dobleyfalta.retrofit.models.partidos.PartidoDTOModel
import com.parana.dobleyfalta.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import java.time.LocalDate
import java.time.temporal.TemporalAccessor
import com.parana.dobleyfalta.SessionManager
import com.parana.dobleyfalta.retrofit.models.auth.Rol
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.parana.dobleyfalta.retrofit.ApiConstants.BASE_URL

// Colores del diseño deseado
val DarkBlue = Color(0xFF102B4E)
val PrimaryOrange = Color(0xFFFF6600)
val DarkGrey = Color(0xFF1A375E)
val LightGrey = Color(0xFFA0B3C4)
val LiveGreen = Color(0xFF50C878)
val BlueEdit = Color(0xFF007ACC)
val RedDelete = Color(0xFFE53935)

@Composable
fun JornadasScreen(
    jornadasViewModel: JornadasViewModel = viewModel(),
    partidosViewModel: PartidosViewModel = viewModel(),
    navController: NavHostController,
    ligaId: Int,
    jornadaId: Int
) {
    val jornadas by jornadasViewModel.jornadas.collectAsState()
    val jornadaActual by jornadasViewModel.jornadaActual.collectAsState()
    val partidosDTO by partidosViewModel.partidosDTO.collectAsState()
    val errorViewModel by partidosViewModel.error.collectAsState() // Para manejar errores de eliminación

    // 💡 NUEVO: Variable de estado para el diálogo de error (Paso 1)
    var mostrarDialogoError by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()


    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context.applicationContext) }

    // 💡 NUEVO: Determinar si el usuario tiene permiso de edición/eliminación
    val userRole = sessionManager.getRolUsuario()
    // Los botones se muestran si el rol es Empleado O Administrador
    val canEditDelete = userRole == Rol.Empleado

    // Si la jornada actual no está en la lista de jornadas (ej: la lista está vacía al inicio),
    // intentamos usar la primera jornada disponible o la inicial.
    val currentJornadaModel = jornadas.find { it.numero == jornadaActual }
        ?: jornadas.firstOrNull()

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val nuevoPartidoFlow = savedStateHandle?.getStateFlow<PartidoDTOModel?>("nuevo_partido", null)
    val nuevoPartido by nuevoPartidoFlow?.collectAsState() ?: remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        // 1. Cargar la lista de jornadas de la liga (si el ViewModel no lo hace por sí solo)
        // **IMPORTANTE**: Asume que tu JornadasViewModel tiene un método para esto.
        jornadasViewModel.cargarJornadasDeLiga(ligaId)

        // 2. Inicializar la jornada actual del ViewModel con el valor de navegación
        jornadasViewModel.setJornadaActual(jornadaId)
    }

    // EFECTO CLAVE: Recargar partidos al cambiar la jornada
    LaunchedEffect(jornadaActual, jornadas) { // Este estaba antes y está OK.
        // Aseguramos que tenemos la ID de la jornada para realizar la llamada a la API
        val jornadaIdToLoad = jornadas.find { it.numero == jornadaActual }?.idJornada

        if (jornadaIdToLoad != null) {
            partidosViewModel.cargarPartidos(jornadaIdToLoad)
        }
    }


    // EFECTO CLAVE: Recargar partidos al cambiar la jornada
    LaunchedEffect(nuevoPartido) {
        if (nuevoPartido != null) {
            // Recargar la jornada actual
            val jornadaSeleccionada = jornadas.find { it.numero == jornadaActual }
            if (jornadaSeleccionada != null) {
                partidosViewModel.cargarPartidos(jornadaSeleccionada.idJornada)
                savedStateHandle?.set("nuevo_partido", null) // limpiar para evitar recargas dobles
            }
        }
    }

    // 💡 MODIFICADO: Muestra el diálogo de error si el ViewModel reporta un error (Paso 2)
    errorViewModel?.let { errorMsg ->
        // Si el mensaje de error no es nulo/vacío, activamos el diálogo
        if (errorMsg.isNotEmpty()) {
            mostrarDialogoError = true
        }
    }

    var mostrarConfirmacionBorrado by remember { mutableStateOf(false) }
    var partidoAEliminar by remember { mutableStateOf<PartidoDTOModel?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val jornadasList = jornadas
            val minJornada = jornadasList.minOfOrNull { it.numero } ?: jornadaActual
            val maxJornada = jornadasList.maxOfOrNull { it.numero } ?: jornadaActual

            val puedeRetroceder = jornadasList.isNotEmpty() && jornadaActual > minJornada
            val puedeAvanzar = jornadasList.isNotEmpty() && jornadaActual < maxJornada

            // Botón Anterior
            IconButton(
                onClick = {
                    if (puedeRetroceder) {
                        jornadasViewModel.setJornadaActual(jornadaActual - 1)
                    }
                },
                enabled = puedeRetroceder
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = "Jornada anterior",
                    tint = PrimaryOrange,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Título de la Jornada
            Text(
                text = "JORNADA $jornadaActual",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            // Botón Siguiente
            IconButton(
                onClick = {
                    if (puedeAvanzar) {
                        jornadasViewModel.setJornadaActual(jornadaActual + 1)
                    }
                },
                enabled = puedeAvanzar
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = "Jornada siguiente",
                    tint = PrimaryOrange,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Lista de partidos
        val partidosDeJornada = partidosDTO

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(partidosDeJornada) { partido ->
                PartidoCard(
                    partido = partido,
                    canEditDelete = canEditDelete, // 💡 Nuevo parámetro
                    onEditClick = {
                        navController.navigate("marcador_partido/${partido.idPartido}")
                    },
                    onDeleteClick = {
                        partidoAEliminar = partido
                        mostrarConfirmacionBorrado = true
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Botón agregar partido (solo para Empleados/Admin)
            if (canEditDelete) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                val realJornadaId = currentJornadaModel?.idJornada ?: return@Button
                                navController.navigate("crear_partido/$realJornadaId")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = PrimaryOrange
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "AGREGAR PARTIDO",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                    }
                }
            }
        }
    }

    // Diálogo de confirmación de eliminación
    if (mostrarConfirmacionBorrado && partidoAEliminar != null) {
        val partido = partidoAEliminar!!

        AlertDialog(
            onDismissRequest = { mostrarConfirmacionBorrado = false },
            title = {
                Text(
                    "Confirmar eliminación",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = { Text("¿Seguro que quieres eliminar este partido?", color = LightGrey) },
            confirmButton = {
                Button(
                    onClick = {
                        // 💡 Implementación de la lógica de eliminación
                        partidosViewModel.eliminarPartido(
                            id = partido.idPartido,
                            jornadaId = partido.jornadaId,
                            onSuccess = {
                                mostrarConfirmacionBorrado = false
                                partidoAEliminar = null
                            }
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

    // 💡 NUEVO: Diálogo para mostrar errores de la operación (Paso 3)
    if (mostrarDialogoError) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoError = false
                partidosViewModel.clearError() // Limpia el error al tocar fuera
            },
            title = {
                Text(
                    "Error al realizar la operación",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = { Text(errorViewModel ?: "Error desconocido.", color = LightGrey) },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogoError = false
                        partidosViewModel.clearError() // Limpia el error al presionar "Aceptar"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                ) { Text("Aceptar") }
            },
            containerColor = DarkGrey,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun PartidoCard(
    partido: PartidoDTOModel,
    canEditDelete: Boolean, // 💡 Nuevo parámetro
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val cardBackgroundColor = DarkGrey
    val textLightColor = LightGrey
    val textStrongColor = Color.White
    val winnerScoreColor = PrimaryOrange

    val (dia, fechaNumerica, hora) = parseFecha(partido.fecha)

    val estadoDb = partido.estadoPartido?.lowercase()

    val (headerText, headerColor, showScores) = when (estadoDb) {
        "proximo" -> Triple("Próximo", PrimaryOrange, false)
        "en_vivo" -> Triple("En Vivo", LiveGreen, false)
        "terminado" -> Triple("Terminado", DarkGrey, true)
        else -> Triple("Desconocido", PrimaryOrange, false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Header estado partido
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(
                        color = headerColor,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo del Silbato (a la izquierda)
                Icon(
                    painter = painterResource(id = R.drawable.silbato_logo),
                    contentDescription = "Silbato logo",
                    tint = Color.White,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 8.dp)
                )

                // Texto de estado
                Text(
                    text = headerText,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            // ----------------------------------------------------------------------

            // Contenido principal: Equipos y Marcador
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Equipo local
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = "${BASE_URL}${partido.logoLocal}",
                        contentDescription = partido.equipoLocalNombre, // Usar el nombre del equipo
                        modifier = Modifier
                            .size(60.dp) // Tamaño adecuado para la tarjeta
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = partido.equipoLocalNombre ?: "",
                        color = textStrongColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }

                // Score y liga
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(2f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${partido.puntosLocal ?: 0}",
                            color = if ((partido.puntosLocal ?: 0) > (partido.puntosVisitante
                                    ?: 0)
                            ) winnerScoreColor else textLightColor,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "-",
                            color = textLightColor,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${partido.puntosVisitante ?: 0}",
                            color = if ((partido.puntosVisitante ?: 0) > (partido.puntosLocal
                                    ?: 0)
                            ) winnerScoreColor else textLightColor,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Equipo visitante
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = "${BASE_URL}${partido.logoVisitante}",
                        contentDescription = partido.equipoVisitanteNombre,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = partido.equipoVisitanteNombre ?: "",
                        color = textStrongColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
            // *** PARTIDO PRÓXIMO/EN VIVO: Fecha/Hora a la derecha y Logo del estadio a la izquierda ***
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Lado Central: Día, Fecha y Hora
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dia,
                        color = textLightColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$fechaNumerica | $hora hs",
                        color = textLightColor,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 💡 CONTROL DE VISIBILIDAD DE BOTONES
            if (canEditDelete) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar",
                            tint = BlueEdit
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Eliminar",
                            tint = RedDelete
                        )
                    }
                }
            }
        }
    }
}

// Se asume que el backend devuelve un formato como "2024-04-22 20:00:00"
fun parseFecha(fechaHoraString: String?): Triple<String, String, String> {
    if (fechaHoraString.isNullOrEmpty()) {
        return Triple("Sin Día", "--/--/----", "--:--")
    }

    val possibleFormats = listOf(
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"), // ISO 8601 (común con Spring/Jackson)
    )

    val spanishLocale = Locale("es", "ES")
    var temporal: TemporalAccessor? = null

    for (formatter in possibleFormats) {
        try {
            temporal = LocalDateTime.parse(fechaHoraString, formatter)
            break
        } catch (ignored: Exception) {
            try {
                temporal = LocalDate.parse(fechaHoraString, formatter)
                break
            } catch (ignored2: Exception) { /* Intentar el siguiente */ }
        }
    }

    return try {
        if (temporal == null) throw IllegalArgumentException("Formato no reconocido.")

        val isDateOnly = temporal is LocalDate

        // Si solo es fecha, usamos medianoche (00:00) para el parseo, pero la hora debe ser vacía.
        val dateTime = if (temporal is LocalDateTime) temporal else (temporal as LocalDate).atStartOfDay()

        val dia = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, spanishLocale).replaceFirstChar { it.uppercase() }
        val fecha = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        // Si solo recibimos la fecha (isDateOnly), la hora es desconocida.
        val horaStr = if (isDateOnly) "" else dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

        Triple(dia, fecha, horaStr)
    } catch (e: Exception) {
        Triple("Error Fmt", "Revisa logs", "--:--")
    }
}