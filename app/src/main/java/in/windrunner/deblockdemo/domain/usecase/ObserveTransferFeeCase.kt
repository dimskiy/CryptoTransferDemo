package `in`.windrunner.deblockdemo.domain.usecase

import `in`.windrunner.deblockdemo.asTimerFlow
import `in`.windrunner.deblockdemo.domain.repository.CalculatorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

private const val CHECK_INTERVAL_SEC = 2

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveTransferFeeCase @Inject constructor(
    private val conversionRepo: CalculatorRepository
) {
    operator fun invoke(): Flow<BigDecimal?> {
        return CHECK_INTERVAL_SEC.seconds.asTimerFlow()
            .flatMapLatest {
                val gasResult = conversionRepo.getEthGasPrice()
                flowOf(gasResult.getOrThrow())
            }
            .map { (gas, _, _) ->
                val fee = 21000.toBigDecimal() * gas / 100000000.toBigDecimal()
                fee
            }
            .flowOn(Dispatchers.IO)
    }
}