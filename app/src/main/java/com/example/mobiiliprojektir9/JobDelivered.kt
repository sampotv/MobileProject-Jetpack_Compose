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
import androidx.navigation.NavController
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme


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
    /*
    .update("state", "closed")
    .update("time_delivered", timestamp)
    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
     */
@Preview(showSystemUi = true)
@Composable
fun ReceiptPreview() {
    MobiiliprojektiR9Theme {
        Receipt()

    }
}