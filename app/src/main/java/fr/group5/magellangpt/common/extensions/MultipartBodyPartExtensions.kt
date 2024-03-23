package fr.group5.magellangpt.common.extensions

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.java.KoinJavaComponent.get
import java.io.File
import java.io.FileOutputStream


private val context : Context = get(Context::class.java)

fun MultipartBody.Part.Companion.fromUri(uri: Uri, partName : String = "files") : MultipartBody.Part {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, uri.getFileName())
    FileOutputStream(file).use { outputStream ->
        inputStream?.copyTo(outputStream)
    }

    val requestFile = RequestBody.create("application/pdf".toMediaTypeOrNull(), file)
    return MultipartBody.Part.createFormData(partName, file.name, requestFile)
}