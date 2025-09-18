package com.parana.dobleyfalta.tienda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.DarkBlue
import com.parana.dobleyfalta.DarkGrey
import com.parana.dobleyfalta.R

// Modelo de producto
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val image: Int, // drawable local
    val inStock: Boolean,
    val popularity: Int,
    val categoria: String
)

// Mock de productos
val products = listOf(
    Product(1, "Pelota ", 29.999, R.drawable.pelota_basquet, true, 95, "Pelotas"),
    Product(2, "Buzo", 79.999, R.drawable.buzo, false, 88, "Indumentaria"),
    Product(3, "Remera", 34.999, R.drawable.remera, true, 92, "Indumentaria")
)

@Composable
fun TiendaScreen(navController: NavController) {
    //Colores
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val White = colorResource(id = R.color.white)
    val DarkGrey = Color(0xFF1A375E)


    var searchQuery by remember { mutableStateOf("") }
    var cart by remember { mutableStateOf(listOf<Int>()) }
    var showFilters by remember { mutableStateOf(false) }

    val filteredProducts = products.filter {
        it.name.lowercase().contains(searchQuery.lowercase())
    }
    var selectedCategoria by remember { mutableStateOf("Mostrar todo") }
    var selectedTamaño by remember { mutableStateOf("Mostrar todo") }

    val sortedProducts = filteredProducts.sortedByDescending { it.popularity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkGrey)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_transparent),
                contentDescription = "Logo",
                modifier = Modifier.size(65.dp)
            )

            Column {
                Text("Tienda Online", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = White, textAlign = TextAlign.Center, modifier = Modifier.width(250.dp))
                Text("Merch original de la liga", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f), textAlign = TextAlign.Center, modifier = Modifier.width(250.dp))
            }

            Box{
                IconButton(onClick = {
                    navController.navigate("carrito") // 🔥 Navega a pantalla del carrito
                }) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Carrito",
                        tint = PrimaryOrange,
                        modifier = Modifier.size(40.dp)
                    )
                }
                if (cart.isNotEmpty()){
                    Box(
                        modifier = Modifier
                            .padding(0.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Surface(
                            color = White,
                            shape = CircleShape,
                            modifier = Modifier.size(26.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(cart.size.toString(), color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                            }
                        }
                    }
                }
            }
        }

        // Barra de búsqueda y filtro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkGrey)
                .padding(top = 2.dp, start = 20.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar productos", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryOrange)},
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryOrange,
                    unfocusedBorderColor = White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.Gray,
                    focusedContainerColor = DarkGrey,
                    unfocusedContainerColor = DarkGrey
                )
            )
            IconButton(onClick = { showFilters = !showFilters }) {
                Icon(painter = painterResource(id = R.drawable.filter_list),
                    contentDescription = "Filtro",
                    tint = PrimaryOrange,
                    modifier = Modifier.size(35.dp)
                )
            }
        }

        // Filtros
        if (showFilters) {
            FiltrosPanel(
                selectedCategoria = selectedCategoria,
                onCategoriaChange = { selectedCategoria = it },
                selectedTamaño = selectedTamaño,
                onTamañoChange = { selectedTamaño = it }
            )
        }

        // Lista de productos
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item(span = { GridItemSpan(this.maxLineSpan)}) {
                Text("Productos",color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp))}
            items(sortedProducts ) { product ->
                ProductCard(product, onAddToCart = {cart = cart + it}, navController = navController)
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onAddToCart: (Int) -> Unit, navController: NavController) {
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { navController.navigate("detalle_producto/${product.id}")},
        colors = CardDefaults.cardColors(containerColor = DarkGrey),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(id = product.image),
                    contentDescription = product.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(bottom = 8.dp)
                )
                if (!product.inStock) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("SOLD OUT", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White,
                    maxLines = 2,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    "$${product.price}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                val stockColor = if (product.inStock) Color(0xFF4CAF50) else Color(0xFFF44336)
                val stockText = if (product.inStock) "En Stock" else "Agotado"

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(stockColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(stockText, fontSize = 12.sp, color = stockColor)
                }

                Button(
                    onClick = { onAddToCart(product.id) },
                    enabled = product.inStock,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (product.inStock) "Agregar al carrito" else "Notificarme",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltrosPanel(
    selectedCategoria: String,
    onCategoriaChange: (String) -> Unit,
    selectedTamaño: String,
    onTamañoChange: (String) -> Unit
) {
    val categorias = listOf("Mostrar todo", "Pelotas", "Indumentaria")
    val tamaños = listOf("Mostrar todo", "S", "M", "L", "XL", "XXL")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGrey),
        //border = BorderStroke(width = 1.dp, color = LightGrey)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("Filtros", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)

            Spacer(modifier = Modifier.height(8.dp))

            DropdownFilter("Categoría", categorias, selectedCategoria, onCategoriaChange)
            Spacer(modifier = Modifier.height(8.dp))

            DropdownFilter("Tamaño", tamaños, selectedTamaño, onTamañoChange)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownFilter(
    label: String,
    options: List<String>,
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val LightGrey = Color(0xFFA0B3C4)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, color = Color.White)},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                focusedLabelColor = DarkGrey,
                unfocusedLabelColor = DarkGrey,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier.background(color = DarkBlue),
            border = BorderStroke(width = 1.dp, color = LightGrey)

        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.White) },
                    onClick = {
                        onSelectedChange(option)
                        expanded = false
                    },
                    modifier = Modifier.background(DarkBlue)
                )
            }
        }
    }
}

//contentScale
// es un parámetro de Image() y AsyncImage() que define cómo se ajusta la imagen
// dentro del espacio que tiene asignado en pantalla.
//
// Sintaxis Básica:
// Image(
//     painter = painterResource(id = R.drawable.mi_imagen),
//     contentDescription = "Ejemplo",
//     contentScale = ContentScale.Fit
// )
//
// Principales valores de ContentScale:
// - ContentScale.Fit
//   * Ajusta la imagen para que quepa COMPLETAMENTE en el espacio disponible,
//     manteniendo su relación de aspecto. Puede dejar espacios vacíos (letterboxing).
//
// - ContentScale.Crop
//   * Rellena todo el espacio disponible, recortando (crop) la imagen si es necesario.
//     Mantiene relación de aspecto, pero puede cortar partes de la imagen.
//
// - ContentScale.FillBounds
//   * Estira la imagen para llenar el espacio, SIN mantener relación de aspecto.
//     Puede distorsionar la imagen (ensanchar o aplastar).
//
// - ContentScale.FillWidth
//   * Escala la imagen para llenar el ancho del contenedor, manteniendo aspecto.
//     Puede dejar espacio vacío arriba y abajo.
//
// - ContentScale.FillHeight
//   * Escala la imagen para llenar el alto del contenedor, manteniendo aspecto.
//     Puede dejar espacio vacío a los lados.
//
// - ContentScale.Inside
//   * Similar a Fit, pero solo escala si la imagen es más grande que el contenedor.
//     Si ya es más chica, no la agranda.
//
// - ContentScale.None
//   * Muestra la imagen en su tamaño original, sin escalar.
//
// Notas:
// - Se usa junto con Modifier.size(), .width(), .height() o .fillMaxSize()
//   para controlar el espacio que ocupa la imagen.
// - Si querés que siempre llene el espacio, lo más común es usar Crop.
// - Si querés que nunca se recorte, lo más común es usar Fit o Inside.