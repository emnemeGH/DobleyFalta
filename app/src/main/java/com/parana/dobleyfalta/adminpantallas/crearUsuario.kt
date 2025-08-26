package com.parana.dobleyfalta.adminpantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavController
import com.parana.dobleyfalta.R

@Composable
fun CreateUserScreen(navController: NavController) {
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
            ) { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { navController.navigate("admin") },
                colors = ButtonDefaults.buttonColors(containerColor = DarkGrey),
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                // Puedes reemplazar con un ícono de back si quieres
                Text("<", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Text(
                text = "Crear Usuario",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f).padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

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
                cursorColor = PrimaryOrange
            )
        )

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Correo Electrónico", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey,
                unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey,
                focusedBorderColor = PrimaryOrange,
                cursorColor = PrimaryOrange
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
                cursorColor = PrimaryOrange
            )
        )

        OutlinedTextField(
            value = "Empleado",
            onValueChange = {},
            enabled = false,
            label = { Text("Rol", color = LightGrey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGrey,
                unfocusedContainerColor = DarkGrey,
                unfocusedBorderColor = DarkGrey,
                disabledTextColor = Color.Gray,
                disabledContainerColor = DarkGrey
            )
        )

        Button(
            onClick = { navController.navigate("otraPantalla") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Crear Cuenta de Empleado", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
