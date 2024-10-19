package com.example.sahara

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlin.math.roundToInt

@Composable
fun Health(
    viewModel: MainViewModel
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            it?.let {
                viewModel.uploadFile(it)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(!viewModel.state.isUploading) {
            Box(
                modifier = Modifier
                    .clickable {
                        photoPicker.launch("*/*")
                    }
                    .background(Color(0xFF2E80DA), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Pick Photo",
                    fontSize = 18.sp
                )

            }
            viewModel.state.wound?.let {
                Text(it.message)
            }
        }
        else {
            val animatedProgress by animateFloatAsState(
                targetValue = viewModel.state.progress,
                animationSpec = tween(durationMillis = 100),
                label = "File upload progress bar"
            )
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(16.dp)
            )
            Text(
                text = "${(viewModel.state.progress * 100).roundToInt()}%"
            )
            Box(
                modifier = Modifier
                    .clickable {
                        viewModel.cancelUpload()
                    }
                    .background(Color(0xFF2E80DA), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Cancel Upload",
                    fontSize = 18.sp
                )

            }
        }
    }
}
