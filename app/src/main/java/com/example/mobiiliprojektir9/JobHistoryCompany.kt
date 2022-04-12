package com.example.mobiiliprojektir9



import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun ClosedDeliveriesCompany(
    navController: NavController,
    coordinatorId: String?,
    jobs: MutableList<Order> = getClosedOrdersCompany(company=String()),
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
        //OpenDeliveries(auth = auth, navController = navController)
        Text(
            text = "Ajetut keikat",
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
                ClosedOrderCompanyRow(
                    job,
                    Modifier.fillParentMaxWidth()
                )
            }
        }
    }
}
@Composable
fun ClosedOrderCompanyRow(job: Order, modifier: Modifier){
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
            Text("Luotu: " + job.time_created?.toDate()?.getStringTimeStampWithDate3())
            Text("Ajettu: " + job.time_delivered?.toDate()?.getStringTimeStampWithDate3())
        }
    }
}

fun Date.getStringTimeStampWithDate3(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'", Locale.getDefault())
    return dateFormat.format(this)
}

fun getCompany(coordinatorId: String?) {
    val db = FirebaseFirestore.getInstance()
    var coordinatorId = "e1rne4WYcTWL0LBgVqfrW7CxyE72"
    db.collection("coordinator")
        .whereEqualTo("coordinatorId", coordinatorId)
        .get()
        .addOnSuccessListener { documents ->
            for(document in documents){
                val data = document.toObject<CoordinatorData>()
                val company = data.company
                Log.d("getCompany Success", company)

            }
        }
        .addOnFailureListener{ exception ->
            Log.w("Failed, ", "Error getting document: ", exception)
        }
}

fun getClosedOrdersCompany(company: String): MutableList<Order>{
    Log.d("function", "getOpenOrders")
    var jobs =  mutableStateListOf<Order>()
    val db = FirebaseFirestore.getInstance()
    db.collection("Jobs")
        .whereEqualTo("company", company)
        .whereEqualTo("state", "closed")
        .orderBy("time_delivered", Query.Direction.ASCENDING)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents){
                var order = document.toObject<Order>()
                val time = order.time_created?.toDate()
                jobs.add(order)
                Log.d("getClosedOrders ", "${document.id} => ${document.data}")
                Log.d("order time ", "$time")
                //Log.d("order time ", "$formattedDate")
            }
        }
        .addOnFailureListener{ exception ->
            Log.w("Failed, ", "Error getting document: ", exception)
        }
    Log.d("jobs ", "$jobs")
    return jobs
}

@Preview
@Composable
fun ClosedDeliveryCompanyPreview(){
    ClosedDeliveriesCompany(rememberNavController(), coordinatorId = String.toString(), auth = FirebaseAuth.getInstance())
}