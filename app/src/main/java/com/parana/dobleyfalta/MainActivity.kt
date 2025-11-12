package com.parana.dobleyfalta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.parana.dobleyfalta.retrofit.RetrofitInitializer
import com.parana.dobleyfalta.ui.theme.DobleYFaltaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitInitializer.initAll(applicationContext)

        setContent {
            val mainViewModel: MainViewModel = viewModel()
            DobleYFaltaTheme {
                MainScreenWithBottomNav(mainViewModel)
            }
        }
    }
}

@Composable
fun MainScreenWithBottomNav(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute?.startsWith("admin") == false) {
                AppBottomNavigationBar(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { innerPadding ->
        // Coloca el NavHost en el slot de content y pasa el padding a los composables hijos
        AppNavHost(
            navController = navController,
            innerPadding = innerPadding,
            mainViewModel = mainViewModel
        )
    }
}

