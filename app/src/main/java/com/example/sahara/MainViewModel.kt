package com.example.sahara

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sahara.auth.SignInResult
import com.example.sahara.auth.UploadState
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

    var state by mutableStateOf(UploadState())
        private set

    private var uploadJob: Job? = null

    fun uploadFile(contentUri: Uri) {
        uploadJob = repository
            .uploadFile(contentUri)
            .onStart {
                state = state.copy(
                    isUploading = true,
                    isUploadComplete = false,
                    message = null,
                    progress = 0f
                )
            }
            .onEach { result ->
                when (result) {
                    is UploadResult.Progress -> {
                        state = state.copy(
                            progress = result.bytesSent / result.totalBytes.toFloat()
                        )
                    }
                    is UploadResult.Success -> {
                        state = state.copy(
                            isUploading = false,
                            isUploadComplete = true,
                            wound = result.wound,
                            message = "Upload successful!"
                        )
                        println(state.wound)
                    }

                    is UploadResult.Error -> TODO()
                }
            }
            .onCompletion { cause ->
                if (cause == null) {
                    state = state.copy(isUploading = false)
                } else if (cause is CancellationException) {
                    state = state.copy(
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
                state = state.copy(
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
