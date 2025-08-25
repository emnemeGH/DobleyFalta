package com.parana.dobleyfalta.partidos

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Definición de colores
val DarkBlue = Color(0xFF102B4E)
val PrimaryOrange = Color(0xFFFF6600)
val DarkGrey = Color(0xFF1A375E)
val LightGrey = Color(0xFFA0B3C4)
val LiveGreen = Color(0xFF50C878)

// Modelo de Partido actualizado para incluir más detalles
data class Partido(
    val id: Int,
    val equipo1: String,
    val equipo2: String,
    val escudo1: String,
    val escudo2: String,
    val score1: Int?,
    val score2: Int?,
    val status: String, // e.g., "Terminado", "Próximo", "En curso"
    val liga: String,
    val categoria: String,
    val genero: String,
    val quarterScores1: List<Int>,
    val quarterScores2: List<Int>,
    val fecha: String,
    val hora: String,
    val estadio: String
)

@Composable
fun JornadasScreen(navController: NavController) {
    var jornada by remember { mutableStateOf(1) }

    // Datos de partidos de ejemplo (mock data)
    val jornadas = mapOf(
        1 to listOf(
            Partido(
                id = 1,
                equipo1 = "ROWING",
                equipo2 = "CAE",
                escudo1 = "https://placehold.co/60x60/FFFFFF/1A375E?text=SR",
                escudo2 = "https://placehold.co/60x60/FFFFFF/1A375E?text=EQ",
                score1 = listOf(19, 9, 15, 18).sum(),
                score2 = listOf(24, 20, 23, 27).sum(),
                status = "Terminado",
                liga = "FEDERACION DE BALONCESTO",
                categoria = "ASB - U21",
                genero = "MASCULINO",
                quarterScores1 = listOf(19, 9, 15, 18),
                quarterScores2 = listOf(24, 20, 23, 27),
                fecha = "05/04/2024",
                hora = "21:30",
                estadio = "EL GIGANTE DE CALLE LAS HERAS"
            ),
            Partido(
                id = 2,
                equipo1 = "EQUIPO A",
                equipo2 = "EQUIPO B",
                escudo1 = "https://placehold.co/60x60/FFFFFF/1A375E?text=A",
                escudo2 = "https://placehold.co/60x60/FFFFFF/1A375E?text=B",
                score1 = 0,
                score2 = 0,
                status = "Próximo",
                liga = "LIGA REGIONAL",
                categoria = "MAYORES",
                genero = "FEMENINO",
                quarterScores1 = emptyList(),
                quarterScores2 = emptyList(),
                fecha = "22/04/2024",
                hora = "20:00",
                estadio = "ESTADIO CENTRAL"
            )
        ),
        2 to listOf(
            Partido(
                id = 3,
                equipo1 = "CAMIONEROS",
                equipo2 = "DEPORTIVO",
                escudo1 = "https://placehold.co/60x60/FFFFFF/1A375E?text=C",
                escudo2 = "https://placehold.co/60x60/FFFFFF/1A375E?text=D",
                score1 = listOf(20, 15, 25, 20).sum(),
                score2 = listOf(18, 19, 19, 19).sum(),
                status = "Terminado",
                liga = "LIGA REGIONAL",
                categoria = "U18",
                genero = "MASCULINO",
                quarterScores1 = listOf(20, 15, 25, 20),
                quarterScores2 = listOf(18, 19, 19, 19),
                fecha = "08/04/2024",
                hora = "19:00",
                estadio = "POLIDEPORTIVO"
            ),
            Partido(
                id = 5,
                equipo1 = "DEPORTIVO",
                equipo2 = "CENTRO",
                escudo1 = "https://placehold.co/60x60/FFFFFF/1A375E?text=DEP",
                escudo2 = "https://placehold.co/60x60/FFFFFF/1A375E?text=CEN",
                score1 = listOf(5).sum(),
                score2 = listOf(10).sum(),
                status = "En Vivo",
                liga = "LIGA NACIONAL",
                categoria = "U16",
                genero = "FEMENINO",
                quarterScores1 = listOf(5),
                quarterScores2 = listOf(10),
                fecha = "08/04/2024",
                hora = "21:00",
                estadio = "ESTADIO MUNICIPAL"
            )
        ),
        3 to listOf(
            Partido(
                id = 4,
                equipo1 = "DEFENSORES",
                equipo2 = "CLUB NORTE",
                escudo1 = "https://placehold.co/60x60/FFFFFF/1A375E?text=DF",
                escudo2 = "https://placehold.co/60x60/FFFFFF/1A375E?text=CN",
                score1 = 0,
                score2 = 0,
                status = "Próximo",
                liga = "TORNEO AMISTOSO",
                categoria = "MAYORES",
                genero = "MASCULINO",
                quarterScores1 = emptyList(),
                quarterScores2 = emptyList(),
                fecha = "12/04/2024",
                hora = "20:30",
                estadio = "ESTADIO NORTE"
            )
        )
    )

    // Formateador para convertir el string de fecha a un objeto de fecha
    val formatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

    // Función para convertir la fecha del partido a un objeto LocalDate
    fun Partido.getLocalDate(): LocalDate? {
        return try {
            LocalDate.parse(this.fecha, formatter)
        } catch (e: Exception) {
            null
        }
    }

    // Ordenar los partidos por fecha de forma descendente (los más nuevos primero)
    val sortedJornadas = remember(jornadas) {
        jornadas.mapValues { (_, partidos) ->
            partidos.sortedByDescending { it.getLocalDate() }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(16.dp)
    ) {
        // Header con título y flechas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { if (jornada > 1) jornada-- },
                enabled = jornada > 1
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = "Jornada Anterior",
                    tint = if (jornada > 1) PrimaryOrange else Color.Gray
                )
            }
            Text(
                text = "Jornada $jornada",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            IconButton(
                onClick = { if (jornada < jornadas.keys.max()) jornada++ },
                enabled = jornada < (jornadas.keys.maxOrNull() ?: jornada)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = "Próxima Jornada",
                    tint = if (jornada < (jornadas.keys.maxOrNull() ?: jornada)) PrimaryOrange else Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de partidos de la jornada ya ordenada
        sortedJornadas[jornada]?.forEach { partido ->
            PartidoCard(partido = partido)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun PartidoCard(partido: Partido) {
    val cardBackgroundColor = DarkGrey
    val textLightColor = LightGrey
    val textStrongColor = Color.White
    val winnerScoreColor = PrimaryOrange

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header con el estado del partido y la estrella de favorito. El color cambia según el status.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(
                        color = when (partido.status) {
                            "Próximo" -> PrimaryOrange
                            "Terminado" -> DarkGrey
                            "En Vivo" -> LiveGreen
                            else -> PrimaryOrange // Color por defecto
                        },
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.silbato_logo),
                    contentDescription = "Status Icon",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = partido.status,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // Contenido principal de la tarjeta
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Equipo 1
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.escudo_rowing),
                        contentDescription = partido.equipo1,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = partido.equipo1,
                        color = textStrongColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                // Detalles del partido y scores
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(2f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = partido.score1.toString(),
                            color = if ((partido.score1 ?: 0) > (partido.score2 ?: 0)) winnerScoreColor else textLightColor,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "-",
                            color = textLightColor,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = partido.score2.toString(),
                            color = if ((partido.score2 ?: 0) > (partido.score1 ?: 0)) winnerScoreColor else textLightColor,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = partido.liga,
                        color = textLightColor,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${partido.categoria} - ${partido.genero}",
                        color = textLightColor,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // Equipo 2
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.escudo_cae),
                        contentDescription = partido.equipo2,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = partido.equipo2,
                        color = textStrongColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Detalles de cuartos y fecha
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Scores por cuartos
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.escudo_rowing),
                            contentDescription = partido.equipo1,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        partido.quarterScores1.forEach { score ->
                            Text(text = score.toString(), color = textStrongColor)
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.escudo_cae),
                            contentDescription = partido.equipo2,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        partido.quarterScores2.forEach { score ->
                            Text(text = score.toString(), color = textStrongColor)
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }

                // Fecha y hora
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Viernes", color = textStrongColor, fontWeight = FontWeight.Bold)
                    Text(text = partido.fecha, color = textStrongColor)
                    Text(text = partido.hora, color = textStrongColor)
                }


            }

            // Ubicación del estadio
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkGrey)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.stadium_logo),
                    contentDescription = "Ubicación",
                    tint = textLightColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = partido.estadio,
                    color = textLightColor,
                    fontSize = 12.sp
                )
            }
        }
    }
}