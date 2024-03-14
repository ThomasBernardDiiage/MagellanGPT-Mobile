package fr.group5.magellangpt.domain.usecases

import android.util.Log
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.domain.repositories.MessageRepository
import org.koin.java.KoinJavaComponent.get
import retrofit2.HttpException

class SendMessageUseCase(
    private val messageRepository: MessageRepository = get(MessageRepository::class.java),
    private val resourcesHelper: ResourcesHelper = get(ResourcesHelper::class.java)
) {
    suspend operator fun invoke(content: String) : Resource<Unit> {
        return try {
            messageRepository.sendMessage(content)
            Resource.Success(Unit)
        } catch (e : HttpException){
            Resource.Error(resourcesHelper.getString(R.string.error_occured_code, e.code()))
        } catch (e : Exception){
            Log.e("SendMessageUseCase", e.message, e)
            Resource.Error(resourcesHelper.getString(R.string.error_occured))
        }
    }
}