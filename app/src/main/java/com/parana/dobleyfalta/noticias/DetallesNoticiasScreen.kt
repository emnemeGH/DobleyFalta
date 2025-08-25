package com.parana.dobleyfalta.noticias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun DetalleNoticiasScreen(
    navController: NavController,
    noticiaId: Int
) {
    // En una aplicación real, se buscaría la noticia en una base de datos o API por su ID.
    // Aquí, usamos una lista de noticias de ejemplo para simular la búsqueda.
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

    // Busca la noticia seleccionada por su ID
    val noticiaSeleccionada = noticias.find { it.id == noticiaId }

    if (noticiaSeleccionada != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlue)
                .padding(top = 50.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen de la noticia en la parte superior
            AsyncImage(
                model = noticiaSeleccionada.imagen,
                contentDescription = "Imagen de la noticia",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            // Contenedor principal de los detalles
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Título de la noticia
                Text(
                    text = noticiaSeleccionada.titulo,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Fecha de publicación
                Text(
                    text = noticiaSeleccionada.fechaPublicacion,
                    color = LightGrey,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Contenido de la noticia
                Text(
                    text = noticiaSeleccionada.contenido,
                    color = LightGrey,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón para volver atrás
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Volver a noticias", color = Color.White)
                }
            }
        }
    } else {
        // Muestra un mensaje de error si la noticia no se encuentra
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Noticia no encontrada.",
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}
