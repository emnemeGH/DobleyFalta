package com.parana.dobleyfalta.equipos

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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

// Pantalla de listado de equipos (requiere NavController en la app real)
@Composable
fun EquiposListScreen(navController: NavController) {
    val equipos = listOf(
        Equipo(1, "Paracao", R.drawable.escudo_paracao),
        Equipo(2, "Rowing", R.drawable.escudo_rowing),
        Equipo(3, "CAE", R.drawable.escudo_cae)
    )


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp)
    ) {
        items(equipos) { equipo ->
            EquipoItem(equipo) {
                navController.navigate("detalles/${equipo.id}")
            }
        }
    }
}

// Elemento individual de la lista
@Composable
fun EquipoItem(equipo: Equipo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF2E2C1) // Cream/Beige
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = equipo.escudoUrl),
                contentDescription = "Escudo de ${equipo.nombre}",
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 12.dp)
            )
            Text(
                text = equipo.nombre,
                fontSize = 22.sp,
                color = Color.Black // Nombres en negro
            )
        }
    }
}
