package com.example.tadpole_g4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.tadpole_g4.navigation.AppNavigation
import com.example.tadpole_g4.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier) {
                val navController = rememberNavController()
                AppNavigation(navController = navController, userViewModel = userViewModel)
            }
        }
    }
}
