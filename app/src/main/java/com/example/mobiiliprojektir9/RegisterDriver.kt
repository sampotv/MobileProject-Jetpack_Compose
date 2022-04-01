package com.example.mobiiliprojektir9

import android.app.Activity.RESULT_OK
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch



//class RegisterDriver : ComponentActivity() {
//
//    @OptIn(ExperimentalComposeUiApi::class)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            MobiiliprojektiR9Theme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    RegisterAsDriver()
//                }
//            }
//        }
//    }
//}


//var user: FirebaseUser? = null


@Composable
fun RegisterDriver(navController: NavController) {

    var emailState by remember {

        mutableStateOf("")
    }
    var passwordState by rememberSaveable {
        mutableStateOf("")
    }
    var companyState by rememberSaveable {
        mutableStateOf("")
    }
    var phoneNumState by rememberSaveable {
        mutableStateOf("")
    }
//    val scaffoldState = rememberScaffoldState()
//    val scope = rememberCoroutineScope()
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        scaffoldState = scaffoldState
//    ) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Rekisteröidy kuljettajana",
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = emailState,
            label = {
                Text("Email")
            },
            onValueChange = {
                emailState = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        OutlinedTextField(
            value = passwordState,
            label = { Text("Salasana") },
            onValueChange = {
                passwordState = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),

            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)

        )
        OutlinedTextField(
            value = companyState,
            label = {
                Text("Yritys")
            },
            onValueChange = {
                companyState = it
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        OutlinedTextField(
            value = phoneNumState,
            label = {
                Text("Puhelinnumero")
            },
            onValueChange = {
                phoneNumState = it
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            register(
                passwordState,
                emailState,
                phoneNumState,
                companyState
            )

        }) {
            Text("Rekisteröidy")

        }
    }
}
fun saveDriverData(driverData: DriverData) {
    val db = FirebaseFirestore.getInstance()
    db.collection("drivers")
        .add(driverData)
        .addOnSuccessListener { documentReference ->
            Log.d(
                "saveDriverData",
                "DocumentSnapshot added with ID: ${documentReference.id}"
            )
        }
        .addOnFailureListener { e ->
            Log.w("SaveDriverData", "Error adding document", e)
        }
}

fun register(
    registerPassword: String,
    registerEmail: String,
    registerPhoneNum: String,
    registerCompany: String
) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    Log.d("auth", "create user")
    auth.createUserWithEmailAndPassword(
        registerEmail.trim(),
        registerPassword.trim()
    ).addOnCompleteListener() { task ->
        if (task.isSuccessful) {
            val user = auth.currentUser
            if (user != null) {
                Log.d("AUTH", "success! ${user.uid}")
                var driverData = DriverData().apply {
                    driverId = user.uid
                    email = registerEmail
                    password = registerPassword
                    phoneNum = registerPhoneNum
                    company = registerCompany
                }
                saveDriverData(driverData)
            }

        } else {
            Log.d("AUTH", "Failed: ${task.exception}")
        }
    }
}

//@Preview()
//@Composable
//fun RegisterDriverPreview() {
//    MobiiliprojektiR9Theme {
//        RegisterDriver()
//    }
//}