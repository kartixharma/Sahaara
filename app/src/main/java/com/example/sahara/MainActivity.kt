package com.example.sahara

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sahara.auth.GoogleAuthUiClient
import com.example.sahara.auth.SignInScreen
import com.example.sahara.ui.theme.SaharaTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        //FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SaharaTheme {
                val navController = rememberNavController()
                val viewModel: MainViewModel = viewModel()
                NavHost(navController, startDestination = "start") {
                    composable("start") {
                        LaunchedEffect(key1 = Unit) {
                            val userData = googleAuthUiClient.getSignedInUser()
                            if (userData != null) {

                                navController.navigate("app")
                            } else {
                                navController.navigate("signIn")
                            }
                        }
                    }
                    composable("signIn"){
                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = { result ->
                                if(result.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult = googleAuthUiClient.signInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        viewModel.onSignInResult(signInResult)
                                    }
                                }
                            }
                        )
                        LaunchedEffect(key1 = viewModel.signInState) {
                            if (viewModel.signInState) {
                                 Toast.makeText(applicationContext, "Signed In Successfully", Toast.LENGTH_SHORT).show()
                                val userData = googleAuthUiClient.getSignedInUser()
                                userData?.run {
                                    viewModel.addUserDataToFirestore(userData)
                                    navController.navigate("app")
                                }
                            }
                        }
                        SignInScreen(
                            onSignInCLick = {
                                lifecycleScope.launch {
                                    val signInIntentSender = googleAuthUiClient.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            }
                        )
                    }
                    composable("app") {
                        MainScreen()
                    }
                }
            }
        }
    }
}
