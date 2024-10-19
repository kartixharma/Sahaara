package com.example.sahara

import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.append
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentLength
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow

class FileRepository(
    private val httpClient: HttpClient,
    private val fileReader: FileReader,
) {
    fun uploadFile(contentUri: Uri): Flow<UploadResult> = channelFlow {
        val info = fileReader.uriToFileInfo(contentUri)

        try {
            val response: Wound = httpClient.submitFormWithBinaryData(
                url = "http://192.168.131.121:8000/wound",
                formData = formData {
                    append("file", info.bytes, Headers.build {
                        append(HttpHeaders.ContentType, info.mimeType)
                        append(HttpHeaders.ContentDisposition, "filename=${info.name}")
                    })
                }
            ) {
                method = HttpMethod.Post
                onUpload { bytesSentTotal, totalBytes ->
                    if (totalBytes > 0L) {
                        send(UploadResult.Progress(bytesSentTotal, totalBytes))
                    }
                }
            }.body<Wound>()

            send(UploadResult.Success(response))
        } catch (e: Exception) {
            close(e)
        }
    }
}

sealed class UploadResult {
    data class Progress(val bytesSent: Long, val totalBytes: Long) : UploadResult()
    data class Success(val wound: Wound) : UploadResult()
    data class Error(val exception: Throwable) : UploadResult()
}
