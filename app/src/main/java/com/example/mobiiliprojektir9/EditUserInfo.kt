package com.example.mobiiliprojektir9

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.compose.ui.text.TextStyle
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String,
    onChange: (String) -> Unit = {},
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyBoardActions: KeyboardActions = KeyboardActions(),
    isEnabled: Boolean = true,
    VisualTransformation: PasswordVisualTransformation,
) {

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        onValueChange = onChange,
        Style =  TextStyle(fontSize = 18.sp),
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
        keyboardActions = keyBoardActions,
        enabled = isEnabled,
        Color = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = androidx.compose.ui.graphics.Color.Black,
            unfocusedBorderColor = androidx.compose.ui.graphics.Color.Gray,
            disabledBorderColor = androidx.compose.ui.graphics.Color.Gray,
            disabledTextColor = androidx.compose.ui.graphics.Color.Black
        ),
        placeholder =
            Text(text = placeholder, style = TextStyle(fontSize = 18.sp, color = Color.Gray)))



    class FormViewModel : ViewModel() {

        var emailAddress by mutableStateOf("");
        var mobileNumber by mutableStateOf("");
        var companyName by mutableStateOf("");
        var password by mutableStateOf("");
    }

    Column() {
    val focusManager = LocalFocusManager.current

    AppTextField(
        text = ViewModel.emailAddress,
        placeholder = "Sähköposti",
        onChange = {
            ViewModel.emailAddress = it
        },
        imeAction = ImeAction.Next,//Show next as IME button
        keyboardType = KeyboardType.Text, //Plain text keyboard
        keyBoardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        ),
        VisualTransformation = PasswordVisualTransformation()
    )

    AppTextField(
        text = ViewModel.mobileNumber,
        placeholder = "Puhelinnumero",
        onChange = {
            ViewModel.mobileNumber = it
        },
        imeAction = ImeAction.Next,//Show next as IME button
        keyboardType = KeyboardType.Phone,
        keyBoardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        ),
        VisualTransformation = PasswordVisualTransformation()
    )

    AppTextField(
        text = ViewModel.companyName,
        placeholder = "Yritys",
        onChange = {
            ViewModel.companyName = it
        },
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Text,
        keyBoardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        ),
        VisualTransformation = PasswordVisualTransformation()
    )


    AppTextField(
        text = ViewModel.password,
        placeholder = "Salasana",
        onChange = {
            ViewModel.password = it
        },
        imeAction = ImeAction.Done,
        VisualTransformation = PasswordVisualTransformation(),
        keyboardType = KeyboardType.Password,
    )
        var db = FirebaseFirestore.getInstance()

        Button(onClick = { /* siirrä tiedot tietokantaan */ }, colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Blue
        )) {
            Text("Päivitä profiili")
        }

}}

