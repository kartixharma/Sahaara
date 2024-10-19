package com.example.sahara.auth


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