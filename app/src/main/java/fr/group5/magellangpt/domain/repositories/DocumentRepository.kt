package fr.group5.magellangpt.domain.repositories

import android.net.Uri
import retrofit2.HttpException
import java.net.UnknownHostException

interface DocumentRepository {

    /**
     * Upload a document to the server
     * Can throw an HttpException if the server returns an error
     * Can throw an UnknownHostException if the server is unreachable
     */
    @Throws(HttpException::class, UnknownHostException::class)
    suspend fun uploadDocument(uri : Uri)
}