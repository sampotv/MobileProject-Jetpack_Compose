package com.example.mobiiliprojektir9


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme

@Composable
fun RegisterAs(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    )
    {
        Spacer(modifier = Modifier.padding(20.dp))
        Text(text = "Rekisteröidy", fontSize = 20.sp)
        Spacer(modifier = Modifier.padding(80.dp))
        Button(
            onClick = { navController.navigate(Screens.RegisterDriver.route)},
            modifier = Modifier.size(220.dp, 80.dp)
        ) {
            Text("Olen kuljettaja")
        }
        Button(
            onClick = { navController.navigate(Screens.RegisterOrganizer.route) },
            modifier = Modifier.size(220.dp, 80.dp)
        ) {
            Text("Olen ajojärjestelijä",)
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun RegisterPreview() {
    MobiiliprojektiR9Theme {
        RegisterAs(navController = rememberNavController())
    }
}