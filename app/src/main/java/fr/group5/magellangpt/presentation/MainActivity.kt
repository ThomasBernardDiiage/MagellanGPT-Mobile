package fr.group5.magellangpt.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    private val navigator : Navigator = get(Navigator::class.java)
) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MagellanGPTTheme {
                Scaffold {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()

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
                        }

                        NavHost(navController = navController, startDestination = Navigator.Destination.Login.route){
                            composable("login"){
                                val viewModel : LoginViewModel = viewModel()
                                val uiState = viewModel.uiState.collectAsStateWithLifecycle()
                                LoginScreen(uiState = uiState.value, onEvent = viewModel::onEvent)
                            }
                            composable(Navigator.Destination.Main.route){
                                val viewModel : MainViewModel = viewModel()
                                val uiState = viewModel.uiState.collectAsStateWithLifecycle()
                                MainScreen(uiState = uiState.value, onEvent = viewModel::onEvent)
                            }
                        }
                    }
                }

            }
        }
    }
}