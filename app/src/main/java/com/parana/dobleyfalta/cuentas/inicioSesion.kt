package com.parana.dobleyfalta.cuentas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parana.dobleyfalta.R
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val focusManager = LocalFocusManager.current

    var email by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("")}
    var mostrarContraseña by remember { mutableStateOf(false) }

    Column( //dentro de los parentesis de column van los parametros, despues van las llaves donde van todos los elemntos que estan dentro de la columna
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            //Aquí 32.dp convierte el número 32 en un valor Dp que padding() entiende. Es equivalente a: 32.toDp() (internamente).
            .padding(32.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo_transparent),
            contentDescription = "Logo",
            tint = Color.Unspecified,
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = "Doble y Falta App",
            fontSize = 32.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 32.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey,
                unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey,
                focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
//        Spacer sirve para aplicar margenes
        Spacer(
            modifier = Modifier.height(5.dp)
        )
        OutlinedTextField(
            value = contraseña,
            onValueChange = { contraseña = it },
            label = { Text("Contraseña", color = LightGrey) },
            visualTransformation = if (mostrarContraseña) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { mostrarContraseña = !mostrarContraseña }) {
                    Icon(
                        painter = painterResource(id = R.drawable.eyebrow),
                        contentDescription = "Mostrar/Ocultar",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey,
                unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey,
                focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Button(
            onClick = {
                if (email == "admin") {
                    navController.navigate("admin")
                } else {
                    navController.navigate("principal")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
        ) {
            Text("Iniciar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Text(
            text = "¿No tienes cuenta? Regístrate aquí",
            color = PrimaryOrange,
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { navController.navigate("registro") }
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

//LocalFocusManager.current → es una variable especial de Compose que te da acceso al administrador de foco de la pantalla.
//Ese focusManager sirve para manejar qué componente (ej: un TextField) tiene el foco.

//Column
//En Compose, Column es un composable que organiza sus elementos hijos uno debajo del otro, en forma de columna (vertical).
//Es el equivalente a un LinearLayout con orientación vertical en XML.

//modifier = Modifier.fillMaxSize()
// "modifier" es la variable y el parámetro de la función Column, Modifier.fillMaxSize() es el objeto que le pasamos a la variable

//.fillMaxSize() es una función de Modifier que le dice al composable que ocupe todo el espacio disponible
// tanto en ancho como en alto dentro de su contenedor padre.

//.clickable(...) { ... }
//Es un modifier de Compose.
//Se lo aplicás a cualquier componente (ej: Column, Box, etc.) para que responda a un clic.
//Lo que pongas en las llaves { ... } es la acción que se ejecuta al hacer clic.

//indication = null
//Normalmente, .clickable muestra una animación
//Con indication = null, desactivás ese efecto visual.
//Sirve para que parezca que “no pasa nada”, aunque igual se ejecute el código.

//interactionSource = remember { MutableInteractionSource() }
//clickable necesita una interactionSource para manejar estados como “presionado”, “enfocado”, etc.
//En este caso se crea una fuente vacía (MutableInteractionSource()) y se guarda con remember para que no se regenere en cada render.
//Es necesario cuando usás indication = null, porque Compose pide igual un interactionSource.

//clearFocus()
// quita el foco de donde esté (ej: el OutlinedTextField), lo cual hace que se desmarque el borde naranja
// y, si hay teclado abierto, también se oculte.

//horizontalAlignment → Alinea a los hijos horizontalmente (izquierda, centro, derecha).
//Valores comunes: Alignment.Start (izquierda) Alignment.CenterHorizontally (centro) Alignment.End (derecha)

//verticalArrangement → Define cómo se distribuyen los hijos verticalmente dentro de la columna.
//Valores comunes: Arrangement.Top (arriba) Arrangement.Center (centro) Arrangement.Bottom (abajo) Arrangement.SpaceBetween, Arrangement.SpaceAround, etc.

//painter
//Define qué imagen o icono mostrar. Aquí se usa painterResource(id = R.drawable.ic_basketball_ball) para cargar un recurso drawable.

//OutlinedTextField
//Es un composable que permite al usuario ingresar texto.
//Tiene un borde visible (de ahí lo de "Outlined") y admite personalización de colores, forma, etc.

//value
//El texto actual que contiene el campo. Aquí está vacío "".

//onValueChange
//Funcion Lambda que se ejecuta cada vez que cambia el texto. Aquí está vacío {} pero normalmente actualizarías un state.
//Es un parametro obligatorio

//label
//Texto que aparece dentro del borde como etiqueta. Se pasa como composable. Ej: { Text("Email", color = LightGrey) }.
// Seria un placeholder "Email"

//shape
//Forma de los bordes. Ej: RoundedCornerShape(12.dp) hace esquinas redondeadas de 12 dp

//colors	Personaliza colores del campo usando OutlinedTextFieldDefaults.colors(). Puedes cambiar:
//focusedContainerColor → fondo cuando el campo está enfocado
//unfocusedContainerColor → fondo cuando no está enfocado
//focusedBorderColor → color del borde enfocado
//unfocusedBorderColor → color del borde sin enfocar
//cursorColor → color del cursor de escritura |