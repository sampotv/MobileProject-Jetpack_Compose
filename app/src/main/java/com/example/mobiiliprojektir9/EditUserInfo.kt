package com.example.mobiiliprojektir9

import android.app.Notification
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.ui.text.font.FontSynthesis
import com.example.mobiiliprojektir9.ui.theme.LogOut
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun Show(userId: String, navController: NavController){

    // testi: val userId = "PPQH4E4bLIfORaMH9p30GkEQlQs2"
    var emailAddress by remember {mutableStateOf("")}
    var mobileNumber by remember {mutableStateOf("")}
    var companyName by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 4.dp,
                title = { Text(text = "Profiili ") },
                navigationIcon = {
                    IconButton(onClick = { /* hamburger menu käyttäjälle tai keikoile emt mby*/ }) {
                        Icon(Icons.Filled.Menu, "menu", tint = Color.White)
                    }
                },
                actions = {
                    LogOut(navController)
                }
            )
        },

        content = {   Column(modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(Modifier.height(50.0.dp))

     TextField(value = emailAddress,
         onValueChange = {emailAddress = it},
         placeholder = {Text("Sähköposti")},
         keyboardOptions = KeyboardOptions(
             keyboardType = KeyboardType.Email,
             imeAction = ImeAction.Next),
     colors = TextFieldDefaults.outlinedTextFieldColors(
         focusedBorderColor = Color.Black,
         unfocusedBorderColor = Color.Gray,
         disabledBorderColor = Color.Gray,
         disabledTextColor = Color.Black),
         textStyle = TextStyle(fontSize = 18.sp))

        Spacer(Modifier.height(30.0.dp))

        TextField(value = password,
            onValueChange = {password = it},
            placeholder = {Text("Salasana")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                disabledBorderColor = Color.Gray,
                disabledTextColor = Color.Black),
            textStyle = TextStyle(fontSize = 18.sp))

        Spacer(Modifier.height(30.0.dp))

        TextField(value = mobileNumber,
            onValueChange = {mobileNumber = it},
            placeholder = {Text("Puhelinnumero")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                disabledBorderColor = Color.Gray,
                disabledTextColor = Color.Black),
            textStyle = TextStyle(fontSize = 18.sp))

        Spacer(Modifier.height(30.0.dp))

        TextField(value = companyName,
            onValueChange = {companyName = it},
            placeholder = {Text("Yritys")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                disabledBorderColor = Color.Gray,
                disabledTextColor = Color.Black),
            textStyle = TextStyle(fontSize = 18.sp))

        Spacer(Modifier.height(30.0.dp))

        val db = FirebaseFirestore.getInstance()

        Button(onClick = { db.collection("drivers")
            .whereEqualTo("driverId", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.update(mapOf(
                        "email" to emailAddress,
                        "password" to password,
                        "company" to companyName,
                        "phoneNum" to mobileNumber
                    ))
                }
            }
            .addOnFailureListener{ exception ->
                Log.w("Failed, ", "Error getting document: ", exception)
            }

        })
        {
            Text(text = "Päivitä tiedot")
        }

}})}





@Preview(showSystemUi = true)
@Composable
fun ShowPreview() {
    MobiiliprojektiR9Theme {
        Show(userId = String.toString(), rememberNavController())
    }
}
