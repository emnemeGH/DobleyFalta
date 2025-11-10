package com.parana.dobleyfalta

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class AppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun navegacion_por_la_app() {

        composeTestRule.onNodeWithTag("menu").performClick()

        Thread.sleep(2000)

        composeTestRule.onNodeWithTag("login-icon").performClick()

        Thread.sleep(2000)

        composeTestRule.onNodeWithText("Email").performTextInput("em@gm.com")

        composeTestRule.onNodeWithText("Contraseña").performTextInput("123456")

        composeTestRule.onNodeWithTag("loginBoton").performClick()

        Thread.sleep(2000)

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithTag("pantalla-home").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("menu").performClick()

        composeTestRule.onNodeWithTag("menu-equipos").performClick()

        Thread.sleep(2000)

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithTag("equipoCard").fetchSemanticsNodes().isNotEmpty()
        }

        Thread.sleep(4000)

        composeTestRule.onAllNodesWithTag("equipoCard")[0].performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithTag("equipo-detalle").fetchSemanticsNodes().isNotEmpty()
        }

        Thread.sleep(4000)

        composeTestRule.onNodeWithTag("noticias-icon").performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithTag("noticiaCard").fetchSemanticsNodes().isNotEmpty()
        }

        Thread.sleep(4000)

        composeTestRule.onAllNodesWithTag("noticiaCard")[0].performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithTag("noticia-detalle").fetchSemanticsNodes().isNotEmpty()
        }

        Thread.sleep(4000)

        composeTestRule.onNodeWithTag("jornadas-icon").performClick()

        Thread.sleep(4000)

        composeTestRule.onNodeWithTag("tabla-icon").performClick()

        Thread.sleep(4000)

        composeTestRule.onNodeWithTag("menu").performClick()

        Thread.sleep(2000)

        composeTestRule.onNodeWithTag("menu-tienda").performClick()

        Thread.sleep(4000)

        composeTestRule.onNodeWithTag("menu").performClick()

        Thread.sleep(2000)

        composeTestRule.onNodeWithTag("miperfil-icon").performClick()


        //Esto es para volver para atras, requiere que este en el hilo principal
//        composeTestRule.activity.runOnUiThread {
//            composeTestRule.activity.onBackPressedDispatcher.onBackPressed()
//        }
    }
}

