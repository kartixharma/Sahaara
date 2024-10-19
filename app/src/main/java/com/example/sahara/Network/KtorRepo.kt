package com.example.sahara.Network

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse

class GetRepo {
    suspend fun initiateSignUp(email: String): HttpResponse = KtorClient.httpClient.post {
        url("http://192.168.147.121:8000/wound")

    }
}