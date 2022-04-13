package com.example.mobiiliprojektir9

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.driverssite.DriverSite
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FetchUserData(userId: String, db: FirebaseFirestore): String {

    var company by remember { mutableStateOf("")}
    val userRef = db.collection("coordinator").whereEqualTo("coordinatorId", userId)
        userRef.get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                
                val companyData = document.get("company") as String
                if(companyData.isNotEmpty())
                {
                    company = companyData
                }
            }
    }
        .addOnFailureListener { exception ->
            Log.d("Failure", "get failed with ", exception)
        }
    
    return company
}

@Composable
fun CreateJob(navController: NavController, userId: String?) {

    val context = LocalContext.current
    var db = FirebaseFirestore.getInstance()
    val company = FetchUserData(userId!!, db)

    val jobData = hashMapOf(
        "address_from" to "haukipudas",
        "address_to" to "oulu",
        "company" to company,
        "content" to "pommi",
        "driver_id" to "",
        "state" to "open",
        "time_created" to java.sql.Timestamp(System.currentTimeMillis())
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    )

    {
        Text(
            "Luo uusi keikka",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.background(Color.White),
            color = Color.DarkGray
        )


        var mista by remember { mutableStateOf("") }
        TextField(
            value = mista,
            placeholder = { Text("Hakuosoite") },
            onValueChange = { newText ->
                mista = newText
            }
        )

        var mihin by remember { mutableStateOf("") }
        TextField(
            value = mihin,
            placeholder = { Text("Vientiosoite") },
            onValueChange = { newText ->
                mihin = newText
            }
        )

        var selite by remember { mutableStateOf("") }
        TextField(
            value = selite,
            placeholder = { Text("Selite") },
            onValueChange = { newText ->
                selite = newText
            }
        )

        Button(
            onClick = {
                if (selite.isEmpty() || mista.isEmpty() || mihin.isEmpty()) {
                    Toast.makeText(
                        context, "Täytä kaikki kentät.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    db.collection("Jobs")
                        .add(jobData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context, "Keikan lisääminen onnistui!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        .addOnFailureListener { e -> Log.w("Keikka", "fail", e) }
                }
            },
            modifier = Modifier.width(140.dp)
        ) {
            Text(text = "Luo uusi keikka")
        }
        Button(onClick = {
            //your onclick code here
        }) {
            Text(text = "Avoimet keikat")
        }
        Button(onClick = {
            //your onclick code here
        }) {
            Text(text = "Kuitatut keikat")
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
