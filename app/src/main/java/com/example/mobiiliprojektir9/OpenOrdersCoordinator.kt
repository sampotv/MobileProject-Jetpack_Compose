package com.example.mobiiliprojektir9

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.example.mobiiliprojektir9.ui.theme.LogOut

@Composable
fun OpenDeliveriesCoordinator(
    navController: NavController,
    userId: String?
){
    val db = FirebaseFirestore.getInstance()
    var companyState by remember { mutableStateOf("")}
    getCompany(userId, db, setCompanyState = {companyState = it})
    var jobs: MutableList<Order> = fetchOpenOrders(companyState, db)
    var drivers: MutableList<DriverData> = getDriverList(db, companyState)
    var selectedDriver by remember { mutableStateOf(DriverData())}
    var isJobReserved by remember { mutableStateOf(false)}
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(
            elevation = 4.dp,
            title = {Text(text = "Avoimet keikat")},
            navigationIcon = {
                IconButton(onClick = { navController.navigate("${Screens.CreateJob.route}/${userId}") }) {
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

                val listState = rememberLazyListState()
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 20.dp, end = 20.dp)
                ){
                    items(jobs.size){index ->
                        Column {
                            OrderRow(
                                jobs[index],
                                Modifier.fillParentMaxWidth(),
                                drivers,
                                setSelectedDriver = {selectedDriver = it},
                                selectedDriver,
                                db,
                                context,
                                isJobReserved,
                                setJobReserved = {isJobReserved = it},
                                onJobReserved = {
                                    isJobReserved = true
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun OrderRow(
    job: Order,
    modifier: Modifier,
    drivers: MutableList<DriverData>,
    setSelectedDriver: (DriverData) -> Unit,
    selectedDriver: DriverData,
    db: FirebaseFirestore,
    context: Context,
    isJobReserved: Boolean,
    setJobReserved: (Boolean) -> Unit,
    onJobReserved: () -> Unit
) {
    Log.d("OrderRow", job.order_id)
    Column(
        modifier
            .padding(start = 8.dp, end = 8.dp, top = 20.dp)
            .border(
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(10.dp),
    ){
        Column(
            modifier
                .padding(2.dp)
        ) {
            Text("Lähtöosoite: " + job.address_from)
            Text("Kohdeosoite: " + job.address_to)
            Text("Sisältö: " + job.content)
            Text("Yritys: " + job.company)
            Text("pvm: " + job.time_created?.toDate()?.getStringTimeStampWithDate())

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                DropDownMenu(drivers, setSelectedDriver, isJobReserved, setJobReserved)
                Button(
                    onClick = {
                        reserveJob(job.order_id, selectedDriver, db, context, onJobReserved)
                    }
                ) {
                    Text(text = "Varaa keikka kuljettajalle", style = MaterialTheme.typography.body1)
                }
            }
        }
    }
}

@Composable
fun DropDownMenu(
    drivers: MutableList<DriverData>,
    setSelectedDriver: (DriverData) -> Unit,
    isJobReserved: Boolean,
    setJobReserved: (Boolean) -> Unit
){
    var expanded by remember { mutableStateOf(false)}
    var textFieldSize by remember { mutableStateOf(Size.Zero)}
    var driverInfo by remember {mutableStateOf("")}

    val icon = if(expanded){
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }
    if(isJobReserved){
        driverInfo = ""
        setJobReserved(false)
    }
    Column(modifier = Modifier.padding(20.dp)){

        OutlinedTextField(
            value = driverInfo,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            label = {Text(text = "Valitse kuljettaja")},
            trailingIcon = {
                Icon(icon, "", Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            drivers.forEach { driverData ->
                DropdownMenuItem(onClick = {
                    driverInfo = driverData.email
                    expanded = false
                    setSelectedDriver(driverData)
                }) {
                   Text(text = driverData.email)
                }
            }
        }
    }
}
private fun reserveJob(
    orderId: String,
    selectedDriver: DriverData,
    db: FirebaseFirestore,
    context: Context,
    onJobReserved: () -> Unit
){
    Log.d("reserveJob Coordinator", "orderId: $orderId, selectedDriver: ${selectedDriver.email}")

    db.collection("Jobs").document(orderId)
        .update("driver_id", selectedDriver.driverId,
            "state", "reserved")
        .addOnSuccessListener {
            Log.d("reserveJob Coordinator", "update successful!")
            Toast.makeText(context, "Keikka varattu", Toast.LENGTH_SHORT).show()
            onJobReserved()

        }
        .addOnFailureListener { e ->
            Log.w("tag", "Error updating", e)
            Toast.makeText(context, "Varaus ei onnistunut", Toast.LENGTH_SHORT).show()
        }
}

fun getDriverList(db: FirebaseFirestore, company: String): MutableList<DriverData> {
    val driverList = mutableStateListOf<DriverData>()

    db.collection("drivers")
        .whereEqualTo("company", company)
        .get()
        .addOnSuccessListener { documents ->
            for(document in documents){
                val driver = document.toObject<DriverData>()
                driverList.add(driver)
                Log.d("getDriverList", "$driverList")
            }
        }
        .addOnFailureListener{ e ->
            Log.d("Failed", "Failed to get driverlist with ", e)
        }

    return driverList
}

private fun getCompany(userId: String?, db: FirebaseFirestore, setCompanyState: (String) -> Unit) {
    db.collection("coordinator").whereEqualTo("coordinatorId", userId)
        .get()
        .addOnSuccessListener { documents ->
            for(document in documents){
                val data = document.toObject<DriverData>()
                val company = data.company
                Log.d("Coordinator getCompany Success", company)
                setCompanyState(company)
            }
        }
        .addOnFailureListener{ exception ->
            Log.w("Failed, ", "Error getting document: ", exception)
        }
}

@Preview
@Composable
fun OpenDeliveryCoordinatorPreview(){
    OpenDeliveriesCoordinator(rememberNavController(),
        userId = String.toString())
}