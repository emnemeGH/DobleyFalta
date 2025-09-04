package com.parana.dobleyfalta

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var rolUsuario = mutableStateOf<String?>(null)
        private set

    fun setRol(rol: String) {
        rolUsuario.value = rol
    }
}
