package com.parana.dobleyfalta.jornadas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.MainViewModel
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.noticias.PrimaryOrange

// Colores
val CardBackground = Color(0xFF1A375E)
val TextWhite = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JornadasPorLigaScreen(navController: NavController, mainViewModel: MainViewModel) {
    var selectedLiga by remember { mutableStateOf<String?>(null) }
    val rol = mainViewModel.rolUsuario.value

    Scaffold(
        containerColor = DarkBlue,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = selectedLiga ?: "Selecciona una Liga",
                        color = TextWhite
                    )
                },
                navigationIcon = {
                    if (selectedLiga != null) {
                        IconButton(onClick = { selectedLiga = null }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = TextWhite
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkBlue
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedLiga == null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    listOf("Liga A", "Liga B").forEach { liga ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedLiga = liga },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = CardBackground)
                        ) {
                            Text(
                                text = liga,
                                color = TextWhite,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(24.dp)
                            )
                        }
                    }

                    // 🔹 Mostrar botones solo si es empleado
                    if (rol == "empleado") {
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { navController.navigate("crear_liga") },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                            shape = RoundedCornerShape(24.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = "Crear Liga",
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            } else {
                // Vista de selección de jornadas
                Text(
                    text = "Selecciona una jornada",
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items((1..10).toList()) { jornadaNumero ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("jornadas_screen/$jornadaNumero")
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = CardBackground)
                        ) {
                            Text(
                                text = "Jornada $jornadaNumero",
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                // 🔹 Botón especial visible solo para empleados
                if (rol == "empleado") {
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { /* Lógica de agregar jornada */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("➕ Agregar jornada")
                    }
                }
            }
        }
    }
}
