package `in`.windrunner.deblockdemo.domain.usecase

import `in`.windrunner.deblockdemo.domain.repository.CalculatorRepository
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import javax.inject.Inject

class ObserveEthWalletBalance @Inject constructor(
    private val calculatorRepository: CalculatorRepository
) {
    
    operator fun invoke(): Flow<BigDecimal> = calculatorRepository.observeWalletEthBalance()
}