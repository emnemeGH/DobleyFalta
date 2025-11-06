package com.parana.dobleyfalta.retrofit.repositories

import com.parana.dobleyfalta.retrofit.clients.RetrofitClientProductos
import com.parana.dobleyfalta.retrofit.models.productos.CrearProductoModel
import com.parana.dobleyfalta.retrofit.models.productos.ProductoApiModel
import retrofit2.Response

class ProductosRepository {
    private val api = RetrofitClientProductos.productosApiService

    suspend fun obtenerProductos(): List<ProductoApiModel> {
        return api.getProductos()
    }

    suspend fun obtenerProductoPorId(id: Int): ProductoApiModel {
        return api.getProductoPorId(id)
    }

    suspend fun actualizarProducto(id: Int, producto: CrearProductoModel): ProductoApiModel {
        return api.updateProducto(id, producto)
    }

    suspend fun crearProducto(producto: CrearProductoModel): Response<CrearProductoModel> {
        return api.crearProducto(producto)
    }

    suspend fun eliminarProducto(id: Int): Boolean {
        val response = api.eliminarProducto(id)
        return response.isSuccessful
    }

}