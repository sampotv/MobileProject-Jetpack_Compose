package com.example.mobiiliprojektir9

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme
import kotlinx.coroutines.launch

@Preview
@Composable
fun RegisterCompany(){
    var emailState by remember {
        mutableStateOf("")
    }
    var passwordState by remember {
        mutableStateOf("")
    }
    var companyState by remember {
        mutableStateOf("")
    }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
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
                modifier = Modifier.fillMaxWidth()
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
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("$emailState, $passwordState, $companyState")
                }
            }) {
                Text("Rekisteröidy")
            }
        }
    }
}

//@Preview
//@Composable
//fun RegisterCompanyPreview() {
//    MobiiliprojektiR9Theme {
//        RegisterCompany()
//    }
//}