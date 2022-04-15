package com.example.mobiiliprojektir9

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.mobiiliprojektir9.ui.theme.LogOut
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File


@Composable
fun Receipt(
    navController: NavController,
    selectedItem: String?,
    auth: FirebaseAuth
) {
    val permissionViewModel = PermissionViewModel()
    val db = FirebaseFirestore.getInstance()
    var jobInfo by remember { mutableStateOf(Order())}
    var openPermissionState by remember { mutableStateOf(false)}
    var cameraState by rememberSaveable { mutableStateOf(false)}
    var updateSuccess by rememberSaveable { mutableStateOf(false)}
    var updateFail by rememberSaveable { mutableStateOf(false)}
    var photoUri by remember { mutableStateOf<Uri?>(null)}
    var shouldShowPhoto by remember {mutableStateOf(false)}

    getJobInfo(selectedItem, setJobInfo = {
        if (it != null) {
            jobInfo = it
        }
    }, db )

    permissionViewModel.setId(jobInfo.driver_id)
    val userId by permissionViewModel.id.collectAsState()

    Scaffold(
        topBar = {TopAppBar(
            elevation = 4.dp,
            title = {Text(text = "Avoimet keikat")},
            navigationIcon = {
                IconButton(onClick = { navController.navigate("${Screens.DriverSite.route}/${userId}") }) {
                    Icon(Icons.Filled.ArrowBack, null, tint = Color.White)
                }
            },
            actions = {
                LogOut(navController)
            }
        )
        }

    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        )
        {
            Spacer(modifier = Modifier.padding(10.dp))
            Text(text = "Keikan kuittaus", fontSize = 30.sp)
            TabRowDefaults.Divider(color = Color.Black, thickness = 5.dp)
            Spacer(modifier = Modifier.padding(10.dp))
            if(updateSuccess) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Keikan kuittaus onnistui!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )
                    Spacer(modifier = Modifier.padding(30.dp))
                    Button(
                        onClick = { navController.navigate("${Screens.DriverSite.route}/${userId}") },
                        modifier = Modifier.size(220.dp, 80.dp)
                    ) {
                        Text("Palaa omiin keikkoihin")
                    }
                }
            } else if(updateFail) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Keikan kuittaus ei onnistunut!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.padding(30.dp))
                    Button(
                        onClick = { navController.navigate("${Screens.DriverSite.route}/${userId}") },
                        modifier = Modifier.size(220.dp, 80.dp)
                    ) {
                        Text("Palaa omiin keikkoihin")
                    }
                }
            }else {
                if (shouldShowPhoto) {
                    Column(
                        modifier = Modifier
                        .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                        )
                    {
                        Column(
                            modifier = Modifier
                                .padding(start = 5.dp, end = 2.dp)
                        ) {
                            Text(jobInfo.address_from, fontSize = 15.sp)
                            Text(jobInfo.address_to, fontSize = 15.sp)
                            Text(jobInfo.content, fontSize = 15.sp)
                            Text(jobInfo.company, fontSize = 15.sp)
                            Text(jobInfo.time_created?.toDate()?.getStringTimeStampWithDate()
                                .toString(),
                                fontSize = 15.sp)
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Image(
                                painter = rememberImagePainter(photoUri),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp, 300.dp).padding(2.dp)
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f)
                        ){
                            Button(
                                onClick = {
                                    jobDone(photoUri,
                                        selectedItem,
                                        onUpdateSuccess = { updateSuccess = true },
                                        onUpdateFail = { updateFail = true })
                                },
                                modifier = Modifier.size(220.dp, 60.dp)
                            ) {
                                Text("Kuittaa keikka ajetuksi", )
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.padding(start = 20.dp, end = 5.dp)
                        ){
                            Text("Lähtöosoite: " + jobInfo.address_from, fontSize = 20.sp)
                            Text("Kohdeosoite: " + jobInfo.address_to, fontSize = 20.sp)
                            Text("Sisältö: " + jobInfo.content, fontSize = 20.sp)
                            Text("Yritys: " + jobInfo.company, fontSize = 20.sp)
                            Text("pvm: " + jobInfo.time_created?.toDate()?.getStringTimeStampWithDate(),
                                fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.padding(40.dp))

                        Button(
                            onClick = { openPermissionState = true },
                            modifier = Modifier.size(220.dp, 80.dp)
                        ) {
                            Text("Ota kuva rahtikirjasta")
                        }
                    }
                }
            }
        }
    }
    if(openPermissionState) {
        OpenPermissionView(
            setCameraState = { cameraState = !cameraState },
            cameraState,
            setOpenPermissionState = {openPermissionState = it}
        )
    }
    if(cameraState){
        CameraView(onImageCaptured = { uri, fromGallery ->
            Log.d("CameraView", "Image Uri captured $uri, $fromGallery")
            photoUri = uri
            cameraState = false
            shouldShowPhoto = true

            }, onError = {imageCaptureException ->
                Log.d("Error ", "Failed with ", imageCaptureException)
            }
        )
    }
}


private fun jobDone(uri: Uri?, selectedItem: String?, onUpdateSuccess: () ->Unit, onUpdateFail: () -> Unit) {
    Log.d("Uri", "$uri")
    val storage = Firebase.storage
    val storageRef = storage.reference
    val path = uri.toString().drop(7)
    Log.d("path", path)
    val file = android.net.Uri.fromFile(File(path))
    val fileRef: StorageReference = storageRef.child("keikkapankki/${file.lastPathSegment}")

    val uploadTask = fileRef.putFile(file)

    uploadTask.addOnFailureListener{ e ->
        Log.d("fail", "failed with", e)
    }.addOnSuccessListener { taskSnapshot ->
        Log.d("upload", "success")
    }
    uploadTask.continueWithTask{ task ->
        if(!task.isSuccessful){
            task.exception?.let {
                throw it
            }
        }
        fileRef.downloadUrl
    }.addOnCompleteListener { task ->
        if(task.isSuccessful) {
            val downloadUri = task.result
            Log.d("urlTask", "$downloadUri")
            updateJob(downloadUri, selectedItem, onUpdateSuccess, onUpdateFail)
        }else {
            Log.d("urlTask", "couldn't get downloadUri")
        }
    }
}

private fun updateJob(downloadUri: Uri?, selectedItem: String?, onUpdateSuccess: () -> Unit, onUpdateFail: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    if (selectedItem != null) {
        val currentTime = java.sql.Timestamp(System.currentTimeMillis())
        db.collection("Jobs").document(selectedItem)
            .update("state", "closed",
                "imageUrl", downloadUri.toString(),
                    "time_delivered", currentTime)
            .addOnSuccessListener {
                Log.d("updateJob", "update successful")
                onUpdateSuccess()
            }
            .addOnFailureListener { e ->
                Log.d("Fail", "update failed with ", e)
                onUpdateFail()
            }
    }
}

@Composable
fun OpenPermissionView(
    setCameraState: (cameraState: Boolean) -> Unit,
    cameraState: Boolean,
    setOpenPermissionState: (Boolean) -> Unit
){
    val TAG = "OpenPermissionView"
    val permissionViewModel = PermissionViewModel()
    val context = LocalContext.current
    val requestPermission by permissionViewModel.performCameraAction.collectAsState()
    Log.d("requestPermission", "$requestPermission")
    if(requestPermission){
        Permission(
            context,
            Manifest.permission.CAMERA
        ){permissionAction ->
            when(permissionAction){
                is PermissionAction.OnPermissionGranted -> {
                    Log.d(TAG, "Permission grant successful")
                    Log.d(TAG, "cameraState: $cameraState")
                        permissionViewModel.setPerformCameraAction(false)
                        setOpenPermissionState(false)
                        setCameraState(cameraState)
                }
                is PermissionAction.OnPermissionDenied -> {
                    permissionViewModel.setPerformCameraAction(false)
                }
            }
        }
    }
}

fun getJobInfo(selectedItem: String?, setJobInfo: (Order?) -> Unit, db: FirebaseFirestore){
    if (selectedItem != null) {
        db.collection("Jobs").document(selectedItem)
            .get()
            .addOnSuccessListener { document ->
                var job = document.toObject<Order>()
                job?.order_id = selectedItem
                setJobInfo(job)
                Log.d("getJobInfo", "$job")
            }
            .addOnFailureListener{ e ->
                Log.d("Failed", "Failed with ", e)
            }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ReceiptPreview() {
        Receipt(rememberNavController(), selectedItem = String.toString(), auth = FirebaseAuth.getInstance())

}