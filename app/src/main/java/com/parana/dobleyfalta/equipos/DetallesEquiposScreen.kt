package com.parana.dobleyfalta.equipos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.R

data class DetallesEquiposScreen(
    val nombre: String,
    val escudoUrl: Int,
    val descripcion: String,
    val ubicacion: String,
    val id: Int
)

@Composable
fun DetallesEquiposScreen(navController: NavController, equipoId: Int) {
    val detalles = listOf(
        DetallesEquiposScreen("Paracao", R.drawable.escudo_paracao, "En el club están comenzando todas las actividades. Ahora en febrero comenzaron todos y ya el fin de semana tenemos Liguilla de básquet femenino y masculino. El sábado tenemos una velada de boxeo, donde hay dos peleas profesionales y seis amateurs. Invitamos a todos que se acerquen, que es un espectáculo muy bonito. La idea es hacerlo al aire libre, esperemos que el tiempo nos ayude. Sino lo haremos en el gimnasio, donde tenemos espacio suficiente”, detalló sobre lo que sucederá en los próximos días.", "parana 1234", 1),

        DetallesEquiposScreen("Rowing", R.drawable.escudo_rowing, "El Paraná Rowing Club es un club deportivo ubicado en la Ciudad de Paraná, capital de la provincia de Entre Ríos, en Argentina.\n" +
                "\n" +
                "Fue fundado en 1917 con el fin de practicar remo y natación, pero luego amplió sus actividades a otros deportes como rugby, básquet, hockey Sobre césped, pelota paleta, vóley, esgrima y tenis, entre otros.\n" +
                "\n" +
                "Cuenta con cinco predios: la sede ubicada en el Parque Urquiza, una playa privada a la vera del río, una isla frente a la playa, el campo de deportes La Tortuguita, en la Avenida Circunvalación José Hernández y el anexo Yarará, en el este de la ciudad. Posee un estadio internacional de hockey sobre césped y en sus instalaciones se realiza anualmente el Seven de la República, el más importante de rugby 7 del país. En 2022 contaba con 8500 socios.El club ha obtenido gran cantidad de campeonatos argentinos y sudamericanos en remo, incluyendo dos representantes olímpicos", "parana 4567", 2),

        DetallesEquiposScreen("CAE", R.drawable.escudo_cae, "El Club Atlético Estudiantes es una institución deportiva de la ciudad Argentina de Paraná en la Provincia de Entre Ríos.\n" +
                "\n" +
                "El Club Atlético Estudiantes (CAE) fue fundado el 5 de mayo de 1905. El club nació para jugar al fútbol y su primer nombre fue “Estudiantes Football Club”, hasta mediados de la década del 30 que se dejó de practicar dicho deporte y por ende el club fue renombrado como en la actualidad y su principal deporte actualmente es el rugby, por el cual es reconocido nacionalmente.\n" +
                "\n" +
                "Desde 1921 cuenta con un lugar físico propio que es donde hoy se encuentra su sede central, en el Parque Urquiza.", "parana 7890", 3)
    )

    val equipo = detalles.find { it.id == equipoId }

    equipo?.let {
        DetallesEquiposItem(it)
    }
}


@Composable
fun DetallesEquiposItem(
    detalles: DetallesEquiposScreen,
    onClick: (() -> Unit)? = null
){
    Column(
        modifier = Modifier
            .fillMaxSize() // ocupa toda la pantalla
            .clickable { onClick?.invoke() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // centrado horizontal
        //verticalArrangement = Arrangement.Center // centrado vertical
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
    }
}
