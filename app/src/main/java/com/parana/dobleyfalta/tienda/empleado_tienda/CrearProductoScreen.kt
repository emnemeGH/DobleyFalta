package com.parana.dobleyfalta.tienda.empleado_tienda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.retrofit.models.productos.CrearProductoModel
import com.parana.dobleyfalta.retrofit.viewmodels.productos.ProductosViewModel

@Composable
fun CrearProductoScreen(
    navController: NavController,
    productosViewModel: ProductosViewModel = viewModel()
) {
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val DarkBlue = colorResource(id = R.color.darkBlue)

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }

    val loading by productosViewModel.loading.collectAsState()
    val error by productosViewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(16.dp)
    ) {

        // Top bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Crear Producto",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Campos
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryOrange,
                unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                focusedLabelColor = PrimaryOrange,
                unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                cursorColor = PrimaryOrange
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryOrange,
                unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                focusedLabelColor = PrimaryOrange,
                unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                cursorColor = PrimaryOrange
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = precio,
            onValueChange = { newValue ->
                // Permite solo dígitos y un punto decimal (o coma, según la localización)
                val filtered = newValue.filter { it.isDigit() || it == '.' }
                precio = filtered },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryOrange,
                unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                focusedLabelColor = PrimaryOrange,
                unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                cursorColor = PrimaryOrange
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = imagen,
            onValueChange = { imagen = it },
            label = { Text("URL de Imagen") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryOrange,
                unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                focusedLabelColor = PrimaryOrange,
                unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                cursorColor = PrimaryOrange
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón crear
        Button(
            onClick = {
                val precioDouble = precio.toDoubleOrNull() ?: 0.0
                val nuevoProducto = CrearProductoModel(
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precioDouble,
                    imagen = imagen
                )
                productosViewModel.crearProducto(nuevoProducto) {
                    navController.popBackStack()
                }
            },
            enabled = !loading && nombre.isNotBlank() && precio.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (loading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
            } else {
                Text("Crear Producto", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (error != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = error ?: "",
                color = Color.Red,
                fontSize = 14.sp
            )
        }
    }
}

