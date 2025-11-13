package com.parana.dobleyfalta.retrofit.viewmodels.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.productos.CrearProductoModel
import com.parana.dobleyfalta.retrofit.models.productos.ProductoModel
import com.parana.dobleyfalta.retrofit.repositories.ProductosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductosViewModel : ViewModel() {

    private val repository = ProductosRepository()

    // Estado de la lista de productos
    private val _productos = MutableStateFlow<List<ProductoModel>>(emptyList())
    val productos = _productos.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    // Estado para errores o mensajes
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    // Cargar todos los productos
    fun cargarProductos() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val lista = repository.obtenerProductos()
                _productos.value = lista
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al cargar productos: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // Crear producto
    fun crearProducto(producto: CrearProductoModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.crearProducto(producto)
                if (response.isSuccessful) {
                    cargarProductos()
                    onSuccess()
                } else {
                    _error.value = "Error al crear producto: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
                e.printStackTrace()
            } finally {
                _loading.value = false
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