package com.parana.dobleyfalta

import com.parana.dobleyfalta.cuentas.validarLogin
import org.junit.Assert.*
import org.junit.Test

class LoginUnitTest {

    @Test
    fun validarLogin_devuelveUser_cuandoCredencialesCorrectas() {
        val user = validarLogin("ad", "ad")
        assertNotNull("Se esperaba un User para credenciales correctas", user)
        assertEquals("ad", user?.email)
        assertEquals("admin", user?.rol)
    }

    @Test
    fun validarLogin_devuelveNull_cuandoEmailIncorrecto() {
        val user = validarLogin("noexiste@test.com", "ad")
        assertNull("Se esperaba null si el email no existe", user)
    }

    @Test
    fun validarLogin_devuelveNull_cuandoContrasenaIncorrecta() {
        val user = validarLogin("ad", "wrong")
        assertNull("Se esperaba null si la contraseña es incorrecta", user)
    }

    @Test
    fun validarLogin_devuelveNull_cuandoCamposVacios() {
        val user = validarLogin("", "")
        assertNull("Se esperaba null si los campos están vacíos", user)
    }
}

//assertNotNull("Se esperaba un User para credenciales correctas", user)
//Asegura que User no sea null
//Parámetros:
//"Se esperaba un User para credenciales correctas" → mensaje opcional.
//user → el valor que comprobamos.
//Si user es null, el test falla y muestra ese mensaje.

//assertEquals("ad", user?.email)
//Asegura que el valor real sea igual al esperado.
//Parámetros:
//"ad" → valor esperado.
//user?.email → valor real que devuelve el código.