package fr.group5.magellangpt

import android.app.Application
import android.content.Context
import androidx.room.Room
import fr.group5.magellangpt.common.helpers.ErrorHelper
import fr.group5.magellangpt.common.helpers.PreferencesHelper
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.common.helpers.implementations.ErrorHelperImpl
import fr.group5.magellangpt.common.helpers.implementations.PreferencesHelperImpl
import fr.group5.magellangpt.common.helpers.implementations.ResourcesHelperImpl
import fr.group5.magellangpt.common.helpers.NavigationHelper
import fr.group5.magellangpt.common.helpers.implementations.NavigationHelperImpl
import fr.group5.magellangpt.data.local.database.ApplicationDatabase
import fr.group5.magellangpt.data.remote.ApiClient
import fr.group5.magellangpt.data.repositories.AuthenticationRepositoryImpl
import fr.group5.magellangpt.data.repositories.ConversationRepositoryImpl
import fr.group5.magellangpt.data.repositories.ModelRepositoryImpl
import fr.group5.magellangpt.domain.repositories.AuthenticationRepository
import fr.group5.magellangpt.domain.repositories.ConversationRepository
import fr.group5.magellangpt.domain.repositories.ModelRepository
import fr.group5.magellangpt.domain.usecases.GetAvailableModelsUseCase
import fr.group5.magellangpt.domain.usecases.LoginUseCase
import fr.group5.magellangpt.domain.usecases.GetConversationUseCase
import fr.group5.magellangpt.domain.usecases.GetConversationsUseCase
import fr.group5.magellangpt.domain.usecases.GetCurrentUserUseCase
import fr.group5.magellangpt.domain.usecases.LogoutUseCase
import fr.group5.magellangpt.domain.usecases.SendMessageUseCase
import fr.group5.magellangpt.domain.usecases.UserConnectedUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.dsl.module

class ApplicationController : Application() {

    private val database: ApplicationDatabase by lazy {
        Room.databaseBuilder(this, ApplicationDatabase::class.java, "magellan-gpt")
            .fallbackToDestructiveMigration() // If migrations needed delete all data and clear schema
            .build()
    }

    private val appModule = module {
        single { ApiClient.apiService }

        single<AuthenticationRepository> { AuthenticationRepositoryImpl() }
        single<ConversationRepository> { ConversationRepositoryImpl() }
        single<ModelRepository> { ModelRepositoryImpl() }

        single<ResourcesHelper> { ResourcesHelperImpl() }
        single<PreferencesHelper> {  PreferencesHelperImpl() }
        single<ErrorHelper> { ErrorHelperImpl() }
        single<NavigationHelper> { NavigationHelperImpl() }

        single<Context> { androidContext()}

        single { GetConversationUseCase() }
        single { LoginUseCase() }
        single { LogoutUseCase() }
        single { UserConnectedUseCase() }
        single { GetCurrentUserUseCase() }
        single { SendMessageUseCase() }
        single { GetAvailableModelsUseCase() }
        single { GetConversationsUseCase() }


        single { database.messageDao() }

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