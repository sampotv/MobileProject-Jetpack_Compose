package com.example.mobiiliprojektir9

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var passwordVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    )
    {
        Spacer(modifier = Modifier.padding(20.dp))
        Text(text = "Kirjaudu sisään", fontSize = 20.sp)
        Spacer(modifier = Modifier.padding(5.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") },
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight(align = Alignment.CenterVertically),
            maxLines = 1
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
            }
        )
        Spacer(modifier = Modifier.padding(20.dp))
        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        context, "Syötä sähköposti ja salasana.",
                        Toast.LENGTH_LONG
                    ).show()
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
        Spacer(modifier = Modifier.padding(50.dp))
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
                    //väliaikaiset routet, pitää laittaa oikeat sivut
                    navController.navigate(route = Screens.RegisterAs.route)
                    //navController.navigate("${Screens.CreatJob.route}/${userId}")
                } else {
                    navController.navigate(route = Screens.OpenOrders.route)
                    //navController.navigate("${Screens.DriverSite.route}/${userId}")
                }
            }
        })

}

@Preview(showSystemUi = true)
@Composable
fun LoginPreview() {
    MobiiliprojektiR9Theme {
        Login(navController = rememberNavController(), auth = FirebaseAuth.getInstance())
    }
}