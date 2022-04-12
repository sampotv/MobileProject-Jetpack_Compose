package com.example.mobiiliprojektir9

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.driverssite.DriverSite

@Composable
fun CreateJob(navController: NavController, userId: String?) {

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly)

    {
        Text("Create a new job",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.background(Color.White),
            color = Color.DarkGray
        )

        var text by remember { mutableStateOf(TextFieldValue("Mistä")) }
        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            }
        )

        var text2 by remember { mutableStateOf(TextFieldValue("Mihin")) }
        TextField(
            value = text2,
            onValueChange = { newText ->
                text2 = newText
            }
        )

        var text3 by remember { mutableStateOf(TextFieldValue("Mitä")) }
        TextField(
            value = text3,
            onValueChange = { newText ->
                text3 = newText
            }
        )

        Button(onClick = {
            //your onclick code here
        }, modifier = Modifier.width(140.dp)) {
            Text(text = "Create Job")
        }
        Button(onClick = {
            //your onclick code here
        }) {
            Text(text = "View open jobs")
        }
        Button(onClick = {
            //your onclick code here
        }) {
            Text(text = "View completed jobs")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateJobPreview() {
    CreateJob(
        rememberNavController(), userId = String.toString()
    )
}
