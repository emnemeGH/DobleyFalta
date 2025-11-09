package com.parana.dobleyfalta.retrofit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.jornadas.CrearJornadaModel
import com.parana.dobleyfalta.retrofit.models.jornadas.JornadaModel
import com.parana.dobleyfalta.retrofit.repositories.JornadasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JornadasViewModel : ViewModel() {

    private val repository = JornadasRepository()

    // Lista de jornadas
    private val _jornadas = MutableStateFlow<List<JornadaModel>>(emptyList())
    val jornadas = _jornadas.asStateFlow()

    // Jornada actual seleccionada
     val _jornadaActual = MutableStateFlow(1)
    val jornadaActual = _jornadaActual.asStateFlow()

    // Max jornada disponible
    private val _maxJornada = MutableStateFlow<Int?>(null)
    val maxJornada = _maxJornada.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun clearError() {
        _error.value = null
    }

    // RENOMBRAR Y MODIFICAR: Reemplazamos 'obtenerJornadas()' por una función que requiere idLiga
    fun cargarJornadasDeLiga(idLiga: Int) { // 👈 Ahora requiere el ID de la Liga
        viewModelScope.launch {
            try {
                _loading.value = true
                // Usamos el repositorio con el filtro de Liga
                val lista = repository.obtenerJornadasPorLiga(idLiga)
                _jornadas.value = lista

                // Calculamos el máximo de jornadas correctamente
                _maxJornada.value = lista.maxOfOrNull { it.numero } ?: 1

                // Mantenemos la lógica de resetear si es necesario
                if (_jornadaActual.value > (_maxJornada.value ?: 1)) {
                    _jornadaActual.value = 1
                }
            } catch (e: Exception) {
                _error.value = "Error al obtener jornadas de la liga $idLiga: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // AÑADIR: Función para establecer la jornada inicial al navegar desde Ligas
    fun setJornadaActual(jornadaNumero: Int) {
        // Establecer solo si es una jornada válida
        val max = _maxJornada.value ?: jornadaNumero
        if (jornadaNumero >= 1 && jornadaNumero <= max) {
            _jornadaActual.value = jornadaNumero
        }
    }

    // Navegar a la siguiente jornada (SE MANTIENE, usa _jornadaActual y _maxJornada)
    fun avanzarJornada() {
        _maxJornada.value?.let { max ->
            if (_jornadaActual.value < max) {
                _jornadaActual.value += 1
            }
        }
    }

    // Navegar a la jornada anterior (SE MANTIENE)
    fun retrocederJornada() {
        if (_jornadaActual.value > 1) {
            _jornadaActual.value -= 1
        }
    }

    fun crearJornada(jornada: CrearJornadaModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.crearJornada(jornada)
                if (response.isSuccessful) {
                    //obtenerJornadas() // refrescar lista y maxJornada
                    onSuccess()
                } else {
                    _error.value = "Error al crear jornada (${response.code()})"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun editarJornada(id: Int, jornada: CrearJornadaModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.editarJornada(id, jornada)
                if (response.isSuccessful) {
                    //obtenerJornadas()
                    onSuccess()
                } else {
                    _error.value = "Error al editar jornada (${response.code()})"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun eliminarJornada(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val ok = repository.eliminarJornada(id)
                if (ok) {
                    //obtenerJornadas()
                    onSuccess()
                } else _error.value = "Error al eliminar jornada"
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
