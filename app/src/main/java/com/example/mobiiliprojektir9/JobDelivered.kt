package com.example.mobiiliprojektir9

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobiiliprojektir9.ui.theme.LogOut
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.util.*

//class JobDelivered : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MobiiliprojektiR9Theme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    Receipt()
//                }
//            }
//        }
//    }
//}

@Composable
fun Receipt(
    navController: NavController,
    selectedItem: String?,
    auth: FirebaseAuth
) {

    var jobInfo by remember { mutableStateOf(Order())}
    val userId = jobInfo.driver_id
    val db = FirebaseFirestore.getInstance()
    Scaffold(
        topBar = {TopAppBar(
            elevation = 4.dp,
            title = {Text(text = "Avoimet keikat")},
            navigationIcon = {
                IconButton(onClick = { navController.navigate("${Screens.DriverSite.route}/${userId}") }) {
                    Icon(Icons.Filled.ArrowBack, null, tint = Color.White)
                }
            },
            actions = {
                LogOut(navController, auth)
            }
        )
        },
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            )
            {
                Spacer(modifier = Modifier.padding(20.dp))
                Text(text = "Keikan kuittaus", fontSize = 20.sp)
                TabRowDefaults.Divider(color = Color.Black, thickness = 5.dp)
                getJobInfo(selectedItem, setJobInfo = {
                    if (it != null) {
                        jobInfo = it
                    }
                }, db )
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

    )
}
fun getJobInfo(selectedItem: String?, setJobInfo: (Order?) -> Unit, db: FirebaseFirestore){
    if (selectedItem != null) {
        db.collection("Jobs").document(selectedItem)
            .get()
            .addOnSuccessListener { document ->
                var job = document.toObject<Order>()
                job?.order_id = selectedItem
                setJobInfo(job)
                Log.d("getJobInfo", "$job")
            }
            .addOnFailureListener{ e ->
                Log.d("Failed", "Failed with ", e)
            }
    }
}
//private fun Date.getStringTimeStampWithDate(): String {
//    val dateFormat = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'", Locale.getDefault())
//    return dateFormat.format(this)
//}
    /*
    .update("state", "closed")
    .update("time_delivered", timestamp)
    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
     */

@Preview(showSystemUi = true)
@Composable
fun ReceiptPreview() {
        Receipt(rememberNavController(), selectedItem = String.toString(), auth = FirebaseAuth.getInstance())

}