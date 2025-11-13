package com.parana.dobleyfalta.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.parana.dobleyfalta.retrofit.viewmodels.equipos.EquiposViewModel
import com.parana.dobleyfalta.retrofit.viewmodels.partidos.PartidosViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPartidoScreen( // ¡Cambiamos el nombre de la función!
    navController: NavController,
    partidoId: Int, // Recibimos el ID del partido a editar
    partidosViewModel: PartidosViewModel = viewModel(), // Usamos el VM de Partidos
    equiposViewModel: EquiposViewModel = viewModel() // Necesitamos este para los dropdowns
) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val CardBackground = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)

    val context = LocalContext.current

    // **DATOS DEL VIEWMODEL DE PARTIDOS**
    val partidosDTO by partidosViewModel.partidosDTO.collectAsState()
    val loading by partidosViewModel.loading.collectAsState()
    val error by partidosViewModel.error.collectAsState()

    // **DATOS DEL VIEWMODEL DE EQUIPOS**
    val equipos by equiposViewModel.equipos.collectAsState()

    // **ESTADOS LOCALES**
    var equipoLocalSeleccionado by remember { mutableStateOf<Int?>(null) }
    var equipoVisitanteSeleccionado by remember { mutableStateOf<Int?>(null) }
    var fechaSeleccionada by remember { mutableStateOf("") }
    var estadoPartido by remember { mutableStateOf("proximo") }
    var idJornada by remember { mutableStateOf(0) } // Necesario para el modelo PUT

    var puntosLocal by remember { mutableStateOf("0") }
    var puntosVisitante by remember { mutableStateOf("0") }

    // Dropdown flags
    var expandirLocal by remember { mutableStateOf(false) }
    var expandirVisitante by remember { mutableStateOf(false) }
    var expandirEstado by remember { mutableStateOf(false) }

    // Errores
    var errorLocal by remember { mutableStateOf<String?>(null) }
    var errorVisitante by remember { mutableStateOf<String?>(null) }
    var errorFecha by remember { mutableStateOf<String?>(null) }

    val opcionesEstado = listOf(
        "Próximo" to "proximo",
        "En vivo" to "en_vivo",
        "Terminado" to "terminado"
    )

    val nombreLocal = equipos.find { it.idEquipo == equipoLocalSeleccionado }?.nombre ?: ""
    val nombreVisitante = equipos.find { it.idEquipo == equipoVisitanteSeleccionado }?.nombre ?: ""

    // 1. Cargar equipos y datos del partido
    LaunchedEffect(Unit) {
        // Cargar equipos si no están
        equiposViewModel.cargarEquipos()
        // Cargar partidos si no están (necesario para encontrar el partido a editar)
        partidosViewModel.cargarTodosLosPartidos()
    }

    // 2. Pre-llenar el formulario cuando los datos del partido estén listos
    LaunchedEffect(partidoId, partidosDTO) {
        if (partidosDTO.isNotEmpty()) {
            val partidoAEditar = partidosDTO.find { it.idPartido == partidoId }

            partidoAEditar?.let {
                // Pre-llenar campos
                idJornada = it.jornadaId
                equipoLocalSeleccionado = it.equipoLocalId
                equipoVisitanteSeleccionado = it.equipoVisitanteId
                fechaSeleccionada = it.fecha ?: ""
                estadoPartido = it.estadoPartido ?: "proximo"
                puntosLocal = it.puntosLocal?.toString() ?: "0"
                puntosVisitante = it.puntosVisitante?.toString() ?: "0"
            } ?: run {
                // Si el partido no se encuentra, salimos
                Toast.makeText(context, "Error: Partido no encontrado ($partidoId)", Toast.LENGTH_LONG).show()
                navController.popBackStack()
            }
        }
    }

    // Manejo de errores del PartidosViewModel
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
            partidosViewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar Partido", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(id = R.drawable.back), contentDescription = "Volver", tint = Color.White, modifier = Modifier.size(30.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBlue)
            )
        },
        containerColor = DarkBlue
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            if (loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = PrimaryOrange)
                Spacer(Modifier.height(8.dp))

            }

            // ----------------------------
            // Equipo Local
            // ----------------------------
            ExposedDropdownMenuBox(
                expanded = expandirLocal,
                onExpandedChange = { expandirLocal = !expandirLocal },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = nombreLocal,
                    onValueChange = {},
                    label = { Text("Equipo Local", color = LightGrey) },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirLocal) },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardBackground, unfocusedContainerColor = CardBackground,
                        focusedBorderColor = PrimaryOrange, unfocusedBorderColor = CardBackground,
                        cursorColor = PrimaryOrange, focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    ),
                    isError = errorLocal != null,
                    supportingText = { errorLocal?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }
                )
                ExposedDropdownMenu(
                    expanded = expandirLocal,
                    onDismissRequest = { expandirLocal = false }
                ) {
                    equipos.forEach { equipo ->
                        DropdownMenuItem(
                            text = { Text(equipo.nombre, color = if (equipo.idEquipo == equipoVisitanteSeleccionado) LightGrey.copy(alpha = 0.5f) else Color.White) },
                            onClick = {
                                if (equipo.idEquipo != equipoVisitanteSeleccionado) {
                                    equipoLocalSeleccionado = equipo.idEquipo
                                    errorLocal = null
                                    expandirLocal = false
                                }
                            },
                            enabled = equipo.idEquipo != equipoVisitanteSeleccionado
                        )
                    }
                }
            }

            // Puntaje Local
            OutlinedTextField(
                value = puntosLocal,
                onValueChange = { puntosLocal = it.filter { c -> c.isDigit() } },
                label = { Text("Puntos Local", color = LightGrey) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardBackground, unfocusedContainerColor = CardBackground,
                    focusedBorderColor = PrimaryOrange, unfocusedBorderColor = CardBackground,
                    cursorColor = PrimaryOrange, focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                )
            )

            // ----------------------------
            // Equipo Visitante
            // ----------------------------
            ExposedDropdownMenuBox(
                expanded = expandirVisitante,
                onExpandedChange = { expandirVisitante = !expandirVisitante },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = nombreVisitante,
                    onValueChange = {},
                    label = { Text("Equipo Visitante", color = LightGrey) },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirVisitante) },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardBackground, unfocusedContainerColor = CardBackground,
                        focusedBorderColor = PrimaryOrange, unfocusedBorderColor = CardBackground,
                        cursorColor = PrimaryOrange, focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    ),
                    isError = errorVisitante != null,
                    supportingText = { errorVisitante?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }
                )
                ExposedDropdownMenu(
                    expanded = expandirVisitante,
                    onDismissRequest = { expandirVisitante = false }
                ) {
                    equipos.forEach { equipo ->
                        DropdownMenuItem(
                            text = { Text(equipo.nombre, color = if (equipo.idEquipo == equipoLocalSeleccionado) LightGrey.copy(alpha = 0.5f) else Color.White) },
                            onClick = {
                                if (equipo.idEquipo != equipoLocalSeleccionado) {
                                    equipoVisitanteSeleccionado = equipo.idEquipo
                                    errorVisitante = null
                                    expandirVisitante = false
                                }
                            },
                            enabled = equipo.idEquipo != equipoLocalSeleccionado
                        )
                    }
                }
            }

            // Puntaje Visitante
            OutlinedTextField(
                value = puntosVisitante,
                onValueChange = { puntosVisitante = it.filter { c -> c.isDigit() } },
                label = { Text("Puntos Visitante", color = LightGrey) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardBackground, unfocusedContainerColor = CardBackground,
                    focusedBorderColor = PrimaryOrange, unfocusedBorderColor = CardBackground,
                    cursorColor = PrimaryOrange, focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                )
            )

            // ----------------------------
            // Fecha y hora
            // ----------------------------
            OutlinedTextField(
                value = fechaSeleccionada,
                onValueChange = {},
                label = { Text("Fecha y hora", color = LightGrey) },
                readOnly = true,
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        val cal = Calendar.getInstance()
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                TimePickerDialog(
                                    context,
                                    { _, hourOfDay, minute ->
                                        cal.set(year, month, dayOfMonth, hourOfDay, minute)
                                        // Formato ISO 8601 para el backend
                                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                                        fechaSeleccionada = sdf.format(cal.time)
                                        errorFecha = null
                                    },
                                    cal.get(Calendar.HOUR_OF_DAY),
                                    cal.get(Calendar.MINUTE),
                                    true
                                ).show()
                            },
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = PrimaryOrange)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardBackground, unfocusedContainerColor = CardBackground,
                    focusedBorderColor = PrimaryOrange, unfocusedBorderColor = CardBackground,
                    cursorColor = PrimaryOrange, focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                )
            )

            // ----------------------------
            // Estado del partido
            // ----------------------------
            ExposedDropdownMenuBox(
                expanded = expandirEstado,
                onExpandedChange = { expandirEstado = !expandirEstado },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = opcionesEstado.find { it.second == estadoPartido }?.first ?: "Próximo",
                    onValueChange = {},
                    label = { Text("Estado del partido", color = LightGrey) },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirEstado) },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardBackground, unfocusedContainerColor = CardBackground,
                        focusedBorderColor = PrimaryOrange, unfocusedBorderColor = CardBackground,
                        cursorColor = PrimaryOrange, focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    )
                )

                DropdownMenu(
                    expanded = expandirEstado,
                    onDismissRequest = { expandirEstado = false }
                ) {
                    opcionesEstado.forEach { (texto, valor) ->
                        DropdownMenuItem(
                            text = { Text(texto) },
                            onClick = {
                                estadoPartido = valor
                                expandirEstado = false
                            }
                        )
                    }
                }
            }

            // ----------------------------
            // Botón Guardar
            // ----------------------------
            Button(
                onClick = {
                    // VALIDACIONES (usamos la misma lógica de CrearPartidoScreen)
                    var esValido = true
                    errorLocal = null
                    errorVisitante = null
                    errorFecha = null
                    partidosViewModel.clearError() // Limpiar error antes de intentar

                    if (equipoLocalSeleccionado == null) {
                        errorLocal = "Selecciona el equipo local."
                        esValido = false
                    }
                    if (equipoVisitanteSeleccionado == null) {
                        errorVisitante = "Selecciona el equipo visitante."
                        esValido = false
                    }
                    if (fechaSeleccionada.isEmpty()) {
                        errorFecha = "Selecciona fecha y hora."
                        esValido = false
                    }
                    if (equipoLocalSeleccionado == equipoVisitanteSeleccionado) {
                        errorLocal = "Los equipos deben ser distintos."
                        errorVisitante = "Los equipos deben ser distintos."
                        esValido = false
                    }

                    if (esValido) {
                        // 1. Llamada a updatePartido para editar la info básica
                        partidosViewModel.updatePartido(
                            partidoId = partidoId,
                            idJornada = idJornada,
                            estadoPartido = estadoPartido,
                            idEquipoLocal = equipoLocalSeleccionado!!,
                            idEquipoVisitante = equipoVisitanteSeleccionado!!,
                            fecha = fechaSeleccionada,
                            onComplete = { exitoEdicion ->
                                if (exitoEdicion) {
                                    // 2. Si la edición es exitosa, actualizamos el puntaje por separado
                                    partidosViewModel.guardarPartido(
                                        partidoId,
                                        puntosLocal.toIntOrNull() ?: 0,
                                        puntosVisitante.toIntOrNull() ?: 0,
                                        onComplete = { exitoPuntaje ->
                                            navController.popBackStack()
                                            partidosViewModel.cargarTodosLosPartidos()
                                        }
                                    )
                                } else {
                                    // El error ya es manejado por el LaunchedEffect(error)
                                    Toast.makeText(context, "Fallo al actualizar el partido. Revisa el error.", Toast.LENGTH_LONG).show()
                                }
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                shape = RoundedCornerShape(12.dp),
                enabled = !loading // Deshabilitar si está cargando
            ) {
                Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}