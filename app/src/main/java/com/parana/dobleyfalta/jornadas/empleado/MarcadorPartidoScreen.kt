package com.parana.dobleyfalta.jornadas.empleado

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.parana.dobleyfalta.PrimaryOrange
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.jornadas.LightGrey
import com.parana.dobleyfalta.retrofit.ApiConstants.BASE_URL
import com.parana.dobleyfalta.retrofit.models.partidos.PartidoDTOModel // Asegúrate de tener este import
import com.parana.dobleyfalta.retrofit.viewmodels.partidos.EquipoType
import com.parana.dobleyfalta.retrofit.viewmodels.partidos.PartidosViewModel

// Definiciones de colores (asumo que están en un archivo de tema o extensiones)
val DarkBlue = Color(0xFF102B4E)
val LiveGreen = Color(0xFF50C878)
val ButtonColor = Color(0xFF584988)

@Composable
fun MarcadorPartidoScreen(
    navController: NavController,
    idPartido: Int,
    partidosViewModel: PartidosViewModel = viewModel()
) {
    val context = LocalContext.current

    // 1️⃣ Carga de datos y estado reactivo (StateFlow)
    val partidosList by partidosViewModel.partidosDTO.collectAsState()

    // Obtiene el partido del StateFlow. Se recompone automáticamente si cambia.
    val partidoDTO: PartidoDTOModel? = remember(partidosList) {
        partidosList.find { it.idPartido == idPartido }
    }

    // 💡 Estado para controlar la UI durante el guardado (evita doble-click)
    var isSaving by remember { mutableStateOf(false) }

    // Carga inicial (Si la lista está vacía, la carga)
    LaunchedEffect(Unit) {
        if (partidosList.isEmpty()) {
            partidosViewModel.cargarTodosLosPartidos()
        }
    }

    // ❌ Estado de carga o error
    if (partidoDTO == null) {
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

    // 2️⃣ Estado local para lógica NO de puntaje
    var currentQuarter by remember { mutableStateOf(1) }

    // 💡 Usamos los puntajes directamente desde el DTO, que está en el StateFlow.
    val scoreLocal = partidoDTO.puntosLocal ?: 0
    val scoreVisitante = partidoDTO.puntosVisitante ?: 0

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBlue // Establece el fondo principal
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // 🔹 Marcador y botones +1/-1
            MarcadorBasquetbol(
                partidoDTO = partidoDTO,
                scoreLocal = scoreLocal,
                scoreVisitante = scoreVisitante,

                onScoreChange = { equipo, incremento ->
                    partidosViewModel.actualizarPuntuacionIncremental(
                        partidoId = idPartido,
                        equipo = equipo,
                        incremento = incremento
                    )
                }
            )

            Spacer(Modifier.height(32.dp))

            // 🔹 BOTÓN GUARDAR PARTIDO
            Button(
                // Envía el puntaje TOTAL actual del DTO en memoria
                onClick = {
                    isSaving = true
                    partidosViewModel.guardarPartido(
                        partidoDTO.idPartido,
                        scoreLocal,
                        scoreVisitante
                    ) { exito ->
                        isSaving = false
                    }
                },
                enabled = !isSaving, // Deshabilita durante el guardado
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                if (isSaving) {
                    // Muestra el progreso mientras se envía la data al backend
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        color = Color.White,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text(
                        text = "Guardar Marcador Final",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

// --------------------------------------------------------------------------------

@Composable
fun MarcadorBasquetbol(
    partidoDTO: PartidoDTOModel,
    scoreLocal: Int,
    scoreVisitante: Int,
    onScoreChange: (EquipoType, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ... (Header) ...
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

        Spacer(Modifier.height(8.dp))

        // Marcador principal (lee el puntaje del StateFlow/ViewModel)
        Text(
            "$scoreLocal - $scoreVisitante",
            fontSize = 70.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        // ... (Nombres de equipos) ...
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
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

        // Logos y botones de puntaje
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "${BASE_URL}${partidoDTO.logoLocal}",
                contentDescription = partidoDTO.equipoLocalNombre,
                modifier = Modifier.size(80.dp).clip(CircleShape),
                contentScale = ContentScale.Fit
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Fila de +1
                Row {
                    Button(
                        onClick = { onScoreChange(EquipoType.LOCAL, 1) }, // Envia incremento +1
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                        modifier = Modifier.size(50.dp, 40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("+1", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold) }

                    Spacer(Modifier.width(16.dp))

                    Button(
                        onClick = { onScoreChange(EquipoType.VISITANTE, 1) }, // Envia incremento +1
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                        modifier = Modifier.size(50.dp, 40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("+1", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold) }
                }

                Spacer(Modifier.height(16.dp))

                // Fila de -1
                Row {
                    Button(
                        onClick = { onScoreChange(EquipoType.LOCAL, -1) }, // Envia decremento -1
                        enabled = scoreLocal > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                        modifier = Modifier.size(50.dp, 40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("-1", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold) }

                    Spacer(Modifier.width(16.dp))

                    Button(
                        onClick = { onScoreChange(EquipoType.VISITANTE, -1) }, // Envia decremento -1
                        enabled = scoreVisitante > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                        modifier = Modifier.size(50.dp, 40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("-1", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold) }
                }
            }

            AsyncImage(
                model = "${BASE_URL}${partidoDTO.logoVisitante}",
                contentDescription = partidoDTO.equipoVisitanteNombre,
                modifier = Modifier.size(80.dp).clip(CircleShape),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(Modifier.height(40.dp))

    }
}

// --------------------------------------------------------------------------------

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
                val isCurrent = i == currentQuarter
                Text(
                    text = "Q$i 0",
                    color = if (isCurrent) PrimaryOrange else LightGrey,
                    fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                if (i < 4) Text("|", color = LightGrey, fontSize = 16.sp)
            }
        }
    }
}