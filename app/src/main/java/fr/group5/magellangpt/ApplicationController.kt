package fr.group5.magellangpt

import android.app.Application
import android.content.Context
import fr.group5.magellangpt.common.helpers.ErrorHelper
import fr.group5.magellangpt.common.helpers.PreferencesHelper
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.common.helpers.implementations.ErrorHelperImpl
import fr.group5.magellangpt.common.helpers.implementations.PreferencesHelperImpl
import fr.group5.magellangpt.common.helpers.implementations.ResourcesHelperImpl
import fr.group5.magellangpt.common.navigation.Navigator
import fr.group5.magellangpt.common.navigation.implementations.NavigatorImpl
import fr.group5.magellangpt.data.repositories.AuthenticationRepositoryImpl
import fr.group5.magellangpt.domain.repositories.AuthenticationRepository
import fr.group5.magellangpt.domain.usecases.LoginUseCase
import fr.group5.magellangpt.domain.usecases.GetConversationUseCase
import fr.group5.magellangpt.domain.usecases.GetCurrentUserUseCase
import fr.group5.magellangpt.domain.usecases.LogoutUseCase
import fr.group5.magellangpt.domain.usecases.UserConnectedUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.dsl.module

class ApplicationController : Application() {

    private val appModule = module {
        single<Navigator> { NavigatorImpl() }

        single<AuthenticationRepository> { AuthenticationRepositoryImpl() }

        single<ResourcesHelper> { ResourcesHelperImpl() }
        single<PreferencesHelper> {  PreferencesHelperImpl() }
        single<ErrorHelper> {  ErrorHelperImpl() }

        single<Context> { androidContext()}

        single { GetConversationUseCase() }
        single { LoginUseCase() }
        single { LogoutUseCase() }
        single { UserConnectedUseCase() }
        single { GetCurrentUserUseCase() }

        // https://developer.android.com/kotlin/coroutines/coroutines-best-practices?hl=fr
        single<CoroutineDispatcher> { Dispatchers.IO }
    }

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            modules(appModule)
            androidContext(this@ApplicationController)
        }
    }
}