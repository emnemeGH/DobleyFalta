package com.parana.dobleyfalta.tabla

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.parana.dobleyfalta.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class EquipoTabla(
    val id: Int,
    val posicion: Int,
    val nombre: String,
    val escudoUrl: Int,
    val pj: Int,
    val pg: Int,
    val pp: Int,
    val pf: Int,
    val pc: Int,
    val puntos: Int,
    val victoriasPorcentaje: Double
)

@Composable
fun TablaScreen (){
    val equiposTabla = listOf(
        EquipoTabla(1, 1, "Paracao", R.drawable.escudo_paracao, 38, 15, 23, 3018, 3125, 53, 0.394),
        EquipoTabla(2, 2, "Rowing", R.drawable.escudo_rowing, 38, 14, 24, 3237, 3348, 52, 0.368),
        EquipoTabla(3, 3, "CAE", R.drawable.escudo_cae, 38, 13, 25, 3199, 3286, 51, 0.342),
        EquipoTabla(4, 4, "Ciclista", R.drawable.escudo_rowing,38, 10, 28, 2723, 3099, 48, 0.263),
        EquipoTabla(5, 5, "Quique", R.drawable.escudo_cae, 28, 6, 32, 2724, 3216, 44, 0.157)
    )

    var listaEquiposTabla by remember { mutableStateOf(equiposTabla) }
    val listaEquiposTablaOrdenada = remember(listaEquiposTabla) {
        listaEquiposTabla.sortedBy { it.nombre }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "TABLA DE POSICIONES TORNEO 2024/25",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Encabezado de tabla
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TablaHeader("N°", 0.1f)
            TablaHeader("Nombre", 0.3f)
            TablaHeader("P.J", 0.1f)
            TablaHeader("P.G", 0.1f)
            TablaHeader("P.P", 0.1f)
            TablaHeader("Puntos", 0.1f)
        }

        // Lista de equipos
        LazyColumn {
            items(equiposTabla) { equipo ->
                EquipoFila(equipo)
            }
        }
    }
}

@Composable
fun TablaHeader(text: String, weight: Float) {
    Text(
        text = text,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        //modifier = Modifier.weight(weight),
        fontSize = 12.sp
    )
}

@Composable
fun EquipoFila(equipo: EquipoTabla) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7F7))
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(equipo.posicion.toString(), modifier = Modifier.weight(0.1f))
        Row(modifier = Modifier.weight(0.3f), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = equipo.escudoUrl),
                contentDescription = equipo.nombre,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(equipo.nombre, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
        }
        Text(equipo.pj.toString(), modifier = Modifier.weight(0.1f))
        Text(equipo.pg.toString(), modifier = Modifier.weight(0.1f))
        Text(equipo.pp.toString(), modifier = Modifier.weight(0.1f))
        Text(equipo.puntos.toString(), modifier = Modifier.weight(0.1f))
    }
}