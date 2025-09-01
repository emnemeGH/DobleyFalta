package com.parana.dobleyfalta.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InicioScreen(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Sección Partido
        PartidosSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Sección Tabla de posiciones
        TablaDePosicionesSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Sección Noticias en carrusel
        NoticiasSection()
    }
}

@Composable
fun NoticiasSection() {
    TODO("Not yet implemented")
}

@Composable
fun TablaDePosicionesSection() {
    TODO("Not yet implemented")
}

@Composable
fun PartidosSection() {
    TODO("Not yet implemented")
}
