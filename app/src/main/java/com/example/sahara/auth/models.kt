package com.example.sahara.auth

import com.example.sahara.Wound


data class SignInResult (
    val data: UserData?,
    val errMsg: String?
)

data class UserData (
    var userId: String = "",
    var username: String? = "",
    val token: String? = "",
    val ppurl: String? = "",
    var email: String = "",
)

data class UploadState(
    val isUploading: Boolean = false,
    val isUploadComplete: Boolean = false,
    val message: String? = null,
    val progress: Float = 0f,
    val wound: Wound? = null
)