package `in`.windrunner.deblockdemo.domain.usecase

import `in`.windrunner.deblockdemo.domain.repository.CalculatorRepository
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

class GetConversionRateCase @Inject constructor(
    private val conversionRepo: CalculatorRepository
) {
    suspend operator fun invoke(fiatCurrencyCode: String): BigDecimal? {
        val result = conversionRepo.getEthConversionRate(fiatCurrencyCode)
        if (result.isFailure) Timber.d(result.exceptionOrNull())

        return result.getOrNull()
    }
}