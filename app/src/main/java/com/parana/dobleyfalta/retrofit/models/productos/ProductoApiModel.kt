package com.parana.dobleyfalta.retrofit.models.productos

data class ProductoApiModel(
    val idProductos: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagen: String
)
