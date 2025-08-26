package com.parana.dobleyfalta.equipos

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.R

// Modelo simple de Equipo
data class Equipo(
    val id: Int,
    val nombre: String,
    val escudoUrl: Int
)

@Composable
fun EquiposListScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val CardBackground = Color(0xFF1A375E)
    val TextWhite = Color(0xFFFFFFFF)

    val equipos = listOf(
        Equipo(1, "Paracao", R.drawable.escudo_paracao),
        Equipo(2, "Rowing", R.drawable.escudo_rowing),
        Equipo(3, "CAE", R.drawable.escudo_cae),
        Equipo(4, "Ciclista", R.drawable.escudo_cae),
        Equipo(5, "Quique", R.drawable.escudo_rowing)
    ).sortedBy { it.nombre } // orden alfabético

    Scaffold(
        containerColor = DarkBlue
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // dos columnas fijas
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Usa el padding proporcionado por el Scaffold
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(equipos) { equipo ->
                EquipoItem(
                    equipo = equipo,
                    cardBackground = CardBackground,
                    textColor = TextWhite
                ) {
                    navController.navigate("detalles/${equipo.id}")
                }
            }
        }
    }
}

@Composable
fun EquipoItem(
    equipo: Equipo,
    cardBackground: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBackground
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        )  {
            Image(
                painter = painterResource(id = equipo.escudoUrl),
                contentDescription = "Escudo de ${equipo.nombre}",
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 8.dp)
            )
            Text(
                text = equipo.nombre,
                fontSize = 20.sp,
                color = textColor
            )
        }
    }
}
