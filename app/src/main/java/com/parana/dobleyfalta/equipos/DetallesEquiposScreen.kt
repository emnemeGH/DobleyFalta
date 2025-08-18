package com.parana.dobleyfalta.equipos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.parana.dobleyfalta.AppTopBar
import com.parana.dobleyfalta.R

data class DetallesEquiposScreen(
    val nombre: String,
    val escudoUrl: Int,
    val descripcion: String,
    val ubicacion: String,
    val id: Int,
    val lat: Double,
    val lng: Double
)

@Composable
fun DetallesEquiposScreen(navController: NavController, equipoId: Int) {
    val detalles = listOf(
        DetallesEquiposScreen(
            "Paracao",
            R.drawable.escudo_paracao,
            "En el club están comenzando todas las actividades. Ahora en febrero comenzaron todos y ya el fin de semana tenemos Liguilla de básquet femenino y masculino. El sábado tenemos una velada de boxeo, donde hay dos peleas profesionales y seis amateurs. Invitamos a todos que se acerquen, que es un espectáculo muy bonito. La idea es hacerlo al aire libre, esperemos que el tiempo nos ayude. Sino lo haremos en el gimnasio, donde tenemos espacio suficiente”, detalló sobre lo que sucederá en los próximos días.",
            "parana 1234",
            1,
            -31.7700219,-60.5329188
        ),

        DetallesEquiposScreen(
            "Rowing",
            R.drawable.escudo_rowing,
            "El Paraná Rowing Club es un club deportivo ubicado en la Ciudad de Paraná, capital de la provincia de Entre Ríos, en Argentina.\n" +
                    "\n" +
                    "Fue fundado en 1917 con el fin de practicar remo y natación, pero luego amplió sus actividades a otros deportes como rugby, básquet, hockey Sobre césped, pelota paleta, vóley, esgrima y tenis, entre otros.",
            "parana 4567",
            2,
            -31.718204,-60.5320849
        ),

        DetallesEquiposScreen(
            "CAE",
            R.drawable.escudo_cae,
            "El Club Atlético Estudiantes es una institución deportiva de la ciudad Argentina de Paraná en la Provincia de Entre Ríos.\n" +
                    "\n" +
                    "El Club Atlético Estudiantes (CAE) fue fundado el 5 de mayo de 1905. El club nació para jugar al fútbol y su primer nombre fue “Estudiantes Football Club”, hasta mediados de la década del 30 que se dejó de practicar dicho deporte y por ende el club fue renombrado como en la actualidad y su principal deporte actualmente es el rugby, por el cual es reconocido nacionalmente",
            "parana 7890",
            3,
            -31.7199324,-60.5372966
        )
    )

    val equipo = detalles.find { it.id == equipoId }

    Scaffold(
        topBar = {
            AppTopBar(
                title = equipo?.nombre ?: "Detalles",
                onMenuClick = { route ->
                    when (route) {
                        "home" -> navController.navigate("home")
                        "equipos" -> navController.navigate("equipos")
                        "tabla" -> navController.navigate("tabla")
                        "noticias" -> navController.navigate("noticias")
                    }
                }
            )
        }
    ) { innerPadding ->
        equipo?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DetallesEquiposItem(it)
            }
        }
    }
}


@Composable
fun DetallesEquiposItem(
    detalles: DetallesEquiposScreen,
    onClick: (() -> Unit)? = null
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick?.invoke() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = detalles.escudoUrl),
            contentDescription = "Escudo de ${detalles.nombre}",
            modifier = Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = detalles.nombre,
            fontSize = 40.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = detalles.descripcion,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ubicación: ${detalles.ubicacion}",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mapa de Google
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            cameraPositionState = rememberCameraPositionState() {
                position = CameraPosition.fromLatLngZoom(
                    LatLng(detalles.lat, detalles.lng), 15f
                )
            }
        ) {
            Marker(
                state = MarkerState(position = LatLng(detalles.lat, detalles.lng)),
                title = detalles.nombre,
                snippet = detalles.ubicacion
            )
        }
    }
}

