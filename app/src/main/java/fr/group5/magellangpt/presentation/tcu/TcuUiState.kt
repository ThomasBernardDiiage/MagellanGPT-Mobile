package fr.group5.magellangpt.presentation.tcu

import fr.group5.magellangpt.domain.models.Question

data class TcuUiState(
    val loadingQuestions : Boolean = false,
    val sendingDocument : Boolean = false,

    val questions : List<Question> = emptyList()
)