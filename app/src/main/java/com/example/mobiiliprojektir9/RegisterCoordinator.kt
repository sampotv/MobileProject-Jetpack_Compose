package com.example.mobiiliprojektir9

import android.service.controls.ControlsProviderService
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterCoordinator(navController: NavController){
    var emailState by remember {
        mutableStateOf("")
    }
    var passwordState by remember {
        mutableStateOf("")
    }
    var phonenumState by remember {
        mutableStateOf("")
    }
    var companyState by remember {
        mutableStateOf("")
    }
    var (coordinatorId, setCoordinatorId) = remember{ mutableStateOf("")}
    val keyboardController = LocalSoftwareKeyboardController.current
    val db = FirebaseFirestore.getInstance()
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Rekisteröidy ajojärjestelijänä",
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = emailState,
                label = {
                    Text("Email")
                },
                onValueChange = {
                    emailState = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email,imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()}),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = passwordState,
                label = {
                    Text("Salasana")
                },
                onValueChange = {
                    passwordState = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()}),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
            OutlinedTextField(
                value = phonenumState,
                label = {
                    Text("Puhelinnumero")
                },
                onValueChange = {
                    phonenumState = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
            OutlinedTextField(
                value = companyState,
                label = {
                    Text("Yritys")
                },
                onValueChange = {
                    companyState = it
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                var coordinatorData = CoordinatorData().apply {
                    email = emailState.toString()
                    password = passwordState.toString()
                    phoneNum = phonenumState.toString()
                    company = companyState.toString()
                }
                saveCoordinatorData(coordinatorData, db)
            }) {
                Text("Rekisteröidy")
            }
        }
}

fun saveCoordinatorData(coordinatorData: CoordinatorData, db: FirebaseFirestore) {
    db.collection("coordinator")
        .add(coordinatorData)
        .addOnSuccessListener { documentReference ->
            Log.d(ControlsProviderService.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")

        }
        .addOnFailureListener { e ->
            Log.w(ControlsProviderService.TAG, "Error adding document", e)
        }
}


//@Preview
//@Composable
//fun RegisterCompanyPreview() {
//    MobiiliprojektiR9Theme {
//        RegisterCompany()
//    }
//}