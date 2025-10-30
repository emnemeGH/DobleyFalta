package com.parana.dobleyfalta.noticias

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.home.NoticiaMiniCard
import com.parana.dobleyfalta.retrofit.models.noticia.NoticiaApiModel
import com.parana.dobleyfalta.retrofit.repositories.NoticiasRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

val DarkBlue = Color(0xFF102B4E)
val DarkGrey = Color(0xFF1A375E)
val PrimaryOrange = Color(0xFFFF6600)
val LightGrey = Color(0xFFA0B3C4)

//Metodo que transforma el string que tienen las noticias como fecha en un objeto localDate para
//poder usar los metodos especiales que tiene localDate
fun NoticiaApiModel.getFechaPublicacionFormateada(): String {
    return try {
        val fechaHora = LocalDateTime.parse(this.fechaPublicacion)

        val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy", Locale("es", "ES"))

        val soloFecha = fechaHora.toLocalDate()

        soloFecha.format(formatter)

    } catch (e: DateTimeParseException) {
        "Fecha no disponible"
    }
}

@Composable
fun NoticiasScreen(navController: NavController) {

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

    var mostrarConfirmacionBorrado by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Noticias Destacadas",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.weight(0.65f))

            Button(
                onClick = { navController.navigate("crear_noticia") },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Crear Noticia",
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
            }
        }

        val listaNoticiasOrdenadas = listaNoticias.sortedByDescending {
            try {
                LocalDateTime.parse(it.fechaPublicacion).toLocalDate()
            } catch (e: Exception) {
                LocalDate.MIN
            }
        }

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
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listaNoticiasOrdenadas) { noticia ->
                        NoticiaDestacadaCardApi(
                            noticia = noticia,
                            alHacerClick = { navController.navigate("detalle_noticia/${noticia.idNoticia}") },
                            alBorrarClick = {
                                mostrarConfirmacionBorrado = true
                            },
                            alEditarClick = { navController.navigate("editar_noticia") }
                        )
                    }
                }
            }
        }
    }

    //Si apretamos en el boton de borrar aparece este dialgo
    if (mostrarConfirmacionBorrado) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionBorrado = false },
            title = {
                Text("Confirmar eliminación", fontWeight = FontWeight.Bold, color = Color.White)
            },
            text = {
                Text("¿Seguro que quieres eliminar esta noticia?", color = LightGrey)
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarConfirmacionBorrado = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Borrar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacionBorrado = false }) {
                    Text("Cancelar", color = PrimaryOrange)
                }
            },
            containerColor = DarkGrey,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun NoticiaDestacadaCardApi(
    noticia: NoticiaApiModel,
    alHacerClick: () -> Unit,
    alBorrarClick: () -> Unit,
    alEditarClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = alHacerClick)
            .testTag("noticiaCard"),
        colors = CardDefaults.cardColors(containerColor = DarkGrey),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = noticia.imagen,
                    contentDescription = "Imagen de la noticia",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = { alEditarClick() },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("editarNoticia")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(color = Color(0x44111111), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = "Editar noticia",
                                tint = colorResource(id = R.color.blue_edit),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = { alBorrarClick() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(color = Color(0x44111111), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete),
                                contentDescription = "Eliminar noticia",
                                tint = colorResource(id = R.color.red_delete),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = noticia.titulo,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = noticia.contenido,
                    color = LightGrey,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    text = noticia.getFechaPublicacionFormateada(),
                    color = LightGrey,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

//GLOSARIO

//contentScale
//es una propiedad que define cómo se ajusta la imagen al espacio que le diste con el Modifier

//ContentScale.Crop
//ContentScale.Crop significa que la imagen se va a recortar para llenar todo el espacio.
// Si sobra, corta los bordes, pero nunca deja “huecos”.

//AsyncImage
// es un composable que sirve para mostrar imágenes donde la fuente de la imagen no está en la misma app
// como una URL en internet o un archivo en memoria externa
// carga la imagen de manera asíncrona, sin bloquear la interfaz ni hacer que se congele la pantalla
// mientras se descarga. Internamente maneja la descarga, el cache y la conversión de la imagen para que
// solo haya que darle la URL o el recurso y se muestre en pantalla cuando esté lista.

//verticalScroll(...)
//Hace que la columna (o cualquier componente que lo tenga) pueda desplazarse verticalmente.
//Sin esto, si la lista de noticias es más alta que la pantalla, no se puede ver toda.

//rememberScrollState()
//Crea un estado de desplazamiento que Compose recuerda mientras la composición viva.
//Este estado contiene información de dónde está actualmente el scroll.
//Si no usaras remember, Compose crearía un nuevo estado cada vez que la pantalla se redibuja,
// y el scroll volvería a la posición inicial constantemente.

//.sortedByDescending { ... }
//sortedByDescending es un método de Kotlin para listas.
//Devuelve una nueva lista ordenada de mayor a menor según el criterio que le pases.

//.sortedByDescending {it.getFechaPublicacion()}
//it.getFechaPublicacion() devuelve un LocalDate.
//Kotlin sabe cómo comparar dos LocalDate: el más reciente es “mayor” y el más antiguo es “menor”.
//Por eso no necesitás hacer nada más; sortedByDescending automáticamente coloca primero las fechas más nuevas.

//mutableStateOf()
//¿Qué es un state en Compose?
//Compose necesita saber cuándo redibujar la UI.
//Para eso usa un mecanismo llamado State: un valor que, cuando cambia, avisa a Compose
// y este vuelve a dibujar lo que dependa de él.
//mutableStateOf(valor) crea un State observable y modificable.
//Ejemplo: un contador básico con mutableStateOf

//    var contador by remember { mutableStateOf(0) }
//
//    Column {
//        Text(text = "Valor del contador: $contador")
//
//        Button(onClick = { contador++ }) {
//            Text("Sumar")
//        }
//    }

//En este caso:
//contador es observable por Compose.
//Cada vez que haces contador++, Compose detecta el cambio y redibuja automáticamente el Text.
//Resultado: ves el número aumentar en pantalla.
//
//Ejemplo Contador sin mutableStateOf
//
//    var contador = 0
//
//    Column {
//        Text(text = "Valor del contador: $contador")
//
//        Button(onClick = { contador++ }) {
//            Text("Sumar")
//        }
//    }

//Qué pasa:
//contador NO es observable por Compose.
//Aunque hagas contador++, Compose no redibuja la UI.
//Resultado: el número no cambia en pantalla, siempre ves 0.
//Sin mutableStateOf, cualquier cambio de la variable no se refleja en pantalla porque Compose no lo “escucha”.

