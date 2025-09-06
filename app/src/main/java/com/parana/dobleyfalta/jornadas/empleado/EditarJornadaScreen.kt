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
import androidx.navigation.NavController
import com.parana.dobleyfalta.DarkBlue
import com.parana.dobleyfalta.MainViewModel
import com.parana.dobleyfalta.PrimaryOrange
import com.parana.dobleyfalta.R
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarJornadaScreen(navController: NavController, mainViewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    // Estados para los campos de la jornada
    var numeroJornada by remember { mutableStateOf("") }
    var fechaInicioJornada by remember { mutableStateOf("") }
    var fechaFinalizacionJornada by remember { mutableStateOf("") }

    // Estados para los errores de validación
    var numeroJornadaError by remember { mutableStateOf<String?>(null) }
    var fechaInicioJornadaError by remember { mutableStateOf<String?>(null) }
    var fechaFinalizacionJornadaError by remember { mutableStateOf<String?>(null) }

    // Estados para controlar la visibilidad de los selectores de fecha
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
                            fechaInicioJornadaError = null
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
                            fechaFinalizacionJornadaError = null
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
                    tint = LightGrey,
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
                numeroJornadaError = if (numeroJornada.isBlank()) "El número de la jornada es obligatorio" else null
                fechaInicioJornadaError = if (fechaInicioJornada.isBlank()) "La fecha de inicio es obligatoria" else null
                fechaFinalizacionJornadaError = if (fechaFinalizacionJornada.isBlank()) "La fecha de finalización es obligatoria" else null

                if (numeroJornadaError == null && fechaInicioJornadaError == null && fechaFinalizacionJornadaError == null) {
                    println("Jornada editada: N° $numeroJornada, Fecha de inicio: $fechaInicioJornada, Fecha de finalización: $fechaFinalizacionJornada")
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
            Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
