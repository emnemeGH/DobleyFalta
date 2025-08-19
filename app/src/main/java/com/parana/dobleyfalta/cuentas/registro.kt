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
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parana.dobleyfalta.R

@Composable
fun RegistroScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = Color(0xFFFF6600)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
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
            text = "Crear Cuenta",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 32.dp)
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Nombre de Usuario", color = LightGrey) },
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
        OutlinedTextField(
            value = "",
            onValueChange = {},
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
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Contraseña", color = LightGrey) },
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
        Button(
            onClick = { navController.navigate("principal") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Text(text = "Crear Cuenta", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Text(
            text = "¿Ya tienes cuenta? Inicia sesión",
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { navController.navigate("login") },
            color = PrimaryOrange,
            fontSize = 14.sp,
            textDecoration = TextDecoration.Underline
        )
    }
}

//GLOSARIO

//colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
// elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)