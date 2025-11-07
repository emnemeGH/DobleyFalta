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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.retrofit.ApiConstants.BASE_URL
import com.parana.dobleyfalta.retrofit.models.equipos.EquipoModel
import com.parana.dobleyfalta.retrofit.viewmodels.equipos.EquiposViewModel

val CardBackground = Color(0xFF1A375E)
val PrimaryOrange = Color(0xFFFF6600)
val LightGrey = Color(0xFFA0B3C4)

@Composable
fun EquiposScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)

    val viewModel = remember { EquiposViewModel() }
    val equipos by viewModel.equipos.collectAsState()
    val cargando by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var mostrarConfirmacionBorrado by remember { mutableStateOf(false) }
    var equipoAEliminar by remember { mutableStateOf<EquipoModel?>(null) }

    LaunchedEffect(Unit) {
        viewModel.cargarEquipos()
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

        when {
            cargando -> {
                Dialog(onDismissRequest = {}) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.White, shape = RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryOrange)
                    }
                }
            }

            error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(error ?: "Error desconocido", color = Color.Red)
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(equipos.sortedBy { it.nombre }) { equipo ->
                        EquipoCard(
                            equipo = equipo,
                            alEditarClick = {
                                navController.navigate("editar_equipo/${equipo.idEquipo}")
                            },
                            alBorrarClick = {
                                equipoAEliminar = equipo
                                mostrarConfirmacionBorrado = true
                            },
                            alHacerClick = {
                                navController.navigate("detalles/${equipo.idEquipo}")
                            }
                        )
                    }
                }
            }
        }

        if (mostrarConfirmacionBorrado && equipoAEliminar != null) {
            AlertDialog(
                onDismissRequest = { mostrarConfirmacionBorrado = false },
                title = {
                    Text(
                        "Confirmar eliminación",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                text = {
                    Text("¿Seguro que quieres eliminar este equipo?", color = LightGrey)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            equipoAEliminar?.let { equipo ->
                                viewModel.eliminarEquipo(equipo.idEquipo) {
                                    mostrarConfirmacionBorrado = false
                                }
                            }
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
}

@Composable
fun EquipoCard(
    equipo: EquipoModel,
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
            AsyncImage(
                model = "${BASE_URL}${equipo.logo}",
                contentDescription = "Logo de ${equipo.nombre}",
                modifier = Modifier.size(80.dp),
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

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = alEditarClick,
                    modifier = Modifier.size(36.dp)
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
                    modifier = Modifier.size(36.dp)
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

