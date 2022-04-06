package com.example.mobiiliprojektir9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {

    lateinit var navController : NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            MobiiliprojektiR9Theme {
                
                navController = rememberNavController()
                SetUpNavigation(navController = navController)

            }
        }
    }
}






