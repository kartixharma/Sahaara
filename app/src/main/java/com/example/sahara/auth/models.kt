package com.example.sahara.auth

import com.example.sahara.FoodResponse
import com.example.sahara.FoodUploadResult
import com.example.sahara.InjuryInfo
import com.example.sahara.ObjectResponse
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

data class HealthUploadState(
    val isUploading: Boolean = false,
    val isUploadComplete: Boolean = false,
    val message: String? = null,
    val progress: Float = 0f,
    val wound: Wound? = null,
    val moreInfo: InjuryInfo? = null
)

data class FoodUploadState(
    val isUploading: Boolean = false,
    val isUploadComplete: Boolean = false,
    val message: String? = null,
    val progress: Float = 0f,
    val food: FoodResponse? = null,
)

data class ObjectUploadState(
    val isUploading: Boolean = false,
    val isUploadComplete: Boolean = false,
    val message: String? = null,
    val progress: Float = 0f,
    val obj: ObjectResponse? = null,
)