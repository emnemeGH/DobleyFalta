package com.parana.dobleyfalta.noticias

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Definición de colores para consistencia con el estilo general
val DarkBlue = Color(0xFF102B4E)
val DarkGrey = Color(0xFF1A375E)
val PrimaryOrange = Color(0xFFFF6600)
val LightGrey = Color(0xFFA0B3C4)

// Modelo de datos para una noticia actualizado
data class Noticia(
    val id: Int, // Agregado para la navegación
    val titulo: String,
    val contenido: String,
    val fechaPublicacion: String,
    val imagen: String
)

@Composable
fun NoticiasScreen(navController: NavController) {
    // Lista de noticias sin ordenar
    val noticias = listOf(
        Noticia(
            id = 1,
            titulo = "Scacchi es nuevo refuerzo de Norte",
            contenido = "Juan Cruz Scacchi se conviertió en la nueva incorporación del Deportivo Norte para ser parte del plantel del elenco de Armstrong en la temporada 2025/2026 de La Liga Argentina.",
            fechaPublicacion = "24 de Agosto, 2025",
            imagen = "https://www.laliganacional.com.ar/uploadsfotos/imagen_juan_cruz_scacchi_4.jpg"
        ),
        Noticia(
            id = 2,
            titulo = "Provincial se aseguró las continuidades de Santiago López y Gastón Gerbaudo",
            contenido = "El nicoleño y el mariajuanense compartirán nuevamente el perímetro en el Rojo y son las primeras renovaciones en confirmarse para el plantel que conducirá Esteban Gatti.",
            fechaPublicacion = "23 de Agosto, 2025",
            imagen = "https://www.laliganacional.com.ar/uploadsfotos/dsc_1894__1_.jpg"
        ),
        Noticia(
            id = 3,
            titulo = "Renovaciones, regresos y apuestas en Villa San Martín",
            contenido = "Continúa Simondi, vuelve el joven Emir Pérez Barrios, llega el brasilero Gusmao y el base Santiago Rath. Con estas confirmaciones va tomando forma lo que promete ser un equipo muy interesante para la temporada. El “Tricolor” es el único representante chaqueño en la categoría.",
            fechaPublicacion = "22 de Agosto, 2025",
            imagen = "https://www.laliganacional.com.ar/uploadsfotos/romulo_j9.jpg"
        )
    )

    // Formateador para convertir la cadena de fecha a un objeto LocalDate
    val formatter = remember { DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy", Locale("es", "ES")) }

    // Función para convertir la fecha de la noticia a un objeto LocalDate
    fun Noticia.getLocalDate(): LocalDate? {
        return try {
            LocalDate.parse(this.fechaPublicacion, formatter)
        } catch (e: Exception) {
            null
        }
    }

    // Ordenar las noticias por fecha de forma descendente (las más nuevas primero)
    val sortedNoticias = remember(noticias) {
        noticias.sortedByDescending { it.getLocalDate() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Noticias Destacadas",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Recorrer la lista de noticias ya ordenada
        sortedNoticias.forEach { noticia ->
            // La tarjeta de noticia es ahora un composable clickable
            NoticiaDestacadaCard(noticia = noticia, onClick = {
                // Al hacer clic, navega a la pantalla de detalle de noticias
                navController.navigate("detalle_noticia/${noticia.id}")
            })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NoticiaDestacadaCard(noticia: Noticia, onClick: () -> Unit) {
    // Contenedor principal de la tarjeta de noticias
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick), // Hacemos la tarjeta clickable
        colors = CardDefaults.cardColors(containerColor = DarkGrey),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Imagen de la noticia
            AsyncImage(
                model = noticia.imagen, // Ahora usa la variable 'imagen'
                contentDescription = "Imagen de la noticia",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            // Contenedor de texto
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Título de la noticia
                Text(
                    text = noticia.titulo,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Contenido de la noticia
                Text(
                    text = noticia.contenido,
                    color = LightGrey,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // Fecha de publicación
                Text(
                    text = noticia.fechaPublicacion,
                    color = LightGrey,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}
