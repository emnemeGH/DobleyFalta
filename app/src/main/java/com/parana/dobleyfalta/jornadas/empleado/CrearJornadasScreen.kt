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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.parana.dobleyfalta.DarkBlue
import com.parana.dobleyfalta.DarkGrey
import com.parana.dobleyfalta.PrimaryOrange
import com.parana.dobleyfalta.equipos.LightGrey
import com.parana.dobleyfalta.retrofit.models.jornadas.CrearJornadaModel
import com.parana.dobleyfalta.retrofit.viewmodels.JornadasViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearJornadaScreen(
      navController: NavController,
  idLiga: Int,
  jornadasViewModel: JornadasViewModel = viewModel()
) {
      val focusManager = LocalFocusManager.current
      val interactionSource = remember { MutableInteractionSource() }

      var numeroJornada by remember { mutableStateOf("") }
      var fechaInicioMillis by remember { mutableStateOf<Long?>(null) }
      var fechaFinMillis by remember { mutableStateOf<Long?>(null) }

      val fechaInicioUI = remember(fechaInicioMillis) { fechaInicioMillis?.let { convertMillisToUIDate(it) } ?: "" }
      val fechaFinUI = remember(fechaFinMillis) { fechaFinMillis?.let { convertMillisToUIDate(it) } ?: "" }

      var numeroError by remember { mutableStateOf<String?>(null) }
      var fechaInicioError by remember { mutableStateOf<String?>(null) }
      var fechaFinError by remember { mutableStateOf<String?>(null) }

      var mostrarFechaInicio by remember { mutableStateOf(false) }
      var mostrarFechaFin by remember { mutableStateOf(false) }

      val apiError by jornadasViewModel.error.collectAsState()
      val isLoading by jornadasViewModel.loading.collectAsState()

      if (mostrarFechaInicio) {
            val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fechaInicioMillis ?: getTodayMillis()
        )
            DatePickerDialog(
        onDismissRequest = { mostrarFechaInicio = false },
        confirmButton = {
            TextButton(
                onClick = {
                    fechaInicioMillis = datePickerState.selectedDateMillis
                    mostrarFechaInicio = false
                    fechaInicioError = null // Limpia el error al seleccionar
                }
            ) {
                Text("Aceptar", color = PrimaryOrange)
            }
        },
        dismissButton = {
            TextButton(onClick = { mostrarFechaInicio = false }) {
                Text("Cancelar", color = Color.White)
            }
        }
            ) {
              DatePicker(state = datePickerState)
            }
          }

      // ------------------- Dialog Fecha Fin CORREGIDO -------------------
      if (mostrarFechaFin) {
            val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fechaFinMillis ?: getTodayMillis()
        )
            DatePickerDialog(
        onDismissRequest = { mostrarFechaFin = false },
        confirmButton = {
            TextButton(
                onClick = {
                    fechaFinMillis = datePickerState.selectedDateMillis
                    mostrarFechaFin = false
                    fechaFinError = null // Limpia el error al seleccionar
                }
            ) {
                Text("Aceptar", color = PrimaryOrange)
            }
        },
        dismissButton = {
            TextButton(onClick = { mostrarFechaFin = false }) {
                Text("Cancelar", color = Color.White)
            }
        }
            ) {
              DatePicker(state = datePickerState)
            }
          }

    // El resto del Composable principal

      Column(
        modifier = Modifier
          .fillMaxSize()
          .background(DarkBlue)
          .padding(32.dp)
          .clickable(indication = null, interactionSource = interactionSource) { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
      ) {
            Text(
              text = "Crear Jornada",
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White,
              modifier = Modifier.padding(bottom = 24.dp)
            )

            // Número de jornada (sin cambios)
            OutlinedTextField(
              value = numeroJornada,
              onValueChange = { numeroJornada = it; numeroError = null; jornadasViewModel.clearError() },
              label = { Text("Número de jornada", color = LightGrey) },
              modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey,
                unfocusedContainerColor = DarkGrey,
                focusedBorderColor = PrimaryOrange,
                unfocusedBorderColor = DarkGrey,
                cursorColor = PrimaryOrange,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
              ),
              isError = numeroError != null,
              supportingText = { numeroError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
              singleLine = true
            )

            // Fecha Inicio (clickable llama a mostrarFechaInicio = true)
            OutlinedTextField(
              value = fechaInicioUI,
              onValueChange = {},
              readOnly = true,
              label = { Text("Fecha de inicio (dd/MM/yyyy)", color = LightGrey) },
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { mostrarFechaInicio = true },
              trailingIcon = {
        Icon(
            Icons.Default.DateRange,
            contentDescription = "Seleccionar fecha de inicio",
            tint = LightGrey,
            modifier = Modifier.clickable { mostrarFechaInicio = true } // Doble clickable para accesibilidad visual
        )
    },
        // *Mantener el resto de los estilos del OutlinedTextField*
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkGrey, unfocusedContainerColor = DarkGrey,
                focusedBorderColor = PrimaryOrange, unfocusedBorderColor = DarkGrey,
                cursorColor = PrimaryOrange, focusedTextColor = Color.White,
                unfocusedTextColor = Color.White, disabledTextColor = Color.White // Agregado disabledTextColor para readOnly
              ),
        isError = fechaInicioError != null,
        supportingText = { fechaInicioError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }
            )

            // Fecha Fin (clickable llama a mostrarFechaFin = true)
            OutlinedTextField(
              value = fechaFinUI,
              onValueChange = {},
              readOnly = true,
              label = { Text("Fecha de fin (dd/MM/yyyy)", color = LightGrey) },
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { mostrarFechaFin = true },
              trailingIcon = {
        Icon(
            Icons.Default.DateRange,
            contentDescription = "Seleccionar fecha de fin",
            tint = LightGrey,
            modifier = Modifier.clickable { mostrarFechaFin = true } // Doble clickable
        )
    },
        // *Mantener el resto de los estilos del OutlinedTextField*
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkGrey, unfocusedContainerColor = DarkGrey,
                focusedBorderColor = PrimaryOrange, unfocusedBorderColor = DarkGrey,
                cursorColor = PrimaryOrange, focusedTextColor = Color.White,
                unfocusedTextColor = Color.White, disabledTextColor = Color.White
              ),
        isError = fechaFinError != null,
        supportingText = { fechaFinError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }
            )

            apiError?.let { Text("Error API: $it", color = Color.Red, fontSize = 14.sp) }

            Spacer(modifier = Modifier.weight(1f))

            // Botón Guardar
            Button(
              onClick = {
                // Validaciones de UI
        val num = numeroJornada.toIntOrNull()
                numeroError = if (numeroJornada.isBlank() || num == null || num <= 0) "Número es obligatorio y válido" else null
                fechaInicioError = if (fechaInicioMillis == null) "Fecha de inicio obligatoria" else null
                fechaFinError = if (fechaFinMillis == null) "Fecha de fin obligatoria" else null

                if (numeroError == null && fechaInicioError == null && fechaFinError == null) {
                  val inicioApi = convertMillisToApiDate(fechaInicioMillis!!)
                  val finApi = convertMillisToApiDate(fechaFinMillis!!)

                  val nuevaJornada = CrearJornadaModel(
                    numero = num!!,
                    fechaInicio = inicioApi, // Formato YYYY-MM-DD
                    fechaFin = finApi, // Formato YYYY-MM-DD
                    idLiga = idLiga
                  )

                  jornadasViewModel.crearJornada(nuevaJornada) {
        // Al tener éxito, navegamos y recargamos la lista (asumo que es necesario)
        jornadasViewModel.cargarJornadasDeLiga(idLiga)
                    navController.popBackStack()
                  }
                }
              },
              enabled = !isLoading,
              modifier = Modifier.fillMaxWidth().height(50.dp),
              colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
            ) {
              if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
              else Text("Guardar Jornada", color = Color.White, fontWeight = FontWeight.Bold)
            }
          }
}

private val UI_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

private fun convertMillisToUIDate(millis: Long): String {
      return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(UI_DATE_FORMATTER)
}

private fun convertMillisToApiDate(millis: Long): String {
      return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .toString() // YYYY-MM-DD
}

private fun getTodayMillis(): Long {
      return Instant.now().atZone(ZoneId.systemDefault())
        .toLocalDate().atStartOfDay(ZoneId.systemDefault())
        .toInstant().toEpochMilli()
}