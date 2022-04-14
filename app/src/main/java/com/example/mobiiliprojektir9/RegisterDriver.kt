package com.example.mobiiliprojektir9

import android.content.Context
import android.util.Log
import android.widget.Toast
import android.service.controls.ControlsProviderService.TAG
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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


//var user: FirebaseUser? = null


@Composable
fun RegisterDriver(navController: NavController) {
    val context = LocalContext.current
    var emailErrorState by remember { mutableStateOf(false)}
    var passwordErrorState by remember { mutableStateOf(false)}
    var passwordVisibility by remember { mutableStateOf(true)}
    var companyErrorState by remember { mutableStateOf(false)}
    var phoneNumErrorState by remember { mutableStateOf(false)}

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
            onValueChange = {
                if (emailErrorState){
                    emailErrorState = false
                }
                emailState = it
            },
            isError = emailErrorState,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            maxLines = 1,
            label = {
                Text("Sähköpostiosoite")
            },
        )
        if(emailErrorState){
            Text(text = "Tarkista sähköpostiosoite", color = Color.Red)
        }
        OutlinedTextField(
            value = passwordState,
            onValueChange = {
                if(passwordErrorState) {
                    passwordErrorState = false
                }
                passwordState = it
            },
            isError = passwordErrorState,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            maxLines = 1,
            label = { Text("Salasana väh. 6 merkkiä") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_visibility),
                        contentDescription = "visibility"
                    )
                }
            }
        )
        if(passwordErrorState) {
            Text(text = "Tarkista salasana")
        }
        OutlinedTextField(
            value = companyState,
            onValueChange = {
                if(companyErrorState){
                    companyErrorState = false
                }
                companyState = it
            },
            isError = companyErrorState,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            label = {
                Text("Yritys")
            },
        )
        if(companyErrorState){
            Text(text = "Tarkista yritys")
        }
        OutlinedTextField(
            value = phoneNumState,
            onValueChange = {
                if(phoneNumErrorState){
                    phoneNumErrorState = false
                }
                phoneNumState = it
            },
            isError = phoneNumErrorState,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            label = {
                Text("Puhelinnumero")
            },
        )
        if(phoneNumErrorState){
            Text(text = "Tarkista puhelinnumero")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            when{
                emailState.isEmpty() -> {
                    emailErrorState = true
                }
                passwordState.isEmpty() -> {
                    passwordErrorState = true
                }
                companyState.isEmpty() -> {
                    companyErrorState = true
                }
                phoneNumState.isEmpty() -> {
                    phoneNumErrorState = true
                }
                else -> {
                    passwordErrorState = false
                    emailErrorState = false
                    companyErrorState = false
                    phoneNumErrorState = false

                    driverRegister(
                        navController,
                        context,
                        passwordState,
                        setPasswordErrorState = {passwordErrorState = it},
                        emailState,
                        setEmailErrorState = {emailErrorState = it},
                        phoneNumState,
                        companyState
                    )
                }
            }
        }) {
            Text("Rekisteröidy")
        }
    }
}

private fun saveDriverData(driverData: DriverData,
                           context: Context,
                           user: FirebaseUser,
                           navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val userId = driverData.driverId
    val company = driverData.company
    db.collection("drivers")
        .add(driverData)
        .addOnSuccessListener { documentReference ->
            Log.d(
                "saveDriverData",
                "DocumentSnapshot added with ID: ${documentReference.id}"
            )
            navController.navigate("${Screens.DriverSite.route}/${userId}")
        }
        .addOnFailureListener { e ->
            Log.w("SaveDriverData", "Error adding document", e)
            user.delete().addOnCompleteListener{task ->
                if(task.isSuccessful){
                    Log.d(TAG, "User account deleted")
                }
            }
            Toast.makeText(context, "Tallettaminen tietokantaan epäonnistui", Toast.LENGTH_SHORT).show()
        }
}



private fun driverRegister(
    navController: NavController,
    context: Context,
    registerPassword: String,
    setPasswordErrorState: (Boolean) -> Unit,
    registerEmail: String,
    setEmailErrorState: (Boolean) -> Unit,
    registerPhoneNum: String,
    registerCompany: String
) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    Log.d("auth", "create user")
    auth.createUserWithEmailAndPassword(
        registerEmail.trim(),
        registerPassword.trim()
    ).addOnCompleteListener { task ->
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
                saveDriverData(
                    driverData,
                    context,
                    user,
                    navController
                )
            }

        } else {
            Log.d("AUTH", "Failed: ${task.exception}")
            Toast.makeText(context, "${task.exception}", Toast.LENGTH_SHORT).show()
            if(task.exception.toString() == "com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]"){
                setPasswordErrorState(true)
            }
            else if(task.exception.toString() == "Failed: com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted."){
                setEmailErrorState(true)
            }
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