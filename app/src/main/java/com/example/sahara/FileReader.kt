package com.example.sahara


import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class FileReader(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun uriToFileInfo(contentUri: Uri): FileInfo {
        return withContext(ioDispatcher) {
            val contentResolver = context.contentResolver

            // Extract the file name from the URI
            val fileName = contentResolver.query(contentUri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(nameIndex)
            } ?: UUID.randomUUID().toString() // Fallback to a random UUID if name not found

            // Get the MIME type
            val mimeType = contentResolver.getType(contentUri) ?: ""

            // Extract the file extension
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                ?: fileName.substringAfterLast('.', "")

            // Get the file bytes
            val bytes = contentResolver.openInputStream(contentUri)?.use { inputStream ->
                inputStream.readBytes()
            } ?: byteArrayOf()

            // Build the full file name with extension if needed
            val fullFileName = if (extension.isNotEmpty() && !fileName.endsWith(".$extension")) {
                "$fileName.$extension"
            } else {
                fileName
            }

            FileInfo(
                name = fullFileName,
                mimeType = mimeType,
                bytes = bytes
            )
        }
    }
}

class FileInfo(
    val name: String,
    val mimeType: String,
    val bytes: ByteArray
)
