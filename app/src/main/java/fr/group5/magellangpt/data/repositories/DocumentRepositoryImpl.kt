package fr.group5.magellangpt.data.repositories

import android.net.Uri
import fr.group5.magellangpt.common.extensions.fromUri
import fr.group5.magellangpt.data.remote.ApiService
import fr.group5.magellangpt.domain.repositories.DocumentRepository
import okhttp3.MultipartBody
import org.koin.java.KoinJavaComponent.get

class DocumentRepositoryImpl(
    private val apiService: ApiService = get(ApiService::class.java),
) : DocumentRepository {
    override suspend fun uploadDocument(uri : Uri) {
        val part = MultipartBody.Part.fromUri(uri)
        apiService.uploadDocument(part)
    }
}