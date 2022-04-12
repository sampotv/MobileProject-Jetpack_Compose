package com.example.mobiiliprojektir9

import android.content.Context
import android.util.Log
import android.widget.Toast
//import androidx.compose.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
//import androidx.compose.unaryPlus
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.example.mobiiliprojektir9.ui.theme.LogOut
import com.firebase.ui.auth.AuthUI.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun OpenDeliveries(
    navController: NavController,
    userId: String?,
//    db: FirebaseFirestore = FirebaseFirestore.getInstance(),
//    company: String? = getCompany(userId, db),
//    jobs: MutableList<Order> = getOpenOrders(company, db),
){
    var userIdTest = "YJ16ji7asQaR7SBpbGJoMRZymys2"//testausta varten otettu driveId tietokannasta
    Log.d("openDeliveries", "$userId")
    //val companyTest = "yritys1"
    val db = FirebaseFirestore.getInstance()
    //val company = "Kuljetusyritys"
    var jobs: MutableList<Order>
    var companyState by remember { mutableStateOf("")}
    var dialogState by remember { mutableStateOf(false)}
    var isJobSelected by remember { mutableStateOf(false)}
    var selectedId by remember { mutableStateOf("")}
    var selectedItem by remember {mutableStateOf( Order())}
    val context = LocalContext.current


    Scaffold(
        topBar = { TopAppBar(
            elevation = 4.dp,
            title = {Text(text = "Avoimet keikat")},
            navigationIcon = {
                IconButton(onClick = { navController.navigate("${Screens.DriverSite.route}/${userId}") }) {
                    Icon(Filled.ArrowBack, null, tint = Color.White)
                }
            },
            actions = {
                LogOut(navController)
            }
        )},
        content = {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                getCompany(userId, db, setCompanyState = {companyState = it})
                jobs  = getOpenOrders(companyState, db)
                val listState = rememberLazyListState()
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 20.dp, end = 20.dp)
                ){
                    items(jobs.size){index ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    selectedItem = jobs[index]
                                    selectedId = jobs[index].order_id
                                    dialogState = true
                                }
                        ){
                            OrderRow(
                                jobs[index],
                                Modifier.fillParentMaxWidth()
                            )
                        }
                    }
                    if(isJobSelected){
                        Log.d("Job selected", selectedId)
                        isJobSelected = false
                        reserveJob(selectedId, userIdTest, context)
                    }
                }
                CustomAlertDialog(
                    selectedItem,
                    dialogState = dialogState,
                    onDismissRequest = { dialogState = !it },
                    onJobSelected = {isJobSelected = it}
                )
            }
        }
    )
}

fun reserveJob(selectedId: String, userId: String?, context: Context) {
    Log.d("reserveJob", "$selectedId,  $userId")//testataan että ollaan saatu tarvittavat tiedot
    val db = FirebaseFirestore.getInstance()
    //päivitetään tietokantaan keikan tietoihin kuljettajan id ja
    // state-kenttään "open" tilalle "reserved"
    db.collection("Jobs").document(selectedId)
        .update("driver_id", userId,
            "state", "reserved")
        .addOnSuccessListener {
            Log.d("tag", "update successful!")
            Toast.makeText(context, "Keikka varattu", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Log.w("tag", "Error updating", e)
            Toast.makeText(context, "Varaus ei onnistunut", Toast.LENGTH_SHORT).show()
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
            modifier
                .padding(2.dp)
        ){
            Text("Lähtöosoite: " + job.address_from)
            Text("Kohdeosoite: " + job.address_to)
            Text("Sisältö: " + job.content)
            Text("Yritys: " + job.company)
            Text("pvm: " + job.time_created?.toDate()?.getStringTimeStampWithDate())
        }
    }
}

fun getCompany(userId: String?, db: FirebaseFirestore, setCompanyState: (String) -> Unit) {
    //Log.d("function", "getCompany $userId")
    db.collection("drivers").whereEqualTo("driverId", userId)
        .get()
        .addOnSuccessListener { documents ->
            for(document in documents){
                val data = document.toObject<DriverData>()
                val company = data.company
                Log.d("getCompany Success", company)
                setCompanyState(company)
            }
        }
        .addOnFailureListener{ exception ->
            Log.w("Failed, ", "Error getting document: ", exception)
        }
}

fun getOpenOrders(company: String, db: FirebaseFirestore): MutableList<Order>{
    Log.d("function", "getOpenOrders")
    Log.d("company", company)
    var jobs = mutableStateListOf<Order>()

    db.collection("Jobs")
        .whereEqualTo("state", "open")
        .whereEqualTo("company", company)
        .orderBy("time_created", Query.Direction.ASCENDING)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents){
                val order = document.toObject<Order>()
                val time = order.time_created?.toDate()
                order.order_id = document.id
                jobs.add(order)
                Log.d("getOpenOrders ", "${document.id} => ${document.data}")
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
@Composable
private fun CustomAlertDialog(
    selectedItem: Order,
    dialogState: Boolean,
    onDismissRequest: (dialogState: Boolean) -> Unit,
    onJobSelected: (Boolean) -> Unit
){
    if(dialogState){
        AlertDialog(
            backgroundColor = Color(0xF55398A1),
            onDismissRequest = {
                onDismissRequest(dialogState)
            },
            title = null,
            text = null,
            buttons = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))

                    Text(
                        text = "Varaa keikka",
                        fontSize = 24.sp,
                        color = Color.Blue
                    )
                    Spacer(modifier = Modifier.padding(vertical = 12.dp))

                    Text("Lähtöosoite: " + selectedItem.address_from, color = Color.White)
                    Text("Kohdeosoite: " + selectedItem.address_to, color = Color.White)
                    Text("Sisältö: " + selectedItem.content, color = Color.White)
                    Text("Yritys: " + selectedItem.company, color = Color.White)
                    Text("pvm: " + selectedItem.time_created?.toDate()?.getStringTimeStampWithDate(), color = Color.White)

                    Spacer(modifier = Modifier.padding(vertical = 10.dp))

                    Text(
                        text = "Haluatko varata tämän keikan?",
                        fontSize = 18.sp,
                        color = Color.Blue
                    )

                    Spacer(modifier = Modifier.padding(vertical = 20.dp))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f),
                        onClick = {
                            onDismissRequest(dialogState)
                            onJobSelected(true)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = Color.Green,
                            contentColor = Color.White
                        )
                    ){
                        Text(text = "Varaa")
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 10.dp))

                    TextButton(
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f),
                        onClick = {
                            onDismissRequest(dialogState)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = Color.Red,
                            contentColor = Color.White
                        )
                    ){
                        Text(text = "Hylkää")
                    }
                }
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

fun Date.getStringTimeStampWithDate(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'", Locale.getDefault())
    return dateFormat.format(this)
}

@Preview
@Composable
fun OpenDeliveryPreview(){
    OpenDeliveries(rememberNavController(),
        userId = String.toString())

}