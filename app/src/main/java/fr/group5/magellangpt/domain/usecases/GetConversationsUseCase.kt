package fr.group5.magellangpt.domain.usecases

import android.util.Log
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.domain.models.Conversation
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.domain.repositories.ConversationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get
import retrofit2.HttpException
import java.net.UnknownHostException

class GetConversationsUseCase(
    private val conversationRepository: ConversationRepository = get(ConversationRepository::class.java),
    private val ioDispatcher: CoroutineDispatcher = get(CoroutineDispatcher::class.java),
    private val resourcesHelper: ResourcesHelper = get(ResourcesHelper::class.java)
) {
    suspend operator fun invoke(): Resource<List<Conversation>> = withContext(ioDispatcher) {
        try {
            Resource.Success(conversationRepository.getConversations().sortedByDescending { it.lastModificationDate })
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