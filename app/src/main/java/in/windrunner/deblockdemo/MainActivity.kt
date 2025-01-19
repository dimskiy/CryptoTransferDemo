package `in`.windrunner.deblockdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import `in`.windrunner.deblockdemo.ui.theme.DeblockDemoTheme
import `in`.windrunner.deblockdemo.ui.transfer_screen.TransferScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            DeblockDemoTheme {
                Scaffold(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .fillMaxSize()
                ) { innerPadding ->
                    TransferScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}