package com.example.mobiiliprojektir9

import android.Manifest.permission.CAMERA
import android.Manifest.permission_group.CAMERA
import android.content.pm.PackageManager
import android.hardware.SensorPrivacyManager.Sensors.CAMERA
import android.media.MediaRecorder.VideoSource.CAMERA
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.proto.TargetGlobal
import java.util.jar.Manifest


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

                                    navController.navigate("${Screens.CreateJob.route}/${userId}")

                                } else {

                                    navController.navigate("${Screens.DriverSite.route}/${userId}")

                                }
                            }
                        })
                }
            }
        }
    }
}




