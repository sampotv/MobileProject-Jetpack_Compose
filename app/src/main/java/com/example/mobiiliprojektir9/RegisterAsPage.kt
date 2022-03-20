package com.example.mobiiliprojektir9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme

class RegisterAsPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobiiliprojektiR9Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Register()
                }
            }
        }
    }
}

@Composable
fun Register() {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    )
    {
        Spacer(modifier = Modifier.padding(20.dp))
        Text(text = "Rekisteröidy", fontSize = 20.sp)
        Spacer(modifier = Modifier.padding(80.dp))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.size(220.dp, 80.dp)
        ) {
            Text("Olen kuljettaja")
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.size(220.dp, 80.dp)
        ) {
            Text("Olen ajojärjestelijä",)
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun RegisterPreview() {
    MobiiliprojektiR9Theme {
        Register()
    }
}