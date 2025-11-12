package com.parana.dobleyfalta.jornadas.empleado

// Se eliminó androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.parana.dobleyfalta.PrimaryOrange
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.jornadas.LightGrey
import com.parana.dobleyfalta.retrofit.models.partidos.PartidoDTOModel
import com.parana.dobleyfalta.retrofit.viewmodels.partidos.EquipoType
import com.parana.dobleyfalta.retrofit.viewmodels.partidos.PartidosViewModel

// Asumo que estos colores están accesibles en el scope de la aplicación
val DarkBlue = Color(0xFF102B4E)
val LiveGreen = Color(0xFF50C878)
val ButtonColor = Color(0xFF584988)


@Composable
fun MarcadorPartidoScreen(
    navController: NavController,
    idPartido: Int, // Recibimos solo el ID por navegación
    partidosViewModel: PartidosViewModel = viewModel()
) {
    // 1. CARGA DE DATOS
    val partidosList by partidosViewModel.partidosDTO.collectAsState()
    val partidoDTO = partidosList.find { it.idPartido == idPartido }

    if (partidoDTO == null) {
        LaunchedEffect(Unit) {
            if (partidosList.isEmpty()) {
                partidosViewModel.cargarTodosLosPartidos()
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlue),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryOrange)
        }
        return
    }

    // 2. ESTADOS LOCALES
    var scoreLocal by remember { mutableStateOf(partidoDTO.puntosLocal ?: 0) }
    var scoreVisitante by remember { mutableStateOf(partidoDTO.puntosVisitante ?: 0) }

    // Estado local para el efecto visual de los cuartos
    var currentQuarter by remember { mutableStateOf(1) } // Q1, Q2, Q3, Q4

    // 3. Sincronización
    LaunchedEffect(partidoDTO) {
        scoreLocal = partidoDTO.puntosLocal ?: 0
        scoreVisitante = partidoDTO.puntosVisitante ?: 0
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
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
                        painter = painterResource(id = R.drawable.back), // Asumiendo R.drawable.back
                        contentDescription = "Volver",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))

            // Marcador de Baloncesto con el diseño solicitado
            MarcadorBasquetbol(
                partidoDTO = partidoDTO,
                scoreLocal = scoreLocal,
                scoreVisitante = scoreVisitante,
                currentQuarter = currentQuarter,
                onScoreChange = { equipo, nuevoPunto ->
                    val isLocal = equipo == EquipoType.LOCAL
                    if (isLocal) {
                        scoreLocal = nuevoPunto
                    } else {
                        scoreVisitante = nuevoPunto
                    }
                    // Llamada en tiempo real al ViewModel para actualizar la DB
                    partidosViewModel.actualizarPuntuacion(
                        partidoDTO.idPartido,
                        equipo,
                        nuevoPunto
                    )
                },
                onNextQuarter = {
                    currentQuarter = if (currentQuarter < 4) currentQuarter + 1 else 4
                }
            )
        }
    }
}

// ===============================================
// 🎯 COMPONENTE DE ESTILO DE MARCADOR
// ===============================================

@Composable
fun MarcadorBasquetbol(
    partidoDTO: PartidoDTOModel,
    scoreLocal: Int,
    scoreVisitante: Int,
    currentQuarter: Int,
    onScoreChange: (EquipoType, Int) -> Unit,
    onNextQuarter: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 1. Header "En vivo"
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

        // 2. Marcador 0 - 0 (Parte principal)
        Text(
            "$scoreLocal - $scoreVisitante",
            fontSize = 70.sp, // Tamaño más grande como en la imagen
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 8.dp)
        )

        // 3. Nombres de Equipos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                partidoDTO.equipoLocalNombre ?: "LOCAL",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                partidoDTO.equipoVisitanteNombre ?: "VISITANTE",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(16.dp))

        // 4. Logos y Botones (+1 / -1)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo Local
            // 🛑 Reemplazar con tu lógica de carga de logo (ej: AsyncImage o tu función de recurso)
            Image(
                painter = painterResource(id = R.drawable.logo_transparent), // Placeholder temporal
                contentDescription = partidoDTO.equipoLocalNombre,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )

            // Controles Centrales (+1 / -1)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Fila de botones +1
                Row {
                    // Botón +1 Local
                    Button(
                        onClick = { onScoreChange(EquipoType.LOCAL, scoreLocal + 1) },
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                        modifier = Modifier.size(50.dp, 40.dp), // Ajustado para ser más ancho que alto
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "+1",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold // Requerido: Texto blanco y bold
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    // Botón +1 Visitante
                    Button(
                        onClick = { onScoreChange(EquipoType.VISITANTE, scoreVisitante + 1) },
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                        modifier = Modifier.size(50.dp, 40.dp), // Ajustado para ser más ancho que alto
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "+1",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold )
                    }
                }
                Spacer(Modifier.height(16.dp))
                // Fila de botones -1
                Row {
                    // Botón -1 Local
                    Button(
                        onClick = { if (scoreLocal > 0) onScoreChange(EquipoType.LOCAL, scoreLocal - 1) },
                        enabled = scoreLocal > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                        modifier = Modifier.size(50.dp, 40.dp), // Ajustado para ser más ancho que alto
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text (
                        text = "-1",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold // Requerido: Texto blanco y bold
                    ) }
                    Spacer(Modifier.width(16.dp))
                    // Botón -1 Visitante
                    Button(
                        onClick = { if (scoreVisitante > 0) onScoreChange(EquipoType.VISITANTE, scoreVisitante - 1) },
                        enabled = scoreVisitante > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                        modifier = Modifier.size(50.dp, 40.dp), // Ajustado para ser más ancho que alto
                        contentPadding = PaddingValues(0.dp)
                    ) { Text (
                        text = "-1",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold // Requerido: Texto blanco y bold
                    ) }
                }
            }

            // Logo Visitante
            // 🛑 Reemplazar con tu lógica de carga de logo (ej: AsyncImage o tu función de recurso)
            Image(
                painter = painterResource(id = R.drawable.logo_transparent), // Placeholder temporal
                contentDescription = partidoDTO.equipoVisitanteNombre,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(Modifier.height(40.dp))

        // 5. Visualización de Cuartos (Efecto Visual)
        CuartosVisualizer(
            nombreEquipo = partidoDTO.equipoLocalNombre ?: "LOCAL",
            currentQuarter = currentQuarter
        )
        Spacer(Modifier.height(8.dp))
        CuartosVisualizer(
            nombreEquipo = partidoDTO.equipoVisitanteNombre ?: "VISITANTE",
            currentQuarter = currentQuarter
        )
        Spacer(Modifier.height(32.dp))

        // 6. Botón Siguiente Cuarto
        Button(
            onClick = onNextQuarter,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
        ) {
            Text(
                text = if (currentQuarter < 4) "Siguiente cuarto" else "Partido finalizado",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 7. Indicador de Cuarto Actual
        Spacer(Modifier.height(16.dp))
        Text(
            "Cuarto actual: Q$currentQuarter",
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ===============================================
// 🧩 COMPONENTE DE CUARTOS (Solo Visual)
// ===============================================
@Composable
fun CuartosVisualizer(nombreEquipo: String, currentQuarter: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            nombreEquipo,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row {
            for (i in 1..4) {
                // Simulación de puntajes de cuartos (si no se almacenan, se ponen en 0)
                val isCurrent = i == currentQuarter
                Text(
                    text = "Q$i 0",
                    color = if (isCurrent) PrimaryOrange else LightGrey, // Resaltar el cuarto actual
                    fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                // Separador
                if (i < 4) {
                    Text("|", color = LightGrey, fontSize = 16.sp)
                }
            }
        }
    }
}