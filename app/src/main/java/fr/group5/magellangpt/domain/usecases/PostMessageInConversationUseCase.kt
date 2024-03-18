package fr.group5.magellangpt.domain.usecases

import android.net.Uri
import android.util.Log
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.domain.repositories.ConversationRepository
import org.koin.java.KoinJavaComponent.get
import retrofit2.HttpException
import java.net.UnknownHostException
import java.util.UUID

class PostMessageInConversationUseCase(
    private val conversationRepository: ConversationRepository = get(ConversationRepository::class.java),
    private val resourcesHelper: ResourcesHelper = get(ResourcesHelper::class.java)
) {
    suspend operator fun invoke(conversationId : UUID, content: String, uris : List<Uri>) : Resource<Unit> {
        return try {
            conversationRepository.sendMessage(conversationId, content, uris)
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