package com.parana.dobleyfalta.jornadas.JornadasPorLigaScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.parana.dobleyfalta.retrofit.models.ligas.LigaModel
import com.parana.dobleyfalta.retrofit.viewmodels.ligas.LigasViewModel
import java.text.SimpleDateFormat
import java.util.Locale

val DarkBlue = Color(0xFF102B4E)
val PrimaryOrange = Color(0xFFFF6600)
val DarkGrey = Color(0xFF1A375E)
val LightGrey = Color(0xFFA0B3C4)

fun formatearFecha(fecha: String?): String {
    if (fecha.isNullOrEmpty()) return "-"
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = parser.parse(fecha.substring(0, 10))
        formatter.format(date!!)
    } catch (e: Exception) {
        fecha
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JornadasPorLigaScreen(navController: NavController) {

    val ligasViewModel: LigasViewModel = viewModel()
    val ligas by ligasViewModel.ligas.collectAsState()
    val loading by ligasViewModel.loading.collectAsState()
    val error by ligasViewModel.error.collectAsState()

    // Restauramos el estado para seleccionar la liga
    var selectedLiga by remember { mutableStateOf<LigaModel?>(null) }

    LaunchedEffect(Unit) {
        ligasViewModel.cargarLigas()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (selectedLiga == null)
                            "Selecciona una Liga"
                        else "Ligas → ${selectedLiga?.nombre}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkGrey)
            )
        },
        containerColor = DarkBlue
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlue)
                .padding(padding)
                .padding(16.dp)
        ) {
            when {
                loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = PrimaryOrange
                    )
                }

                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error ?: "Error desconocido",
                            color = Color.White
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { ligasViewModel.cargarLigas() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryOrange,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }

                selectedLiga == null -> {
                    // Muestra la lista de Ligas para selección
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(ligas) { liga ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        // Selecciona la liga y muestra sus jornadas
                                        selectedLiga = liga
                                    },
                                colors = CardDefaults.cardColors(containerColor = DarkGrey),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = liga.nombre,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                                    )
                                }
                            }
                        }
                    }
                }

                else -> {
                    // Muestra las Jornadas de la liga seleccionada
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(selectedLiga?.jornadas ?: emptyList()) { jornada ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            // NAVEGACIÓN MODIFICADA: Ahora solo pasa ligaId y jornadaNumero
                                            val ligaId = selectedLiga?.idLiga ?: 0
                                            navController.navigate("jornadas_screen/$ligaId/${jornada.numero}")
                                        },
                                    colors = CardDefaults.cardColors(containerColor = DarkGrey),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(70.dp)
                                            .padding(horizontal = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Jornada Nº ${jornada.numero}",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 18.sp
                                        )

                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = formatearFecha(jornada.fechaInicio.toString()),
                                                color = LightGrey,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = formatearFecha(jornada.fechaFin.toString()),
                                                color = LightGrey,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { selectedLiga = null },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = PrimaryOrange
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "VOLVER A LIGAS",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}