package com.example.mobiiliprojektir9

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


@Composable
fun Login(navController: NavController, auth: FirebaseAuth) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var showLoading = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primaryVariant
                    )
                )
            )
    )
    {
        Spacer(modifier = Modifier.padding(20.dp))
        Logo()
        Spacer(modifier = Modifier.padding(20.dp))
        Text(text = "Kirjaudu sisään", fontSize = 20.sp, color = Color.White)
        Spacer(modifier = Modifier.padding(5.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") },
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight(align = Alignment.CenterVertically),
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFFFFFFFFF)
            )
        )

        TextField(value = password,
            onValueChange = { password = it },
            placeholder = { Text("Salasana") },
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight(align = Alignment.CenterVertically),
            maxLines = 1,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_visibility),
                        contentDescription = "visibility"
                    )

                }
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFFFFFFFFF)
            )
        )
        if (showLoading.value) {
            LoadingAnimation()
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Button(
            onClick = {
                showLoading.value = true
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        context, "Syötä sähköposti ja salasana.",
                        Toast.LENGTH_LONG
                    ).show()
                    showLoading.value = false
                } else {
                    auth.signInWithEmailAndPassword(email.trim(), password.trim())
                        .addOnCompleteListener() { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "signInWithEmail:success")
                                val user = auth.currentUser!!.uid
                                if (user != null) {
                                    updateUI(user, navController)
                                }
                            } else {
                                showLoading.value = false
                                val errorCode =
                                    (task.exception as FirebaseAuthException?)!!.errorCode

                                when (errorCode) {
                                    "ERROR_WRONG_PASSWORD" -> Toast.makeText(
                                        context, "Salasana on virheellinen.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    "ERROR_INVALID_EMAIL" -> Toast.makeText(
                                        context, "Virheellinen sähköpostiosoite.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    "ERROR_USER_NOT_FOUND" -> Toast.makeText(
                                        context, "Käyttäjää ei löydy.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    else -> {
                                        Toast.makeText(
                                            context, "Tuntematon virhe.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                                    }
                                }
                            }
                        }
                }
            },
            modifier = Modifier.size(220.dp, 50.dp)
        ) {
            Text("Kirjaudu sisään")
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Text("Uusi käyttäjä? Rekisteröidy täältä!")
        Button(onClick = { navController.navigate(route = Screens.RegisterAs.route) }) {
            Text("Rekisteröidy")
        }
    }
}


private fun updateUI(userId: String, navController: NavController) {
    //erottelu, onko ajojärjestelijä vai ajaja

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

private fun loginWithCredentials() {

}

@Preview(showSystemUi = true)
@Composable
fun LoginPreview() {
    MobiiliprojektiR9Theme {
        Login(navController = rememberNavController(), auth = FirebaseAuth.getInstance())
    }
}