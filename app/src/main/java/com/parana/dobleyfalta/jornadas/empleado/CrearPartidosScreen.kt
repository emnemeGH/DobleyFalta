package com.parana.dobleyfalta.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.parana.dobleyfalta.R
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
    // 💡 DEFINICIÓN DE COLORES Y ESTILOS
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val CardBackground = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)

    val estado by crearPartidoViewModel.estadoPartido.collectAsState()
    val equipos by equiposViewModel.equipos.collectAsState()
    val partidoCreado by crearPartidoViewModel.partidoCreado.collectAsState()

    val context = LocalContext.current

    var equipoLocalSeleccionado by remember { mutableStateOf<Int?>(null) }
    var equipoVisitanteSeleccionado by remember { mutableStateOf<Int?>(null) }
    var fechaSeleccionada by remember { mutableStateOf("") }

    // 💡 Estados de error para los campos
    var errorLocal by remember { mutableStateOf<String?>(null) }
    var errorVisitante by remember { mutableStateOf<String?>(null) }
    var errorFecha by remember { mutableStateOf<String?>(null) }

    var expandirLocal by remember { mutableStateOf(false) }
    var expandirVisitante by remember { mutableStateOf(false) }

    val nombreLocal = equipos.find { it.idEquipo == equipoLocalSeleccionado }?.nombre ?: ""
    val nombreVisitante = equipos.find { it.idEquipo == equipoVisitanteSeleccionado }?.nombre ?: ""


    // Cargar equipos una sola vez
    LaunchedEffect(Unit) { equiposViewModel.cargarEquipos() }

    // Cuando el partido se crea correctamente
    LaunchedEffect(estado) {
        if (estado == "ok" && partidoCreado != null) {
            // 🔥 CLAVE: Se envía el 'partidoCreado' (PartidoModel Parcelable), que es lo que
            // la JornadasScreen corregida espera recibir.
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("nuevo_partido", partidoCreado)

            navController.popBackStack() // vuelve a JornadasScreen
        }
    }

    Scaffold(
        // 💡 ESTILOS PARA EL TOP BAR
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Crear Partido",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Volver a jornadas",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkBlue
                )
            )
        },
        // 💡 ESTILOS PARA EL CONTENIDO DEL SCAFFOLD
        containerColor = DarkBlue
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Equipo local (El código del dropdown sigue igual y es funcional)
            ExposedDropdownMenuBox(
                expanded = expandirLocal,
                onExpandedChange = { expandirLocal = !expandirLocal },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = nombreLocal,
                    onValueChange = {},
                    label = { Text("Equipo Local", color = LightGrey) },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth(),
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirLocal) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardBackground,
                        unfocusedContainerColor = CardBackground,
                        unfocusedBorderColor = CardBackground,
                        focusedBorderColor = PrimaryOrange,
                        cursorColor = PrimaryOrange,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledTextColor = Color.White,
                        disabledContainerColor = CardBackground,
                    ),
                    isError = errorLocal != null,
                    supportingText = {
                        errorLocal?.let { Text(it, color = Color.Red, fontSize = 12.sp) }
                    }
                )
                ExposedDropdownMenu(
                    expanded = expandirLocal,
                    onDismissRequest = { expandirLocal = false }
                ) {
                    equipos.forEach { equipo ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    equipo.nombre,
                                    color = if (equipo.idEquipo == equipoVisitanteSeleccionado) LightGrey.copy(alpha = 0.5f) else Color.Black
                                )
                            },
                            onClick = {
                                if (equipo.idEquipo != equipoVisitanteSeleccionado) {
                                    equipoLocalSeleccionado = equipo.idEquipo
                                    errorLocal = null
                                    expandirLocal = false
                                }
                            },
                            enabled = equipo.idEquipo != equipoVisitanteSeleccionado
                        )
                    }
                }
            }

            // Equipo visitante (El código del dropdown sigue igual y es funcional)
            ExposedDropdownMenuBox(
                expanded = expandirVisitante,
                onExpandedChange = { expandirVisitante = !expandirVisitante },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = nombreVisitante,
                    onValueChange = {},
                    label = { Text("Equipo Visitante", color = LightGrey) },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth(),
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirVisitante) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardBackground,
                        unfocusedContainerColor = CardBackground,
                        unfocusedBorderColor = CardBackground,
                        focusedBorderColor = PrimaryOrange,
                        cursorColor = PrimaryOrange,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledTextColor = Color.White,
                        disabledContainerColor = CardBackground,
                    ),
                    isError = errorVisitante != null,
                    supportingText = {
                        errorVisitante?.let { Text(it, color = Color.Red, fontSize = 12.sp) }
                    }
                )
                ExposedDropdownMenu(
                    expanded = expandirVisitante,
                    onDismissRequest = { expandirVisitante = false }
                ) {
                    equipos.forEach { equipo ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    equipo.nombre,
                                    color = if (equipo.idEquipo == equipoLocalSeleccionado) LightGrey.copy(alpha = 0.5f) else Color.Black
                                )
                            },
                            onClick = {
                                if (equipo.idEquipo != equipoLocalSeleccionado) {
                                    equipoVisitanteSeleccionado = equipo.idEquipo
                                    errorVisitante = null
                                    expandirVisitante = false
                                }
                            },
                            enabled = equipo.idEquipo != equipoLocalSeleccionado
                        )
                    }
                }
            }

            // Fecha y hora (El selector de fecha/hora es funcional)
            OutlinedTextField(
                value = fechaSeleccionada,
                onValueChange = { /* Solo lectura */ },
                label = { Text("Fecha y hora", color = LightGrey) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground,
                    unfocusedBorderColor = CardBackground,
                    focusedBorderColor = PrimaryOrange,
                    cursorColor = PrimaryOrange,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White,
                    disabledContainerColor = CardBackground,
                ),
                isError = errorFecha != null,
                supportingText = {
                    errorFecha?.let { Text(it, color = Color.Red, fontSize = 12.sp) }
                },
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
                                        errorFecha = null
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
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Seleccionar fecha",
                            tint = PrimaryOrange
                        )
                    }
                }
            )

            // Mensaje de error general del ViewModel
            estado?.let { message ->
                if (message.startsWith("error")) {
                    Text(
                        text = message.substringAfter("error: ").trim(),
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón guardar
            Button(
                onClick = {
                    // Lógica de Validación (limpiamos y validamos al hacer clic)
                    var esValido = true
                    errorLocal = null
                    errorVisitante = null
                    errorFecha = null

                    // 💡 AJUSTE DE LIMPIEZA: Limpiar el error general del ViewModel.
                    // Asumiendo que tu ViewModel tiene una función clearError().
                    crearPartidoViewModel.clearError()

                    if (equipoLocalSeleccionado == null) {
                        errorLocal = "Selecciona el equipo local."
                        esValido = false
                    }
                    if (equipoVisitanteSeleccionado == null) {
                        errorVisitante = "Selecciona el equipo visitante."
                        esValido = false
                    }
                    if (fechaSeleccionada.isEmpty()) {
                        errorFecha = "Selecciona fecha y hora."
                        esValido = false
                    }
                    if (equipoLocalSeleccionado != null && equipoLocalSeleccionado == equipoVisitanteSeleccionado) {
                        errorLocal = "Los equipos deben ser distintos."
                        errorVisitante = "Los equipos deben ser distintos."
                        crearPartidoViewModel.setError("Los equipos local y visitante deben ser distintos.")
                        esValido = false
                    }

                    if (esValido) {
                        val partido = CrearPartidoModel(
                            idJornada = jornadaId,
                            idEquipoLocal = equipoLocalSeleccionado!!,
                            idEquipoVisitante = equipoVisitanteSeleccionado!!,
                            fecha = fechaSeleccionada
                        )
                        crearPartidoViewModel.crearPartido(partido)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                // 💡 ESTILOS DEL BOTÓN
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Guardar Partido",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}