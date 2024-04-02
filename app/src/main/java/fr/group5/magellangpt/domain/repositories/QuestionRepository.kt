package fr.group5.magellangpt.domain.repositories

import android.net.Uri
import fr.group5.magellangpt.domain.models.Question

interface QuestionRepository {

    suspend fun getQuestions() : List<Question>

    /**
     * Analyse le document TCU
     * @param uri : Uri du document TCU
     */
    suspend fun analyseTcu(uri : Uri) : List<Question>
}