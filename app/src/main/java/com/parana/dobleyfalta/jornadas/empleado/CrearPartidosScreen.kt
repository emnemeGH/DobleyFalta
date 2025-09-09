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
import com.parana.dobleyfalta.DarkBlue
import com.parana.dobleyfalta.DarkGrey
import com.parana.dobleyfalta.MainViewModel
import com.parana.dobleyfalta.PrimaryOrange
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.equipos.LightGrey
import java.text.SimpleDateFormat
import java.util.*

// Definición de colores locales para mantener el contexto del archivo
val DarkGrey = Color(0xFF1A375E)
val LightGrey = Color(0xFFA0B3C4)

// Información de los equipos y estadios para simular la base de datos
val equiposConEstadios = mapOf(
    "ROWING" to "EL GIGANTE DE CALLE LAS HERAS",
    "CAE" to "Estadio del CAE",
    "EQUIPO A" to "Estadio del Equipo A",
    "EQUIPO B" to "Estadio del Equipo B",
    "CAMIONEROS" to "Estadio de Camioneros",
    "DEPORTIVO" to "Estadio del Deportivo",
    "CENTRO" to "Estadio del Centro"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPartidosScreen(navController: NavController, mainViewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    // Estados para los campos del formulario
    var equipoLocal by remember { mutableStateOf("") }
    var equipoVisitante by remember { mutableStateOf("") }
    var fechaPartido by remember { mutableStateOf("") }
    var horaPartido by remember { mutableStateOf("") }
    var lugarPartido by remember { mutableStateOf("") }

    // Estados para los errores de validación
    var equipoLocalError by remember { mutableStateOf<String?>(null) }
    var equipoVisitanteError by remember { mutableStateOf<String?>(null) }
    var fechaError by remember { mutableStateOf<String?>(null) }
    var horaError by remember { mutableStateOf<String?>(null) }
    var lugarError by remember { mutableStateOf<String?>(null) }

    // Estados para los selectores de equipos
    var expandedLocal by remember { mutableStateOf(false) }
    var expandedVisitante by remember { mutableStateOf(false) }

    // Estados para los selectores de fecha y hora
    var mostrarSeleccionFecha by remember { mutableStateOf(false) }
    var mostrarSeleccionHora by remember { mutableStateOf(false) }

    // Efecto para pre-llenar el lugar cuando se selecciona el equipo local
    LaunchedEffect(equipoLocal) {
        lugarPartido = equiposConEstadios[equipoLocal] ?: ""
    }

    // Diálogo para seleccionar la fecha
    if (mostrarSeleccionFecha) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { mostrarSeleccionFecha = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            fechaPartido = formatter.format(Date(selectedDateMillis))
                            fechaError = null
                        }
                        mostrarSeleccionFecha = false
                    }
                ) {
                    Text("Aceptar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Diálogo para seleccionar la hora
    if (mostrarSeleccionHora) {
        val timePickerState = rememberTimePickerState()
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        AlertDialog(
            onDismissRequest = { mostrarSeleccionHora = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val calendar = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            set(Calendar.MINUTE, timePickerState.minute)
                        }
                        horaPartido = formatter.format(calendar.time)
                        horaError = null
                        mostrarSeleccionHora = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            title = { Text("Seleccionar Hora", color = Color.White) },
            text = { TimePicker(state = timePickerState) },
            containerColor = DarkGrey,
            modifier = Modifier.background(DarkGrey)
        )
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
        // Botón para volver
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
                    contentDescription = "Volver a jornadas",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Crear Partido",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Selector de equipo local
        ExposedDropdownMenuBox(
            expanded = expandedLocal,
            onExpandedChange = { expandedLocal = !expandedLocal },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            OutlinedTextField(
                value = equipoLocal,
                onValueChange = {},
                readOnly = true,
                label = { Text("Equipo local", color = LightGrey) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocal) },
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
                isError = equipoLocalError != null,
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedLocal,
                onDismissRequest = { expandedLocal = false },
                modifier = Modifier.background(DarkGrey)
            ) {
                equiposConEstadios.keys.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption, color = Color.White) },
                        onClick = {
                            equipoLocal = selectionOption
                            equipoLocalError = null
                            expandedLocal = false
                        }
                    )
                }
            }
        }
        equipoLocalError?.let {
            Text(it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selector de equipo visitante
        ExposedDropdownMenuBox(
            expanded = expandedVisitante,
            onExpandedChange = { expandedVisitante = !expandedVisitante },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            OutlinedTextField(
                value = equipoVisitante,
                onValueChange = {},
                readOnly = true,
                label = { Text("Equipo visitante", color = LightGrey) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedVisitante) },
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
                isError = equipoVisitanteError != null,
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedVisitante,
                onDismissRequest = { expandedVisitante = false },
                modifier = Modifier.background(DarkGrey)
            ) {
                equiposConEstadios.keys.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption, color = Color.White) },
                        onClick = {
                            equipoVisitante = selectionOption
                            equipoVisitanteError = null
                            expandedVisitante = false
                        }
                    )
                }
            }
        }
        equipoVisitanteError?.let {
            Text(it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para la fecha (con selector)
        OutlinedTextField(
            value = fechaPartido,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha del partido", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource
                ) { mostrarSeleccionFecha = true },
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
            isError = fechaError != null,
            supportingText = {
                fechaError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha del partido",
                    tint = LightGrey,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) { mostrarSeleccionFecha = true }
                )
            }
        )

        // Campo de texto para la hora (con selector)
        OutlinedTextField(
            value = horaPartido,
            onValueChange = {},
            readOnly = true,
            label = { Text("Hora del partido", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource
                ) { mostrarSeleccionHora = true },
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
            isError = horaError != null,
            supportingText = {
                horaError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar hora del partido",
                    tint = LightGrey,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) { mostrarSeleccionHora = true }
                )
            }
        )

        // Campo de texto para el lugar del partido (editable)
        OutlinedTextField(
            value = lugarPartido,
            onValueChange = {
                lugarPartido = it
                lugarError = null
            },
            label = { Text("Lugar del partido", color = LightGrey) },
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
            isError = lugarError != null,
            supportingText = {
                lugarError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            }
        )

        // Botón para guardar el partido
        Button(
            onClick = {
                // Validar campos
                equipoLocalError = if (equipoLocal.isBlank()) "El equipo local es obligatorio" else null
                equipoVisitanteError = if (equipoVisitante.isBlank()) "El equipo visitante es obligatorio" else null
                fechaError = if (fechaPartido.isBlank()) "La fecha es obligatoria" else null
                horaError = if (horaPartido.isBlank()) "La hora es obligatoria" else null
                lugarError = if (lugarPartido.isBlank()) "El lugar es obligatorio" else null

                // Validar que los equipos no sean el mismo
                if (equipoLocal == equipoVisitante && equipoLocal.isNotBlank()) {
                    equipoVisitanteError = "Los equipos local y visitante no pueden ser el mismo"
                }

                if (equipoLocalError == null && equipoVisitanteError == null && fechaError == null && horaError == null && lugarError == null) {
                    println("Partido creado: Equipo Local='$equipoLocal', Equipo Visitante='$equipoVisitante', Fecha='$fechaPartido', Hora='$horaPartido', Lugar='$lugarPartido'")
                    navController.popBackStack()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Guardar Partido", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
