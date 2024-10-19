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
    fun uploadWoundFile(contentUri: Uri): Flow<WoundUploadResult> = channelFlow {
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
                        send(WoundUploadResult.Progress(bytesSentTotal, totalBytes))
                    }
                }
            }.body<Wound>()
            send(WoundUploadResult.Success(response))
            try {
                val response2: InjuryInfo = httpClient.submitFormWithBinaryData(
                    url = "http://192.168.131.121:8000/wound",
                    formData = formData {
                        append("file", info.bytes, Headers.build {
                            append(HttpHeaders.ContentType, info.mimeType)
                            append(HttpHeaders.ContentDisposition, "filename=${info.name}")
                        })
                    }
                ) {
                    method = HttpMethod.Patch
                    onUpload { bytesSentTotal, totalBytes ->
                        if (totalBytes > 0L) {
                            send(WoundUploadResult.Progress(bytesSentTotal, totalBytes))
                        }
                    }
                }.body<InjuryInfo>()
                send(WoundUploadResult.MoreInfo(response2))
            } catch (e: Exception) {
                close(e)
            }
        } catch (e: Exception) {
            send(WoundUploadResult.Error(e))
        }
    }


    fun uploadFoodFile(contentUri: Uri): Flow<FoodUploadResult> = channelFlow {
        val info = fileReader.uriToFileInfo(contentUri)

        try {
            val response: FoodResponse = httpClient.submitFormWithBinaryData(
                url = "http://192.168.131.121:8000/food",
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
                        send(FoodUploadResult.Progress(bytesSentTotal, totalBytes))
                    }
                }
            }.body<FoodResponse>()
            send(FoodUploadResult.Success(response))
        } catch (e: Exception) {
            send(FoodUploadResult.Error(e))
        }
    }

    fun uploadObjectFile(contentUri: Uri): Flow<ObjectUploadResult> = channelFlow {
        val info = fileReader.uriToFileInfo(contentUri)

        try {
            val response: ObjectResponse = httpClient.submitFormWithBinaryData(
                url = "http://192.168.131.121:8000/environment",
                formData = formData {
                    append("video_file", info.bytes, Headers.build {
                        append(HttpHeaders.ContentType, info.mimeType)
                        append(HttpHeaders.ContentDisposition, "filename=${info.name}")
                    })
                }
            ) {
                method = HttpMethod.Post
                onUpload { bytesSentTotal, totalBytes ->
                    if (totalBytes > 0L) {
                        send(ObjectUploadResult.Progress(bytesSentTotal, totalBytes))
                    }
                }
            }.body<ObjectResponse>()
            send(ObjectUploadResult.Success(response))
        } catch (e: Exception) {
            send(ObjectUploadResult.Error(e))
        }
    }

}

sealed class WoundUploadResult {
    data class Progress(val bytesSent: Long, val totalBytes: Long) : WoundUploadResult()
    data class Success(val wound: Wound) : WoundUploadResult()
    data class MoreInfo(val info: InjuryInfo) : WoundUploadResult()
    data class Error(val exception: Throwable) : WoundUploadResult()
}

sealed class FoodUploadResult {
    data class Progress(val bytesSent: Long, val totalBytes: Long) : FoodUploadResult()
    data class Success(val food: FoodResponse) : FoodUploadResult()
    data class Error(val exception: Throwable) : FoodUploadResult()
}

sealed class ObjectUploadResult {
    data class Progress(val bytesSent: Long, val totalBytes: Long) : ObjectUploadResult()
    data class Success(val obj: ObjectResponse) : ObjectUploadResult()
    data class Error(val exception: Throwable) : ObjectUploadResult()
}
