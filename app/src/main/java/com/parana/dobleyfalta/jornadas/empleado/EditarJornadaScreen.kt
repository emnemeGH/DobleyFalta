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
import com.parana.dobleyfalta.DarkBlue // Asumo que estos están definidos
import com.parana.dobleyfalta.PrimaryOrange // Asumo que estos están definidos
import com.parana.dobleyfalta.R // Asumo que R.drawable.back existe
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
        // Solo tomamos los primeros 10 caracteres (YYYY-MM-DD)
        val date = parser.parse(dateString.substring(0, 10))
        if (date != null) formatter.format(date) else ""
    } catch (e: Exception) {
        ""
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarJornadaScreen(navController: NavController, idJornada: Int) {

    val jornadasViewModel: JornadasViewModel = viewModel()
    val jornadaAEditar by jornadasViewModel.jornadaAEditar.collectAsState() // 🚨 Observamos el estado
    val loading by jornadasViewModel.loading.collectAsState()
    val error by jornadasViewModel.error.collectAsState()

    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    // Estados locales para el formulario
    var numeroJornada by remember { mutableStateOf("") }
    var fechaInicioJornada by remember { mutableStateOf("") }
    var fechaFinalizacionJornada by remember { mutableStateOf("") }
    var idLigaActual by remember { mutableStateOf<Int?>(null) } // Puede ser null inicialmente

    // Estados de error
    var numeroJornadaError by remember { mutableStateOf<String?>(null) }
    var fechaInicioJornadaError by remember { mutableStateOf<String?>(null) }
    var fechaFinalizacionJornadaError by remember { mutableStateOf<String?>(null) }

    // Estados para el selector de fecha
    var mostrarSeleccionFechaInicio by remember { mutableStateOf(false) }
    var mostrarSeleccionFechaFinalizacion by remember { mutableStateOf(false) }


    // 🚨 1. EFECTO PARA CARGAR LA JORNADA USANDO EL VIEWMODEL
    LaunchedEffect(idJornada) {
        jornadasViewModel.cargarJornadaParaEditar(idJornada)
    }

    // 🚨 2. EFECTO PARA LLENAR EL FORMULARIO CUANDO LA JORNADA LLEGA
    LaunchedEffect(jornadaAEditar) {
        val jornada = jornadaAEditar
        if (jornada != null) {
            numeroJornada = jornada.numero.toString()
            fechaInicioJornada = formatDateFromApi(jornada.fechaInicio)
            fechaFinalizacionJornada = formatDateFromApi(jornada.fechaFin)
            idLigaActual = jornada.liga?.idLiga // Obtenemos y SINCRONIZAMOS el ID de la Liga
        }
    }

    // 🚨 3. EFECTO PARA MOSTRAR ERRORES DEL VIEWMODEL
    LaunchedEffect(error) {
        if (error != null && error!!.isNotEmpty()) {
            scope.launch {
                snackbarHostState.showSnackbar(error!!, duration = SnackbarDuration.Short)
                jornadasViewModel.clearError() // Limpiar error después de mostrar
            }
        }
    }


    // Diálogo para seleccionar la fecha de inicio (Mismos diálogos, solo ajustes de color)
    if (mostrarSeleccionFechaInicio) {
        val initialDateMillis = try {
            SimpleDateFormat(UI_DATE_FORMAT, locale).parse(fechaInicioJornada)?.time
        } catch (e: Exception) {
            System.currentTimeMillis()
        } ?: System.currentTimeMillis()

        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)

        DatePickerDialog(
            onDismissRequest = { mostrarSeleccionFechaInicio = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            val formatter = SimpleDateFormat(UI_DATE_FORMAT, locale)
                            fechaInicioJornada = formatter.format(Date(selectedDateMillis))
                            fechaInicioJornadaError = null
                        }
                        mostrarSeleccionFechaInicio = false
                    }
                ) { Text("Aceptar", color = PrimaryOrange) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarSeleccionFechaInicio = false }) {
                    Text("Cancelar", color = Color.Gray) // Usando Color.Gray si LightGrey no está definido aquí
                }
            }
        ) { DatePicker(state = datePickerState) }
    }

    // Diálogo para seleccionar la fecha de finalización
    if (mostrarSeleccionFechaFinalizacion) {
        val initialDateMillis = try {
            SimpleDateFormat(UI_DATE_FORMAT, locale).parse(fechaFinalizacionJornada)?.time
        } catch (e: Exception) {
            System.currentTimeMillis()
        } ?: System.currentTimeMillis()

        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)

        DatePickerDialog(
            onDismissRequest = { mostrarSeleccionFechaFinalizacion = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            val formatter = SimpleDateFormat(UI_DATE_FORMAT, locale)
                            fechaFinalizacionJornada = formatter.format(Date(selectedDateMillis))
                            fechaFinalizacionJornadaError = null
                        }
                        mostrarSeleccionFechaFinalizacion = false
                    }
                ) { Text("Aceptar", color = PrimaryOrange) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarSeleccionFechaFinalizacion = false }) {
                    Text("Cancelar", color = Color.Gray) // Usando Color.Gray si LightGrey no está definido aquí
                }
            }
        ) { DatePicker(state = datePickerState) }
    }

    // Contenido principal de la pantalla
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        // 🚨 LÓGICA DE CARGA MEJORADA: Esperamos a que los datos estén listos
        val isDataReady = jornadaAEditar != null && idLigaActual != null

        if (loading || !isDataReady) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBlue),
                contentAlignment = Alignment.Center
            ) {
                // Si la carga está activa, mostramos el spinner
                if (loading) {
                    CircularProgressIndicator(color = PrimaryOrange)
                } else if (jornadaAEditar == null && !loading) {
                    // Si ya terminó de cargar y no hay jornada (fallo de carga inicial)
                    Text("No se pudo cargar la jornada con ID $idJornada.", color = Color.White)
                } else if (jornadaAEditar != null && idLigaActual == null && !loading) {
                    // Si la jornada se cargó, pero no tiene ID de liga (problema de datos en el backend)
                    Text("Error: La jornada cargada no tiene una Liga asociada.", color = Color.Red)
                }
            }
            // Si no estamos listos, salimos.
            if (!isDataReady) return@Scaffold
        }

        // Formulario (solo se muestra cuando los datos están cargados)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlue)
                .padding(paddingValues)
                .padding(horizontal = 32.dp)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource
                ) { focusManager.clearFocus() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // ... (el resto del formulario es el mismo)

            // Botón para volver
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(0.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Volver a jornadas",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Editar Jornada",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campo de texto para el número de la jornada
            OutlinedTextField(
                value = numeroJornada,
                onValueChange = {
                    numeroJornada = it
                    numeroJornadaError = null
                },
                label = { Text("Número de la jornada", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.DarkGray, // Usando Color.DarkGray si DarkGrey no está definido
                    unfocusedContainerColor = Color.DarkGray,
                    unfocusedBorderColor = Color.DarkGray,
                    focusedBorderColor = PrimaryOrange,
                    cursorColor = PrimaryOrange,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                isError = numeroJornadaError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    numeroJornadaError?.let {
                        Text(it, color = Color.Red, fontSize = 12.sp)
                    }
                }
            )

            // Campo de texto para la fecha de inicio (con selector)
            OutlinedTextField(
                value = fechaInicioJornada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de inicio (DD/MM/YYYY)", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) { mostrarSeleccionFechaInicio = true },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    unfocusedBorderColor = Color.DarkGray,
                    focusedBorderColor = PrimaryOrange,
                    cursorColor = PrimaryOrange,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White
                ),
                isError = fechaInicioJornadaError != null,
                supportingText = {
                    fechaInicioJornadaError?.let {
                        Text(it, color = Color.Red, fontSize = 12.sp)
                    }
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Seleccionar fecha de inicio de la jornada",
                        tint = Color.Gray,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = interactionSource
                        ) { mostrarSeleccionFechaInicio = true }
                    )
                }
            )

            // Campo de texto para la fecha de finalización (con selector)
            OutlinedTextField(
                value = fechaFinalizacionJornada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de finalización (DD/MM/YYYY)", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) { mostrarSeleccionFechaFinalizacion = true },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    unfocusedBorderColor = Color.DarkGray,
                    focusedBorderColor = PrimaryOrange,
                    cursorColor = PrimaryOrange,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White
                ),
                isError = fechaFinalizacionJornadaError != null,
                supportingText = {
                    fechaFinalizacionJornadaError?.let {
                        Text(it, color = Color.Red, fontSize = 12.sp)
                    }
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Seleccionar fecha de finalización de la jornada",
                        tint = Color.Gray,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = interactionSource
                        ) { mostrarSeleccionFechaFinalizacion = true }
                    )
                }
            )

            // Botón para guardar los cambios
            Button(
                onClick = {
                    // Validación de campos
                    numeroJornadaError = if (numeroJornada.isBlank() || numeroJornada.toIntOrNull() == null) "Debe ser un número válido." else null
                    fechaInicioJornadaError = if (fechaInicioJornada.isBlank()) "La fecha de inicio es obligatoria" else null
                    fechaFinalizacionJornadaError = if (fechaFinalizacionJornada.isBlank()) "La fecha de finalización es obligatoria" else null

                    // 🚨 VERIFICACIÓN FINAL: isDataReady ya nos asegura que idLigaActual != null (en una carga exitosa)
                    if (numeroJornadaError == null && fechaInicioJornadaError == null && fechaFinalizacionJornadaError == null && idLigaActual != null) {

                        val jornadaEditada = CrearJornadaModel(
                            numero = numeroJornada.toInt(),
                            fechaInicio = formatDateToApi(fechaInicioJornada),
                            fechaFin = formatDateToApi(fechaFinalizacionJornada),
                            idLiga = idLigaActual!! // ¡idLigaActual ya no es null aquí!
                        )

                        jornadasViewModel.editarJornada(
                            id = idJornada,
                            jornada = jornadaEditada,
                            onSuccess = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Jornada Nº $numeroJornada actualizada con éxito.")
                                    navController.popBackStack()
                                }
                            }
                        )
                    } else if (idLigaActual == null) {
                        // 🚨 Este bloque solo se debería ejecutar si, *después* de que isDataReady fue TRUE,
                        // el valor se pierde o si el usuario pulsa muy rápido durante una recarga.
                        scope.launch {
                            snackbarHostState.showSnackbar("Error: No se encontró la liga asociada a esta jornada. Recargue la pantalla.", duration = SnackbarDuration.Short)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}