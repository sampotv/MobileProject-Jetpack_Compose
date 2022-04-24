package com.example.mobiiliprojektir9

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobiiliprojektir9.ui.theme.LogOut
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

@Composable
fun TopBar(navController: NavController, keikka: String, userId: String?) {
    var showMenu by remember { mutableStateOf(false) }
    TopAppBar(
        elevation = 4.dp,
        title = { Text(text = keikka) },
        navigationIcon = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Filled.Menu, "menu", tint = Color.White)
                DropdownMenu(expanded = showMenu,
                    modifier = Modifier
                        .width(110.dp),
                    onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(onClick = { // Profiilin muokkaukseen:

                        var db = FirebaseFirestore.getInstance()

                        db.collection("drivers").whereEqualTo("driverId", userId)
                            .limit(1).get()
                            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                                if (task.isSuccessful) {
                                    val isEmpty = task.result.isEmpty
                                    if (isEmpty) {
                                        navController.navigate("${Screens.EditCoordInfo.route}/${userId}")
                                    } else {
                                        navController.navigate("${Screens.EditUserInfo.route}/${userId}")
                                    }
                                }
                            }) }) {
                        Icon(Icons.Filled.Person,
                            contentDescription = "Person",
                            modifier = Modifier.padding(end = 5.dp).size(ButtonDefaults.IconSize))
                        Text(text = "Profiili", style = MaterialTheme.typography.body1)
                    }
                }
            }
        },
        actions = {
            LogOut(navController)
        },
    )
}