package com.parana.dobleyfalta.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.parana.dobleyfalta.MainViewModel
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.jornadas.DarkGrey
import com.parana.dobleyfalta.jornadas.LightGrey
import com.parana.dobleyfalta.jornadas.LiveGreen
import com.parana.dobleyfalta.jornadas.Partido
import com.parana.dobleyfalta.jornadas.PrimaryOrange
import com.parana.dobleyfalta.noticias.getFechaPublicacionFormateada
import com.parana.dobleyfalta.retrofit.models.noticia.NoticiaApiModel
import com.parana.dobleyfalta.retrofit.repositories.NoticiasRepository
import com.parana.dobleyfalta.tabla.TablaLiga
import com.parana.dobleyfalta.tabla.equiposTabla
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime


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
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
    ) {

        //SECCIÓN DE PARTIDOS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Partidos",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = White,
                textAlign = TextAlign.Start
            )

            Text(
                text = "Ver más...",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = White,
                textAlign = TextAlign.End,
                modifier = Modifier.clickable { navController.navigate("jornadas_por_liga_screen")}
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
        ){
            PartidosCarousel()
        }

        //SECCIÓN DE TABLA DE POSICIONES
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Tablas de posiciones",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = White,
                textAlign = TextAlign.Start,
            )

            Text(
                text = "Ver más...",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = White,
                textAlign = TextAlign.End,
                modifier = Modifier.clickable { navController.navigate("tabla")}
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
        ){
            TablaLiga(nombreLiga = "LIGA A", equipos = equiposTabla)
        }

        //SECCIÓN DE NOTICIAS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Noticias",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = White,
                textAlign = TextAlign.Start
            )

            Text(
                text = "Ver más...",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = White,
                textAlign = TextAlign.End,
                modifier = Modifier.clickable { navController.navigate("noticias")}
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(800.dp)
        ){
            NoticiasSection(navController)
        }

//        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun PartidosCarousel() {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { partidosProximos.size })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        PartidoCardHome(partidosProximos[page])
    }
}

@Composable
fun PartidoCardHome(partido: Partido) {
    val cardBackgroundColor = DarkGrey
    val textLightColor = LightGrey
    val textStrongColor = Color.White
    val winnerScoreColor = PrimaryOrange

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            // Header (status)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(
                        color = when (partido.status) {
                            "Próximo" -> PrimaryOrange
                            "Terminado" -> DarkGrey
                            "En Vivo" -> LiveGreen
                            else -> PrimaryOrange
                        },
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.silbato_logo),
                    contentDescription = "Status Icon",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = partido.status,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // Contenido principal
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Equipo 1
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.escudo_rowing),
                        contentDescription = partido.equipo1,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = partido.equipo1,
                        color = textStrongColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                // VS + fecha/hora
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("VS", color = PrimaryOrange, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(partido.fecha, color = textLightColor, fontSize = 14.sp)
                    Text(partido.hora, color = textLightColor, fontSize = 14.sp)
                }

                // Equipo 2
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.escudo_cae),
                        contentDescription = partido.equipo2,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = partido.equipo2,
                        color = textStrongColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

val partidosProximos = listOf(
    Partido(
        id = 1,
        equipo1 = "ROWING",
        equipo2 = "CAE",
        escudo1 = R.drawable.escudo_rowing,
        escudo2 = R.drawable.escudo_cae,
        score1 = 0,
        score2 = 0,
        status = "Próximo",
        liga = "FEDERACION DE BALONCESTO",
        quarterScores1 = emptyList(),
        quarterScores2 = emptyList(),
        fecha = "22/09/2025",
        hora = "21:30",
        estadio = "EL GIGANTE DE CALLE LAS HERAS"
    ),
    Partido(
        id = 2,
        equipo1 = "CICLISTA",
        equipo2 = "OLIMPIA",
        escudo1 = R.drawable.escudo_ciclista,
        escudo2 = R.drawable.escudo_olimpia,
        score1 = 0,
        score2 = 0,
        status = "Próximo",
        liga = "LIGA REGIONAL",
        quarterScores1 = emptyList(),
        quarterScores2 = emptyList(),
        fecha = "25/09/2025",
        hora = "20:00",
        estadio = "ESTADIO CENTRAL"
    )
)

@Composable
fun NoticiasSection(navController: NavController) {

    val repository = remember { NoticiasRepository() }
    val scope = rememberCoroutineScope()

    var listaNoticias by remember { mutableStateOf(listOf<NoticiaApiModel>()) }
    var cargando by remember { mutableStateOf(true) }
    var errorCarga by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                listaNoticias = repository.obtenerNoticias()
                errorCarga = false
            } catch (e: Exception) {
                e.printStackTrace()
                errorCarga = true
            } finally {
                cargando = false
            }
        }
    }

    // Ordenar las noticias por fecha (más recientes primero)
    val listaNoticiasOrdenadas = listaNoticias.sortedByDescending {
        try {
            LocalDateTime.parse(it.fechaPublicacion).toLocalDate()
        } catch (e: Exception) {
            LocalDate.MIN
        }
    }

    val noticiasHome = listaNoticiasOrdenadas.take(3)

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (cargando) {
            Text("Cargando noticias...", color = Color.White)
        } else if (errorCarga) {
            Text("Error al cargar noticias", color = Color.Red)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(noticiasHome) { noticia ->
                    NoticiaMiniCard(
                        noticia = noticia,
                        alHacerClick = { navController.navigate("detalle_noticia/${noticia.idNoticia}") }
                    )
                }
            }
        }
    }
}

@Composable
fun NoticiaMiniCard(
    noticia: NoticiaApiModel,
    alHacerClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // tamaño fijo de la card
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = alHacerClick),
        colors = CardDefaults.cardColors(containerColor = DarkGrey),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Imagen arriba
            AsyncImage(
                model = noticia.imagen,
                contentDescription = "Imagen de la noticia",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            // Texto abajo
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = noticia.titulo,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2
                )
                Text(
                    text = noticia.contenido,
                    color = LightGrey,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp),
                    maxLines = 4
                )
                Text(
                    text = noticia.getFechaPublicacionFormateada(),
                    color = LightGrey,
                    fontSize = 12.sp
                )
            }
        }
    }
}
