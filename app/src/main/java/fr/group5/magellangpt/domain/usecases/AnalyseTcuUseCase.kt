package fr.group5.magellangpt.domain.usecases

import android.net.Uri
import android.util.Log
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.domain.models.Question
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.domain.repositories.QuestionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get
import retrofit2.HttpException
import java.net.UnknownHostException

class AnalyseTcuUseCase(
    private val ioDispatcher: CoroutineDispatcher = get(CoroutineDispatcher::class.java),
    private val questionRepository: QuestionRepository = get(QuestionRepository::class.java),
    private val resourcesHelper: ResourcesHelper = get(ResourcesHelper::class.java)
) {

    suspend operator fun invoke(uri :Uri) : Resource<List<Question>> = withContext(ioDispatcher){
        try {
            Resource.Success(questionRepository.analyseTcu(uri))
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