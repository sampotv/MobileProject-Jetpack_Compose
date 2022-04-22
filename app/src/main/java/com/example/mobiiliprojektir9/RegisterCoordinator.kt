package com.example.mobiiliprojektir9

import android.content.Context
import android.service.controls.ControlsProviderService
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun RegisterCoordinator(navController: NavController) {
    val context = LocalContext.current
    var emailErrorState by remember { mutableStateOf(false) }
    var passwordErrorState by remember { mutableStateOf(false) }
    var passwordVisibility by remember { mutableStateOf(false) }
    var companyErrorState by remember { mutableStateOf(false) }
    var phoneNumErrorState by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }

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
    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primaryVariant
                    )
                )
            )
            .padding(24.dp)
            .fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Rekisteröidy ajojärjestelijänä",
            style = MaterialTheme.typography.h5,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box() {
            TextField(
                value = emailState,
                onValueChange = {
                    if (emailErrorState) {
                        emailErrorState = false
                    }
                    emailState = it
                },
                isError = emailErrorState,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                label = {
                    Text("Email")
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }
        if (emailErrorState) {
            Text(text = "Tarkista sähköpostiosoite", color = Color.Red)
        }
        TextField(
            value = passwordState,
            onValueChange = {
                if (passwordErrorState) {
                    passwordErrorState = false
                }
                passwordState = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            maxLines = 1,
            label = { Text("Salasana väh. 6 merkkiä") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_visibility),
                        contentDescription = "visibility"
                    )
                }
            }

        )
        if (passwordErrorState) {
            Text(text = "Tarkista salasana", color = Color.Red)
        }
        TextField(
            value = phonenumState,
            onValueChange = {
                if (phoneNumErrorState) {
                    phoneNumErrorState = false
                }
                phonenumState = it
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
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        if (phoneNumErrorState) {
            Text(text = "Tarkista puhelinnumero", color = Color.Red)
        }
        TextField(
            value = companyState,
            onValueChange = {
                if (companyErrorState) {
                    companyErrorState = false
                }
                companyState = it
            },
            isError = companyErrorState,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(8.dp),
            label = {
                Text("Yritys")
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        if (companyErrorState) {
            Text(text = "Tarkista yritys", color = Color.Red)
        }
        if (showLoading) {
            LoadingAnimation()
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.size(220.dp, 50.dp),
            onClick = {
                when {
                    emailState.isEmpty() -> {
                        emailErrorState = true
                    }
                    passwordState.isEmpty() -> {
                        passwordErrorState = true
                    }
                    companyState.isEmpty() -> {
                        companyErrorState = true
                    }
                    phonenumState.isEmpty() -> {
                        phoneNumErrorState = true
                    }
                    else -> {
                        passwordErrorState = false
                        emailErrorState = false
                        companyErrorState = false
                        phoneNumErrorState = false

                        showLoading = true

                        coordinatorRegister(
                            navController,
                            context,
                            passwordState,
                            setPasswordErrorState = { passwordErrorState = it },
                            emailState,
                            setEmailErrorState = { emailErrorState = it },
                            phonenumState,
                            companyState,
                            setLoadingAnimation = { showLoading = it }
                        )
                    }
                }
            },
        ) {
            Text("Rekisteröidy", style =  MaterialTheme.typography.body1)
        }
    }
}

private fun saveCoordinatorData(
    coordinatorData: CoordinatorData,
    context: Context,
    user: FirebaseUser,
    navController: NavController,
    setLoadingAnimation: (Boolean) -> Unit,
) {
    val userId = coordinatorData.coordinatorId
    val db = FirebaseFirestore.getInstance()
    db.collection("coordinator")
        .add(coordinatorData)
        .addOnSuccessListener { documentReference ->
            Log.d(
                "saveCoordinatorData",
                "DocumentSnapshot added with ID: ${documentReference.id}"
            )
            navController.navigate("${Screens.CreateJob.route}/${userId}")
        }
        .addOnFailureListener { e ->
            setLoadingAnimation(false)
            Log.w(ControlsProviderService.TAG, "Error adding document", e)
            user.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ControlsProviderService.TAG, "User account deleted")
                }
            }
            Toast.makeText(context, "Tallettaminen tietokantaan epäonnistui", Toast.LENGTH_SHORT)
                .show()
        }
}

private fun coordinatorRegister(
    navController: NavController,
    context: Context,
    registerPassword: String,
    setPasswordErrorState: (Boolean) -> Unit,
    registerEmail: String,
    setEmailErrorState: (Boolean) -> Unit,
    registerPhoneNum: String,
    registerCompany: String,
    setLoadingAnimation: (Boolean) -> Unit,
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
                var coordinatorData = CoordinatorData().apply {
                    coordinatorId = user.uid
                    email = registerEmail
                    password = registerPassword
                    phoneNum = registerPhoneNum
                    company = registerCompany
                }
                saveCoordinatorData(coordinatorData,
                    context,
                    user,
                    navController,
                    setLoadingAnimation)
            }

        } else {
            setLoadingAnimation(false)
            Log.d("AUTH", "Failed: ${task.exception}")
            Toast.makeText(context, "${task.exception}", Toast.LENGTH_SHORT).show()
            if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]") {
                setPasswordErrorState(true)
            } else if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted."
                || task.exception.toString() == "com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account."
            ) {
                setEmailErrorState(true)
            }
        }
    }
}
//@Preview
//@Composable
//fun RegisterCompanyPreview() {
//    MobiiliprojektiR9Theme {
//        RegisterCompany()
//    }
//}