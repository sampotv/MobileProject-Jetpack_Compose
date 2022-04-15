package com.example.mobiiliprojektir9

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.Context
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import com.example.mobiiliprojektir9.Common.Companion.checkIfPermissionGranted


private val TAG = "Permission"

@Composable
fun Permission(
    context: Context,
    permission: String,
    permissionAction: (PermissionAction) -> Unit
){
    val permissionGranted =
        checkIfPermissionGranted(
            context,
            permission
        )

    if(permissionGranted){
        Log.d(TAG, "Permission already granted")
        permissionAction(PermissionAction.OnPermissionGranted)
        return
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted: Boolean ->
        if(isGranted){
            Log.d(TAG, "Permission provided by user")
            //Permission Accepted
            permissionAction(PermissionAction.OnPermissionGranted)
        } else{
            Log.d(TAG, "Permissiondenied by user")
            //Permission denied
            permissionAction(PermissionAction.OnPermissionDenied)
        }
    }
    SideEffect{
        launcher.launch(permission)
    }
}

sealed class PermissionAction {

    object OnPermissionGranted : PermissionAction()

    object OnPermissionDenied : PermissionAction()
}