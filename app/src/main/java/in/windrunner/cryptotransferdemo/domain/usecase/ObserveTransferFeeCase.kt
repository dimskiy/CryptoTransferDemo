package `in`.windrunner.cryptotransferdemo.domain.usecase

import `in`.windrunner.cryptotransferdemo.asTimerFlow
import `in`.windrunner.cryptotransferdemo.domain.model.DomainException
import `in`.windrunner.cryptotransferdemo.domain.repository.CalculatorRepository
import `in`.windrunner.cryptotransferdemo.mapResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

private const val CHECK_INTERVAL_SEC = 6

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveTransferFeeCase @Inject constructor(
    private val conversionRepo: CalculatorRepository
) {
    operator fun invoke(): Flow<Result<BigDecimal>> {
        return CHECK_INTERVAL_SEC.seconds
            .asTimerFlow()
            .flatMapLatest {
                val gasResult = conversionRepo.getEthGasPrice()

                if (gasResult.isSuccess) flowOf(
                    gasResult.mapResult {
                        21000.toBigDecimal() * it / 100000000.toBigDecimal()
                    }
                ) else flowOf(
                    Result.failure(
                        DomainException.NoTransferFee(gasResult.exceptionOrNull())
                    )
                )
            }
    }
}