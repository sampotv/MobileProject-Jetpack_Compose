package com.example.mobiiliprojektir9



import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
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
    userId: String?,

    //jobs: MutableList<Order> = getClosedOrdersCompany(company= String()),
    //auth: FirebaseAuth
){
    val db = FirebaseFirestore.getInstance()
    var companyState by remember { mutableStateOf("")}
    getCompany(userId, db, setCompanyState = {companyState = it})
    var jobs by remember { mutableStateOf(mutableListOf<Order>())}
    getClosedOrdersCompany(db, setJobs = { jobs = it}, companyState)
    var showImage by remember { mutableStateOf(false)}
    var imageUrl by remember { mutableStateOf("")}
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
                //.clickable { }
        ){
            items(jobs){job ->
                ClosedOrderCompanyRow(
                    job,
                    Modifier.fillParentMaxWidth(),
                    onShowImage = {showImage = it},
                    setImageUrl = {imageUrl = it}
                )
            }
        }
        if(showImage){
            Box(
                modifier = Modifier.fillMaxSize()
            ){
                IconButton(
                    onClick = { showImage = false },
                    modifier = Modifier.align(TopEnd)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
                Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun ClosedOrderCompanyRow(job: Order, modifier: Modifier, onShowImage: (Boolean) -> Unit, setImageUrl: (String) -> Unit){
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
        Button(onClick = {
            setImageUrl(job.imageUrl)
            onShowImage(true) },
            modifier = Modifier.padding(top = 3.dp)
        ) {
            Text(text = "Näytä rahtikirja")
        }
    }
}

fun Date.getStringTimeStampWithDate3(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'", Locale.getDefault())
    return dateFormat.format(this)
}


@Composable
fun getClosedOrdersCompany(db: FirebaseFirestore, setJobs: (MutableList<Order>) -> Unit, company: String){
    var jobs by remember { mutableStateOf(mutableListOf<Order>())}
    //var compa = getCompany(userId , db )
    Log.d("function", "getClosedOrders")


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
                setJobs(jobs)
                //Log.d("order time ", "$formattedDate")

            }
        }
        .addOnFailureListener{ exception ->
            Log.w("Failed, ", "Error getting document: ", exception)
        }
    Log.d("jobs ", "$jobs")
    //return jobs
}

private fun getCompany(userId: String?, db: FirebaseFirestore, setCompanyState: (String) -> Unit) {
    db.collection("coordinator").whereEqualTo("coordinatorId", userId)
        .get()
        .addOnSuccessListener { documents ->
            for(document in documents){
                val data = document.toObject<CoordinatorData>()
                val company = data.company
                Log.d("getCompany Success", company)
                setCompanyState(company)
            }
        }
        .addOnFailureListener{ exception ->
            Log.w("Failed, ", "Error getting document: ", exception)
        }
}

@Preview
@Composable
fun ClosedDeliveryCompanyPreview(){
    ClosedDeliveriesCompany(rememberNavController(), userId = String.toString())
}