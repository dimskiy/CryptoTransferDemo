package `in`.windrunner.deblockdemo

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