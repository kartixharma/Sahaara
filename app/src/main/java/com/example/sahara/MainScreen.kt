package com.example.sahara

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sahara.auth.FoodUploadState
import com.example.sahara.auth.HealthUploadState
import com.example.sahara.auth.ObjectUploadState
import java.io.File
import java.io.FileOutputStream

fun compressImage(context: Context, uri: Uri): Uri? {
    // Load bitmap from the given URI
    val inputStream = context.contentResolver.openInputStream(uri)
    val originalBitmap = BitmapFactory.decodeStream(inputStream)
    inputStream?.close()

    // Define a new file for the compressed image
    val compressedFile = File(context.cacheDir, "compressed_image.jpg")

    // Compress the bitmap and write it to the new file
    val outputStream = FileOutputStream(compressedFile)
    originalBitmap?.compress(Bitmap.CompressFormat.JPEG, 20, outputStream)
    outputStream.flush()
    outputStream.close()

    // Get a URI for the compressed file
    return FileProvider.getUriForFile(
        context,
        "com.example.sahara.fileProvider",
        compressedFile
    )
}

data class BottomNavigationItem(
    val title: String,
    val Icon: ImageVector
)

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onSignOut: () -> Unit
){
    val items = listOf(
        BottomNavigationItem(
            title = "Food",
            Icon = Icons.Outlined.Fastfood
        ),
        BottomNavigationItem(
            title = "Environment",
            Icon = Icons.Outlined.Landscape
        ),
        BottomNavigationItem(
            title = "Health",
            Icon = Icons.Outlined.HealthAndSafety
        ),
        BottomNavigationItem(
            title = "Profile",
            Icon = Icons.Outlined.PersonOutline
        ),
    )
    val context = LocalContext.current
    var selItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val file = File(context.filesDir, "temp.png")
    val file1 = File(context.filesDir, "temp1.png")
    var uri = FileProvider.getUriForFile(
        context,
        "com.example.sahara.fileProvider",
        file
    )
    var uri2 = FileProvider.getUriForFile(
        context,
        "com.example.sahara.fileProvider",
        file1
    )
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val interactionSource = remember { MutableInteractionSource() }
    val currentRoute = navBackStackEntry?.destination?.route
    var showDialog by remember { mutableStateOf(false) }
    val storageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            it?.let {
                if(currentRoute=="Food") {
                    viewModel.foodImageUri = it
                    viewModel.foodState = FoodUploadState()
                    viewModel.uploadFoodFile(it)
                }
                if(currentRoute=="Health") {
                    viewModel.woundImageUri = it
                    viewModel.healthState = HealthUploadState()
                    viewModel.uploadWoundFile(it)
                }
                if(currentRoute == "Environment"){
                    viewModel.objectVidUri = it
                    viewModel.objectState = ObjectUploadState()
                    viewModel.uploadObjectFile(it)
                }
            }
        }
    )
    val cLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { boolean ->
        if(boolean) {
            val uric = compressImage(context, uri)
            uric?.let {
                    viewModel.foodImageUri = null
                    viewModel.foodImageUri = it
                    viewModel.foodState = FoodUploadState()
                    viewModel.uploadFoodFile(it)

            }
        }
    }
    val cLauncher2 = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { boolean ->
        if(boolean) {
            val uric = compressImage(context, uri2)
            uric?.let {
                viewModel.woundImageUri = null
                viewModel.woundImageUri = it
                viewModel.healthState = HealthUploadState()
                viewModel.uploadWoundFile(it)
            }
        }
    }

    val vLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.CaptureVideo()) {
        if(it) {
            viewModel.objectVidUri = uri
            viewModel.objectState = ObjectUploadState()
            viewModel.uploadObjectFile(uri)

        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        if(!it) {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
        else{
            cLauncher.launch(uri)
        }
    }
if(showDialog) {
    AlertDialog(
        onDismissRequest = { showDialog = false },
        title = { Text(text = "Choose an Option") },
        text = {
            if(currentRoute == "Environment") {
                Text("Select a video from storage or record a new video with the camera.")
            }
            else{
                Text("Select an image from storage or take a new photo with the camera.")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    storageLauncher.launch("*/*")
                    showDialog = false
                }
            ) {
                Text(text = "From Storage", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    if(currentRoute != "Environment") {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.CAMERA
                            )
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            if(currentRoute == "Food") cLauncher.launch(uri)
                            if(currentRoute == "Health") cLauncher2.launch(uri2)
                        } else {
                            permissionLauncher.launch(android.Manifest.permission.CAMERA)
                        }
                    }
                    else{
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.CAMERA
                            )
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            vLauncher.launch(uri)
                        } else {
                            permissionLauncher.launch(android.Manifest.permission.CAMERA)
                        }
                    }
                    showDialog = false
                }
            ) {
                Text(text = "From Camera", color = MaterialTheme.colorScheme.primary)
            }
        }
    )
}
    Scaffold(
        floatingActionButton = {
            if(currentRoute != "Profile") {
                Button(
                    colors = ButtonDefaults.buttonColors(Color(0xff0080c7)),
                    onClick = {
                        showDialog = true
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(80.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.camera),
                        contentDescription = null,
                        Modifier.size(25.dp),
                        tint = Color(0xffF6FBFA),
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(
                contentColor = Color.Transparent,
                containerColor = Color(0xfff7fbff)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                    horizontalArrangement = Arrangement.Center) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        items.forEachIndexed { index, item ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable(indication = null, interactionSource = interactionSource) {
                                        navController.navigate(item.title)
                                        selItemIndex = index
                                    }) {
                                val color =
                                    if (currentRoute == item.title)
                                        Color(0xFF2E80DA)
                                    else
                                        Color(0xff9baab5)
                                Icon(
                                    imageVector = item.Icon,
                                    item.title,
                                    tint = color,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.title,
                                    color = color,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "Food",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("Food") {
                Food(viewModel)
            }
            composable("Environment") {
                Environment(viewModel)
            }
            composable("Health") {
                Health(
                    viewModel
                )
            }
            composable("Profile") {
                Profile(
                    viewModel,
                    onSignOut = { onSignOut() }
                )
            }
        }
    }
}