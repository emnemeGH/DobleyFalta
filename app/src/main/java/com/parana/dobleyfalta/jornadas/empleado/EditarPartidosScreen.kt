package com.parana.dobleyfalta.jornadas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.parana.dobleyfalta.MainViewModel

@Composable
fun EditarPartidosScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    partido: Partido // ⬅ recibe el partido a editar
) {
    var score1 by remember { mutableStateOf(partido.score1 ?: 0) }
    var score2 by remember { mutableStateOf(partido.score2 ?: 0) }
    var quarters1 by remember { mutableStateOf(partido.quarterScores1.toMutableList().ifEmpty { mutableListOf(0, 0, 0, 0) }) }
    var quarters2 by remember { mutableStateOf(partido.quarterScores2.toMutableList().ifEmpty { mutableListOf(0, 0, 0, 0) }) }
    var currentQuarter by remember { mutableStateOf(0) }

    var showForm by remember { mutableStateOf(false) }

    if (showForm) {
        FormularioModificarPartido(
            partido = partido,
            onGuardar = { local, visitante, fecha, hora, lugar ->
                println("Modificado: $local vs $visitante en $lugar el $fecha $hora")
                showForm = false
                navController.popBackStack() // volver después de guardar
            },
            onCancelar = { showForm = false }
        )
    } else {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.background(Color.White)) {
                Text(
                    "En vivo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF28A745))
                        .padding(8.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                // Marcador
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(partido.equipo1, fontWeight = FontWeight.Bold)
                        Text("🏀", fontSize = 30.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$score1 - $score2", fontSize = 40.sp, fontWeight = FontWeight.Bold)
                        Row {
                            Button(onClick = {
                                score1++
                                quarters1[currentQuarter]++
                            }) { Text("+1") }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = {
                                if (score1 > 0 && quarters1[currentQuarter] > 0) {
                                    score1--; quarters1[currentQuarter]--
                                }
                            }) { Text("-1") }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = {
                                score2++
                                quarters2[currentQuarter]++
                            }) { Text("+1") }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = {
                                if (score2 > 0 && quarters2[currentQuarter] > 0) {
                                    score2--; quarters2[currentQuarter]--
                                }
                            }) { Text("-1") }
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(partido.equipo2, fontWeight = FontWeight.Bold)
                        Text("🏀", fontSize = 30.sp)
                    }
                }

                // Cuartos
                Column(Modifier.padding(16.dp)) {
                    Text("${partido.equipo1}: Q1 ${quarters1[0]} | Q2 ${quarters1[1]} | Q3 ${quarters1[2]} | Q4 ${quarters1[3]}")
                    Text("${partido.equipo2}: Q1 ${quarters2[0]} | Q2 ${quarters2[1]} | Q3 ${quarters2[2]} | Q4 ${quarters2[3]}")
                }

                // Control de cuartos
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row {
                        Button(
                            onClick = { if (currentQuarter > 0) currentQuarter-- },
                            enabled = currentQuarter > 0,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) { Text("Cuarto anterior") }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = { if (currentQuarter < 3) currentQuarter++ },
                            enabled = currentQuarter < 3,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                        ) { Text("Siguiente cuarto") }
                    }
                    Text("Cuarto actual: Q${currentQuarter + 1}")
                }

                // Lugar
                Text(
                    "🏟 ${partido.estadio}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.DarkGray)
                        .padding(8.dp),
                    color = Color.White,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        // Link de modificar
        Text(
            "Modificar otros campos",
            color = Color(0xFF007BFF),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showForm = true }
                .padding(8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioModificarPartido(
    partido: Partido,
    onGuardar: (String, String, String, String, String) -> Unit,
    onCancelar: () -> Unit
) {
    var equipoLocal by remember { mutableStateOf(partido.equipo1) }
    var equipoVisitante by remember { mutableStateOf(partido.equipo2) }
    var fecha by remember { mutableStateOf(partido.fecha) }
    var hora by remember { mutableStateOf(partido.hora) }
    var lugar by remember { mutableStateOf(partido.estadio) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A375E))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Modificar Partido", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = equipoLocal,
            onValueChange = { equipoLocal = it },
            label = { Text("Equipo local", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = equipoVisitante,
            onValueChange = { equipoVisitante = it },
            label = { Text("Equipo visitante", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha (dd/MM/yyyy)", color = Color.LightGray) },
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = hora,
            onValueChange = { hora = it },
            label = { Text("Hora (HH:mm)", color = Color.LightGray) },
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = lugar,
            onValueChange = { lugar = it },
            label = { Text("Lugar del partido", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = { onGuardar(equipoLocal, equipoVisitante, fecha, hora, lugar) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
            ) {
                Text("Guardar", color = Color.White)
            }
            Button(onClick = onCancelar, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                Text("Cancelar", color = Color.White)
            }
        }
    }
}
