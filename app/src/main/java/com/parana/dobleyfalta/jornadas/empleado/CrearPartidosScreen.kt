package com.parana.dobleyfalta.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.parana.dobleyfalta.retrofit.models.partidos.CrearPartidoModel
import com.parana.dobleyfalta.retrofit.viewmodels.equipos.EquiposViewModel
import com.parana.dobleyfalta.ui.viewmodels.CrearPartidoViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPartidoScreen(
    navController: NavController,
    jornadaId: Int,
    crearPartidoViewModel: CrearPartidoViewModel = viewModel(),
    equiposViewModel: EquiposViewModel = viewModel()
) {
    val estado by crearPartidoViewModel.estado.collectAsState()
    val equipos by equiposViewModel.equipos.collectAsState()
    val partidoCreado by crearPartidoViewModel.partidoCreado.collectAsState()

    val context = LocalContext.current

    var equipoLocalSeleccionado by remember { mutableStateOf<Int?>(null) }
    var equipoVisitanteSeleccionado by remember { mutableStateOf<Int?>(null) }
    var fechaSeleccionada by remember { mutableStateOf("") }

    var expandirLocal by remember { mutableStateOf(false) }
    var expandirVisitante by remember { mutableStateOf(false) }

    // Cargar equipos una sola vez
    LaunchedEffect(Unit) { equiposViewModel.cargarEquipos() }

    // Cuando el partido se crea correctamente
    LaunchedEffect(estado) {
        if (estado == "ok" && partidoCreado != null) {
            // guardamos el nuevo partido en el backstack
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("nuevo_partido", partidoCreado)

            navController.popBackStack() // vuelve a JornadasScreen
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Crear Partido") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Equipo local
            ExposedDropdownMenuBox(
                expanded = expandirLocal,
                onExpandedChange = { expandirLocal = !expandirLocal }
            ) {
                OutlinedTextField(
                    value = equipos.find { it.idEquipo == equipoLocalSeleccionado }?.nombre ?: "",
                    onValueChange = {},
                    label = { Text("Equipo Local") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true
                )
                ExposedDropdownMenu(
                    expanded = expandirLocal,
                    onDismissRequest = { expandirLocal = false }
                ) {
                    equipos.forEach { equipo ->
                        DropdownMenuItem(
                            text = { Text(equipo.nombre) },
                            onClick = {
                                equipoLocalSeleccionado = equipo.idEquipo
                                expandirLocal = false
                            }
                        )
                    }
                }
            }

            // Equipo visitante
            ExposedDropdownMenuBox(
                expanded = expandirVisitante,
                onExpandedChange = { expandirVisitante = !expandirVisitante }
            ) {
                OutlinedTextField(
                    value = equipos.find { it.idEquipo == equipoVisitanteSeleccionado }?.nombre ?: "",
                    onValueChange = {},
                    label = { Text("Equipo Visitante") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true
                )
                ExposedDropdownMenu(
                    expanded = expandirVisitante,
                    onDismissRequest = { expandirVisitante = false }
                ) {
                    equipos.forEach { equipo ->
                        DropdownMenuItem(
                            text = { Text(equipo.nombre) },
                            onClick = {
                                equipoVisitanteSeleccionado = equipo.idEquipo
                                expandirVisitante = false
                            }
                        )
                    }
                }
            }

            // Fecha y hora
            OutlinedTextField(
                value = fechaSeleccionada,
                onValueChange = {},
                label = { Text("Fecha y hora") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
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
                                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                                        fechaSeleccionada = sdf.format(cal.time)
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
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    }
                }
            )

            // Botón guardar
            Button(
                onClick = {
                    when {
                        equipoLocalSeleccionado == null || equipoVisitanteSeleccionado == null || fechaSeleccionada.isEmpty() -> {
                            crearPartidoViewModel.setError("Completa todos los campos")
                        }
                        equipoLocalSeleccionado == equipoVisitanteSeleccionado -> {
                            crearPartidoViewModel.setError("Los equipos deben ser distintos")
                        }
                        else -> {
                            val partido = CrearPartidoModel(
                                idJornada = jornadaId,
                                idEquipoLocal = equipoLocalSeleccionado!!,
                                idEquipoVisitante = equipoVisitanteSeleccionado!!,
                                fecha = fechaSeleccionada
                            )
                            crearPartidoViewModel.crearPartido(partido)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Partido")
            }

            estado?.let {
                Text(
                    text = it,
                    color = if (it.startsWith("error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
