package com.example.mobiiliprojektir9

import android.annotation.SuppressLint
import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import java.io.File
import java.nio.file.Files.createFile
import java.text.SimpleDateFormat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private val TAG = "CameraView"
@Composable
fun CameraView(
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit
){
    Log.d(TAG, "cameraview")
    val context = LocalContext.current
    var lensFacing by remember {mutableStateOf(CameraSelector.LENS_FACING_BACK)}
    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder().build()
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ){
        uri: Uri? ->
        if(uri != null){
            onImageCaptured(uri, true)
        }
    }
    CameraPreviewView(
        imageCapture,
        lensFacing
    ){cameraUIAction ->
        when (cameraUIAction){
            is CameraUIAction.OnCameraClick -> {
                imageCapture.takePicture(context, lensFacing, onImageCaptured, onError)
            }
            is CameraUIAction.OnSwitchCameraClick -> {
                lensFacing =
                    if(lensFacing == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
                    else
                        CameraSelector.LENS_FACING_BACK
            }
            is CameraUIAction.OnGalleryViewClick -> {
                if(true == context.getOutputDirectory().listFiles()?.isNotEmpty()){
                    galleryLauncher.launch("image/*")
                }
            }
        }

    }
}

sealed class CameraUIAction{
    object OnCameraClick : CameraUIAction()
    object OnGalleryViewClick : CameraUIAction()
    object OnSwitchCameraClick : CameraUIAction()
}

@SuppressLint("RestrictedApi")
@Composable
private fun CameraPreviewView(
    imageCapture: ImageCapture,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    cameraUIAction: (CameraUIAction) -> Unit
){
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    val previewView = remember { PreviewView(context) }

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(modifier = Modifier.fillMaxSize()){
        AndroidView({previewView}, modifier = Modifier.fillMaxSize()){

        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Bottom
        ){
            CameraControls(cameraUIAction)
        }
    }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}
@Composable
fun CameraControls(cameraUIAction: (CameraUIAction) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        CameraControl(
            painter = painterResource(id = R.drawable.sharp_flip_camera_android_24),
            R.string.icn_camera_view_switch_camera_content_description,
            modifier= Modifier.size(64.dp),
            onClick = { cameraUIAction(CameraUIAction.OnSwitchCameraClick) }
        )

        CameraControl(
            painter = painterResource(id = R.drawable.sharp_lens_24),
            R.string.icn_camera_view_camera_shutter_content_description,
            modifier= Modifier
                .size(64.dp)
                .padding(1.dp)
                .border(1.dp, Color.White, CircleShape),
            onClick = { cameraUIAction(CameraUIAction.OnCameraClick) }
        )

        CameraControl(
            painter = painterResource(id = R.drawable.sharp_photo_library_24),
            R.string.icn_camera_view_view_gallery_content_description,
            modifier= Modifier.size(64.dp),
            onClick = { cameraUIAction(CameraUIAction.OnGalleryViewClick) }
        )

    }
}

@Composable
fun CameraControl(
    painter: Painter,
    contentDescId: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter,
            contentDescription = stringResource(id = contentDescId),
            modifier = modifier,
            tint = Color.White
        )
    }
}

private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val PHOTO_EXTENSION =  ".jpg"

fun ImageCapture.takePicture(
    context: Context,
    lensFacing: Int,
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit
){
    val outputDirectory = context.getOutputDirectory()
    //create output file to hold the image
    val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
    val outputFileOptions = getOutputFileOptions(lensFacing, photoFile)

    this.takePicture(
        outputFileOptions,
        Executors.newSingleThreadExecutor(),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults){
                val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                // If the folder selected is an external media directory, this is
                // unnecessary but otherwise other apps will not be able to access our
                // images unless we scan them using [MediaScannerConnection]
                val mimeType = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(savedUri.toFile().extension)
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(savedUri.toFile().absolutePath),
                    arrayOf(mimeType)
                ){ _, uri ->

                }
                onImageCaptured(savedUri, false)
            }
            override fun onError(exception: ImageCaptureException){
                onError(exception)
            }
        })
}

fun getOutputFileOptions(
    lensFacing: Int,
    photoFile: File
): ImageCapture.OutputFileOptions{
    // Setup image capture metadata
    val metadata = ImageCapture.Metadata().apply {
        //Mirror image when using the front camera
        isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
    }
    //create output options object which contains file + metadata
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
        .setMetadata(metadata)
        .build()

    return outputOptions
}

fun createFile(baseFolder: File, format: String, extension: String) =
    File(
        baseFolder, SimpleDateFormat(format, java.util.Locale.ENGLISH)
            .format(System.currentTimeMillis()) + extension
    )

fun Context.getOutputDirectory(): File {
    val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
        File(it, this.resources.getString(R.string.app_name)).apply {mkdirs()}
    }
    return if(mediaDir != null && mediaDir.exists())
        mediaDir else this.filesDir
}