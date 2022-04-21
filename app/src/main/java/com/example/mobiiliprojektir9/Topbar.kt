package com.example.mobiiliprojektir9

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobiiliprojektir9.ui.theme.LogOut

@Composable
fun TopBar(navController: NavController){
    TopAppBar(
        elevation = 4.dp,
        title = { Text(text = "Keikat") },
        navigationIcon = {
            IconButton(onClick = { /* hamburger menu käyttäjälle*/ }) {
                Icon(Icons.Filled.Menu, "menu", tint = Color.White)
            }
        },
        actions = {
            LogOut(navController)
        }
    )
}