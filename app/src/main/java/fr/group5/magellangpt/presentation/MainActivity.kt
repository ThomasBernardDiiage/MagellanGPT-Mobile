package fr.group5.magellangpt.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.group5.magellangpt.common.helpers.ErrorHelper
import fr.group5.magellangpt.common.navigation.Navigator
import fr.group5.magellangpt.presentation.login.LoginScreen
import fr.group5.magellangpt.presentation.login.LoginViewModel
import fr.group5.magellangpt.presentation.main.MainScreen
import fr.group5.magellangpt.presentation.main.MainViewModel
import fr.group5.magellangpt.presentation.theme.MagellanGPTTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.java.KoinJavaComponent.get

class MainActivity(
    private val navigator : Navigator = get(Navigator::class.java),
    private val errorHelper: ErrorHelper = get(ErrorHelper::class.java),
) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MagellanGPTTheme {

                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                    ) {
                        LaunchedEffect(Unit) {
                            navigator.sharedFlow.onEach {
                                when(it){
                                    is Navigator.NavigationEvent.NavigateTo -> {
                                        navController.navigate(it.destination.route) {
                                            // avoiding multiple copies on the top of the back stack
                                            launchSingleTop = true

                                            if(!it.popupTo.isNullOrEmpty())
                                                popUpTo(it.popupTo){
                                                    inclusive = it.inclusive
                                                }
                                        }
                                    }
                                    is Navigator.NavigationEvent.GoBack -> {
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

                        NavHost(navController = navController, startDestination = Navigator.Destination.Login.route){
                            composable("login"){
                                val viewModel : LoginViewModel = viewModel()
                                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                                LoginScreen(uiState = uiState, onEvent = viewModel::onEvent)
                            }
                            composable(Navigator.Destination.Main.route){
                                val viewModel : MainViewModel = viewModel()
                                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                                MainScreen(uiState = uiState, onEvent = viewModel::onEvent)
                            }
                        }
                    }
                }

            }
        }
    }
}