package fr.group5.magellangpt.domain.usecases

import android.net.Uri
import android.util.Log
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.domain.repositories.DocumentRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get
import retrofit2.HttpException
import java.net.UnknownHostException

class UploadDocumentUseCase(
    private val documentRepository: DocumentRepository = get(DocumentRepository::class.java),
    private val resourcesHelper: ResourcesHelper = get(ResourcesHelper::class.java),
    private val ioDispatcher: CoroutineDispatcher = get(CoroutineDispatcher::class.java)
) {
    suspend operator fun invoke(uri : Uri) : Resource<Unit> = withContext(ioDispatcher){
        try {
            documentRepository.uploadDocument(uri)
            Resource.Success(Unit)
        }
        catch (e : UnknownHostException){
            Resource.Error(resourcesHelper.getString(R.string.error_network))
        }
        catch (e : HttpException){
            Resource.Error(resourcesHelper.getString(R.string.error_occured_code, e.code()))
        }
        catch (e : Exception){
            Log.e("Exception catched", e.message, e)
            Resource.Error(resourcesHelper.getString(R.string.error_occured))
        }
    }
}