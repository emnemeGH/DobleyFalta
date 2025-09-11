package com.parana.dobleyfalta.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.MainViewModel
import com.parana.dobleyfalta.R

@Composable
fun HomeScreen(navController: NavController, mainViewModel: MainViewModel){
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val White = colorResource(id = R.color.white)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "HOME",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        /*
        // Sección Partido
        PartidosSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Sección Tabla de posiciones
        TablaDePosicionesSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Sección Noticias en carrusel
        NoticiasSection()
        */

    }
}
/*
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
*/