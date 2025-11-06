package com.parana.dobleyfalta.equipos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.R

data class Equipo(
    val id: Int,
    val nombre: String,
    val escudoUrl: Int
)

val CardBackground = Color(0xFF1A375E)
val PrimaryOrange = Color(0xFFFF6600)
val LightGrey = Color(0xFFA0B3C4)

@Composable
fun EquiposScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)

    val equipos = listOf(
        Equipo(1, "Paracao", R.drawable.escudo_paracao),
        Equipo(2, "Rowing", R.drawable.escudo_rowing),
        Equipo(3, "CAE", R.drawable.escudo_cae),
        Equipo(4, "Ciclista", R.drawable.escudo_ciclista),
        Equipo(5, "Olimpia", R.drawable.escudo_olimpia),
        Equipo(6, "Echagüe", R.drawable.escudo_echague),
        Equipo(7, "Bancario", R.drawable.escudo_bancario)
    )


    var listaEquipos by remember { mutableStateOf(equipos) }
    var mostrarConfirmacionBorrado by remember { mutableStateOf(false) }
    var equipoAEliminar by remember { mutableStateOf<Equipo?>(null) }

    val listaEquiposOrdenada = remember(listaEquipos) {
        listaEquipos.sortedBy { it.nombre }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Equipos",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Button(
                onClick = { navController.navigate("crear_equipo") },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Agregar Equipo",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(listaEquiposOrdenada) { equipo ->
                EquipoGridCard(
                    equipo = equipo,
                    alEditarClick = {
                        navController.navigate("editar_equipo")
                    },
                    alBorrarClick = {
                        equipoAEliminar = equipo
                        mostrarConfirmacionBorrado = true
                    },
                    alHacerClick = { navController.navigate("detalles/${equipo.id}") }
                )
            }
        }
    }

    if (mostrarConfirmacionBorrado && equipoAEliminar != null) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionBorrado = false },
            title = {
                Text("Confirmar eliminación", fontWeight = FontWeight.Bold, color = Color.White)
            },
            text = {
                Text("¿Seguro que quieres eliminar este equipo?", color = LightGrey)
            },
            confirmButton = {
                Button(
                    onClick = {
                        equipoAEliminar?.let { equipo ->
                            listaEquipos = listaEquipos.filter { it.id != equipo.id }
                        }
                        mostrarConfirmacionBorrado = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.testTag("confirmarEliminar")
                ) {
                    Text("Borrar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacionBorrado = false }) {
                    Text("Cancelar", color = PrimaryOrange)
                }
            },
            containerColor = CardBackground,
            shape = RoundedCornerShape(16.dp)
        )
    }
}


@Composable
fun EquipoGridCard(
    equipo: Equipo,
    alEditarClick: () -> Unit,
    alBorrarClick: () -> Unit,
    alHacerClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp),
        onClick = alHacerClick,
        modifier = Modifier.testTag("equipoCard")
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = equipo.escudoUrl),
                contentDescription = "Escudo de ${equipo.nombre}",
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = equipo.nombre,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = alEditarClick,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("editarEquipo")
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Editar equipo",
                        tint = colorResource(id = R.color.blue_edit),
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = alBorrarClick,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("eliminarEquipo")
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "Eliminar equipo",
                        tint = colorResource(id = R.color.red_delete),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

//Glosario

//LazyVerticalGrid
// es un composable que te permite mostrar una lista de elementos en forma de rejilla (grid) de forma lazy
//Sintaxis Basica:
//LazyVerticalGrid(
//    columns = GridCells.Fixed(2), // cantidad de columnas
//    verticalArrangement = Arrangement.spacedBy(8.dp), // espacio entre filas
//    horizontalArrangement = Arrangement.spacedBy(8.dp), // espacio entre columnas
//    contentPadding = PaddingValues(8.dp) // padding alrededor de la grilla
//) {
//    items(lista) { item ->
//        // Aca va el contenido de cada celda
//    }
//}