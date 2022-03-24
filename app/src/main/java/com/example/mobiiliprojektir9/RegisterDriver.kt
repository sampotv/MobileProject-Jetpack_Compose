package com.example.mobiiliprojektir9

import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.google.firebase.firestore.FirebaseFirestore


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



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterDriver(navController: NavController){

    var emailState by remember {
        mutableStateOf("")
    }
    var passwordState by remember {
        mutableStateOf("")
    }
    var phonenumState by remember {
        mutableStateOf("")
    }
    var companyState by remember {
        mutableStateOf("")
    }
    val db = FirebaseFirestore.getInstance()
    val isSaved = remember { mutableStateOf(false)}

    val keyboardController = LocalSoftwareKeyboardController.current
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
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        OutlinedTextField(
            value = passwordState,
            label = {
                Text("Salasana")
            },
            onValueChange = {
                passwordState = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        OutlinedTextField(
            value = phonenumState,
            label = {
                Text("Puhelinnumero")
            },
            onValueChange = {
                phonenumState = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }),
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
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            var driverData = DriverData().apply {
                email = emailState.toString()
                password = passwordState.toString()
                phoneNum = phonenumState.toString()
                company = companyState.toString()
            }
            saveDriverData(driverData, db)
//            Toast.makeText(
//                context,
//                "$emailState, $passwordState, $companyState",
//                Toast.LENGTH_LONG
//            ).show()
        }) {
            Text("Rekisteröidy")
        }
    }
}

fun saveDriverData(driverData: DriverData, db: FirebaseFirestore) {
    db.collection("drivers")
        .add(driverData)
        .addOnSuccessListener { documentReference ->
            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w(TAG, "Error adding document", e)
        }
}

//@Preview()
//@Composable
//fun RegisterDriverPreview() {
//    MobiiliprojektiR9Theme {
//        RegisterDriver()
//    }
//}