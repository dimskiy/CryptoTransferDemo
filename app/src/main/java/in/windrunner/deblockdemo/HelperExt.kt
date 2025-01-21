package `in`.windrunner.deblockdemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

fun Duration.asTimerFlow(): Flow<Unit> = flow {
    while (true) {
        emit(Unit)
        delay(this@asTimerFlow)
    }
}.cancellable()

fun String.isDecimalCompatible(): Boolean = matches(Regex("^\\d*\\.?\\d*\$"))

fun String.swapCommaWithDot() = replace(',', '.')

@Composable
fun Modifier.forceKeyboardShow(): Modifier {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    return focusRequester(focusRequester)
}