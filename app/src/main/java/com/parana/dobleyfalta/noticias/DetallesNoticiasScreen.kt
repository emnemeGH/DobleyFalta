package com.parana.dobleyfalta.noticias

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.parana.dobleyfalta.retrofit.ApiConstants.BASE_URL
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.parana.dobleyfalta.retrofit.models.noticia.NoticiaApiModel
import com.parana.dobleyfalta.retrofit.repositories.NoticiasRepository


@Composable
fun DetalleNoticiasScreen(
    navController: NavController,
    noticiaId: Int
) {

    val repository = remember { NoticiasRepository() }

    var loading by remember { mutableStateOf(true) }
    var noticia by remember { mutableStateOf<NoticiaApiModel?>(null) }
    var errorCarga by remember { mutableStateOf(false) }

    LaunchedEffect(noticiaId) {
        try {
            noticia = repository.obtenerNoticiaPorId(noticiaId)
            errorCarga = false
            loading = false
        } catch (e: Exception) {
            e.printStackTrace()
            errorCarga = true
        }
    }

    if (loading) {
        Dialog(onDismissRequest = {}) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryOrange)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue),
        contentAlignment = Alignment.Center
    ) {
        when {
            errorCarga -> {
                Text(
                    text = "Error al cargar la noticia. Intente más tarde.",
                    color = Color.Red,
                    fontSize = 18.sp
                )
            }

            noticia != null -> {
                noticia?.let { it ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 50.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        var cargandoImagen by remember { mutableStateOf(true) }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(16.dp))
                        ) {
                            AsyncImage(
                                model = "${BASE_URL}${it.imagen}",
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
                                        .background(Color.White, shape = RoundedCornerShape(16.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = PrimaryOrange)
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = noticia!!.titulo,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Text(
                                text = noticia!!.getFechaPublicacionFormateada(),
                                color = LightGrey,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Text(
                                text = noticia!!.contenido,
                                color = LightGrey,
                                fontSize = 16.sp,
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .testTag("volverANoticias"),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "Volver a noticias", color = Color.White)
                            }
                        }
                    }
                }

            }
        }
    }
}
