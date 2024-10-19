package com.example.sahara.auth


import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sahara.R
import com.example.sahara.ui.theme.nunito

@Composable
fun SignInScreen(

    onSignInCLick:() -> Unit,
) {
    val brush = Brush.linearGradient(
        listOf(
            Color(0xFF238CDD),
            Color(0xFF255DCC)
        )
    )
    Image(
        modifier = Modifier
            .fillMaxSize(),
        contentScale = ContentScale.Crop,
        painter = painterResource(R.drawable.login),
        contentDescription = null
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//        Image(
//            modifier = Modifier,
//            painter = painterResource(R.drawable.oig4__rndcloiljdx4hxpn),
//            contentDescription = null
//        )
        Text(text = "Sahaara",
            fontFamily = nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 36.sp,
            color = Color(0xFF333333))
//        Text(modifier = Modifier.padding(horizontal = 20.dp),
//            text = "Dive in and connect with friends, share stories, and discover new perspectives.",
//            style = MaterialTheme.typography.titleMedium,
//            textAlign = TextAlign.Center, color = Color(0xFF1D1D1D))
        Spacer(modifier = Modifier.height(70.dp))
        Button(
            onClick = onSignInCLick,
            modifier = Modifier
                .background(brush, CircleShape)
                .fillMaxWidth(0.7f)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            shape = CircleShape
        ) {
            Text(
                modifier = Modifier.padding(end = 20.dp),
                text = "Continue with Google",
                fontFamily = nunito,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Image(modifier = Modifier.scale(1.2f),
                painter = painterResource(id = R.drawable.goog_0ed88f7c),
                contentDescription = null,
            )
        }
    }

}