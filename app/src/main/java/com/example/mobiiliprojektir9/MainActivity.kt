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
import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class MainActivity : ComponentActivity() {


    lateinit var navController : NavHostController

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)

        setContent {
            MobiiliprojektiR9Theme {

                navController = rememberNavController()
                SetUpNavigation(navController = navController, auth = auth)

                //jos käyttäjä on kirjautuneena, ohjaa oikealle sivulle
                val currentUser = auth.currentUser
                if(currentUser != null){
                    //Täytyy tehdä kuljettajan ja ajojärjestelijän erottelu
                    navController.navigate(route = Screens.OpenOrders.route)
                }

            }
        }
    }
}






