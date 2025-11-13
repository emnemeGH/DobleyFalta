package com.parana.dobleyfalta.retrofit.repositories

import com.parana.dobleyfalta.retrofit.clients.RetrofitClientProductos
import com.parana.dobleyfalta.retrofit.models.productos.CrearProductoModel
import com.parana.dobleyfalta.retrofit.models.productos.ProductoModel
import retrofit2.Response

class ProductosRepository {
    private val api = RetrofitClientProductos.productosApiService

    suspend fun obtenerProductos(): List<ProductoModel> {
        return api.getProductos()
    }

    suspend fun obtenerProductoPorId(id: Int): ProductoModel {
        return api.getProductoPorId(id)
    }

    suspend fun actualizarProducto(id: Int, producto: CrearProductoModel): ProductoModel {
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