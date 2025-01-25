package `in`.windrunner.cryptotransferdemo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import `in`.windrunner.cryptotransferdemo.domain.model.DomainException
import `in`.windrunner.cryptotransferdemo.ui.theme.CryptoTransferDemoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CryptoTransferDemoTheme {
                val snackHostState = remember { SnackbarHostState() }
                var snackbarMessage by remember { mutableStateOf<String?>(null) }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackHostState,
                            modifier = Modifier
                                .padding(bottom = 80.dp)
                                .imePadding()
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .fillMaxSize()
                ) { innerPadding ->
                    Navigation(
                        startDestination = NavScreen.Transfer,
                        onError = { snackbarMessage = getErrorMessage(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                LaunchedEffect(snackbarMessage) {
                    snackbarMessage?.let {
                        snackHostState.showSnackbar(it)
                        snackbarMessage = null
                    }
                }
            }
        }
    }

    private fun getErrorMessage(error: Throwable): String? =
        (error as? DomainException)?.getTextResource()
            ?.let(this::getString)
            ?: error.message
}