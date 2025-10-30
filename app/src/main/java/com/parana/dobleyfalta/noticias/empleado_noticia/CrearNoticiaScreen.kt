package com.parana.dobleyfalta.noticias.empleado_noticia

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.retrofit.clients.RetrofitClientNoticias
import com.parana.dobleyfalta.retrofit.models.noticia.CrearNoticiaModel
import com.parana.dobleyfalta.retrofit.repositories.NoticiasRepository
import com.parana.dobleyfalta.retrofit.viewmodels.CrearNoticiaViewModel
import java.time.Instant
import java.time.ZoneOffset
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import android.util.Base64
import android.graphics.BitmapFactory
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import java.io.InputStream
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearNoticiaScreen(
    navController: NavController,
    viewModel: CrearNoticiaViewModel = viewModel()
) {
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val success by viewModel.success.collectAsState()

    if (loading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    }

    error?.let {
        Text("Error: $it", color = Color.Red)
    }

    if (success) {
        LaunchedEffect(Unit) {
            navController.navigate("noticias")
        }
    }

    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val focusManager = LocalFocusManager.current

    var tituloNoticia by remember { mutableStateOf("") }
    var contenidoNoticia by remember { mutableStateOf("") }
    var fechaPublicacion by remember { mutableStateOf("") }

    var tituloError by remember { mutableStateOf<String?>(null) }
    var contenidoError by remember { mutableStateOf<String?>(null) }
    var fechaError by remember { mutableStateOf<String?>(null) }
    var urlError by remember { mutableStateOf<String?>(null) }

    var mostrarSeleccionFechas by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(horizontal = 32.dp)
            .padding(top = 32.dp)
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

        Text(
            text = "Crear Noticia",
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
            singleLine = true,
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
                fechaError?.let {
                    Text(it, color = Color.Red, fontSize = 11.sp)
                }
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha",
                    tint = LightGrey,
                    modifier = Modifier
                        .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                        ) { mostrarSeleccionFechas = true },
                )
            }
        )

        val context = LocalContext.current
        var imagenBase64 by remember { mutableStateOf<String?>(null) }
        var imagenPreview by remember { mutableStateOf<ImageBitmap?>(null) }


        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                if (uri != null) {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bytes = inputStream?.readBytes()
                    inputStream?.close()

                    if (bytes != null) {
                        imagenBase64 = Base64.encodeToString(bytes, Base64.DEFAULT)

                        val bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        imagenPreview = bitmap.asImageBitmap()
                    }
                }
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
            Text("Seleccionar imagen", color = Color.White, fontWeight = FontWeight.Bold)
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
                urlError = null
                var formValido = true

                if (tituloNoticia.isBlank()) {
                    tituloError = "El titulo es obligatorio"
                    formValido = false
                }
                if (contenidoNoticia.isBlank()) {
                    contenidoError = "El contenido es obligatorio"
                    formValido = false
                }
                if (fechaPublicacion.isBlank()) {
                    fechaError = "La fecha es obligatoria"
                    formValido = false
                }
                if (imagenBase64 == null) {
                    urlError = "Debe seleccionar una imagen"
                    formValido = false
                }

                if (formValido) {
                    imagenBase64?.let {
                        val noticia = CrearNoticiaModel(
                            titulo = tituloNoticia,
                            contenido = contenidoNoticia,
                            fechaPublicacion = fechaPublicacion + "T00:00:00",
                            imagen = it
                        )

                        viewModel.crearNoticia(noticia)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Publicar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }

    if (mostrarSeleccionFechas) {
        DatePickerDialog(
            onDismissRequest = { mostrarSeleccionFechas = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val fecha = Instant.ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()

                            fechaPublicacion = fecha.toString()
                            fechaError = null
                        }
                        mostrarSeleccionFechas = false
                    }
                ) { Text("Confirmar", color = PrimaryOrange) }
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

//GLOSARIO

//Instant.ofEpochMilli()
//Instant es una clase de java.time que representa un punto exacto en la línea del tiempo,
// independiente de zona horaria.
//ofEpochMilli() es un método estático que convierte un número de milisegundos desde 1970-01-01 00:00:00 UTC
// en un objeto Instant.
//Ejemplo:
    //val millis = 1693747200000L  // milisegundos desde 1970
    //val instant = Instant.ofEpochMilli(millis)
    //println(instant)  // Salida: 2023-09-03T00:00:00Z
//millis es solo un número (Long) que representa un momento específico en el tiempo.
//Instant.ofEpochMilli(millis) transforma ese número en un objeto de tiempo real que podés manipular,
//formatear o convertir a zonas horarias.

// .atZone
// Convierte un Instant (un punto exacto en el tiempo UTC) a un ZonedDateTime
// asociado a una zona horaria específica. Devuelve un ZonedDateTime.
// Antes de usar atZone(), un Instant representa solo un momento absoluto, sin zona horaria.
// Después de atZone(zona), ese instante se interpreta según la zona horaria indicada,
// dando fecha, hora y zona horaria local.

// ZoneOffset.UTC // ZoneId.systemDefault()
//La diferencia clave ente ZoneOffset.UTC y  ZoneId.systemDefault() es que no se ajusta al horario local del
// dispositivo, por lo que evitamos desfases de días al convertir a LocalDate. (Habia desfase con ZoneId)
//ZoneId.systemDefault()
// Devuelve la zona horaria configurada en el dispositivo del usuario, por ejemplo: "America/Argentina/Buenos_Aires".
// Con esto, el ZonedDateTime resultante tiene fecha, hora y zona horaria, listo para convertir a LocalDate.
// Sin atZone() no podríamos obtener una fecha local correcta.
//Ejemplo:
//val millis = 1693747200000L
//val instant = Instant.ofEpochMilli(millis)   // 2023-09-03T00:00:00Z (UTC) Coordinated Universal Time
//val zoned = instant.atZone(ZoneId.systemDefault())
//println(zoned)  // Ej: 2023-09-03T03:00:00-03:00[America/Argentina/Buenos_Aires]

//.toLocalDate()
// recorta un ZonedDateTime y devuelve solo el año, mes y día, ignorando la hora y la zona horaria.

//toString
// No solo lo pasa de localDate a String, si no que tambien nos da el formato de fehca ISO, que es el que
//Usamos en la base

//millis
//es el valor en milisegundos desde 1970 (epoch time se llama a esto) que te da el DatePicker para representar
//la fecha que seleccionaste.
//selectedDateMillis devuelve ese número.

//datePickerState.selectedDateMillis
// es una propiedad del DatePicker que te da la fecha seleccionada como un Long en milisegundos
// desde la época Unix. la época Unix es el número de milisegundos transcurridos desde 1970-01-01 00:00:00 UTC.
//Ej: 0 = exactamente esa fecha/hora; 86 400 000 = un día después, etc.
//¿Por qué en milisegundos y no “25/12/2025”?
//Porque los milisegundos son un formato neutro y universal: no dependen de idioma ni formato regional,
// se comparan y ordenan fácil (son números), son compatibles con las APIs de Kotlin
//Android no tiene un tipo nativo "date" simple como <input type="date"> en HTML, sino que todo gira en torno
// a Instant, Date, Calendar o LocalDateTime, y casi siempre usa milisegundos desde 1970 como formato base

//DatePickerDialog
//es un diálogo nativo de Android que le permite al usuario seleccionar una fecha (año, mes y día).
// Se usa mucho para campos de formulario como fecha de nacimiento, fecha de vencimiento, etc.

//readOnly = true
//hace que el campo se muestre como editable a nivel visual,
// pero no permite que el usuario modifique el contenido.

//singleLine = true
//significa que el campo de texto solo acepta una línea.
//Si el usuario escribe y llega al final, en lugar de bajar a otra línea (multilínea),
// el texto sigue corriéndose hacia la derecha.