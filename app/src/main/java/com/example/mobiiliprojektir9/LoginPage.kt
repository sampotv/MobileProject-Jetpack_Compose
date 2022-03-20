package com.example.mobiiliprojektir9

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme

class LoginPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MobiiliprojektiR9Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Login()
                }
            }
        }
    }
}

@Composable
fun Login() {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    )
    {
        Spacer(modifier = Modifier.padding(20.dp))
        Text(text = "Kirjaudu sisään", fontSize = 20.sp)
        Spacer(modifier = Modifier.padding(5.dp))
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            placeholder = { Text("Email") },
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight(align = Alignment.CenterVertically),
            maxLines = 1
        )

        TextField(value = password.value,
            onValueChange = { password.value = it },
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
            onClick = { /*TODO*/ },
            modifier = Modifier.size(220.dp, 50.dp)
        ) {
            Text("Kirjaudu kuljettajana")
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.size(220.dp, 50.dp)
        ) {
            Text("Kirjaudu ajojärjästelijänä")
        }
        Spacer(modifier = Modifier.padding(50.dp))
        Text("Uusi käyttäjä? Rekisteröidy täältä!")
        Button(onClick = { /*TODO*/ }) {
            Text("Rekisteröidy")
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginPreview() {
    MobiiliprojektiR9Theme {
        Login()
    }
}