package com.example.mobiiliprojektir9

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.example.driverssite.DriverSite


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
            BackHandler(false) {

            }
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
           route = Screens.OpenOrders.route + "/{userId}"
        ){ backStackEntry ->
            OpenDeliveries(
                navController = navController,
                backStackEntry.arguments?.getString("userId")
            )

        }
        composable(
            route = Screens.OpenOrdersCoordinator.route + "/{userId}"
        ){ backStackEntry ->
            OpenDeliveriesCoordinator(
                navController = navController,
                backStackEntry.arguments?.getString("userId")
            )

        }
        composable(
            route = Screens.DriverSite.route + "/{userId}",
        ) { backStackEntry ->
            DriverSite(
                navController = navController,
                backStackEntry.arguments?.getString("userId"),
                auth = auth)
            BackHandler(true) {
                // Or do nothing
            }
        }
        composable(
            route = Screens.CreateJob.route + "/{userId}",
        )
        { backStackEntry ->
            CreateJob(navController = navController, backStackEntry.arguments?.getString("userId"))
            BackHandler(true) {
                // Or do nothing
            }
        }
        composable(
            route = Screens.JobDelivered.route + "/{selectedItem}",
        ) { backStackEntry ->
            Receipt(navController = navController, backStackEntry.arguments?.getString("selectedItem"), auth = auth)
        }
        composable(
            route = Screens.JobHistory.route + "/{userId}",
        ) { backStackEntry ->
            ClosedDeliveries(navController = navController, backStackEntry.arguments?.getString("userId"))
        }
        composable(
            route = Screens.JobHistoryCompany.route + "/{userId}",
        ) { backStackEntry ->
            ClosedDeliveriesCompany(navController = navController, backStackEntry.arguments?.getString("userId"))
        }
        /*composable(
            route = Screens.ClosedOrders.route + "/{userId}",
        ) { backStackEntry ->
            ClosedDeliveries(navController = navController, backStackEntry.arguments?.getString("userId"))
        }*/
    }
}
