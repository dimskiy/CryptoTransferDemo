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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import kotlin.time.Duration

fun Duration.asTimerFlow(): Flow<Unit> = flow {
    while (true) {
        emit(Unit)
        delay(this@asTimerFlow)
    }
}.cancellable()

fun String.isDecimalCompatible(): Boolean = matches(Regex("^\\d*\\.?\\d*\$"))

fun String.swapCommaWithDot() = replace(',', '.')

fun <T> Result<T>.getError(): Throwable =
    exceptionOrNull() ?: IllegalStateException("No error found")

fun <NEW, OLD> Result<OLD>.wrapError(): Result<NEW> = Result.failure(getError())

fun <NEW, OLD> Result<OLD>.mapResult(mapper: (OLD) -> NEW): Result<NEW> {
    return takeIf(Result<*>::isSuccess)
        ?.getOrNull()
        ?.let { Result.success(mapper(it)) }
        ?: wrapError()
}

fun <NEW, OLD> Result<OLD>.flatMapLatestResult(
    mapper: (OLD) -> Flow<Result<NEW>>
): Flow<Result<NEW>> = getOrNull()
    ?.let { mapper(it) }
    ?: flowOf(this.wrapError())

fun <T> Flow<T>.debug(tag: String): Flow<T> = onEach {
    Timber.tag("FLOW-DEBUG").d("($tag) emitted: ${it.toString()}")
}.onCompletion {
    Timber.tag("FLOW-DEBUG").d("($tag) COMPLETED")
}

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