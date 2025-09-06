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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import com.parana.dobleyfalta.DarkBlue
import com.parana.dobleyfalta.MainViewModel
import com.parana.dobleyfalta.PrimaryOrange
import com.parana.dobleyfalta.R
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarLigasScreen(navController: NavController, mainViewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    // Estados para los campos del formulario
    var nombreLiga by remember { mutableStateOf("") }
    var tipoTorneo by remember { mutableStateOf("") }
    var cantidadEquipos by remember { mutableStateOf("") }

    // Estados para las fechas
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFinalizacion by remember { mutableStateOf("") }

    // Estados para controlar la visibilidad de los selectores de fecha
    var mostrarSeleccionFechaInicio by remember { mutableStateOf(false) }
    var mostrarSeleccionFechaFinalizacion by remember { mutableStateOf(false) }

    // Estados para los errores de validación
    var nombreError by remember { mutableStateOf<String?>(null) }
    var tipoTorneoError by remember { mutableStateOf<String?>(null) }
    var cantidadEquiposError by remember { mutableStateOf<String?>(null) }
    var fechaInicioError by remember { mutableStateOf<String?>(null) }
    var fechaFinalizacionError by remember { mutableStateOf<String?>(null) }

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
                            fechaInicio = formatter.format(Date(selectedDateMillis))
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
                            fechaFinalizacion = formatter.format(Date(selectedDateMillis))
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

        // Campo de texto para el nombre de la liga
        OutlinedTextField(
            value = nombreLiga,
            onValueChange = {
                nombreLiga = it
                nombreError = null
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
            supportingText = {
                nombreError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            }
        )

        // Campo de texto para el tipo de torneo
        OutlinedTextField(
            value = tipoTorneo,
            onValueChange = {
                tipoTorneo = it
                tipoTorneoError = null
            },
            label = { Text("Tipo de torneo", color = LightGrey) },
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
            isError = tipoTorneoError != null,
            supportingText = {
                tipoTorneoError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            }
        )

        // Campo de texto para la cantidad de equipos
        OutlinedTextField(
            value = cantidadEquipos,
            onValueChange = {
                cantidadEquipos = it
                cantidadEquiposError = null
            },
            label = { Text("Cantidad de equipos", color = LightGrey) },
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
            isError = cantidadEquiposError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = {
                cantidadEquiposError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            }
        )

        // Campo de texto para la fecha de inicio (con selector)
        OutlinedTextField(
            value = fechaInicio,
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
                    contentDescription = "Seleccionar fecha de inicio",
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
            value = fechaFinalizacion,
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
                    contentDescription = "Seleccionar fecha de finalización",
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
                nombreError = if (nombreLiga.isBlank()) "El nombre es obligatorio" else null
                tipoTorneoError = if (tipoTorneo.isBlank()) "El tipo de torneo es obligatorio" else null
                cantidadEquiposError = if (cantidadEquipos.isBlank()) "La cantidad de equipos es obligatoria" else null
                fechaInicioError = if (fechaInicio.isBlank()) "La fecha de inicio es obligatoria" else null
                fechaFinalizacionError = if (fechaFinalizacion.isBlank()) "La fecha de finalización es obligatoria" else null

                if (nombreError == null && tipoTorneoError == null && cantidadEquiposError == null && fechaInicioError == null && fechaFinalizacionError == null) {
                    println("Liga editada: Nombre='$nombreLiga', Tipo='$tipoTorneo', Equipos='$cantidadEquipos', Fecha Inicio='$fechaInicio', Fecha Fin='$fechaFinalizacion'")
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
