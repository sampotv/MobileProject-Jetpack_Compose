package com.example.mobiiliprojektir9

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.lang.IllegalStateException


@Composable
fun OpenDeliveries(
    navController: NavController,
    userId: String?,
    jobList: MutableList<Order> = getOpenOrders()
//    openOrdersViewModel: OpenOrdersViewModel = viewModel(factory = OpenOrdersViewModelFactory(OpenOrdersRepo()))
){
//    when(val jobsList = openOrdersViewModel.getOpenOrdersInfo().collectAsState(initial = null).value){
//        is OnError ->{
//            Text(text = "Yritä myöhemmin uudelleen")
//        }
//        is OnSuccess -> {
//            val listOfJobs = jobsList.querySnapshot?.toObjects(Order::class.java)
//        }
//    }
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = "Avoimet keikat",
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.width(24.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp, end = 20.dp)
            ){
            items(jobList){job ->
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
                shape = RoundedCornerShape(8.dp))
            .padding(10.dp),
    ){
        Column(
           modifier.padding(2.dp)
        ){
            Text("Lähtöosoite: " + job.locationFrom)
            Text("Kohdeosoite: " + job.locationTo)
            Text("Sisältö: " + job.content)
            Text("Yritys: " + job.company)
            Text("pvm: " + job.time_created)
        }
    }
}
fun getOpenOrders(): MutableList<Order>{
    Log.d("function", "getOpenOrders")
    var jobs =  mutableListOf<Order>()
    val db = FirebaseFirestore.getInstance()
    db.collection("Jobs")
        .whereEqualTo("state", "open")
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents){
                var order = document.toObject<Order>()
                jobs.add(order)
                Log.d("getOpenOrders ", "${document.id} => ${document.data}")
                Log.d("order ", "$order")
            }
        }
        .addOnFailureListener{ exception ->
            Log.w("Failed, ", "Error getting document: ", exception)
        }
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
    OpenDeliveries(rememberNavController(), userId = String.toString())
}