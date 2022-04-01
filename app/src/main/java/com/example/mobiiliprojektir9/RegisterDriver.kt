package com.example.mobiiliprojektir9

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


//var user: FirebaseUser? = null

//@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterDriver(navController: NavController){
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var emailState by rememberSaveable {
        mutableStateOf("")
    }
    var passwordState by rememberSaveable {
        mutableStateOf("")
    }
    var companyState by rememberSaveable {
        mutableStateOf("")
    }
//    val scaffoldState = rememberScaffoldState()
//    val scope = rememberCoroutineScope()
//    val keyboardController = LocalSoftwareKeyboardController.current
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email,imeAction = ImeAction.Done),
//                keyboardActions = KeyboardActions(
//                    onDone = {keyboardController?.hide()}),
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
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,imeAction = ImeAction.Done),
//                keyboardActions = KeyboardActions(
//                    onDone = {keyboardController?.hide()}),
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
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                keyboardActions = KeyboardActions(
//                    onDone = {keyboardController?.hide()}),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                auth.createUserWithEmailAndPassword(
                    emailState.trim(),
                    passwordState.trim()
                ).addOnCompleteListener(){ task ->
                    if (task.isSuccessful){
                        Log.d("AUTH", "Success!")
                    }else{
                        Log.d("AUTH", "Failed: ${task.exception}")
                    }
                }
            }) {
                Text("Rekisteröidy")
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