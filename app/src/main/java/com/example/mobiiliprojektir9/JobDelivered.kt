package com.example.mobiiliprojektir9

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.camera2.internal.annotation.CameraExecutor
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
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.mobiiliprojektir9.ui.theme.LogOut
import com.example.mobiiliprojektir9.ui.theme.MobiiliprojektiR9Theme
import com.firebase.ui.auth.AuthUI.TAG
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

//class JobDelivered : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MobiiliprojektiR9Theme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    Receipt()
//                }
//            }
//        }
//    }
//}

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
    var jobDone by rememberSaveable { mutableStateOf(false)}
    var photoUri by remember { mutableStateOf<Uri?>(null)}
    var shouldShowPhoto by remember {mutableStateOf(false)}
 //   var uriState by remember { mutableStateOf(Uri)}

    getJobInfo(selectedItem, setJobInfo = {
        if (it != null) {
            jobInfo = it
        }
    }, db )

    permissionViewModel.setId(jobInfo.driver_id)

    Scaffold(
        topBar = {TopAppBar(
            elevation = 4.dp,
            title = {Text(text = "Avoimet keikat")},
            navigationIcon = {
                val userId by permissionViewModel.id.collectAsState()
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
            Spacer(modifier = Modifier.padding(20.dp))
            Text(text = "Keikan kuittaus", fontSize = 30.sp)
            TabRowDefaults.Divider(color = Color.Black, thickness = 5.dp)
            Spacer(modifier = Modifier.padding(20.dp))
            if(shouldShowPhoto){
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Column(
                        modifier = Modifier
                            .padding(2.dp)
                    ) {
                        Text( jobInfo.address_from, fontSize = 15.sp)
                        Text(jobInfo.address_to, fontSize = 15.sp)
                        Text(jobInfo.content, fontSize = 15.sp)
                        Text( jobInfo.company, fontSize = 15.sp)
                        Text(jobInfo.time_created?.toDate()?.getStringTimeStampWithDate().toString(),
                            fontSize = 15.sp)
                    }
                    Image(
                        painter = rememberImagePainter(photoUri),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp,300.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(20.dp))
//                Button(
//                    onClick = { openPermissionState = true },
//                    modifier = Modifier.size(220.dp, 60.dp)
//                ) {
//                    Text("Ota kuva rahtikirjasta")
//                }
                Button(
                    onClick = { jobDone(photoUri, onJobDone = {jobDone = true}, selectedItem) },
                    modifier = Modifier.size(220.dp, 80.dp)
                ) {
                    Text("Kuittaa keikka ajetuksi", )
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(2.dp)
                ) {
                    Text("Lähtöosoite: " + jobInfo.address_from, fontSize = 20.sp)
                    Text("Kohdeosoite: " + jobInfo.address_to, fontSize = 20.sp)
                    Text("Sisältö: " + jobInfo.content, fontSize = 20.sp)
                    Text("Yritys: " + jobInfo.company, fontSize = 20.sp)
                    Text("pvm: " + jobInfo.time_created?.toDate()?.getStringTimeStampWithDate(),
                        fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.padding(30.dp))
            Button(
                onClick = { openPermissionState = true },
                modifier = Modifier.size(220.dp, 80.dp)
            ) {
                Text("Ota kuva rahtikirjasta")
            }
//            Button(
//                onClick = { /*TODO*/ },
//                modifier = Modifier.size(220.dp, 80.dp)
//            ) {
//                Text("Kuittaa keikka ajetuksi", )
//            }
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
            })
        }
    }
}

fun jobDone(uri: Uri?, onJobDone: () ->Unit, selectedItem: String?) {
    Log.d("Uri", "$uri")
    val storage = Firebase.storage
    val storageRef = storage.reference
    val path = uri.toString().drop(7)
    Log.d("path", path)
    val file = android.net.Uri.fromFile(File(path))
    val fileRef: StorageReference? = storageRef.child("keikkapankki/${file.lastPathSegment}")

    val uploadTask = fileRef?.putFile(file)

    uploadTask?.addOnFailureListener{ e ->
        Log.d("fail", "failed with", e)
    }?.addOnSuccessListener { taskSnapshot ->
        Log.d("upload", "success")
    }
    val urlTask = uploadTask?.continueWithTask{ task ->
        if(!task.isSuccessful){
            task.exception?.let {
                throw it
            }
        }
        fileRef.downloadUrl
    }?.addOnCompleteListener { task ->
        if(task.isSuccessful) {
            val downloadUri = task.result
            Log.d("urlTask", "$downloadUri")
            updateJob(downloadUri, selectedItem)
        }else {
            Log.d("urlTask", "couldn't get downloadUri")
        }
    }
}

fun updateJob(downloadUri: Uri?, selectedItem: String?) {
    val db = FirebaseFirestore.getInstance()
    if (selectedItem != null) {
        val currentTime = java.sql.Timestamp(System.currentTimeMillis())
        db.collection("Jobs").document(selectedItem)
            .update("state", "closed",
                "imageUrl", downloadUri.toString(),
                    "time_delivered", currentTime)
            .addOnSuccessListener { Log.d("updateJob", "update successful") }
            .addOnFailureListener { e -> Log.d("Fail", "update failed with ", e)}
    }
}

@Composable
fun OpenPermissionView(
    setCameraState: (cameraState: Boolean) -> Unit,
    cameraState: Boolean,
    setOpenPermissionState: (Boolean) -> Unit
){
    Log.d("OpenCameraview", "true")
    val TAG = "OpenCameraView"
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

//    if(cameraState && !requestPermission){
//        CameraView(onImageCaptured = { uri, fromGallery ->
//            Log.d("CameraView", "Image Uri captured $uri")
//        }, onError = {imageCaptureException ->
//            Log.d("Error ", "Failed with ", imageCaptureException)
//        })
//    }
//    setCameraState(cameraState)
//    setOpenCameraState(false)
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

    /*
    .update("state", "closed")
    .update("time_delivered", timestamp)
    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
     */

@Preview(showSystemUi = true)
@Composable
fun ReceiptPreview() {
        Receipt(rememberNavController(), selectedItem = String.toString(), auth = FirebaseAuth.getInstance())

}