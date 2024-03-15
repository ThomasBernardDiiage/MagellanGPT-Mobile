package fr.group5.magellangpt.domain.usecases

import android.util.Log
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.domain.models.Model
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.domain.repositories.ModelRepository
import org.koin.java.KoinJavaComponent.get
import retrofit2.HttpException
import java.net.UnknownHostException

class GetAvailableModelsUseCase(
    private val modelRepository : ModelRepository = get(ModelRepository::class.java),
    private val resourcesHelper: ResourcesHelper = get(ResourcesHelper::class.java)
) {
    suspend operator fun invoke() : Resource<List<Model>> {
        return try {
            Resource.Success(modelRepository.getAvailableModels().sortedBy { it.index })
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