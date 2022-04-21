package com.example.mobiiliprojektir9

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobiiliprojektir9.ui.theme.LogOut
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FetchUserData(userId: String, db: FirebaseFirestore): String {

    var company by remember { mutableStateOf("") }
    val userRef = db.collection("coordinator").whereEqualTo("coordinatorId", userId)
    userRef.get()
        .addOnSuccessListener { documents ->
            for (document in documents) {

                val companyData = document.get("company") as String
                if (companyData.isNotEmpty()) {
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
    val scroll = rememberScrollState(0)
    var db = FirebaseFirestore.getInstance()
    var yritys = FetchUserData(userId!!, db)
    var mista by rememberSaveable { mutableStateOf("") } //ei tallenna statea jonkun takia ?
    var mihin by rememberSaveable { mutableStateOf("") }
    var selite by rememberSaveable { mutableStateOf("") }
    var jobCreated by rememberSaveable { mutableStateOf(false) }
    if (jobCreated) {
        mista = ""
        mihin = ""
        selite = ""
        jobCreated = false
    }

    Scaffold(
        topBar = {
            TopBar(navController)
        },
        content = {
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
                    fontSize = 20.sp,
                    modifier = Modifier.background(Color.White),
                    color = Color.DarkGray
                )

                TextField(
                    value = mista,
                    placeholder = { Text("Hakuosoite") },
                    onValueChange = {
                        mista = it
                    },
                    modifier = Modifier
                        .width(300.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    maxLines = 1
                )

                TextField(
                    value = mihin,
                    placeholder = { Text("Vientiosoite") },
                    onValueChange = {
                        mihin = it
                    },
                    modifier = Modifier
                        .width(300.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    maxLines = 1
                )

                TextField(
                    value = selite,
                    placeholder = { Text("Selite") },
                    onValueChange = {
                        selite = it
                    },
                    modifier = Modifier
                        .height(80.dp)
                        .width(300.dp)
                        .verticalScroll(scroll)
                )

                Button(
                    onClick = {

                        var createJobData = CreateJobData().apply {
                            address_from = mista
                            address_to = mihin
                            company = yritys
                            content = selite
                            driver_id = ""
                            state = "open"
                            time_created = java.sql.Timestamp(System.currentTimeMillis())
                        }

                        if (selite.isEmpty() || mista.isEmpty() || mihin.isEmpty()) {
                            Toast.makeText(
                                context, "Täytä kaikki kentät.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            db.collection("Jobs")
                                .add(createJobData)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context, "Keikan lisääminen onnistui!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.d("Create job documentRef", it.id)
                                    jobCreated = true
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context, "Keikan lisääminen epäonnistui",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                    },
                    modifier = Modifier
                        .width(160.dp)
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text(text = "Luo uusi keikka", textAlign = TextAlign.Center, style = MaterialTheme.typography.body1)
                }
                Spacer(Modifier.height(30.0.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            navController.navigate("${Screens.OpenOrdersCoordinator.route}/${userId}")
                        },
                        modifier = Modifier
                            .width(160.dp)
                            .height(50.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(text = "Avoimet keikat", textAlign = TextAlign.Center, style = MaterialTheme.typography.body1)
                    }
                    Button(
                        onClick = {
                            navController.navigate("${Screens.JobHistoryCompany.route}/${userId}")
                        },
                        modifier = Modifier
                            .width(160.dp)
                            .height(50.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(text = "Kuitatut keikat", textAlign = TextAlign.Center, style = MaterialTheme.typography.body1)
                    }
                }
            }
        }
    )

}


@Preview(showBackground = true)
@Composable
fun CreateJobPreview() {
    CreateJob(
        rememberNavController(), userId = String.toString()
    )
}
