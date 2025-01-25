package `in`.windrunner.cryptotransferdemo.domain.usecase

import `in`.windrunner.cryptotransferdemo.domain.repository.CalculatorRepository
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import javax.inject.Inject

class ObserveEthWalletBalance @Inject constructor(
    private val calculatorRepository: CalculatorRepository
) {
    
    operator fun invoke(): Flow<BigDecimal> = calculatorRepository.observeWalletEthBalance()
}