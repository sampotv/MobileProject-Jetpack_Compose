package com.example.mobiiliprojektir9


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobiiliprojektir9.ui.theme.LogOut
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme
import com.google.firebase.auth.FirebaseAuth


@Composable
fun RegisterAs(navController: NavController) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colors.primaryVariant
            )
    )
    {

        Button(
            shape = MaterialTheme.shapes.medium,
            onClick = { navController.navigate(Screens.RegisterDriver.route)},
            modifier = Modifier.size(220.dp, 80.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_driver),
                contentDescription = "driver"
            )
            Text("Olen kuljettaja", style =  MaterialTheme.typography.body1, modifier = Modifier.padding(10.dp))
        }
        Spacer(modifier = Modifier.padding(20.dp))
        Button(
            shape = MaterialTheme.shapes.medium,
            onClick = { navController.navigate(Screens.RegisterOrganizer.route) },
            modifier = Modifier.size(220.dp, 80.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_organizer),
                contentDescription = "organizer"

            )
            Text("Olen ajojärjestelijä", style =  MaterialTheme.typography.body1, modifier = Modifier.padding(10.dp))
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