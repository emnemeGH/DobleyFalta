package com.parana.dobleyfalta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavHost()
        }
    }
}

@Composable
fun PantallaPrincipal(navController: androidx.navigation.NavController) {
    androidx.compose.material3.Button(
        onClick = { navController.navigate("equipos") },
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Ver Equipos")
    }
}
