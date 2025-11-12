package com.parana.dobleyfalta.jornadas.empleado

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
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.retrofit.models.ligas.LigaUpdateModel
import com.parana.dobleyfalta.retrofit.viewmodels.ligas.LigasViewModel
import com.parana.dobleyfalta.equipos.LightGrey // Asumo que estos colores están en equipos
import com.parana.dobleyfalta.DarkGrey // Asumo que estos colores están en el paquete principal

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

// --------------------------- COMPOSABLE DE PANTALLA ---------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarLigasScreen(
    navController: NavController,
    idLiga: Int, // 🚨 Argumento de navegación pasado aquí
    ligasViewModel: LigasViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val ligaSeleccionada by ligasViewModel.ligaSeleccionada.collectAsState()
    val apiError by ligasViewModel.error.collectAsState()
    val isLoading by ligasViewModel.loading.collectAsState()

    // 1. Estados para los campos editables
    var nombreLiga by remember { mutableStateOf("") }
    var fechaInicioMillis by remember { mutableStateOf<Long?>(null) } // ✅ Usa Long? para DatePicker
    var fechaFinMillis by remember { mutableStateOf<Long?>(null) }     // ✅ Usa Long? para DatePicker
    var anioLigaEditable by remember { mutableStateOf("") }

    // 2. Estados calculados para la UI (Muestra DD/MM/YYYY)
    val fechaInicioLiga = remember(fechaInicioMillis) {
        fechaInicioMillis?.let { convertMillisToUIDate(it) } ?: ""
    }
    val fechaFinalizacionLiga = remember(fechaFinMillis) {
        fechaFinMillis?.let { convertMillisToUIDate(it) } ?: ""
    }

    // 3. Estados para los errores
    var nombreError by remember { mutableStateOf<String?>(null) }
    var anioError by remember { mutableStateOf<String?>(null) }
    var fechaInicioError by remember { mutableStateOf<String?>(null) }
    var fechaFinalizacionError by remember { mutableStateOf<String?>(null) }

    // 4. Estados para controlar la visibilidad de los selectores de fecha
    var mostrarSeleccionFechaInicio by remember { mutableStateOf(false) }
    var mostrarSeleccionFechaFinalizacion by remember { mutableStateOf(false) }


    // 5. CARGA INICIAL DE DATOS
    LaunchedEffect(idLiga) {
        ligasViewModel.obtenerLigaPorId(idLiga)
    }

    // 6. SINCRONIZACIÓN DE DATOS con los estados
    LaunchedEffect(ligaSeleccionada) {
        ligaSeleccionada?.let { liga ->
            // Convertir las fechas de String a Long para los DatePickers
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC)

            // Asumiendo que fechaInicio y fechaFin vienen en formato "YYYY-MM-DD"
            fechaInicioMillis = try {
                // Parseamos la fecha, y obtenemos los milisegundos
                Instant.from(formatter.parse(liga.fechaInicio)).toEpochMilli()
            } catch (e: Exception) { null }

            fechaFinMillis = try {
                Instant.from(formatter.parse(liga.fechaFin)).toEpochMilli()
            } catch (e: Exception) { null }

            // Llenar campos
            nombreLiga = liga.nombre
            anioLigaEditable = liga.anio.toString()
        }
    }

    // 7. Sincronizar campo Año con la fecha de inicio (como en CrearLigaScreen)
    LaunchedEffect(fechaInicioMillis) {
        fechaInicioMillis?.let { millis ->
            anioLigaEditable = getYearFromMillis(millis).toString()
        }
    }

    // Diálogo para seleccionar la fecha de inicio
    if (mostrarSeleccionFechaInicio) {
        val initialDateMillis = fechaInicioMillis ?: getTodayMillis()
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)

        DatePickerDialog(
            onDismissRequest = { mostrarSeleccionFechaInicio = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            fechaInicioMillis = millis
                            fechaInicioError = null
                        }
                        mostrarSeleccionFechaInicio = false
                    }
                ) { Text("Aceptar", color = PrimaryOrange) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarSeleccionFechaInicio = false }) { Text("Cancelar", color = Color.White) }
            },
            colors = DatePickerDefaults.colors(containerColor = DarkGrey)
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Diálogo para seleccionar la fecha de finalización
    if (mostrarSeleccionFechaFinalizacion) {
        val initialDateMillis = fechaFinMillis ?: getTodayMillis()
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)

        DatePickerDialog(
            onDismissRequest = { mostrarSeleccionFechaFinalizacion = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            fechaFinMillis = millis
                            fechaFinalizacionError = null
                        }
                        mostrarSeleccionFechaFinalizacion = false
                    }
                ) { Text("Aceptar", color = PrimaryOrange) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarSeleccionFechaFinalizacion = false }) { Text("Cancelar", color = Color.White) }
            },
            colors = DatePickerDefaults.colors(containerColor = DarkGrey)
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // --------------------------- CUERPO DE LA PANTALLA ---------------------------
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(32.dp)
            .clickable(
                indication = null,
                interactionSource = interactionSource
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
                    contentDescription = "Volver a ligas",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Editar Liga",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Si está cargando y la liga aún es nula, mostramos un indicador
        if (isLoading && ligaSeleccionada == null) {
            CircularProgressIndicator(color = PrimaryOrange)
        } else if (ligaSeleccionada != null) {

            // Campo Nombre
            OutlinedTextField(
                value = nombreLiga,
                onValueChange = {
                    nombreLiga = it
                    nombreError = null
                    ligasViewModel.clearError()
                },
                label = { Text("Nombre de la liga", color = LightGrey) },
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
                supportingText = { nombreError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
                singleLine = true
            )

            // Campo del Año (editable)
            OutlinedTextField(
                value = anioLigaEditable,
                onValueChange = { newValue ->
                    if (newValue.length <= 4 && newValue.all { it.isDigit() }) {
                        anioLigaEditable = newValue
                        anioError = null
                    } else if (newValue.length > 4) {
                        anioError = "Máximo 4 dígitos"
                    } else if (newValue.isNotEmpty() && !newValue.all { it.isDigit() }) {
                        anioError = "Solo se permiten números"
                    }
                    ligasViewModel.clearError()
                },
                readOnly = false,
                label = { Text("Año de la liga", color = LightGrey) },
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
                isError = anioError != null,
                supportingText = { anioError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Campo Fecha de inicio
            OutlinedTextField(
                value = fechaInicioLiga, // Usamos la variable calculada (String) para mostrar
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de inicio (dd/MM/yyyy)", color = LightGrey) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable { mostrarSeleccionFechaInicio = true },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkGrey,
                    unfocusedContainerColor = DarkGrey,
                    unfocusedBorderColor = DarkGrey,
                    focusedBorderColor = PrimaryOrange,
                    cursorColor = PrimaryOrange,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White // Importante para readOnly
                ),
                isError = fechaInicioError != null,
                supportingText = { fechaInicioError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
                trailingIcon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = "Seleccionar fecha", tint = LightGrey, modifier = Modifier.clickable { mostrarSeleccionFechaInicio = true }) }
            )

            // Campo Fecha de finalización
            OutlinedTextField(
                value = fechaFinalizacionLiga, // Usamos la variable calculada (String) para mostrar
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de finalización (dd/MM/yyyy)", color = LightGrey) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable { mostrarSeleccionFechaFinalizacion = true },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkGrey,
                    unfocusedContainerColor = DarkGrey,
                    unfocusedBorderColor = DarkGrey,
                    focusedBorderColor = PrimaryOrange,
                    cursorColor = PrimaryOrange,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White // Importante para readOnly
                ),
                isError = fechaFinalizacionError != null,
                supportingText = { fechaFinalizacionError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
                trailingIcon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = "Seleccionar fecha", tint = LightGrey, modifier = Modifier.clickable { mostrarSeleccionFechaFinalizacion = true }) }
            )

            // Mensaje de error de la API
            apiError?.let { msg ->
                Text(
                    text = "Error API: $msg",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón para guardar la liga
            Button(
                onClick = {
                    // 1. Limpiar y validar errores
                    ligasViewModel.clearError()
                    nombreError = if (nombreLiga.isBlank()) "El nombre es obligatorio" else null
                    fechaInicioError = if (fechaInicioLiga.isBlank()) "La fecha de inicio es obligatoria" else null
                    fechaFinalizacionError = if (fechaFinalizacionLiga.isBlank()) "La fecha de finalización es obligatoria" else null

                    val anioValidado = anioLigaEditable.toIntOrNull()
                    anioError = when {
                        anioLigaEditable.isBlank() -> "El año es obligatorio"
                        anioValidado == null || anioLigaEditable.length != 4 -> "Año inválido"
                        else -> null
                    }


                    if (nombreError == null && fechaInicioError == null && fechaFinalizacionError == null && anioError == null) {
                        val anio = anioValidado!!

                        // 2. Convertir a formato API (YYYY-MM-DD)
                        val inicioApiDate = fechaInicioMillis?.let { convertMillisToApiDate(it) } ?: ""
                        val finApiDate = fechaFinMillis?.let { convertMillisToApiDate(it) } ?: ""

                        // 3. Crear modelo de datos
                        val updateModel = LigaUpdateModel(
                            nombre = nombreLiga,
                            fechaInicio = inicioApiDate,
                            fechaFin = finApiDate,
                            anio = anio
                        )

                        // 4. Enviar a la API
                        ligasViewModel.actualizarLiga(idLiga, updateModel) {
                            navController.popBackStack()
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        } // Fin de ligaSeleccionada != null
    }
}

// --------------------------- UTILIDADES DE FECHA CORREGIDAS ---------------------------

private val UI_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

/**
 * Convierte milisegundos (Long del DatePicker) al formato "dd/MM/yyyy" para la UI.
 * CORRECCIÓN: Usa la zona horaria del sistema para mostrar la fecha correcta en la UI.
 */
private fun convertMillisToUIDate(millis: Long): String {
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault()) // 👈 CORRECCIÓN CLAVE: Usar la zona del usuario.
        .toLocalDate()
        .format(UI_DATE_FORMATTER)
}

/**
 * Convierte milisegundos (Long del DatePicker) al formato "YYYY-MM-DD" para la API.
 * 🚀 SOLUCIÓN FORZADA: Agrega .plusDays(1) para compensar el desfase al guardar
 * en una base de datos/API que no respeta la zona horaria del cliente.
 */
private fun convertMillisToApiDate(millis: Long): String {
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .plusDays(1) // 👈 ¡ESTO ASEGURA QUE SE ENVÍE EL DÍA CORRECTO!
        .toString() // LocalDate.toString() devuelve YYYY-MM-DD
}

/**
 * Obtiene el año a partir de milisegundos.
 * CORREGIDO: Usa la zona horaria del sistema.
 */
private fun getYearFromMillis(millis: Long): Int {
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault()) // 👈 CORRECCIÓN
        .get(ChronoField.YEAR)
}

/**
 * Obtiene los milisegundos de la medianoche de hoy (Local) para el valor inicial del DatePicker.
 * CORREGIDO: Usa la zona horaria del sistema para inicializar el calendario correctamente.
 */
private fun getTodayMillis(): Long {
    return Instant.now()
        .atZone(ZoneId.systemDefault()) // 👈 CORRECCIÓN
        .toLocalDate()
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}