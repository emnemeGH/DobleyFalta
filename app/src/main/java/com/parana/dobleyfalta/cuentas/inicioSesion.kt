package com.parana.dobleyfalta.cuentas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.parana.dobleyfalta.R

import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {
    val DarkBlue = Color(0xFF102B4E)
    val PrimaryOrange = Color(0xFFFF6600)

    Column( //dentro de los parentesis de column van los parametros, despues van las llaves donde van todos los elemntos que estan dentro de la columna
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            //Aquí 32.dp convierte el número 32 en un valor Dp que padding() entiende. Es equivalente a: 32.toDp() (internamente).
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo_transparent),
            contentDescription = "Logo",
            tint = Color.Unspecified,
            modifier = Modifier.size(96.dp)
        )
    }
}

//Glosario

//@Composable
//Es una anotación de Jetpack Compose.
//Significa que esta función dibuja UI (interfaz de usuario) en la pantalla.
//Compose funciona declarando funciones que "pintan" componentes en lugar de usar XML como antes

//fun LoginScreen(...)
//Es simplemente una función de Kotlin llamada LoginScreen.
//En Compose, en lugar de hacer actividades con XML, defines pantallas como funciones.

//navController: NavController
//Aquí la función recibe un parámetro llamado navController.
//NavController es un objeto que controla la navegación entre pantallas en Jetpack Compose
// (por ejemplo, pasar de la pantalla de login a la pantalla de registro).
//Dentro de la función LoginScreen, puedes usarlo para hacer algo como:
//navController.navigate("HomeScreen")
//Eso cambiaría de pantalla al HomeScreen.

//Column
//En Compose, Column es un composable que organiza sus elementos hijos uno debajo del otro, en forma de columna (vertical).
//Es el equivalente a un LinearLayout con orientación vertical en XML.

//modifier = Modifier.fillMaxSize()
// "modifier" es la variable y el parámetro de la función Column, Modifier.fillMaxSize() es el objeto que le pasamos a la variable

//.fillMaxSize() es una función de Modifier que le dice al composable que ocupe todo el espacio disponible
// tanto en ancho como en alto dentro de su contenedor padre.

//horizontalAlignment → Alinea a los hijos horizontalmente (izquierda, centro, derecha).
//Valores comunes: Alignment.Start (izquierda) Alignment.CenterHorizontally (centro) Alignment.End (derecha)

//verticalArrangement → Define cómo se distribuyen los hijos verticalmente dentro de la columna.
//Valores comunes: Arrangement.Top (arriba) Arrangement.Center (centro) Arrangement.Bottom (abajo) Arrangement.SpaceBetween, Arrangement.SpaceAround, etc.

//painter
//Define qué imagen o icono mostrar. Aquí se usa painterResource(id = R.drawable.ic_basketball_ball) para cargar un recurso drawable.