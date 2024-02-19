package fr.group5.magellangpt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.group5.magellangpt.presentation.login.LoginScreen
import fr.group5.magellangpt.presentation.login.LoginViewModel
import fr.group5.magellangpt.ui.theme.MagellanGPTTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MagellanGPTTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel : LoginViewModel = viewModel()
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
                    LoginScreen(uiState = uiState.value, onEvent = viewModel::onEvent)
                }
            }
        }
    }
}