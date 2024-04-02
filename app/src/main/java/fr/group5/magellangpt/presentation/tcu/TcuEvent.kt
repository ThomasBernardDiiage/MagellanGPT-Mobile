package fr.group5.magellangpt.presentation.tcu

import android.net.Uri

sealed class TcuEvent {
    data class OnDocumentLoaded(val uri : Uri) : TcuEvent()
    data object OnLoadQuestions : TcuEvent()

}