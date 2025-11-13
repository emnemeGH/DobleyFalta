package com.parana.dobleyfalta.retrofit.models.productos

data class ProductoModel(
    val idProducto: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagen: String,
    val inventario: List<InventarioModel>

) {
    // Esto ayuda a saber si hay stock total
    val inStock: Boolean
        get() = inventario.any { it.stock > 0 }
}
