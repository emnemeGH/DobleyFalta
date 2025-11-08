package com.parana.dobleyfalta

import com.parana.dobleyfalta.cuentas.validarCampoNoVacio
import com.parana.dobleyfalta.cuentas.validarLongitudContraseña
import org.junit.Assert.*
import org.junit.Test

class RegistroTest {

    @Test
    fun validarCampoNoVacio_retornaError_siEstaVacio() {
        val resultado = validarCampoNoVacio("", "Usuario")
        assertEquals("Usuario es obligatorio", resultado)
    }

    @Test
    fun validarCampoNoVacio_retornaNull_siTieneValor() {
        val resultado = validarCampoNoVacio("Emanuel", "Usuario")
        assertNull(resultado)
    }

    @Test
    fun validarLongitudContraseña_retornaError_siMenorA6() {
        val resultado = validarLongitudContraseña("123")
        assertEquals("La contraseña debe tener al menos 6 caracteres", resultado)
    }

    @Test
    fun validarLongitudContraseña_retornaNull_siCumple() {
        val resultado = validarLongitudContraseña("123456")
        assertNull(resultado)
    }
}

//assertNull es una función de JUnit que se usa para verificar que un valor sea null.
//Sintaxis:
//assertNull(valor)
//valor → la variable o expresión que estás probando.
//El test pasa si valor es null.
//El test falla si valor tiene algún valor distinto de null.