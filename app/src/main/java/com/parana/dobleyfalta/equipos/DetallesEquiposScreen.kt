package com.parana.dobleyfalta.equipos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.parana.dobleyfalta.R

// Renombramos el data class para evitar el conflicto de nombres con el Composable
data class EquipoDetalles(
    val nombre: String,
    val escudoUrl: Int,
    val descripcion: String,
    val ubicacion: String,
    val id: Int,
    val lat: Double,
    val lng: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallesEquiposScreen(navController: NavController, equipoId: Int) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val CardBackground = Color(0xFF1A375E)
    val PrimaryOrange = Color(0xFFFF6600)
    val TextWhite = Color.White

    val detalles = listOf(
        EquipoDetalles(
            "Paracao",
            R.drawable.escudo_paracao,
            "En el club están comenzando todas las actividades. Ahora en febrero comenzaron todos y ya el fin de semana tenemos Liguilla de básquet femenino y masculino. El sábado tenemos una velada de boxeo, donde hay dos peleas profesionales y seis amateurs. Invitamos a todos que se acerquen, que es un espectáculo muy bonito. La idea es hacerlo al aire libre, esperemos que el tiempo nos ayude. Sino lo haremos en el gimnasio, donde tenemos espacio suficiente”, detalló sobre lo que sucederá en los próximos días.",
            "Juan Báez 745",
            1,
            -31.7700219, -60.5329188
        ),
        EquipoDetalles(
            "Rowing",
            R.drawable.escudo_rowing,
            "El Paraná Rowing Club es un club deportivo ubicado en la Ciudad de Paraná, capital de la provincia de Entre Ríos, en Argentina.Fue fundado en 1917 con el fin de practicar remo y natación, pero luego amplió sus actividades a otros deportes como rugby, básquet, hockey Sobre césped, pelota paleta, vóley, esgrima y tenis, entre otros.",
            "Av. Laurencena 933",
            2,
            -31.718204, -60.5320849
        ),
        EquipoDetalles(
            "CAE",
            R.drawable.escudo_cae,
            "El Club Atlético Estudiantes es una institución deportiva de la ciudad Argentina de Paraná en la Provincia de Entre Ríos.El Club Atlético Estudiantes (CAE) fue fundado el 5 de mayo de 1905. \n \n El club nació para jugar al fútbol y su primer nombre fue “Estudiantes Football Club”, hasta mediados de la década del 30 que se dejó de practicar dicho deporte y por ende el club fue renombrado como en la actualidad y su principal deporte actualmente es el rugby, por el cual es reconocido nacionalmente",
            "Los Vascos 729",
            3,
            -31.7199324, -60.5372966
        )
    )

    val equipo = detalles.find { it.id == equipoId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = DarkBlue
    ) {
        if (equipo != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()), // Permite el scroll vertical
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = equipo.escudoUrl),
                    contentDescription = "Escudo de ${equipo.nombre}",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(top = 50.dp)
                )

                Text(
                    text = equipo.nombre,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardBackground
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = equipo.descripcion,
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        color = TextWhite.copy(alpha = 0.95f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.85f)), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardBackground
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.85f)), RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Direccion: ${equipo.ubicacion}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.testTag("textoUbicacion")
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        val paranaLatLng = LatLng(equipo.lat, equipo.lng)
                        val cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(paranaLatLng, 15f)
                        }

                        GoogleMap(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            cameraPositionState = cameraPositionState
                        ) {
                            Marker(
                                state = MarkerState(position = paranaLatLng),
                                title = equipo.nombre,
                                snippet = equipo.ubicacion
                            )
                        }
                    }
                }
            }
        } else {
            // Maneja el caso en que el equipo no se encuentre
            Text(
                text = "Equipo no encontrado",
                color = TextWhite,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}
