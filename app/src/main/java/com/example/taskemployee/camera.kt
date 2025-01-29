package com.example.taskemployee

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.taskemployee.retrofit.EmployeeViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CameraView(viewModel: EmployeeViewModel) {
    val context = LocalContext.current
    val capturedImageUris = remember { mutableStateListOf<Uri>() }
    val images by viewModel.allImages.observeAsState(emptyList())

    var currentCaptureUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            currentCaptureUri?.let { uri ->
                capturedImageUris.add(uri)
                viewModel.addImage(uri.toString())
                currentCaptureUri = null
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            currentCaptureUri?.let { uri ->
                cameraLauncher.launch(uri)
            }
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

        Button(onClick = {
            val newFile = context.createImageFile()
            val newUri = FileProvider.getUriForFile(
                context,
                "${BuildConfig.APPLICATION_ID}.provider",
                newFile
            )

            currentCaptureUri = newUri

            val permissionCheck = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            )

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(newUri)
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }) {
            Text("Capture Image")
        }

        LazyRow {
            items(images) { data ->
                Image(
                    painter = rememberImagePainter(data.uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .padding(5.dp)
                )
            }
        }

}

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}
