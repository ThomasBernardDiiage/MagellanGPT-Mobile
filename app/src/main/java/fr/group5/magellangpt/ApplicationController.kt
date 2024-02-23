package fr.group5.magellangpt

import android.app.Application
import android.content.Context
import fr.group5.magellangpt.common.helpers.ErrorHelper
import fr.group5.magellangpt.common.helpers.MediaPlayerHelper
import fr.group5.magellangpt.common.helpers.PreferencesHelper
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.common.helpers.implementations.ErrorHelperImpl
import fr.group5.magellangpt.common.helpers.implementations.MediaPlayerHelperImpl
import fr.group5.magellangpt.common.helpers.implementations.PreferencesHelperImpl
import fr.group5.magellangpt.common.helpers.implementations.ResourcesHelperImpl
import fr.group5.magellangpt.common.navigation.Navigator
import fr.group5.magellangpt.common.navigation.implementations.NavigatorImpl
import fr.group5.magellangpt.data.repositories.AuthenticationRepositoryImpl
import fr.group5.magellangpt.domain.repositories.AuthenticationRepository
import fr.group5.magellangpt.domain.usecases.AuthenticationUseCase
import fr.group5.magellangpt.domain.usecases.ConversationUseCase
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
        single<MediaPlayerHelper> { MediaPlayerHelperImpl() }
        single<PreferencesHelper> {  PreferencesHelperImpl() }
        single<ErrorHelper> {  ErrorHelperImpl() }

        single<Context> { androidContext()}

        single { ConversationUseCase() }
        single { AuthenticationUseCase() }

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