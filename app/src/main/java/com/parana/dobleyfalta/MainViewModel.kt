package com.parana.dobleyfalta

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var rolUsuario = mutableStateOf<String?>(null)
        private set

    // Lista de ligas compartida entre pantallas
    private val _ligas = mutableStateListOf("Liga A", "Liga B")
    val ligas: List<String> get() = _ligas

    fun setRol(rol: String) {
        rolUsuario.value = rol
    }

    fun agregarLiga(nombre: String) {
        _ligas.add(nombre)
    }

    fun eliminarLiga(nombre: String) {
        _ligas.remove(nombre)
    }

}
