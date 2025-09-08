package com.parana.dobleyfalta

import com.parana.dobleyfalta.cuentas.validarCampoNoVacio
import com.parana.dobleyfalta.cuentas.validarLongitudContraseña
import com.parana.dobleyfalta.cuentas.validarUnicidad
import org.junit.Assert.*
import org.junit.Test

class RegistroTest {

    private val usuariosExistentes = listOf("juan", "maria")
    private val emailsExistentes = listOf("juan@mail.com", "maria@mail.com")

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

    @Test
    fun validarUnicidad_retornaError_siYaExiste() {
        val resultadoUsuario = validarUnicidad("juan", usuariosExistentes, "usuario")
        val resultadoEmail = validarUnicidad("juan@mail.com", emailsExistentes, "email")
        assertEquals("El usuario ya existe", resultadoUsuario)
        assertEquals("El email ya existe", resultadoEmail)
    }

    @Test
    fun validarUnicidad_retornaNull_siNoExiste() {
        val resultadoUsuario = validarUnicidad("nuevoUsuario", usuariosExistentes, "usuario")
        val resultadoEmail = validarUnicidad("nuevo@mail.com", emailsExistentes, "email")
        assertNull(resultadoUsuario)
        assertNull(resultadoEmail)
    }
}

//assertNull es una función de JUnit que se usa para verificar que un valor sea null.
//Sintaxis:
//assertNull(valor)
//valor → la variable o expresión que estás probando.
//El test pasa si valor es null.
//El test falla si valor tiene algún valor distinto de null.