package com.example.mobiiliprojektir9

import android.webkit.PermissionRequest
import androidx.compose.material.AlertDialog

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable


//
//@Composable
//fun Permission(
//    permission: String = android.Manifest.permission.CAMERA,
//    rationale: String = "Sovellus tarvitsee luvan käyttää kameraa.",
//    permissionNotAvailableContent: @Composable () -> Unit = { },
//    content: @Composable () -> Unit = { }
//){
//    val permissionState = rememberPermissionState(permission)
//    PermissionRequired (
//        permissionState = permissionState,
//        permissionNotGrantedContent = {
//            Rationale(
//                text = rationale,
//                onRequestPermission = {permissionState.launchPermissionRequest()}
//            )
//        },
//        permissionNotAvailableContent = permissionNotAvailableContent,
//        content = content
//    )
//}
//
//@Composable
//private fun Rationale(
//    text: String,
//    onRequestPermission: () -> Unit
//){
//    AlertDialog(
//        onDismissRequest = { /*TODO*/ },
//        title = {
//            Text(text = "Permission request")
//        },
//        text = {
//            Text(text)
//        },
//        buttons = {
//            Button(onClick = onRequestPermission){
//                Text("Ok")
//            }
//        }
//    )
//}