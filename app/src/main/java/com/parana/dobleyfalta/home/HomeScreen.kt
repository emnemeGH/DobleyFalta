package com.parana.dobleyfalta.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import java.time.format.DateTimeFormatter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.parana.dobleyfalta.MainViewModel
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.jornadas.DarkGrey
import com.parana.dobleyfalta.jornadas.LightGrey
import com.parana.dobleyfalta.jornadas.LiveGreen
import com.parana.dobleyfalta.jornadas.PrimaryOrange
import com.parana.dobleyfalta.noticias.getFechaPublicacionFormateada
import com.parana.dobleyfalta.retrofit.ApiConstants.BASE_URL
import com.parana.dobleyfalta.retrofit.models.noticia.NoticiaApiModel
import com.parana.dobleyfalta.retrofit.models.partidos.PartidoDTOModel
import com.parana.dobleyfalta.retrofit.repositories.NoticiasRepository
import com.parana.dobleyfalta.retrofit.viewmodels.ligas.LigasViewModel
import com.parana.dobleyfalta.retrofit.viewmodels.partidos.PartidosViewModel
import com.parana.dobleyfalta.retrofit.viewmodels.tablas.TablaViewModel
import com.parana.dobleyfalta.tabla.TablaLiga
import com.parana.dobleyfalta.tabla.toEquipoTabla
//import com.parana.dobleyfalta.tabla.TablaLiga
//import com.parana.dobleyfalta.tabla.equiposTabla
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

    val partidosViewModel: PartidosViewModel = viewModel()
    val partidos by partidosViewModel.partidosDTO.collectAsState()
    val loading by partidosViewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        partidosViewModel.cargarTodosLosPartidos() // método que llame al backend
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .testTag("pantalla-home")
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
            .height(190.dp)
        ){
            when {
                loading -> {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(DarkGrey, shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryOrange)
                    }
                }
                else -> {
                    PartidosCarousel(partidos = partidos)
                }
            }
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
            TablaHome(idLiga = 1, nombreLiga = "LIGA A")
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
fun PartidosCarousel(partidos: List<PartidoDTOModel>) {
    val partidosProximos = partidos.filter { it.estadoPartido == "proximo" }
    if (partidosProximos.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay próximos partidos", color = Color.White)
        }
        return
    }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { partidosProximos.size })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        PartidoCardHome(partido = partidosProximos[page])
    }
}

@Composable
fun PartidoCardHome(partido: PartidoDTOModel) {
    val cardBackgroundColor = DarkGrey
    val textLightColor = LightGrey
    val textStrongColor = Color.White
    val winnerScoreColor = PrimaryOrange

    val fechaHora = partido.fecha?.let {
        try {
            LocalDateTime.parse(it)
        } catch (e: Exception) {
            null
        }
    }

    val fechaTexto = fechaHora?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: ""
    val horaTexto = fechaHora?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: ""

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
                        color = when (partido.estadoPartido) {
                            "proximo" -> PrimaryOrange
                            "terminado" -> DarkGrey
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
                    text = partido.estadoPartido.toString(),
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
                    AsyncImage(
                        model = "${BASE_URL}${partido.logoLocal}",
                        contentDescription = partido.equipoLocalNombre,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = partido.equipoLocalNombre ?: "Local",
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
                    Text(fechaTexto, color = textLightColor, fontSize = 14.sp)
                    Text(horaTexto, color = textLightColor, fontSize = 14.sp)
                }

                // Equipo 2
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = "${BASE_URL}${partido.logoVisitante}",
                        contentDescription = partido.equipoVisitanteNombre,
                        modifier = Modifier.size(60.dp).clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = partido.equipoVisitanteNombre ?: "Visitante",
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
            var cargandoImagen by remember { mutableStateOf(true) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = "${BASE_URL}${noticia.imagen}",
                    contentDescription = "Imagen de la noticia",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop,
                    onSuccess = { cargandoImagen = false },
                    onLoading = { cargandoImagen = true },
                    onError = { cargandoImagen = false }
                )

                if (cargandoImagen) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.White, shape = RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

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
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
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

@Composable
fun TablaHome(idLiga: Int, nombreLiga: String) {
    val tablasViewModel: TablaViewModel = viewModel()
    val tablas by tablasViewModel.tablas.collectAsState()
    val loading by tablasViewModel.loading.collectAsState()
    val error by tablasViewModel.error.collectAsState()

    // Carga la tabla solo una vez (cuando entra a la pantalla)
    LaunchedEffect(idLiga) {
        tablasViewModel.cargarTablaPorLiga(idLiga)
    }

    val equipos = tablas[idLiga]?.map { it.toEquipoTabla() } ?: emptyList()

    when {
        loading -> Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGrey, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryOrange)
        }
        error != null -> Text("Error: $error", color = Color.Red)
        equipos.isEmpty() -> Text("No hay datos disponibles para $nombreLiga", color = Color.White)
        else -> TablaLiga(nombreLiga = nombreLiga, equipos = equipos)
    }
}
