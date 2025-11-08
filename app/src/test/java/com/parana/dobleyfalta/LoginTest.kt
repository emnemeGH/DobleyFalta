package com.parana.dobleyfalta


import com.parana.dobleyfalta.cuentas.validarCampos
import org.junit.Assert.*
import org.junit.Test

class LoginUnitTest {

    @Test
    fun `devuelve error si el email está vacío`() {
        val resultado = validarCampos("", "1234")
        assertEquals("El email es obligatorio", resultado)
    }

    @Test
    fun `devuelve error si la contraseña está vacía`() {
        val resultado = validarCampos("user@test.com", "")
        assertEquals("La contraseña es obligatoria", resultado)
    }

    @Test
    fun `devuelve null si ambos campos son válidos`() {
        val resultado = validarCampos("user@test.com", "1234")
        assertNull(resultado)
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