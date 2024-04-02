package fr.group5.magellangpt

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.microsoft.signalr.HubConnectionBuilder
import fr.group5.magellangpt.common.helpers.ErrorHelper
import fr.group5.magellangpt.common.helpers.NavigationHelper
import fr.group5.magellangpt.common.helpers.PreferencesHelper
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.common.helpers.implementations.ErrorHelperImpl
import fr.group5.magellangpt.common.helpers.implementations.NavigationHelperImpl
import fr.group5.magellangpt.common.helpers.implementations.PreferencesHelperImpl
import fr.group5.magellangpt.common.helpers.implementations.ResourcesHelperImpl
import fr.group5.magellangpt.data.local.database.ApplicationDatabase
import fr.group5.magellangpt.data.remote.ApiClient
import fr.group5.magellangpt.data.repositories.AuthenticationRepositoryImpl
import fr.group5.magellangpt.data.repositories.ConversationRepositoryImpl
import fr.group5.magellangpt.data.repositories.DocumentRepositoryImpl
import fr.group5.magellangpt.data.repositories.ModelRepositoryImpl
import fr.group5.magellangpt.data.repositories.QuestionRepositoryImpl
import fr.group5.magellangpt.data.repositories.QuotaRepositoryImpl
import fr.group5.magellangpt.domain.repositories.AuthenticationRepository
import fr.group5.magellangpt.domain.repositories.ConversationRepository
import fr.group5.magellangpt.domain.repositories.DocumentRepository
import fr.group5.magellangpt.domain.repositories.ModelRepository
import fr.group5.magellangpt.domain.repositories.QuestionRepository
import fr.group5.magellangpt.domain.repositories.QuotaRepository
import fr.group5.magellangpt.domain.usecases.AnalyseTcuUseCase
import fr.group5.magellangpt.domain.usecases.CreateConversationUseCase
import fr.group5.magellangpt.domain.usecases.GetAvailableModelsUseCase
import fr.group5.magellangpt.domain.usecases.GetConversationUseCase
import fr.group5.magellangpt.domain.usecases.GetConversationsUseCase
import fr.group5.magellangpt.domain.usecases.GetCurrentQuotaUseCase
import fr.group5.magellangpt.domain.usecases.GetCurrentUserUseCase
import fr.group5.magellangpt.domain.usecases.GetMessagesUseCase
import fr.group5.magellangpt.domain.usecases.GetTcuQuestionsUseCase
import fr.group5.magellangpt.domain.usecases.LoginUseCase
import fr.group5.magellangpt.domain.usecases.LogoutUseCase
import fr.group5.magellangpt.domain.usecases.PostMessageInConversationUseCase
import fr.group5.magellangpt.domain.usecases.UploadDocumentUseCase
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
        single<DocumentRepository> { DocumentRepositoryImpl() }
        single<QuotaRepository> { QuotaRepositoryImpl() }
        single<QuestionRepository> { QuestionRepositoryImpl() }

        single<ResourcesHelper> { ResourcesHelperImpl() }
        single<PreferencesHelper> {  PreferencesHelperImpl() }
        single<ErrorHelper> { ErrorHelperImpl() }
        single<NavigationHelper> { NavigationHelperImpl() }

        single<Context> { androidContext()}
        single { HubConnectionBuilder.create(BuildConfig.BASE_URL + "quota").build() }

        single { GetConversationUseCase() }
        single { LoginUseCase() }
        single { LogoutUseCase() }
        single { UserConnectedUseCase() }
        single { GetCurrentUserUseCase() }
        single { PostMessageInConversationUseCase() }
        single { GetAvailableModelsUseCase() }
        single { GetConversationsUseCase() }
        single { GetMessagesUseCase() }
        single { CreateConversationUseCase() }
        single { UploadDocumentUseCase() }
        single { GetCurrentQuotaUseCase() }
        single { GetTcuQuestionsUseCase() }
        single { AnalyseTcuUseCase() }

        single { database.messageDao() }
        single { database.conversationDao() }
        single { database.modelDao() }

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