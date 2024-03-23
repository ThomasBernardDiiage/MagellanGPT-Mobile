package fr.group5.magellangpt.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.helpers.ErrorHelper
import fr.group5.magellangpt.common.helpers.NavigationHelper
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.presentation.knowledgebase.KnowledgeBaseScreen
import fr.group5.magellangpt.presentation.knowledgebase.KnowledgeBaseViewModel
import fr.group5.magellangpt.presentation.login.LoginScreen
import fr.group5.magellangpt.presentation.login.LoginViewModel
import fr.group5.magellangpt.presentation.main.MainScreen
import fr.group5.magellangpt.presentation.main.MainViewModel
import fr.group5.magellangpt.presentation.settings.SettingsScreen
import fr.group5.magellangpt.presentation.settings.SettingsViewModel
import fr.group5.magellangpt.presentation.theme.MagellanGPTTheme
import fr.thomasbernard03.composents.navigationbars.NavigationBar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.java.KoinJavaComponent.get

class MainActivity(
    private val navigationHelper : NavigationHelper = get(NavigationHelper::class.java),
    private val resourcesHelper: ResourcesHelper = get(ResourcesHelper::class.java),
    private val errorHelper: ErrorHelper = get(ErrorHelper::class.java),
) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            MagellanGPTTheme {

                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }


                val showNavigationBar = navController
                    .currentBackStackEntryAsState().value?.destination?.route == NavigationHelper.Destination.Settings.route ||
                        navController
                            .currentBackStackEntryAsState().value?.destination?.route == NavigationHelper.Destination.KnowledgeBase.route

                var title by remember { mutableStateOf("") }
                var subtitle by remember { mutableStateOf("") }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    topBar = {
                        AnimatedVisibility(visible = showNavigationBar){
                            NavigationBar(
                                title = title,
                                subtitle = subtitle,
                                showBackButton = true,
                                onBack = { navigationHelper.goBack() },
                                actions = { Box(modifier = Modifier.size(44.dp)) }
                            )
                        }
                    }
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                    ) {
                        LaunchedEffect(Unit) {
                            navigationHelper.sharedFlow.onEach {
                                when(it){
                                    is NavigationHelper.NavigationEvent.NavigateTo -> {
                                        navController.navigate(it.destination.route) {
                                            // avoiding multiple copies on the top of the back stack
                                            launchSingleTop = true

                                            if(!it.popupTo.isNullOrEmpty())
                                                popUpTo(it.popupTo){
                                                    inclusive = it.inclusive
                                                }
                                        }
                                    }
                                    is NavigationHelper.NavigationEvent.GoBack -> {
                                        navController.navigateUp()
                                    }
                                }
                            }.launchIn(this)

                            errorHelper.sharedFlow.onEach {
                                if (snackbarHostState.currentSnackbarData != null)
                                    snackbarHostState.currentSnackbarData?.dismiss()

                                snackbarHostState.showSnackbar(it.message, withDismissAction = true)
                            }.launchIn(this)
                        }

                        NavHost(navController = navController, startDestination = NavigationHelper.Destination.Login.route){
                            composable(NavigationHelper.Destination.Login.route){
                                val viewModel : LoginViewModel = viewModel()
                                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                                LoginScreen(uiState = uiState, onEvent = viewModel::onEvent)
                            }
                            composable(NavigationHelper.Destination.Main.route){
                                val viewModel : MainViewModel = viewModel()
                                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                                MainScreen(uiState = uiState, onEvent = viewModel::onEvent)
                            }
                            composable(NavigationHelper.Destination.Settings.route){
                                title = resourcesHelper.getString(R.string.settings)
                                subtitle = ""
                                val viewModel : SettingsViewModel = viewModel()
                                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                                SettingsScreen(uiState = uiState, onEvent = viewModel::onEvent)
                            }
                            composable(NavigationHelper.Destination.KnowledgeBase.route){
                                title = resourcesHelper.getString(R.string.settings)
                                subtitle = resourcesHelper.getString(R.string.knowledge_base)
                                val viewModel : KnowledgeBaseViewModel = viewModel()
                                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                                KnowledgeBaseScreen(uiState, viewModel::onEvent)
                            }
                        }
                    }
                }
            }
        }
    }
}