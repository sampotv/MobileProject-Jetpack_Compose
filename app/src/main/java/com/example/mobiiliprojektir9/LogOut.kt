package com.example.mobiiliprojektir9.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobiiliprojektir9.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

//Kirjaudu ulos-nappi

@Composable
fun LogOut(navController: NavController){

    val auth = FirebaseAuth.getInstance()
    Button(
        onClick = {signOutUser(navController, auth)},
        elevation = ButtonDefaults.elevation(0.dp,0.dp),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        )
    ) {
        Icon(
            Icons.Filled.Person,
            contentDescription = "Person",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Kirjaudu ulos")
    }
}

fun signOutUser(navController: NavController, auth: FirebaseAuth){
    auth.signOut()
    navController.navigate(route = Screens.Login.route){
        popUpTo(0) //poistaa viimeisemmän composablen pois muistista,
                      // takaisin nappi sulkee apin kirjautumis-näkymästä eikä palaa uloskirjautumista edeltävään näkymään
    }
}
@Preview(showSystemUi = true)
@Composable
fun LogOutPreview(){
    
    LogOut(navController = rememberNavController())
}