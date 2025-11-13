package com.parana.dobleyfalta.tienda

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.parana.dobleyfalta.R
import com.parana.dobleyfalta.retrofit.ApiConstants.BASE_URL
import com.parana.dobleyfalta.retrofit.viewmodels.productos.ProductosViewModel

@Composable
fun DetalleProductoScreen(
    productId: Int,
    navController: NavController,
    onAddToCart: (Int) -> Unit
) {
    //val product = productos.find { it.id == productId }
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val DarkBlue = colorResource(id = R.color.darkBlue)

    val productosViewModel: ProductosViewModel = viewModel()
    val productos by productosViewModel.productos.collectAsState()
    val loading by productosViewModel.loading.collectAsState()
    val error by productosViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        if (productos.isEmpty()) {
            productosViewModel.cargarProductos()
        }
    }

    val producto = productos.find { it.idProducto == productId }


//    if (producto == null) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(DarkBlue),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("Producto no encontrado", color = Color.White)
//        }
//        return
//    }


    when {
        loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBlue),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryOrange)
            }
        }

        error != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBlue),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: $error", color = Color.Red)
            }
        }

        producto == null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBlue),
                contentAlignment = Alignment.Center
            ) {
                Text("Producto no encontrado", color = Color.White)
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBlue)
                    .padding(16.dp)
            ) {
                // Top bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Detalle del Producto",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Imagen del producto
                AsyncImage(
                    //model = "$BASE_URL${producto.imagen}",
                    model = producto.imagen,
                    contentDescription = producto.nombre,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(producto.nombre, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("$${producto.precio}", color = PrimaryOrange, fontSize = 22.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(12.dp))

                // Estado de stock
                val stockColor = if (producto.inStock) Color(0xFF4CAF50) else Color(0xFFF44336)
                val stockText = if (producto.inStock) "En Stock" else "Agotado"

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(stockColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(stockText, fontSize = 16.sp, color = stockColor)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar talles disponibles
                val tallesDisponibles = producto.inventario
                    .filter { it.stock > 0 }
                    .map { it.id.talle }
                    .distinct()

                if (tallesDisponibles.isNotEmpty()) {
                    Text(
                        text = "Talles disponibles:",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        tallesDisponibles.forEach { talle ->
                            Surface(
                                shape = CircleShape,
                                color = PrimaryOrange.copy(alpha = 0.2f),
                                border = ButtonDefaults.outlinedButtonBorder
                            ) {
                                Text(
                                    text = talle.toString(),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Descripción
                Text(
                    text = producto.descripcion,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                // Botón agregar al carrito
                Button(
                    onClick = { onAddToCart(producto.idProducto) },
                    enabled = producto.inStock,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (producto.inStock) "Agregar al carrito" else "Notificarme",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


    }
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(DarkBlue)
//            .padding(16.dp)
//    ) {
//        // Top bar
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(onClick = { navController.popBackStack() }) {
//                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
//            }
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = "Detalle del Producto",
//                color = Color.White,
//                fontWeight = FontWeight.Bold,
//                fontSize = 20.sp
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Imagen
//        Image(
//            painter = painterResource(id = product.imagen),
//            contentDescription = product.nombre,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(250.dp)
//                .clip(RoundedCornerShape(16.dp))
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Nombre y precio
//        Text(product.nombre, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
//        Spacer(modifier = Modifier.height(4.dp))
//        Text("$${product.precio}", color = PrimaryOrange, fontSize = 22.sp, fontWeight = FontWeight.Bold)
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // Estado de stock
//        val stockColor = if (product.inStock) Color(0xFF4CAF50) else Color(0xFFF44336)
//        val stockText = if (product.inStock) "En Stock" else "Agotado"
//
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Box(
//                modifier = Modifier
//                    .size(10.dp)
//                    .clip(CircleShape)
//                    .background(stockColor)
//            )
//            Spacer(modifier = Modifier.width(6.dp))
//            Text(stockText, fontSize = 16.sp, color = stockColor)
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Descripción (mock)
//        Text(
//            text = "Este es un producto de alta calidad y edición limitada de ${product.categoria}. Perfecto para fanáticos de tu equipo favorito.",
//            color = Color.White.copy(alpha = 0.9f),
//            fontSize = 16.sp
//        )
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        // Botón agregar al carrito
//        Button(
//            onClick = { onAddToCart(product.id) },
//            enabled = product.inStock,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(56.dp),
//            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
//            shape = RoundedCornerShape(12.dp)
//        ) {
//            Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White)
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                if (product.inStock) "Agregar al carrito" else "Notificarme",
//                color = Color.White,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
    }
}


