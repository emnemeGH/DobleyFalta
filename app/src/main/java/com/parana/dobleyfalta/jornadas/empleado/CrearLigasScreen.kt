package com.parana.dobleyfalta.jornadas.empleado

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.parana.dobleyfalta.DarkBlue
import com.parana.dobleyfalta.DarkGrey
import com.parana.dobleyfalta.PrimaryOrange
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.equipos.LightGrey
import com.parana.dobleyfalta.retrofit.models.ligas.CrearLigaModel
import com.parana.dobleyfalta.retrofit.viewmodels.ligas.LigasViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.Calendar

// --------------------------- COMPOSABLE DE PANTALLA ---------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearLigaScreen(
    navController: NavController,
    ligasViewModel: LigasViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    // Estados para almacenar el valor en milisegundos (la fuente de verdad de la fecha)
    var nombreLiga by remember { mutableStateOf("") }
    var fechaInicioMillis by remember { mutableStateOf<Long?>(null) }
    var fechaFinMillis by remember { mutableStateOf<Long?>(null) }

    // Estados calculados para la UI (Muestra DD/MM/YYYY)
    val fechaInicioLiga = remember(fechaInicioMillis) {
        fechaInicioMillis?.let { convertMillisToUIDate(it) } ?: ""
    }
    val fechaFinalizacionLiga = remember(fechaFinMillis) {
        fechaFinMillis?.let { convertMillisToUIDate(it) } ?: ""
    }
    val anioLiga = remember(fechaInicioMillis) {
        fechaInicioMillis?.let { getYearFromMillis(it).toString() } ?: Calendar.getInstance().get(Calendar.YEAR).toString()
    }

    // Estados para los errores
    var nombreError by remember { mutableStateOf<String?>(null) }
    var fechaInicioError by remember { mutableStateOf<String?>(null) }
    var fechaFinalizacionError by remember { mutableStateOf<String?>(null) }

    // Estados para controlar la visibilidad de los selectores de fecha
    var mostrarSeleccionFechaInicio by remember { mutableStateOf(false) }
    var mostrarSeleccionFechaFinalizacion by remember { mutableStateOf(false) }

    // Observar el estado del ViewModel
    val apiError by ligasViewModel.error.collectAsState()
    val isLoading by ligasViewModel.loading.collectAsState()


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
                            fechaInicioMillis = millis // Guardamos los milisegundos
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
                            fechaFinMillis = millis // Guardamos los milisegundos
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
            text = "Crear Liga",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

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
                focusedContainerColor = DarkGrey, unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey, focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange, focusedTextColor = Color.White,
                unfocusedTextColor = Color.White, disabledTextColor = Color.White
            ),
            isError = nombreError != null,
            supportingText = { nombreError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
            singleLine = true
        )

        // Campo del Año (informativo, basado en fecha de inicio)
        OutlinedTextField(
            value = anioLiga,
            onValueChange = {},
            readOnly = true,
            label = { Text("Año de la liga", color = LightGrey) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey, unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey, focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange, focusedTextColor = Color.White,
                unfocusedTextColor = Color.White, disabledTextColor = Color.White
            ),
        )


        // Campo Fecha de inicio
        OutlinedTextField(
            value = fechaInicioLiga,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha de inicio (dd/MM/yyyy)", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { mostrarSeleccionFechaInicio = true },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey, unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey, focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange, focusedTextColor = Color.White,
                unfocusedTextColor = Color.White, disabledTextColor = Color.White
            ),
            isError = fechaInicioError != null,
            supportingText = { fechaInicioError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha de inicio de la liga",
                    tint = LightGrey,
                    modifier = Modifier.clickable { mostrarSeleccionFechaInicio = true }
                )
            }
        )

        // Campo Fecha de finalización
        OutlinedTextField(
            value = fechaFinalizacionLiga,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha de finalización (dd/MM/yyyy)", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { mostrarSeleccionFechaFinalizacion = true },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey, unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey, focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange, focusedTextColor = Color.White,
                unfocusedTextColor = Color.White, disabledTextColor = Color.White
            ),
            isError = fechaFinalizacionError != null,
            supportingText = { fechaFinalizacionError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha de finalización de la liga",
                    tint = LightGrey,
                    modifier = Modifier.clickable { mostrarSeleccionFechaFinalizacion = true }
                )
            }
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

                if (nombreError == null && fechaInicioError == null && fechaFinalizacionError == null) {
                    val anio = anioLiga.toIntOrNull() ?: Calendar.getInstance().get(Calendar.YEAR)

                    // 2. Convertir a formato API (YYYY-MM-DD)
                    val inicioApiDate = fechaInicioMillis?.let { convertMillisToApiDate(it) } ?: ""
                    val finApiDate = fechaFinMillis?.let { convertMillisToApiDate(it) } ?: ""

                    // 3. Crear modelo de datos
                    val createModel = CrearLigaModel(
                        nombre = nombreLiga,
                        // ¡Formato simple YYYY-MM-DD para java.sql.Date!
                        fechaInicio = inicioApiDate,
                        fechaFin = finApiDate,
                        anio = anio
                    )

                    // 4. Enviar a la API
                    ligasViewModel.crearLiga(createModel) {
                        navController.popBackStack()
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Guardar Liga", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
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