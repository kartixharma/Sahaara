package com.example.sahara

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sahara.ui.theme.nunito

@Composable
fun Environment(
    viewModel: MainViewModel
) {
    val scrollState = rememberScrollState()
    val obj = viewModel.objectState.obj
    val comp by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.env
        ))
    val comp2 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.search
        ))

    if(obj == null && !viewModel.objectState.isUploading){
        Column(
            modifier = Modifier
                .fillMaxSize().padding(top = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sahaara",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = nunito,
                color = Color(0xff0080c7)
            )
            LottieAnimation(
                modifier = Modifier.weight(1f),
                composition = comp,
                iterations = LottieConstants.IterateForever
            )
        }
    }
    if(obj == null && viewModel.objectState.isUploading){
        Column(
            modifier = Modifier
                .fillMaxSize().padding(top = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sahaara",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = nunito,
                color = Color(0xff0080c7)
            )
            LottieAnimation(
                modifier = Modifier.weight(1f),
                composition = comp2,
                iterations = LottieConstants.IterateForever
            )
            val animatedProgress by animateFloatAsState(
                targetValue = viewModel.objectState.progress,
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
        }
    }

    obj?.let {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Detected Objects",
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = obj.detected_objects.joinToString(", "),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            DisplayResponseInfo(response = obj.response)
        }
    }
}

@Composable
fun DisplayResponseInfo(response: Response) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Project: ${response.project_name}",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Type: ${response.type}",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Objects That Can Be Used: ${response.objects_that_can_be_used}",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Time Taken: ${response.time_taken}",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Time Required: ${response.time_require}",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            DisplayDetailedProcess(detailedProcess = response.detailed_process)
        }
    }
}

@Composable
fun DisplayDetailedProcess(detailedProcess: DetailedProcess) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Detailed Process",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Materials Needed: ${detailedProcess.materials_needed}",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Instructions: ${detailedProcess.instructions}",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Tips: ${detailedProcess.tips}",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}