package com.parana.dobleyfalta.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.parana.dobleyfalta.retrofit.models.ligas.LigaModel
import com.parana.dobleyfalta.retrofit.viewmodels.ligas.LigasViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JornadasPorLigaScreen(navController: NavController) {

    // ViewModel de Ligas
    val ligasViewModel: LigasViewModel = viewModel()
    val ligas by ligasViewModel.ligas.collectAsState()
    val loading by ligasViewModel.loading.collectAsState()
    val error by ligasViewModel.error.collectAsState()

    // Estado: liga seleccionada
    var selectedLiga by remember { mutableStateOf<LigaModel?>(null) }
    val scope = rememberCoroutineScope()

    // Cargar ligas al entrar
    LaunchedEffect(Unit) {
        ligasViewModel.cargarLigas()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    if (selectedLiga == null) "Selecciona una Liga"
                    else "Ligas → ${selectedLiga?.nombre}"
                )
            })
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            when {
                loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error ?: "Error desconocido",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { ligasViewModel.cargarLigas() }) {
                            Text("Reintentar")
                        }
                    }
                }

                selectedLiga == null -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(ligas) { liga ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedLiga = liga
                                        // Próximo paso: cargar jornadas de esta liga
                                        scope.launch {
                                            // viewModelJornada.cargarJornadasPorLiga(liga.idLiga)
                                        }
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = liga.nombre,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = "Año: ${liga.anio}",
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = "Inicio: ${liga.fechaInicio}",
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Text(
                                        text = "Fin: ${liga.fechaFin}",
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Jornadas de ${selectedLiga?.nombre}",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(8.dp)
                        )
                        Spacer(Modifier.height(8.dp))

                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(selectedLiga?.jornadas ?: emptyList()) { jornada ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Column(Modifier.padding(16.dp)) {
                                        Text(
                                            "Jornada Nº ${jornada.numero}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text("Inicio: ${jornada.fechaInicio}")
                                        Text("Fin: ${jornada.fechaFin}")
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { selectedLiga = null },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Volver a Ligas")
                        }
                    }
                }
            }
        }
    }
}