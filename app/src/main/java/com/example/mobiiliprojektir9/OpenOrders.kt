package com.example.mobiiliprojektir9

import android.util.Log
import androidx.activity.compose.LocalActivityResultRegistryOwner.current
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.example.mobiiliprojektir9.ui.theme.LogOut
import com.firebase.ui.auth.AuthUI.TAG
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*



@Composable
fun OpenDeliveries(
    navController: NavController,
    userId: String?,
    jobs: MutableList<Order> = getOpenOrders(),
    auth: FirebaseAuth
){
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        LogOut(auth = auth, navController = navController) //Uloskirjautumis nappi, saa ja pitää sijoittaa järkevämmin
        Text(
            text = "Avoimet keikat",
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.width(24.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp, end = 20.dp)
                .clickable {  }
            ){
            items(jobs){job ->
                OrderRow(
                    job,
                    Modifier.fillParentMaxWidth()
                )
            }
        }
    }
}
@Composable
fun OrderRow(job: Order, modifier: Modifier){
    Column(
        modifier
            .padding(8.dp, top = 20.dp)
            .border(
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(10.dp),
    ){
        Column(
           modifier.padding(2.dp)
        ){
            Text("Lähtöosoite: " + job.address_from)
            Text("Kohdeosoite: " + job.address_to)
            Text("Sisältö: " + job.content)
            Text("Yritys: " + job.company)
            Text("pvm: " + job.time_created?.toDate()?.getStringTimeStampWithDate())
        }
    }
}

fun Date.getStringTimeStampWithDate(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'", Locale.getDefault())
    return dateFormat.format(this)
}

fun getOpenOrders(): MutableList<Order>{
    Log.d("function", "getOpenOrders")
    var jobs =  mutableStateListOf<Order>()
    val db = FirebaseFirestore.getInstance()
    db.collection("Jobs")
        .whereEqualTo("state", "open")
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents){
                var order = document.toObject<Order>()
                val time = order.time_created?.toDate()
                jobs.add(order)
                Log.d("getOpenOrders ", "${document.id} => ${document.data}")
                Log.d("order time ", "$time")
                //Log.d("order time ", "$formattedDate")
            }
        }
        .addOnFailureListener{ exception ->
            Log.w("Failed, ", "Error getting document: ", exception)
        }
//    db.collection("Jobs").addSnapshotListener(snapshot, e ->
//        if(e != null){
//            Log.w("snapshotListener", "Listen failed", e)
//            return@addSnapshotListener
//        }
//        if(snapshot != null && snapshot.exists()){
//            Log.d("tag", "Current data: ${snapshot.data}")
//        } else {
//            Log.d("tag", "Current data: null")
//        }
//    )

    Log.d("jobs ", "$jobs")
    return jobs
}

//class OpenOrdersViewModelFactory(private val openOrdersRepo: OpenOrdersRepo): ViewModelProvider.Factory{
//    override fun<T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(OpenOrdersViewModel::class.java)){
//            return OpenOrdersViewModel(openOrdersRepo) as T
//        }
//        throw IllegalStateException()
//    }
//}

@Preview
@Composable
fun OpenDeliveryPreview(){
    OpenDeliveries(rememberNavController(), userId = String.toString(), auth = FirebaseAuth.getInstance())
}