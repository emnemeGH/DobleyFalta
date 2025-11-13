package com.parana.dobleyfalta.jornadas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.parana.dobleyfalta.DarkBlue
import com.parana.dobleyfalta.PrimaryOrange
import com.parana.dobleyfalta.DarkGrey
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.retrofit.models.jornadas.CrearJornadaModel
import com.parana.dobleyfalta.retrofit.viewmodels.JornadasViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Formato de la API (YYYY-MM-DD)
private const val API_DATE_FORMAT = "yyyy-MM-dd"
// Formato de la UI (DD/MM/YYYY)
private const val UI_DATE_FORMAT = "dd/MM/yyyy"
private val locale = Locale.getDefault()

fun formatDateToApi(dateString: String): String {
    return try {
        val parser = SimpleDateFormat(UI_DATE_FORMAT, locale)
        val formatter = SimpleDateFormat(API_DATE_FORMAT, locale)
        val date = parser.parse(dateString)
        if (date != null) formatter.format(date) else ""
    } catch (e: Exception) {
        ""
    }
}

fun formatDateFromApi(dateString: String?): String {
    if (dateString.isNullOrEmpty()) return ""
    return try {
        val parser = SimpleDateFormat(API_DATE_FORMAT, locale)
        val formatter = SimpleDateFormat(UI_DATE_FORMAT, locale)
        val date = parser.parse(dateString.substring(0, 10))
        if (date != null) formatter.format(date) else ""
    } catch (e: Exception) {
        ""
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarJornadaScreen(navController: NavController, idJornada: Int, idLiga: Int? = null) {

    val jornadasViewModel: JornadasViewModel = viewModel()
    val jornadaAEditar by jornadasViewModel.jornadaAEditar.collectAsState()
    val loading by jornadasViewModel.loading.collectAsState()
    val error by jornadasViewModel.error.collectAsState()

    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Estados locales del formulario
    var numeroJornada by remember { mutableStateOf("") }
    var fechaInicioJornada by remember { mutableStateOf("") }
    var fechaFinalizacionJornada by remember { mutableStateOf("") }
    var idLigaActual by remember { mutableStateOf<Int?>(idLiga) }

    // Errores de validación
    var numeroJornadaError by remember { mutableStateOf<String?>(null) }
    var fechaInicioJornadaError by remember { mutableStateOf<String?>(null) }
    var fechaFinalizacionJornadaError by remember { mutableStateOf<String?>(null) }

    // Selectores de fecha
    var mostrarSeleccionFechaInicio by remember { mutableStateOf(false) }
    var mostrarSeleccionFechaFinalizacion by remember { mutableStateOf(false) }

    // Cargar jornada
    LaunchedEffect(idJornada) {
        jornadasViewModel.cargarJornadaParaEditar(idJornada)
    }

    // Llenar formulario
    LaunchedEffect(jornadaAEditar) {
        jornadaAEditar?.let { jornada ->
            numeroJornada = jornada.numero.toString()
            fechaInicioJornada = formatDateFromApi(jornada.fechaInicio)
            fechaFinalizacionJornada = formatDateFromApi(jornada.fechaFin)

            if (idLigaActual == null) {
                idLigaActual = jornada.liga?.idLiga
            }
        }
    }

    // Mostrar errores del ViewModel
    LaunchedEffect(error) {
        error?.takeIf { it.isNotEmpty() }?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
                jornadasViewModel.clearError()
            }
        }
    }

    // --- Diálogos de selección de fecha ---
    if (mostrarSeleccionFechaInicio) {
        val initialDateMillis = try {
            SimpleDateFormat(UI_DATE_FORMAT, locale).parse(fechaInicioJornada)?.time
        } catch (e: Exception) { System.currentTimeMillis() } ?: System.currentTimeMillis()

        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)

        DatePickerDialog(
            onDismissRequest = { mostrarSeleccionFechaInicio = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        fechaInicioJornada = SimpleDateFormat(UI_DATE_FORMAT, locale).format(Date(it))
                        fechaInicioJornadaError = null
                    }
                    mostrarSeleccionFechaInicio = false
                }) { Text("Aceptar", color = PrimaryOrange) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarSeleccionFechaInicio = false }) { Text("Cancelar", color = Color.White) }
            },
            colors = DatePickerDefaults.colors(containerColor = DarkGrey)
        ) { DatePicker(state = datePickerState) }
    }

    if (mostrarSeleccionFechaFinalizacion) {
        val initialDateMillis = try {
            SimpleDateFormat(UI_DATE_FORMAT, locale).parse(fechaFinalizacionJornada)?.time
        } catch (e: Exception) { System.currentTimeMillis() } ?: System.currentTimeMillis()

        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)

        DatePickerDialog(
            onDismissRequest = { mostrarSeleccionFechaFinalizacion = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        fechaFinalizacionJornada = SimpleDateFormat(UI_DATE_FORMAT, locale).format(Date(it))
                        fechaFinalizacionJornadaError = null
                    }
                    mostrarSeleccionFechaFinalizacion = false
                }) { Text("Aceptar", color = PrimaryOrange) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarSeleccionFechaFinalizacion = false }) { Text("Cancelar", color = Color.White) }
            },
            colors = DatePickerDefaults.colors(containerColor = DarkGrey)
        ) { DatePicker(state = datePickerState) }
    }

    // --- Contenido principal ---
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->

        val isDataReady = jornadaAEditar != null && idLigaActual != null

        if (loading || !isDataReady) {
            Box(
                modifier = Modifier.fillMaxSize().background(DarkBlue),
                contentAlignment = Alignment.Center
            ) {
                if (loading) CircularProgressIndicator(color = PrimaryOrange)
                else if (jornadaAEditar == null && !loading) Text("No se pudo cargar la jornada con ID $idJornada.", color = Color.White)
                else if (jornadaAEditar != null && idLigaActual == null && !loading) Text("Error: La jornada cargada no tiene una Liga asociada.", color = Color.Red)
            }
            if (!isDataReady) return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlue)
                .padding(paddingValues)
                .padding(horizontal = 32.dp)
                .clickable(indication = null, interactionSource = interactionSource) { focusManager.clearFocus() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // Botón volver
            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.Start) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(painter = painterResource(id = R.drawable.back), contentDescription = "Volver", tint = Color.White, modifier = Modifier.size(30.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Editar Jornada", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(bottom = 32.dp))

            // Número de jornada
            OutlinedTextField(
                value = numeroJornada,
                onValueChange = { numeroJornada = it; numeroJornadaError = null },
                label = { Text("Número de la jornada", color = LightGrey) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
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
                isError = numeroJornadaError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = { numeroJornadaError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }
            )

            // Fecha inicio
            OutlinedTextField(
                value = fechaInicioJornada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de inicio (DD/MM/YYYY)", color = LightGrey) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable(indication = null, interactionSource = interactionSource) { mostrarSeleccionFechaInicio = true },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkGrey,
                    unfocusedContainerColor = DarkGrey,
                    unfocusedBorderColor = DarkGrey,
                    focusedBorderColor = PrimaryOrange,
                    cursorColor = PrimaryOrange,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White
                ),
                isError = fechaInicioJornadaError != null,
                supportingText = { fechaInicioJornadaError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
                trailingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha", tint = LightGrey,
                        modifier = Modifier.clickable(indication = null, interactionSource = interactionSource) { mostrarSeleccionFechaInicio = true })
                }
            )

            // Fecha fin
            OutlinedTextField(
                value = fechaFinalizacionJornada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de finalización (DD/MM/YYYY)", color = LightGrey) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable(indication = null, interactionSource = interactionSource) { mostrarSeleccionFechaFinalizacion = true },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkGrey,
                    unfocusedContainerColor = DarkGrey,
                    unfocusedBorderColor = DarkGrey,
                    focusedBorderColor = PrimaryOrange,
                    cursorColor = PrimaryOrange,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White
                ),
                isError = fechaFinalizacionJornadaError != null,
                supportingText = { fechaFinalizacionJornadaError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
                trailingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha", tint = LightGrey,
                        modifier = Modifier.clickable(indication = null, interactionSource = interactionSource) { mostrarSeleccionFechaFinalizacion = true })
                }
            )

            // Botón guardar cambios
            Button(
                onClick = {
                    numeroJornadaError = if (numeroJornada.isBlank() || numeroJornada.toIntOrNull() == null) "Debe ser un número válido." else null
                    fechaInicioJornadaError = if (fechaInicioJornada.isBlank()) "La fecha de inicio es obligatoria" else null
                    fechaFinalizacionJornadaError = if (fechaFinalizacionJornada.isBlank()) "La fecha de finalización es obligatoria" else null

                    if (numeroJornadaError == null && fechaInicioJornadaError == null && fechaFinalizacionJornadaError == null && idLigaActual != null) {
                        val jornadaEditada = CrearJornadaModel(
                            numero = numeroJornada.toInt(),
                            fechaInicio = formatDateToApi(fechaInicioJornada),
                            fechaFin = formatDateToApi(fechaFinalizacionJornada),
                            idLiga = idLigaActual!!
                        )
                        jornadasViewModel.editarJornada(
                            id = idJornada,
                            jornada = jornadaEditada,
                            onSuccess = {
                                scope.launch {
                                    navController.popBackStack()
                                }
                            }
                        )
                    } else if (idLigaActual == null) {
                        scope.launch { snackbarHostState.showSnackbar("Error: No se encontró la liga asociada. Recargue la pantalla.", duration = SnackbarDuration.Short) }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(60.dp).padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (loading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
