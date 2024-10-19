package com.example.sahara

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sahara.auth.FoodUploadState
import com.example.sahara.auth.HealthUploadState
import com.example.sahara.auth.ObjectUploadState
import com.example.sahara.auth.SignInResult
import com.example.sahara.auth.UserData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import java.io.FileNotFoundException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.cancellation.CancellationException

class MainViewModel(
    private val repository: FileRepository
): ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    var woundImageUri by mutableStateOf<Uri?>(null)
    var foodImageUri by mutableStateOf<Uri?>(null)
    var objectVidUri by mutableStateOf<Uri?>(null)
    var signInState by mutableStateOf(false)
    var userData by mutableStateOf(UserData())

    fun onSignInResult(result: SignInResult) {
        if(result.data != null){
            userData = result.data
            signInState = true
        }
    }

    fun addUserDataToFirestore(userData: UserData) {
        val userDocument = firestore.collection("users").document(userData.email)
        val userDataMap = mapOf(
            "userId" to userData.userId,
            "username" to userData.username,
            "ppurl" to userData.ppurl,
            "email" to userData.email,
        )
        userDocument.get().addOnSuccessListener {
            if(it.exists()){

            }
            else{
                userDocument.set(userDataMap)
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "User data added to Firestore successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e(ContentValues.TAG, "Error adding user data to Firestore", e)
                    }
            }
        }
    }

    var healthState by mutableStateOf(HealthUploadState())
    var foodState by mutableStateOf(FoodUploadState())
    var objectState by mutableStateOf(ObjectUploadState())

    private var uploadJob: Job? = null

    fun uploadWoundFile(contentUri: Uri) {
        uploadJob = repository
            .uploadWoundFile(contentUri)
            .onStart {
                healthState = healthState.copy(
                    isUploading = true,
                    isUploadComplete = false,
                    message = null,
                    progress = 0f
                )
            }
            .onEach { result ->
                when (result) {
                    is WoundUploadResult.Progress -> {
                        healthState = healthState.copy(
                            progress = result.bytesSent / result.totalBytes.toFloat()
                        )
                    }
                    is WoundUploadResult.Success -> {
                        healthState = healthState.copy(
                            isUploading = false,
                            isUploadComplete = true,
                            wound = result.wound,
                            message = "Upload successful!"
                        )
                        println(healthState.wound)
                    }

                    is WoundUploadResult.Error -> {
                        healthState = HealthUploadState()
                    }
                    is WoundUploadResult.MoreInfo -> {
                        healthState = healthState.copy(
                            isUploading = false,
                            isUploadComplete = true,
                            moreInfo = result.info
                        )
                    }

                }
            }
            .onCompletion { cause ->
                if (cause == null) {
                    healthState = healthState.copy(isUploading = false)
                } else if (cause is CancellationException) {
                    healthState = healthState.copy(
                        isUploading = false,
                        message = "The upload was cancelled!",
                        isUploadComplete = false,
                        progress = 0f
                    )
                }
            }
            .catch { cause ->
                val message = when (cause) {
                    is OutOfMemoryError -> "File too large!"
                    is FileNotFoundException -> "File not found!"
                    is UnresolvedAddressException -> "No internet!"
                    else -> "Something went wrong!"
                }
                healthState = healthState.copy(
                    isUploading = false,
                    message = message
                )
            }
            .launchIn(viewModelScope)
    }


    fun uploadFoodFile(contentUri: Uri) {
        uploadJob = repository
            .uploadFoodFile(contentUri)
            .onStart {
                foodState = foodState.copy(
                    isUploading = true,
                    isUploadComplete = false,
                    message = null,
                    progress = 0f
                )
            }
            .onEach { result ->
                when (result) {
                    is FoodUploadResult.Progress -> {
                        foodState = foodState.copy(
                            progress = result.bytesSent / result.totalBytes.toFloat()
                        )
                    }
                    is FoodUploadResult.Success -> {
                        foodState = foodState.copy(
                            isUploading = false,
                            isUploadComplete = true,
                            food = result.food,
                            message = "Upload successful!"
                        )
                    }

                    is FoodUploadResult.Error -> {
                        foodState = FoodUploadState()
                    }

                }
            }
            .onCompletion { cause ->
                if (cause == null) {
                    foodState = foodState.copy(isUploading = false)
                } else if (cause is CancellationException) {
                    foodState = foodState.copy(
                        isUploading = false,
                        message = "The upload was cancelled!",
                        isUploadComplete = false,
                        progress = 0f
                    )
                }
            }
            .catch { cause ->
                val message = when (cause) {
                    is OutOfMemoryError -> "File too large!"
                    is FileNotFoundException -> "File not found!"
                    is UnresolvedAddressException -> "No internet!"
                    else -> "Something went wrong!"
                }
                foodState = foodState.copy(
                    isUploading = false,
                    message = message
                )
            }
            .launchIn(viewModelScope)
    }

    fun uploadObjectFile(contentUri: Uri) {
        uploadJob = repository
            .uploadObjectFile(contentUri)
            .onStart {
                objectState = objectState.copy(
                    isUploading = true,
                    isUploadComplete = false,
                    message = null,
                    progress = 0f
                )
            }
            .onEach { result ->
                when (result) {
                    is ObjectUploadResult.Progress -> {
                        objectState = objectState.copy(
                            progress = result.bytesSent / result.totalBytes.toFloat()
                        )
                    }
                    is ObjectUploadResult.Success -> {
                        objectState = objectState.copy(
                            isUploading = false,
                            isUploadComplete = true,
                            obj = result.obj,
                            message = "Upload successful!"
                        )
                    }

                    is ObjectUploadResult.Error -> {
                        objectState = ObjectUploadState()
                    }

                }
            }
            .onCompletion { cause ->
                if (cause == null) {
                    objectState = objectState.copy(isUploading = false)
                } else if (cause is CancellationException) {
                    objectState = objectState.copy(
                        isUploading = false,
                        message = "The upload was cancelled!",
                        isUploadComplete = false,
                        progress = 0f
                    )
                }
            }
            .catch { cause ->
                val message = when (cause) {
                    is OutOfMemoryError -> "File too large!"
                    is FileNotFoundException -> "File not found!"
                    is UnresolvedAddressException -> "No internet!"
                    else -> "Something went wrong!"
                }
                foodState = foodState.copy(
                    isUploading = false,
                    message = message
                )
            }
            .launchIn(viewModelScope)
    }

    fun cancelUpload() {
        uploadJob?.cancel()
    }
}
