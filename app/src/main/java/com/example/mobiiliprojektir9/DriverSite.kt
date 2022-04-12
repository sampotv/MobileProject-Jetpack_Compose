package com.example.driverssite

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.example.mobiiliprojektir9.Screens
import com.google.firebase.auth.FirebaseAuth

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
    val userIdTest = "YJ16ji7asQaR7SBpbGJoMRZymys2"

    DisplayList(items = listItems,navController,userIdTest)
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
private val listItems: List<ListItem> = listOf(
    ListItem("Job1"),
    ListItem("Job2"),
    ListItem("Job3"),
    ListItem("Job4"),
    ListItem("Job5"),
    ListItem("Job6"),
    ListItem("Job7"),
    ListItem("Job8"),
    ListItem("Job9"),
    ListItem("Job10"),
    ListItem("Job11"),
    ListItem("Job12")
)

@Composable
fun DisplayList(items: List<ListItem>, navController: NavController,userId: String?) {
    Log.d("DisplayList", "$userId")
    Column(verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally){
        LazyColumn(modifier = Modifier
            .padding(top = 20.dp, bottom = 500.dp)
            .height(300.dp)) {
            items(items) { item ->
                ListItem(item)
            } }
        OpenJobs(navController, userId)
    }
}
@Composable
fun OpenJobs(navController: NavController, userId: String?){
    Log.d("OpenJobs", "$userId")
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally){

        Button(onClick = { navController.navigate("${Screens.OpenOrders.route}/${userId}") }, colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Blue
        ), modifier = Modifier
            .height(60.dp)
            .padding(bottom = 20.dp)) {

            Text("View open jobs", color = White)

        }

        Button(onClick = { navController.navigate(route = Screens.JobHistory.route)}, colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Blue
        ), modifier = Modifier
            .padding(bottom = 20.dp)
            .height(60.dp)) {

            Text("View completed jobs", color = White)

        }

    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DriverSite(
        rememberNavController(), userId = String.toString(), auth = FirebaseAuth.getInstance()
    )}


