package com.example.mobiiliprojektir9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme

class JobHistory : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobiiliprojektiR9Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    History()
                }
            }
        }
    }
}

@Composable
fun History() {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    )
    {
        Spacer(modifier = Modifier.padding(20.dp))
        Text(text = "Ajetut keikat", fontSize = 20.sp)
        Divider(color = Color.Black, thickness = 5.dp)
        Spacer(modifier = Modifier.padding(10.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text("Tietokannasta keikka1 tiedot")
            Divider(color = Color.Red, thickness = 1.dp)
            Text("Tietokannasta keikka2 tiedot")
            Divider(color = Color.Red, thickness = 1.dp)
            Text("Tietokannasta keikka3 tiedot")
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun HistoryPreview() {
    MobiiliprojektiR9Theme {
        History()
    }
}