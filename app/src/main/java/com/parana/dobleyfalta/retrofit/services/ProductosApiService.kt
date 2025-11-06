package com.parana.dobleyfalta.retrofit.services

import com.parana.dobleyfalta.retrofit.models.productos.ProductoApiModel
import com.parana.dobleyfalta.retrofit.models.productos.CrearProductoModel
import retrofit2.Response
import retrofit2.http.*

interface ProductosApiService {

    @GET("api/productos")
    suspend fun getProductos(): List<ProductoApiModel>

    @GET("api/productos/{id}")
    suspend fun getProductoPorId(@Path("id") id:Int): ProductoApiModel

    @PUT("api/productos/{id}")
    suspend fun updateProducto(@Path("id") id:Int, @Body producto: CrearProductoModel): ProductoApiModel

    @POST("api/productos")
    suspend fun crearProducto(@Body nuevoProducto:CrearProductoModel): Response<CrearProductoModel>

    @DELETE("api/productos/{id}")
    suspend fun eliminarProducto(@Path("id") id:Int): Response<Unit>

}