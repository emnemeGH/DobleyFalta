package com.parana.dobleyfalta.noticias.empleado_noticia

import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.retrofit.models.noticia.CrearNoticiaModel
import com.parana.dobleyfalta.retrofit.repositories.NoticiasRepository
import kotlinx.coroutines.launch
import java.io.InputStream
import java.time.Instant
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarNoticiaScreen(navController: NavController, noticiaId: Int) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)

    val repository = remember { NoticiasRepository() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var tituloNoticia by remember { mutableStateOf("") }
    var contenidoNoticia by remember { mutableStateOf("") }
    var fechaPublicacion by remember { mutableStateOf("") }
    var imagenBase64 by remember { mutableStateOf<String?>(null) }
    var imagenPreview by remember { mutableStateOf<ImageBitmap?>(null) }

    var tituloError by remember { mutableStateOf<String?>(null) }
    var contenidoError by remember { mutableStateOf<String?>(null) }
    var fechaError by remember { mutableStateOf<String?>(null) }

    var mostrarSeleccionFechas by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var cargando by remember { mutableStateOf(true) }
    var errorCarga by remember { mutableStateOf(false) }

    LaunchedEffect(noticiaId) {
        try {
            val noticia = repository.obtenerNoticiaPorId(noticiaId)
            tituloNoticia = noticia.titulo
            contenidoNoticia = noticia.contenido
            fechaPublicacion = noticia.fechaPublicacion.substring(0, 10)
            cargando = false
        } catch (e: Exception) {
            errorCarga = true
            cargando = false
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val inputStream: InputStream? = context.contentResolver.openInputStream(it)
                val bytes = inputStream?.readBytes()
                inputStream?.close()
                if (bytes != null) {
                    imagenBase64 = Base64.encodeToString(bytes, Base64.DEFAULT)
                    val bitmap =
                        android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    imagenPreview = bitmap.asImageBitmap()
                }
            }
        }
    )

    if (cargando) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryOrange)
        }
    }

    if (errorCarga) {
        Text(
            "Error al cargar la noticia",
            color = Color.Red,
            modifier = Modifier.padding(16.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(32.dp)
            .verticalScroll(rememberScrollState())
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { navController.navigate("noticias") },
                modifier = Modifier.padding(0.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Volver a noticias",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Editar Noticia",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = tituloNoticia,
            onValueChange = {
                tituloNoticia = it
                tituloError = null
            },
            label = { Text("Título de la noticia", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey,
                unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey,
                focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            isError = tituloError != null,
            supportingText = {
                tituloError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            }
        )

        OutlinedTextField(
            value = contenidoNoticia,
            onValueChange = {
                contenidoNoticia = it
                contenidoError = null
            },
            label = { Text("Contenido", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 180.dp, max = 300.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey,
                unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey,
                focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            isError = contenidoError != null,
            supportingText = {
                contenidoError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            }
        )

        OutlinedTextField(
            value = fechaPublicacion,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha", color = LightGrey) },
            modifier = Modifier
                .width(160.dp)
                .padding(bottom = 16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { mostrarSeleccionFechas = true }
                .align(Alignment.Start),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey,
                unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey,
                focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                disabledTextColor = Color.White
            ),
            isError = fechaError != null,
            supportingText = {
                fechaError?.let { Text(it, color = Color.Red, fontSize = 11.sp) }
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha",
                    tint = LightGrey,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { mostrarSeleccionFechas = true }
                )
            }
        )

        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Seleccionar nueva imagen", color = Color.White, fontWeight = FontWeight.Bold)
        }

        imagenPreview?.let { img ->
            Image(
                bitmap = img,
                contentDescription = "Vista previa",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = {
                tituloError = null
                contenidoError = null
                fechaError = null

                var valido = true
                if (tituloNoticia.isBlank()) { tituloError = "El título es obligatorio"; valido = false }
                if (contenidoNoticia.isBlank()) { contenidoError = "El contenido es obligatorio"; valido = false }
                if (fechaPublicacion.isBlank()) { fechaError = "La fecha es obligatoria"; valido = false }

                if (valido) {
                    scope.launch {
                        try {
                            val noticiaActualizada = CrearNoticiaModel(
                                titulo = tituloNoticia,
                                contenido = contenidoNoticia,
                                fechaPublicacion = "${fechaPublicacion}T00:00:00",
                                imagen = imagenBase64 ?: ""
                            )
                            repository.actualizarNoticia(noticiaId, noticiaActualizada)
                            navController.navigate("noticias")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }

    if (mostrarSeleccionFechas) {
        DatePickerDialog(
            onDismissRequest = { mostrarSeleccionFechas = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val fecha = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                        fechaPublicacion = fecha.toString()
                        fechaError = null
                    }
                    mostrarSeleccionFechas = false
                }) { Text("Confirmar", color = PrimaryOrange) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarSeleccionFechas = false }) {
                    Text("Cancelar", color = PrimaryOrange)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
