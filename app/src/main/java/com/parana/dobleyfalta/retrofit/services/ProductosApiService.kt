package com.parana.dobleyfalta.retrofit.services

import com.parana.dobleyfalta.retrofit.models.productos.ProductoModel
import com.parana.dobleyfalta.retrofit.models.productos.CrearProductoModel
import retrofit2.Response
import retrofit2.http.*

interface ProductosApiService {

    @GET("api/productos/all")
    suspend fun getProductos(): List<ProductoModel>

    @GET("api/productos/{id}")
    suspend fun getProductoPorId(@Path("id") id:Int): ProductoModel

    @PUT("api/productos/{id}")
    suspend fun updateProducto(@Path("id") id:Int, @Body producto: CrearProductoModel): ProductoModel

    @POST("api/productos")
    suspend fun crearProducto(@Body nuevoProducto:CrearProductoModel): Response<CrearProductoModel>

    @DELETE("api/productos/{id}")
    suspend fun eliminarProducto(@Path("id") id:Int): Response<Unit>

}