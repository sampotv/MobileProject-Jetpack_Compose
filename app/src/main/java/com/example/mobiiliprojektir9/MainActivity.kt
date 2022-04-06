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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
//import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {


    lateinit var navController: NavHostController

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)

        setContent {
            MobiiliprojektiR9Theme {

                val currentUser = auth.currentUser
                navController = rememberNavController()
                SetUpNavigation(navController = navController, auth = auth)

                //jos käyttäjä on kirjautuneena, ohjaa oikealle sivulle

                if (currentUser != null) {

                    val userId = currentUser.uid
                    var db = FirebaseFirestore.getInstance()

                    db.collection("drivers").whereEqualTo("driverId", userId)
                        .limit(1).get()
                        .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                            if (task.isSuccessful) {
                                val isEmpty = task.result.isEmpty
                                if (isEmpty) {
                                    //väliaikainen route, pitää olla järjestelijän etusivu
                                    navController.navigate(route = Screens.RegisterAs.route)
                                } else {

                                    navController.navigate(route = Screens.OpenOrders.route)

                                    //navController.navigate("${Screens.DriverSite.route}/${userId}")
                                }
                            }
                        })
                }

            }
        }
    }
}






