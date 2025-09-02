package com.parana.dobleyfalta.noticias

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.parana.dobleyfalta.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

val DarkBlue = Color(0xFF102B4E)
val DarkGrey = Color(0xFF1A375E)
val PrimaryOrange = Color(0xFFFF6600)
val LightGrey = Color(0xFFA0B3C4)

data class Noticia(
    val id: Int, // Agregado para la navegación
    val titulo: String,
    val contenido: String,
    val fechaPublicacion: String,
    val imagen: String
)

val noticias = listOf(
    Noticia(
        id = 1,
        titulo = "Scacchi es nuevo refuerzo de Norte",
        contenido = "Juan Cruz Scacchi se conviertió en la nueva incorporación del Deportivo Norte para ser parte del plantel del elenco de Armstrong en la temporada 2025/2026 de La Liga Argentina.",
        fechaPublicacion = "2025-08-24",
        imagen = "https://www.laliganacional.com.ar/uploadsfotos/imagen_juan_cruz_scacchi_4.jpg"
    ),
    Noticia(
        id = 2,
        titulo = "Provincial se aseguró las continuidades de Santiago López y Gastón Gerbaudo",
        contenido = "El nicoleño y el mariajuanense compartirán nuevamente el perímetro en el Rojo y son las primeras renovaciones en confirmarse para el plantel que conducirá Esteban Gatti.",
        fechaPublicacion = "2025-08-23",
        imagen = "https://www.laliganacional.com.ar/uploadsfotos/dsc_1894__1_.jpg"
    ),
    Noticia(
        id = 3,
        titulo = "Renovaciones, regresos y apuestas en Villa San Martín",
        contenido = "Continúa Simondi, vuelve el joven Emir Pérez Barrios, llega el brasilero Gusmao y el base Santiago Rath. Con estas confirmaciones va tomando forma lo que promete ser un equipo muy interesante para la temporada. El “Tricolor” es el único representante chaqueño en la categoría.",
        fechaPublicacion = "2025-08-22",
        imagen = "https://www.laliganacional.com.ar/uploadsfotos/romulo_j9.jpg"
    )
)

//Metodo que transforma el string que tienen las noticias como fecha en un objeto localDate para
//poder usar los metodos especiales que tiene localDate
fun Noticia.getFechaPublicacion(): LocalDate? {
    return try {
        LocalDate.parse(this.fechaPublicacion)
    } catch (e: Exception) {
        null
    }
}

@Composable
fun NoticiasScreen(navController: NavController) {

    //Si se agrega o elimina una noticia, al ser un mutableState, se redibuja la UI
    var listaNoticias by remember { mutableStateOf(noticias) }

    //Ordenamos las noticias de mas recientes a mas antiguas
    //Por que usé remember?
    //Si no se pone remember, cada vez que Compose redibuja la pantalla, se recalcularía listaNoticias.sortedByDescending(...).
    val listaNoticiasOrdenadas = remember(listaNoticias) {
        listaNoticias.sortedByDescending { it.getFechaPublicacion() }
    }

    var mostrarConfirmacionBorrado by remember { mutableStateOf(false) }
    var noticiaAEliminar by remember { mutableStateOf<Noticia?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(24.dp)
            .padding(bottom = 48.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Noticias Destacadas",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        //Por cada noticia creamos un card.
        listaNoticiasOrdenadas.forEach { noticia ->
            NoticiaDestacadaCard(
                noticia = noticia,
                alHacerClick = { navController.navigate("detalle_noticia/${noticia.id}") },
                alBorrarClick = {
                    noticiaAEliminar = noticia
                    mostrarConfirmacionBorrado = true
                },
                alEditarClick = { navController.navigate("editar_noticia")}
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    //Si apretamos en el boton de borrar aparece este dialgo
    if (mostrarConfirmacionBorrado && noticiaAEliminar != null) {
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
                        noticiaAEliminar?.let {
                            listaNoticias = listaNoticias.filter { n -> n.id != it.id }
                        }
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
fun NoticiaDestacadaCard(
    noticia: Noticia,
    alHacerClick: () -> Unit,
    alBorrarClick: () -> Unit,
    alEditarClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = alHacerClick),
        colors = CardDefaults.cardColors(containerColor = DarkGrey),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            //Caja que contiene la imagen e iconos
            //Box es util para superponer elementos
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
                        modifier = Modifier.size(36.dp)
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
                        //caja que contiene el fondo y el icono
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
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = noticia.contenido,
                    color = LightGrey,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                //La estrcutura del formateador
                val formatter =
                    DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy", Locale("es", "ES"))
                //Nos traemos la fecha
                val fecha = noticia.getFechaPublicacion()
                //Si la fecha no es null la formateamos a texto mas estetico para el usuario
                val fechaFormateada = fecha?.format(formatter) ?: "Fecha no disponible"

                //?:
                //operador Elvis.
                //Si lo de la izquierda es null, devuelve lo de la derecha.
                //Si lo de la izquierda tiene un valor, devuelve ese valor.

                Text(
                    text = fechaFormateada,
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

//DateTimeFormatter.ofPattern
//crea un formateador de fechas. Es un objeto que le dice a Kotlin cómo leer o escribir fechas en texto.

//¿Qué es DateTimeFormatter?
//Es una clase de Kotlin que sirve para convertir fechas en texto y texto en fechas.
//Se usa junto con LocalDate, LocalDateTime, etc.
//Si tenés un objeto de fecha (LocalDate) y lo querés mostrar como string → usás format.
//Si tenés un string de fecha y lo querés convertir en objeto de fecha → usás parse.
//Con el método ofPattern, le decís qué formato vas a usar.
//Ejemplo:
//val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//Ese formateador entiende cosas como "22/08/2025".

//"dd 'de' MMMM, yyyy"
//dd → día con dos dígitos. Ejemplo: 01, 09, 24
//'de' → el texto literal "de". Los apóstrofes ' ' indican que no es parte de la fecha,
//  sino que debe aparecer tal cual.
//MMMM → nombre completo del mes, según el locale (idioma).
//Con Locale("es", "ES") → "Agosto", "Septiembre", "Octubre".
//Si fuera inglés (Locale.ENGLISH) sería "August", "September", etc.
//, (coma) → carácter literal coma, que también debe estar en el texto.
//yyyy → año con 4 dígitos. Ejemplo: 2025

//Locale("es", "ES")
//Locale define el idioma/región que se va a usar para interpretar y mostrar las fechas.
//"es", "ES" = Español de España.

//fun Noticia.getFechaPublicacion()
//Esto es una funcion de extension.
//Es una forma de agregar una función a una clase existente sin modificar el código original de la clase.
//En este caso, le estamos “agregando” getFechaPublicacion() a la clase Noticia.

//LocalDate.parse(...)
//convierte el String (ej: "23-08-2025") en un objeto de tipo LocalDate (2025-08-23).

//format es un método de LocalDate.
//Lo que hace es recibir un objeto DateTimeFormatter y devolver un String con la fecha en ese formato.