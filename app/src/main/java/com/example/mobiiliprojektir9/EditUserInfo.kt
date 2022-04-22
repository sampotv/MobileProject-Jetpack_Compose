package com.example.mobiiliprojektir9

import android.app.Notification
import android.graphics.Paint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.ui.text.font.FontSynthesis
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun Show(userId: String, navController: NavController){


    var emailAddress by remember {mutableStateOf("")}
    var mobileNumber by remember {mutableStateOf("")}
    var companyName by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}

    Column(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
                Alignment.CenterHorizontally) {


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

        val db = FirebaseFirestore.getInstance()

        Button(onClick = { db.collection("DriverData").whereEqualTo("drivers", userId)
                .update(mapOf(
                "email" to emailAddress,
                "password" to password,
                "company" to companyName,
                "phoneNum" to mobileNumber
            ))


        }, colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Blue
        )) {
            Text("Päivitä profiili", color = Color.White)
        }

}}





@Preview(showSystemUi = true)
@Composable
fun Show() {

}

