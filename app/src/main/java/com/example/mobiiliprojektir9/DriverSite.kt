package com.example.driverssite

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobiiliprojektir9.Order
import com.example.mobiiliprojektir9.OrderRow
import com.example.mobiiliprojektir9.Screens
import com.example.mobiiliprojektir9.getStringTimeStampWithDate
import com.example.mobiiliprojektir9.ui.theme.LogOut
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.util.*

data class ListItem(val name: String)

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//
//            Surface(color = MaterialTheme.colors.background) {
//                DisplayList(items = listItems)
//            }
//
//
//        }
//
//    }
//}

@Composable
fun DriverSite(navController: NavController, userId: String?, auth: FirebaseAuth){
//    val userIdTest = "PPQH4E4bLIfORaMH9p30GkEQlQs2"
    val items = getListItems(userId)
    DisplayList(items, navController, userId, auth)
}

@Composable
fun ListItem(item: ListItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(60.dp)
            .background(color = Gray)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(CenterVertically),
                text = item.name,
                color = White,
                fontSize = 16.sp
            )
        }
    }
}
private fun getListItems(userId: String?): MutableList<Order>{
    val db = FirebaseFirestore.getInstance()
    var listItems = mutableStateListOf<Order>()

    db.collection("Jobs")
        .whereEqualTo("driver_id", userId)
        .whereEqualTo("state", "reserved")
        .orderBy("time_created", Query.Direction.ASCENDING)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents){
                val item = document.toObject<Order>()
                val time = item.time_created?.toDate()
                item.order_id = document.id
                listItems.add(item)
                Log.d("getListItems ", "${document.id} => ${document.data}")
                Log.d("order time ", "$time")
                //Log.d("order time ", "$formattedDate")
            }
        }
        .addOnFailureListener{ exception ->
            Log.w("Failed, ", "Error getting document: ", exception)
        }
    Log.d("listItems ", "$listItems")
    return listItems
}

@Composable
fun DisplayList(items: MutableList<Order>, navController: NavController, userId: String?, auth: FirebaseAuth) {
    var selectedId by remember { mutableStateOf("")}
    Log.d("DisplayList", "$userId")
    
    Scaffold(
        topBar = { TopAppBar(
            elevation = 4.dp,
            title = {Text(text = "Omat keikat")},
            actions = {
                LogOut(navController)
            }
        )
        },
        content = {
            Column(verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally){

                LazyColumn(modifier = Modifier
                    .padding(top = 20.dp, bottom = 50.dp)
                    .height(350.dp)) {
                    items(items.size){index ->
                        Row(
                            modifier = Modifier
                                .clickable (onClick = {
                                    selectedId = items[index].order_id
                                    navController.navigate("${Screens.JobDelivered.route}/${selectedId}")
                                })
                        ){
                            ItemRow(
                                items[index],
                                Modifier.fillParentMaxWidth()
                            )
                        }
                    }
                }
                //OpenJobs(navController, userId)
                Column(modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Button(onClick = { navController.navigate("${Screens.OpenOrders.route}/${userId}") }, colors = ButtonDefaults.textButtonColors(
                        backgroundColor = Color.Blue
                    ), modifier = Modifier
                        .size(220.dp, 80.dp)
//                        .height(80.dp)
//                        .width(200.dp)
                        .padding(bottom = 20.dp)) {

                        Text("View open jobs", color = White)
                    }

                    Button(onClick = { navController.navigate("${Screens.JobHistory.route}/${userId}")}, colors = ButtonDefaults.textButtonColors(
                        backgroundColor = Color.Blue
                    ), modifier = Modifier
                        .size(220.dp, 80.dp)
//                        .padding(bottom = 20.dp)
//                        .width(200.dp)
                        .height(80.dp)) {

                        Text("View completed jobs", color = White)
                    }
                }
            }
        })
}
@Composable
fun ItemRow(item: Order, modifier: Modifier){
    Row(
        modifier
            .padding(8.dp, top = 20.dp)
            .border(
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(10.dp),
    ){
        Column(
            modifier
                .padding(2.dp)
        ){
            Text("Lähtöosoite: " + item.address_from)
            Text("Kohdeosoite: " + item.address_to)
            Text("Sisältö: " + item.content)
            Text("Yritys: " + item.company)
            Text("Luotu: " + item.time_created?.toDate()?.getStringTimeStampWithDate())
        }
    }
}

@Composable
fun OpenJobs(navController: NavController, userId: String?){
    Log.d("OpenJobs", "$userId")
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally){

        Button(onClick = { navController.navigate("${Screens.OpenOrders.route}/${userId}") }, colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Blue
        ), modifier = Modifier
            .height(100.dp)
            .width(200.dp)
            .padding(bottom = 20.dp)) {

            Text("View open jobs", color = White)

        }

        Button(onClick = { navController.navigate("${Screens.JobHistory.route}/${userId}")}, colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Blue
        ), modifier = Modifier
            .padding(bottom = 20.dp)
            .width(200.dp)
            .height(100.dp)) {

            Text("View completed jobs", color = White)

        }

    }
}
//private val listItems: List<ListItem> = listOf(
//    ListItem("Job1"),
//    ListItem("Job2"),
//    ListItem("Job3"),
//    ListItem("Job4"),
//    ListItem("Job5"),
//    ListItem("Job6"),
//    ListItem("Job7"),
//    ListItem("Job8"),
//    ListItem("Job9"),
//    ListItem("Job10"),
//    ListItem("Job11"),
//    ListItem("Job12")
//)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DriverSite(
        rememberNavController(), userId = String.toString(), auth = FirebaseAuth.getInstance()
    )}


