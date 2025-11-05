package com.parana.dobleyfalta.tabla

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.parana.dobleyfalta.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.DarkBlue
import com.parana.dobleyfalta.DarkGrey
import com.parana.dobleyfalta.noticias.LightGrey

data class EquipoTabla(
    val id: Int,
    val posicion: Int,
    val nombre: String,
    val escudoUrl: Int,
    val puntos: Int,
    val pj: Int,
    val pg: Int,
    val pp: Int,
    val pf: Int,
    val pc: Int,
)

val equiposTabla = listOf(
    EquipoTabla(1, 1, "Paracao", R.drawable.escudo_paracao, 53, 38, 15, 23, 3018, 3125),
    EquipoTabla(2, 2, "Rowing", R.drawable.escudo_rowing, 52, 38, 14, 24, 3237, 3348),
    EquipoTabla(3, 3, "CAE", R.drawable.escudo_cae, 51, 38, 13, 25, 3199, 3286),
    EquipoTabla(4, 4, "Ciclista", R.drawable.escudo_rowing, 48,38, 10, 28, 2723, 3099),
    EquipoTabla(5, 5, "Quique", R.drawable.escudo_cae, 44, 28, 6, 32, 2724, 3216)
)

@Composable
fun TablaScreen (navController: NavController ){
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val White = colorResource(id = R.color.white)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "TABLA DE POSICIONES TORNEO",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Desliza la tabla para ver más información",
            fontSize = 13.sp,
            color = White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            textAlign = TextAlign.Center
        )

        TablaLiga(nombreLiga = "LIGA A", equipos = equiposTabla)
        TablaLiga(nombreLiga = "LIGA B", equipos = equiposTabla)

        Spacer(modifier = Modifier.height(80.dp))

    }
}


@Composable
fun TablaHeader(text: String, modifier: Modifier) {
    Text(
        text = text,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(4.dp) .fillMaxWidth(),
        fontSize = 14.sp
    )
}

@Composable
fun TablaLiga(nombreLiga: String, equipos: List<EquipoTabla>) {
    val DarkGrey = Color(0xFF1A375E)

    Text(
        text = nombreLiga,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp, bottom = 10.dp),
        textAlign = TextAlign.Center
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.Black),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                // Encabezado
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkGrey)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TablaHeader("N°", Modifier.width(50.dp))
                    TablaHeader("Equipo", Modifier.width(150.dp))
                    TablaHeader("Puntos", Modifier.width(65.dp))
                    TablaHeader("P.J", Modifier.width(50.dp))
                    TablaHeader("P.G", Modifier.width(50.dp))
                    TablaHeader("P.P", Modifier.width(60.dp))
                    TablaHeader("P.F", Modifier.width(70.dp))
                    TablaHeader("P.C", Modifier.width(55.dp))
                }

                // Lista de equipos
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(207.dp)
                ) {
                    items(equipos) { equipo ->
                        EquipoFila(equipo)
                    }
                }
            }
        }
    }
}

@Composable
fun EquipoFila(equipo: EquipoTabla) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGrey)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CeldaEquipo(equipo.posicion.toString(), Modifier.width(56.dp) .padding(start = 10.dp))
        Row(modifier = Modifier.width(160.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = equipo.escudoUrl),
                contentDescription = equipo.nombre,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(equipo.nombre, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black)
        }
        CeldaEquipo(equipo.puntos.toString(), Modifier.width(60.dp))
        CeldaEquipo(equipo.pj.toString(), Modifier.width(50.dp))
        CeldaEquipo(equipo.pg.toString(), Modifier.width(50.dp))
        CeldaEquipo(equipo.pp.toString(), Modifier.width(50.dp))
        CeldaEquipo(equipo.pf.toString(), Modifier.width(70.dp))
        CeldaEquipo(equipo.pc.toString(), Modifier.width(70.dp))

    }
    HorizontalDivider(
        thickness = 1.dp
    )
}

@Composable
fun CeldaEquipo(texto: String, modifier: Modifier) {
    Text(
        text = texto,
        fontSize = 14.sp,
        modifier = modifier.padding(4.dp),
        color = Color.Black
    )
}