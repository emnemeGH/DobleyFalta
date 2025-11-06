package com.parana.dobleyfalta.retrofit.viewmodels.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.productos.CrearProductoModel
import com.parana.dobleyfalta.retrofit.models.productos.ProductoApiModel
import com.parana.dobleyfalta.retrofit.repositories.ProductosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductosViewModel : ViewModel() {

    private val repository = ProductosRepository()

    // Estado de la lista de productos
    private val _productos = MutableStateFlow<List<ProductoApiModel>>(emptyList())
    val productos: StateFlow<List<ProductoApiModel>> = _productos

    // Estado para errores o mensajes
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Cargar todos los productos
    fun cargarProductos() {
        viewModelScope.launch {
            try {
                val lista = repository.obtenerProductos()
                _productos.value = lista
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al cargar productos: ${e.message}"
            }
        }
    }

    // Crear producto
    fun crearProducto(nuevo: CrearProductoModel) {
        viewModelScope.launch {
            try {
                repository.crearProducto(nuevo)
                cargarProductos() // recargar lista
            } catch (e: Exception) {
                _error.value = "Error al crear producto: ${e.message}"
            }
        }
    }

    // Actualizar producto
    fun actualizarProducto(id: Int, actualizado: CrearProductoModel) {
        viewModelScope.launch {
            try {
                repository.actualizarProducto(id, actualizado)
                cargarProductos()
            } catch (e: Exception) {
                _error.value = "Error al actualizar producto: ${e.message}"
            }
        }
    }

    // Eliminar producto
    fun eliminarProducto(id: Int) {
        viewModelScope.launch {
            try {
                val ok = repository.eliminarProducto(id)
                if (ok) cargarProductos()
                else _error.value = "No se pudo eliminar el producto"
            } catch (e: Exception) {
                _error.value = "Error al eliminar producto: ${e.message}"
            }
        }
    }
}