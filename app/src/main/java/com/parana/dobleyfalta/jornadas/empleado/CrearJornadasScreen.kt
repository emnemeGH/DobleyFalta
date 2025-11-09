package com.parana.dobleyfalta.jornadas.empleado

import com.parana.dobleyfalta.DarkGrey
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
import androidx.navigation.NavController
import com.parana.dobleyfalta.MainViewModel
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.DarkBlue
import com.parana.dobleyfalta.PrimaryOrange
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearJornadaScreen(navController: NavController, mainViewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    // Estados para los campos y la validación
    var numeroJornada by remember { mutableStateOf("") }
    var fechaInicioJornada by remember { mutableStateOf("") }
    var fechaFinalizacionJornada by remember { mutableStateOf("") }
    var numeroJornadaError by remember { mutableStateOf<String?>(null) }
    var fechaInicioError by remember { mutableStateOf<String?>(null) }
    var fechaFinalizacionError by remember { mutableStateOf<String?>(null) }
    var mostrarSeleccionFechaInicio by remember { mutableStateOf(false) }
    var mostrarSeleccionFechaFinalizacion by remember { mutableStateOf(false) }


    // Diálogo para seleccionar la fecha de inicio
    if (mostrarSeleccionFechaInicio) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { mostrarSeleccionFechaInicio = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            fechaInicioJornada = formatter.format(Date(selectedDateMillis))
                            fechaInicioError = null // Limpiar el error al seleccionar una fecha
                        }
                        mostrarSeleccionFechaInicio = false
                    }
                ) {
                    Text("Aceptar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Diálogo para seleccionar la fecha de finalización
    if (mostrarSeleccionFechaFinalizacion) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { mostrarSeleccionFechaFinalizacion = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            fechaFinalizacionJornada = formatter.format(Date(selectedDateMillis))
                            fechaFinalizacionError = null
                        }
                        mostrarSeleccionFechaFinalizacion = false
                    }
                ) {
                    Text("Aceptar")
                }
            }
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
            text = "Crear Jornada",
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
            label = { Text("Número de la jornada", color = LightGrey) },
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
            label = { Text("Fecha de inicio", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource
                ) { mostrarSeleccionFechaInicio = true },
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
            isError = fechaInicioError != null,
            supportingText = {
                fechaInicioError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha de inicio de la jornada",
                    tint = LightGrey,
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
            label = { Text("Fecha de finalización", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource
                ) { mostrarSeleccionFechaFinalizacion = true },
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
            isError = fechaFinalizacionError != null,
            supportingText = {
                fechaFinalizacionError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha de finalización de la jornada",
                    tint = LightGrey,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) { mostrarSeleccionFechaFinalizacion = true }
                )
            }
        )

        // Botón para guardar
        Button(
            onClick = {
                numeroJornadaError = if (numeroJornada.isBlank()) "El número de la jornada es obligatorio" else null
                fechaInicioError = if (fechaInicioJornada.isBlank()) "La fecha de inicio es obligatoria" else null
                fechaFinalizacionError = if (fechaFinalizacionJornada.isBlank()) "La fecha de finalización es obligatoria" else null

                if (fechaInicioJornada == "20/09/2025") { // Simulación de una fecha que ya existe
                    fechaInicioError = "Ya existe una jornada registrada para esta fecha"
                }

                if (numeroJornadaError == null && fechaInicioError == null && fechaFinalizacionError == null) {
                    // Si todo es correcto, navega de vuelta
                    navController.popBackStack()
                    println("Jornada creada: N° $numeroJornada, Fecha de inicio: $fechaInicioJornada y fecha de finalización: $fechaFinalizacionJornada")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Guardar", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
