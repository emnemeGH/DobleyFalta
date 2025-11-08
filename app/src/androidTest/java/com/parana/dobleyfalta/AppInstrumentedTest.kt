package com.parana.dobleyfalta

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class AppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun login_exitoso_redirige_aPerfil() {

        composeTestRule.onNodeWithTag("login-icon").performClick()

        Thread.sleep(2000)

        composeTestRule.onNodeWithText("Email").performTextInput("em@gm.com")

        composeTestRule.onNodeWithText("Contraseña").performTextInput("123456")

        Thread.sleep(2000)

        composeTestRule.onNodeWithTag("loginBoton").performClick()

        composeTestRule.onNodeWithTag("menu").performClick()

        composeTestRule.onNodeWithTag("menuEquipos").performClick()

        Thread.sleep(2000)

        composeTestRule.onAllNodesWithTag("equipoCard")[1].performClick()

        Thread.sleep(2000)

        //composeTestRule.onNodeWithTag("textoUbiacion").assertIsDisplayed()

        Thread.sleep(4000)

        //Esto es para volver para atras, requiere que este en el hilo principal
        composeTestRule.activity.runOnUiThread {
            composeTestRule.activity.onBackPressedDispatcher.onBackPressed()
        }

        Thread.sleep(2000)

        composeTestRule.onAllNodesWithTag("editarEquipo")[1].assertExists().performClick()

        Thread.sleep(2000)

        composeTestRule.onAllNodesWithTag("campoEditarEquipo")[0].performTextClearance()

        composeTestRule.onAllNodesWithTag("campoEditarEquipo")[1].performTextClearance()

        composeTestRule.onAllNodesWithTag("campoEditarEquipo")[2].performTextClearance()

        composeTestRule.onAllNodesWithTag("campoEditarEquipo")[3].performTextClearance()

        Thread.sleep(2000)

        composeTestRule.onNodeWithTag("botonGuardar").performClick()

        Thread.sleep(2000)

        composeTestRule.onAllNodesWithTag("campoEditarEquipo")[0].performTextReplacement("Real Madrid")

        composeTestRule.onAllNodesWithTag("campoEditarEquipo")[1].performTextReplacement("Diamante")

        composeTestRule.onAllNodesWithTag("campoEditarEquipo")[2].performTextReplacement("Aménabar 723")

        composeTestRule.onAllNodesWithTag("campoEditarEquipo")[3].performTextReplacement("https://ejemplo.com/logo.png")

        Thread.sleep(2000)

        composeTestRule.onNodeWithTag("botonGuardar").performClick()

        Thread.sleep(2000)

        composeTestRule.onAllNodesWithTag("eliminarEquipo")[0].performClick()

        Thread.sleep(2000)

        composeTestRule.onNodeWithTag("confirmarEliminar").performClick()

        Thread.sleep(1000)

        composeTestRule.onNodeWithTag("menu").performClick()

        Thread.sleep(2000)

        composeTestRule.onNodeWithTag("menuNoticias").performClick()

        Thread.sleep(2000)

        composeTestRule.onAllNodesWithTag("noticiaCard")[0].performClick()

        Thread.sleep(2000)

        composeTestRule.onNodeWithTag("volverANoticias").performClick()

        Thread.sleep(2000)

        composeTestRule.onAllNodesWithTag("editarNoticia")[0].performClick()
    }
}

//performTextClearance()
//Borra todo el texto actual de un TextField o BasicTextField.
//Simula la acción de “seleccionar todo + suprimir”.
//Lo deja vacío.

//performTextReplacement(newText: String)
//Reemplaza todo el contenido actual por el texto nuevo que le pases.
//Es como si hicieras: seleccionar todo → escribir lo nuevo.

//@RunWith(AndroidJUnit4::class)
//Es un anotación de JUnit que le dice al framework qué “runner” usar para ejecutar los tests.
//AndroidJUnit4 es el runner que permite ejecutar tests en un dispositivo o emulador Android,
//porque necesita acceso al framework de Android (actividades, contextos, vistas, etc.).
//Sin esto, el test no podría interactuar con Activities, Composables o Views en un dispositivo real o emulado.

//@get:Rule
//En JUnit, una regla (Rule) es un objeto que permite ejecutar código antes y después de cada test automáticamente.
//Por ejemplo, puede:
//Abrir una base de datos antes de probar y cerrarla después.
//Inicializar tu Activity antes de probar la UI.
//Tomar capturas de pantalla si un test falla.
//@get:Rule le dice a JUnit que esa propiedad (variable) debe comportarse como una regla.
//Cada regla se ejecuta antes y después de cada test individual (cada @Test).
//“Antes” significa que la regla puede inicializar cosas que el test necesita
// (por ejemplo, abrir una Activity, crear una carpeta temporal, inicializar la UI de Compose, etc.).
//“Después” significa que la regla puede limpiar o deshacer lo que inicializó, así no queda nada persistente entre tests.
//Por ejemplo:
//@get:Rule
//val reglaTemporal = TemporaryFolder()
//
//@Test
//fun testUno() {
//    val archivo = reglaTemporal.newFile("archivo1.txt")
//    // archivo existe solo durante este test
//}
//
//@Test
//fun testDos() {
//    val archivo = reglaTemporal.newFile("archivo2.txt")
//    // archivo1.txt ya no existe, regla borró todo después del testUno
//}
//En neustro codigo:
//val composeTestRule = createAndroidComposeRule<MainActivity>()
//Antes del test: lanza la MainActivity y prepara la UI.
//Después del test: destruye la Activity y limpia la UI para que el siguiente test empiece “desde cero”.

//composeTestRule.onNodeWithText("Email").performTextInput("em")
//esto es código de Espresso para Jetpack Compose (Compose Testing)
//composeTestRule -> Es la regla que inicializa la UI de Compose para el test.
//Permite buscar elementos, simular interacciones y verificar resultados en la pantalla.

//onNodeWithText("Email") -> Busca un “nodo” (componente de UI) que tenga exactamente el texto "Email".
// En la pantalla de login, sería el OutlinedTextField cuyo label = { Text("Email") }.

// performTextInput("em") -> Simula que el usuario escribe "em" dentro de ese TextField.
// Es como si alguien tocara el campo y tecleara esas letras en la app.

//assertIsDisplayed()
//Verifica que ese nodo (componente de UI) esté visible en pantalla en ese momento del test.
//Si no está visible, el test fallará.
//Basicamente busca el texto Mi Perfil
//Si el nodo sí está visible, el test sigue ejecutándose.
//Si el nodo no está visible, lanza una AssertionError y el test falla.