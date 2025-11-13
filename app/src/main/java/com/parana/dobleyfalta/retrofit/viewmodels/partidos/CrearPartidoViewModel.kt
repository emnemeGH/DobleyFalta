package com.parana.dobleyfalta.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.partidos.CrearPartidoModel
// 💡 IMPORTANTE: El backend devuelve PartidoModel, no PartidoDTOModel
import com.parana.dobleyfalta.retrofit.models.partidos.PartidoModel
import com.parana.dobleyfalta.retrofit.repositories.PartidosRepository // 💡 Importar el Repositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 💡 Inyectamos el PartidosRepository. Usamos un valor por defecto para CompositionLocalProvider si es necesario.
class CrearPartidoViewModel( private val repository: PartidosRepository = PartidosRepository() ) : ViewModel() {

    private val _estadoPartido = MutableStateFlow("proximo") // valor inicial
    val estadoPartido: StateFlow<String> = _estadoPartido

    // 💡 Cambiamos el tipo a PartidoModel, ya que es lo que devuelve la API y el repositorio.
    private val _partidoCreado = MutableStateFlow<PartidoModel?>(null)
    val partidoCreado: StateFlow<PartidoModel?> = _partidoCreado

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun setError(mensaje: String?) {
        _error.value = mensaje
    }

    // Función para limpiar solo el estado de error de la API (no el error del campo)
    fun clearError() {
        _error.value = null
    }

    fun cambiarEstado(nuevoEstado: String) {
        _estadoPartido.value = nuevoEstado
    }

    fun crearPartido(partido: CrearPartidoModel, onSuccess: (() -> Unit)? = null) {
        // Limpiar estados antes de la llamada
        clearError()
        _partidoCreado.value = null

        viewModelScope.launch {
            try {
                // 💡 LLAMADA REAL AL REPOSITORIO
                val response = repository.crearPartido(partido)

                if (response.isSuccessful) {
                    val nuevoPartido = response.body()
                    if (nuevoPartido != null) {
                        _partidoCreado.value = nuevoPartido
                        _estadoPartido.value = "ok"
                        onSuccess?.invoke()
                    } else {
                        // Respuesta 2xx sin cuerpo (aunque la API debería devolver el modelo)
                        setError("Error: Respuesta exitosa, pero el servidor no devolvió el partido creado.")
                    }
                } else {
                    // Manejo de errores 4xx o 5xx
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido del servidor."
                    val errorMessage = "Error ${response.code()}: $errorBody"
                    setError(errorMessage)
                }

            } catch (e: Exception) {
                // Manejo de errores de red (IOException) o parseo (JsonParseException)
                val mensajeError = "Error de red/conexión: ${e.message}"
                setError(mensajeError)
            }
        }
    }
}