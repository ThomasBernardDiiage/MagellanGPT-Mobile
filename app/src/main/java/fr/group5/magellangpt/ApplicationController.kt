package fr.group5.magellangpt

import android.app.Application
import android.content.Context
import fr.group5.magellangpt.common.helpers.MediaPlayerHelper
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.common.helpers.implementations.MediaPlayerHelperImpl
import fr.group5.magellangpt.common.helpers.implementations.ResourcesHelperImpl
import fr.group5.magellangpt.common.navigation.Navigator
import fr.group5.magellangpt.common.navigation.implementations.NavigatorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.dsl.module

class ApplicationController : Application() {

    private val appModule = module {
        single<Navigator> { NavigatorImpl() }

        single<ResourcesHelper> { ResourcesHelperImpl() }
        single<MediaPlayerHelper> { MediaPlayerHelperImpl() }


        single<Context> { androidContext()}
    }

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            modules(appModule)
            androidContext(this@ApplicationController)
        }
    }
}