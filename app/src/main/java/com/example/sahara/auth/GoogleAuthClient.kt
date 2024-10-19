package com.example.sahara.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.sahara.MainViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result =try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }
    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val cred = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = cred.googleIdToken
        val googleCred = GoogleAuthProvider.getCredential(googleIdToken,null)
        return try {
            val user = auth.signInWithCredential(googleCred).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        email = email.toString(),
                        userId = uid,
                        username = displayName,
                        ppurl = photoUrl.toString().substring(0,photoUrl.toString().length-6)
                    )
                },
                errMsg = null
            )
        } catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data= null,
                errMsg = e.message
            )
        }
    }

    suspend fun signOut(){
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        }catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            email = email.toString(),
            userId = uid,
            username = displayName,
            ppurl = photoUrl.toString()
        )
    }
    private fun buildSignInRequest(): BeginSignInRequest{
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("59476724147-l33slu6va63d2ckl2i72drjn5rskb06s.apps.googleusercontent.com")
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}