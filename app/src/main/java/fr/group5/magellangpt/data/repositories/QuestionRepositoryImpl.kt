package fr.group5.magellangpt.data.repositories

import android.net.Uri
import fr.group5.magellangpt.common.extensions.fromUri
import fr.group5.magellangpt.data.remote.ApiService
import fr.group5.magellangpt.domain.models.Question
import fr.group5.magellangpt.domain.repositories.QuestionRepository
import okhttp3.MultipartBody
import org.koin.java.KoinJavaComponent.get

class QuestionRepositoryImpl(
    private val apiService: ApiService = get(ApiService::class.java)
) : QuestionRepository {
    override suspend fun getQuestions(): List<Question> {
        val questionsDto = apiService.getTcuQuestions()

        return questionsDto.map {
            Question(
                index = it.index,
                question = it.text,
                answer = ""
            )
        }
    }

    override suspend fun analyseTcu(uri: Uri): List<Question> {
        val filePart = MultipartBody.Part.fromUri(uri, partName = "file")

        val result = apiService.tcuAnalysis(filePart)
        return result.map {
            Question(
                index = it.index,
                question = it.question,
                answer = it.answer
            )
        }
    }
}