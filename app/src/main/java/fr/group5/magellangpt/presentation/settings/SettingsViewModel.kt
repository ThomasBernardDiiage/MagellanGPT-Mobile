package fr.group5.magellangpt.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.group5.magellangpt.common.helpers.NavigationHelper
import fr.group5.magellangpt.domain.usecases.GetCurrentQuotaUseCase
import fr.group5.magellangpt.domain.usecases.GetCurrentUserUseCase
import fr.group5.magellangpt.domain.usecases.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase = get(LogoutUseCase::class.java),
    private val navigationHelper : NavigationHelper = get(NavigationHelper::class.java),
    private val getCurrentUserUseCase: GetCurrentUserUseCase = get(GetCurrentUserUseCase::class.java),
    private val getCurrentQuotaUseCase: GetCurrentQuotaUseCase = get(GetCurrentQuotaUseCase::class.java)
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            getCurrentQuotaUseCase().collectLatest { quota ->
                _uiState.update { it.copy(currentQuota = quota.currentQuota, maxQuota = quota.maxQuota) }
            }
        }
    }

    fun onEvent(event : SettingsEvent){
        when(event){
            is SettingsEvent.OnLogout -> onLogout()
            is SettingsEvent.OnGetUserInformation -> { getUserInformation() }
            is SettingsEvent.OnGoToKnowledgeBase -> { navigationHelper.navigateTo(NavigationHelper.Destination.KnowledgeBase) }
            is SettingsEvent.OnGoToTCU -> { navigationHelper.navigateTo(NavigationHelper.Destination.Tcu) }
        }
    }


    private fun onLogout(){
        viewModelScope.launch {
            logoutUseCase()
            navigationHelper.navigateTo(
                route = NavigationHelper.Destination.Login,
                popupTo = NavigationHelper.Destination.Main.route,
                inclusive = true
            )
        }
    }

    private fun getUserInformation(){
        viewModelScope.launch {
            getCurrentUserUseCase { user ->
                _uiState.update { it.copy(firstname = user.firstname, lastname = user.lastname, email = user.email) }
            }
        }
    }
}