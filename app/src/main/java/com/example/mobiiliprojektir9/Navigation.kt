package com.example.mobiiliprojektir9

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth

/*
NAVIGAATION NÄYTETTÄVIEN SIVUJEN MÄÄRITYS
Annetaan Screens luokasta route ja sen jälkeen sivu, joka halutaan avata annetulla routella
Jos sivulta täytyy päästä vielä eteenpäin toiselle sivulle, välitetään sille navController
Esim. LoginPagessa: onClick = { navcontroller.navigate(Screens.RegisterAs.route) }
HUOM! Jotta navigaatio toimii, täytyy "sivu" olla composable funktio. Luokan kutsu ei toimi.
*/

@Composable
fun SetUpNavigation(navController : NavHostController, auth: FirebaseAuth) {
    NavHost(navController = navController, startDestination = Screens.Login.route)
    {
        composable(
            route = Screens.Login.route
        ){
            Login(navController = navController, auth = auth)
        }
        composable(
            route = Screens.RegisterAs.route
        ){
            RegisterAs(navController = navController)
        }
        composable(
            route = Screens.RegisterDriver.route
        ){
            RegisterDriver(navController = navController)
        }
        composable(
            route = Screens.RegisterOrganizer.route
        ){
            RegisterCoordinator(navController = navController)
        }
        composable(
            route = Screens.OpenOrders.route
        ){
            val orders : List<Order> = emptyList() //onko parempi tapa?
            OpenDeliveries(orders = orders, navController = navController, auth = auth)
        }
        /*composable(
            route = Screens.CreateJobs.route
        ){

            CreateJobs(navController = navController)
        }*/
    }
}