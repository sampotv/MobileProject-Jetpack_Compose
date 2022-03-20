package com.example.mobiiliprojektir9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme

class JobDelivered : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobiiliprojektiR9Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Receipt()
                }
            }
        }
    }
}
@Composable
fun Receipt() {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    )
    {
        Spacer(modifier = Modifier.padding(20.dp))
        Text(text = "Keikan kuittaus", fontSize = 20.sp)
        TabRowDefaults.Divider(color = Color.Black, thickness = 5.dp)
        Spacer(modifier = Modifier.padding(80.dp))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.size(220.dp, 80.dp)
        ) {
            Text("Ota kuva rahtikirjasta")
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.size(220.dp, 80.dp)
        ) {
            Text("Kuittaa keikka ajetuksi",)
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun ReceiptPreview() {
    MobiiliprojektiR9Theme {
        Receipt()
    }
}