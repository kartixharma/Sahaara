package com.example.sahara

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.sahara.auth.SignInResult
import com.example.sahara.auth.UserData
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel: ViewModel() {
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
}