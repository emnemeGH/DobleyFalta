package com.parana.dobleyfalta.equipos.empleado_equipos

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.retrofit.models.equipos.CrearEquipoModel
import com.parana.dobleyfalta.retrofit.viewmodels.equipos.CrearEquipoViewModel
import com.parana.dobleyfalta.retrofit.viewmodels.ligas.LigasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearEquipoScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val CardBackground = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val focusManager = LocalFocusManager.current

    var nombreEquipo by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenBase64 by remember { mutableStateOf<String?>(null) }

    var imagenPreview by remember { mutableStateOf<ImageBitmap?>(null) }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var ciudadError by remember { mutableStateOf<String?>(null) }
    var direccionError by remember { mutableStateOf<String?>(null) }
    var descripcionError by remember { mutableStateOf<String?>(null) }
    var logoError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

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
                    logoError = null
                }
            }
        }
    )

    val ligasViewModel: LigasViewModel = viewModel()
    val ligas by ligasViewModel.ligas.collectAsState()
    val loadingLigas by ligasViewModel.loading.collectAsState()
    val errorLigas by ligasViewModel.error.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var nombreLigaSeleccionada by remember { mutableStateOf("") }
    var idLigaSeleccionada by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        ligasViewModel.cargarLigas()
    }

    var latitud by remember { mutableStateOf<Double?>(null) }
    var longitud by remember { mutableStateOf<Double?>(null) }

    val defaultLocation = LatLng(-31.74, -60.52)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 13f)
    }

    var markerPosition by remember { mutableStateOf(defaultLocation) }

    val crearEquipoViewModel: CrearEquipoViewModel = viewModel()
    val loading by crearEquipoViewModel.loading.collectAsState()
    val error by crearEquipoViewModel.error.collectAsState()

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Volver a equipos",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Crear Equipo",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        CampoDeTexto(
            valor = nombreEquipo,
            alCambiarValor = {
                nombreEquipo = it
                nombreError = null
            },
            etiqueta = "Nombre del equipo",
            error = nombreError
        )

        CampoDeTexto(
            valor = ciudad,
            alCambiarValor = {
                ciudad = it
                ciudadError = null
            },
            etiqueta = "Ciudad",
            error = ciudadError
        )

        CampoDeTexto(
            valor = direccion,
            alCambiarValor = {
                direccion = it
                direccionError = null
            },
            etiqueta = "Dirección",
            error = direccionError
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                if (it.length <= 255) {
                    descripcion = it
                    descripcionError = null
                }
            },
            label = { Text("Descripción", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp, max = 180.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                unfocusedBorderColor = CardBackground,
                focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            isError = descripcionError != null,
            supportingText = {
                descripcionError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
            }
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            OutlinedTextField(
                value = nombreLigaSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar liga", color = LightGrey) },
                modifier = Modifier
                    .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground,
                    unfocusedBorderColor = CardBackground,
                    focusedBorderColor = PrimaryOrange,
                    cursorColor = PrimaryOrange,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.Gray,
                    disabledContainerColor = CardBackground,
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (loadingLigas) {
                    DropdownMenuItem(
                        text = { Text("Cargando ligas...") },
                        onClick = {}
                    )
                } else if (errorLigas != null) {
                    DropdownMenuItem(
                        text = { Text("Error al cargar ligas") },
                        onClick = {}
                    )
                } else {
                    ligas.forEach { liga ->
                        DropdownMenuItem(
                            text = { Text(liga.nombre) },
                            onClick = {
                                nombreLigaSeleccionada = liga.nombre
                                idLigaSeleccionada = liga.idLiga
                                expanded = false
                            },
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange.copy(alpha = 0.9f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Seleccionar imagen", color = Color.White, fontWeight = FontWeight.Bold)
        }

        imagenPreview?.let { img ->
            Image(
                bitmap = img,
                contentDescription = "Vista previa",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 8.dp)
            )
        }

        logoError?.let {
            Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Text(
            text = "Seleccioná la ubicación del club:",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
                .padding(top = 16.dp)
        )

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(10.dp)),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                markerPosition = latLng
                latitud = latLng.latitude
                longitud = latLng.longitude
            }
        ) {
            Marker(
                state = MarkerState(position = markerPosition),
                title = "Ubicación del equipo"
            )
        }

        error?.let {
            Text(text = it, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                nombreError = null
                ciudadError = null
                direccionError = null
                logoError = null
                var esValido = true

                if (nombreEquipo.isNullOrBlank()) {
                    nombreError = "El nombre es obligatorio"
                    esValido = false
                }
                if (ciudad.isNullOrBlank()) {
                    ciudadError = "La ciudad es obligatoria"
                    esValido = false
                }
                if (direccion.isNullOrBlank()) {
                    direccionError = "La dirección es obligatoria"
                    esValido = false
                }
                if (descripcion.isNullOrBlank()) {
                    descripcionError = "La descripción es obligatoria"
                    esValido = false
                }
                if (imagenBase64.isNullOrBlank()) {
                    logoError = "El logo es obligatorio"
                    esValido = false
                }

                if (latitud == null || longitud == null) {
                    direccionError = "Seleccioná una ubicación en el mapa"
                    esValido = false
                }

                if (esValido) {
                    val nuevo = CrearEquipoModel(
                        nombre = nombreEquipo,
                        ciudad = ciudad,
                        direccion = direccion,
                        descripcion = descripcion,
                        logo = imagenBase64,
                        idLiga = idLigaSeleccionada,
                        lat = latitud,
                        lng = longitud
                    )
                    crearEquipoViewModel.crearEquipo(nuevo) {
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Guardar Equipo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun CampoDeTexto(
    valor: String,
   //Cada vez que el usuario escribe, OutlinedTextField llama a onValueChange con el texto actualizado.
    //it vendria a ser el string que se pasa por parametro
    alCambiarValor: (String) -> Unit,
    etiqueta: String,
    error: String?
) {
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val CardBackground = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)

    OutlinedTextField(
        value = valor,
        //Para que Compose sepa qué hacer cada vez que cambie el texto, necesita ejecutar la función más tarde,
        // no en el momento en que definís el composable.
        //Si escribieras valor = it ahí, se intentaría ejecutar inmediatamente
        // (cuando it ni siquiera existe todavía).
        //Ademas no se puede modificar el valor de los parametros desde aca.
        onValueChange = alCambiarValor,
        label = { Text(etiqueta, color = LightGrey) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CardBackground,
            unfocusedContainerColor = CardBackground,
            unfocusedBorderColor = CardBackground,
            focusedBorderColor = PrimaryOrange,
            cursorColor = PrimaryOrange,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
        ),
        isError = error != null,
        supportingText = {
            error?.let {
                Text(it, color = Color.Red, fontSize = 12.sp)
            }
        },
        singleLine = true
    )
}

