package com.parana.dobleyfalta.jornadas

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.DarkBlue
import com.parana.dobleyfalta.DarkGrey
import com.parana.dobleyfalta.MainViewModel
import com.parana.dobleyfalta.PrimaryOrange
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.equipos.LightGrey
import kotlinx.parcelize.Parcelize

// Modelo de Partido actualizado para incluir más detalles
@Parcelize
data class Partido(
    val id: Int,
    val equipo1: String,
    val equipo2: String,
    val escudo1: Int,
    val escudo2: Int,
    val score1: Int?,
    val score2: Int?,
    val status: String, // e.g., "Terminado", "Próximo", "En curso"
    val liga: String,
    val quarterScores1: List<Int>,
    val quarterScores2: List<Int>,
    val fecha: String,
    val hora: String,
    val estadio: String
) : Parcelable


@Composable
fun EditarPartidosScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    partido: Partido // recibe el partido a editar
) {

    // Definición de colores para mantener la coherencia
    val DarkBlue = Color(0xFF102B4E)
    val PrimaryOrange = Color(0xFFFF6600)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val LiveGreen = Color(0xFF50C878)
    val BlueEdit = Color(0xFF007ACC)
    val RedDelete = Color(0xFFE53935)

    // Agregamos un Box para establecer el fondo de toda la pantalla a DarkBlue
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {
        var score1 by remember { mutableStateOf(partido.score1 ?: 0) }
        var score2 by remember { mutableStateOf(partido.score2 ?: 0) }
        var quarters1 by remember {
            mutableStateOf(
                partido.quarterScores1.toMutableList().ifEmpty { mutableListOf(0, 0, 0, 0) })
        }
        var quarters2 by remember {
            mutableStateOf(
                partido.quarterScores2.toMutableList().ifEmpty { mutableListOf(0, 0, 0, 0) })
        }
        var currentQuarter by remember { mutableStateOf(0) }

        var showForm by remember { mutableStateOf(false) }

        if (showForm) {
            FormularioModificarPartido(
                navController = navController,
                partido = partido,
                onGuardar = { local, visitante, fecha, hora, lugar ->
                    println("Modificado: $local vs $visitante en $lugar el $fecha $hora")
                    showForm = false
                    navController.popBackStack() // volver después de guardar
                },
                onCancelar = { showForm = false }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.Center
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
                Spacer(Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkGrey)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "En vivo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(LiveGreen)
                                .padding(8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Equipo 1
                            Column(
                                modifier = Modifier.weight(1f), // Da un peso de 1, para que ocupe una parte equitativa
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    partido.equipo1,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    maxLines = 1, // Limita el texto a una sola línea
                                    overflow = TextOverflow.Ellipsis, // Agrega "..." si el texto es demasiado largo
                                    textAlign = TextAlign.Center // Centra el texto en su columna
                                )
                                Spacer(Modifier.height(8.dp))
                                Image(
                                    painter = painterResource(id = getEscudoResourceId(partido.equipo1)),
                                    contentDescription = partido.equipo1,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            // Marcador y botones (le damos más peso para que tenga más espacio)
                            Column(
                                modifier = Modifier.weight(2f), // Este peso mayor le da más espacio al marcador central
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "$score1 - $score2",
                                    fontSize = 50.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(Modifier.height(16.dp))
                                Row {
                                    // Columna para los botones del equipo 1
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Button(onClick = {
                                            score1++
                                            quarters1[currentQuarter]++
                                        }) { Text("+1") }
                                        Spacer(Modifier.height(8.dp))
                                        Button(onClick = {
                                            if (score1 > 0 && quarters1[currentQuarter] > 0) {
                                                score1--; quarters1[currentQuarter]--
                                            }
                                        }) { Text("-1") }
                                    }
                                    Spacer(Modifier.width(16.dp))
                                    // Columna para los botones del equipo 2
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Button(onClick = {
                                            score2++
                                            quarters2[currentQuarter]++
                                        }) { Text("+1") }
                                        Spacer(Modifier.height(8.dp))
                                        Button(onClick = {
                                            if (score2 > 0 && quarters2[currentQuarter] > 0) {
                                                score2--; quarters2[currentQuarter]--
                                            }
                                        }) { Text("-1") }
                                    }
                                }
                            }

                            // Equipo 2
                            Column(
                                modifier = Modifier.weight(1f), // Da un peso de 1, igual que el primer equipo
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    partido.equipo2,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    maxLines = 1, // Limita el texto a una sola línea
                                    overflow = TextOverflow.Ellipsis, // Agrega "..." si el texto es demasiado largo
                                    textAlign = TextAlign.Center // Centra el texto en su columna
                                )
                                Spacer(Modifier.height(8.dp))
                                Image(
                                    painter = painterResource(id = getEscudoResourceId(partido.equipo2)),
                                    contentDescription = partido.equipo2,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }

                        // Cuartos
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Columna para el equipo 1: nombre y cuartos centrados
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = partido.equipo1,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Q1 ${quarters1[0]} | Q2 ${quarters1[1]} | Q3 ${quarters1[2]} | Q4 ${quarters1[3]}",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Spacer(Modifier.height(16.dp))

                            // Columna para el equipo 2: nombre y cuartos centrados
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = partido.equipo2,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Q1 ${quarters2[0]} | Q2 ${quarters2[1]} | Q3 ${quarters2[2]} | Q4 ${quarters2[3]}",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }


                        // Control de cuartos
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    onClick = { if (currentQuarter > 0) currentQuarter-- },
                                    enabled = currentQuarter > 0,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                ) { Text("Cuarto anterior") }
                                Spacer(Modifier.width(8.dp))
                                Button(
                                    onClick = { if (currentQuarter < 3) currentQuarter++ },
                                    enabled = currentQuarter < 3,
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                                ) { Text("Siguiente cuarto") }
                            }
                            Text(
                                "Cuarto actual: Q${currentQuarter + 1}",
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }
                        Spacer(Modifier.height(8.dp))

                        // Lugar
                        Text(
                            "${partido.estadio}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DarkGrey)
                                .padding(8.dp),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    "Modificar otros campos",
                    color = PrimaryOrange,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showForm = true }
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

// Función auxiliar para obtener el ID de recurso de drawable según el nombre del equipo
@DrawableRes
fun getEscudoResourceId(nombreEquipo: String): Int {
    return when (nombreEquipo) {
        "ROWING" -> R.drawable.escudo_rowing
        "CAE" -> R.drawable.escudo_cae
        "PARACAO" -> R.drawable.escudo_paracao
        else -> R.drawable.logo_transparent // O un icono genérico por defecto
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioModificarPartido(
    navController: NavController,
    partido: Partido,
    onGuardar: (String, String, String, String, String) -> Unit,
    onCancelar: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    var equipoLocal by remember { mutableStateOf(partido.equipo1) }
    var equipoVisitante by remember { mutableStateOf(partido.equipo2) }
    var fecha by remember { mutableStateOf(partido.fecha) }
    var hora by remember { mutableStateOf(partido.hora) }
    var lugar by remember { mutableStateOf(partido.estadio) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue) // Fondo oscuro como en la pantalla de creación
            .padding(24.dp)
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

        Text(
            "Modificar Partido",
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = equipoLocal,
            onValueChange = { equipoLocal = it },
            label = { Text("Equipo local", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
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
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = equipoVisitante,
            onValueChange = { equipoVisitante = it },
            label = { Text("Equipo visitante", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
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
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha (dd/MM/yyyy)", color = LightGrey) },
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
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
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = hora,
            onValueChange = { hora = it },
            label = { Text("Hora (HH:mm)", color = LightGrey) },
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
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
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = lugar,
            onValueChange = { lugar = it },
            label = { Text("Lugar del partido", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey,
                unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey,
                focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onGuardar(equipoLocal, equipoVisitante, fecha, hora, lugar) },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onCancelar,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancelar", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
