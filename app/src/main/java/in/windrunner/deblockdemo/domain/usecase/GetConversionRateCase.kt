package `in`.windrunner.deblockdemo.domain.usecase

import `in`.windrunner.deblockdemo.domain.DomainException
import `in`.windrunner.deblockdemo.domain.repository.CalculatorRepository
import `in`.windrunner.deblockdemo.mapResult
import java.math.BigDecimal
import javax.inject.Inject

class GetConversionRateCase @Inject constructor(
    private val conversionRepo: CalculatorRepository
) {
    suspend operator fun invoke(fiatCurrencyCode: String): Result<BigDecimal> {
        val result = conversionRepo.getEthConversionRate(fiatCurrencyCode)

        return if (result.isSuccess) {
            result.mapResult { it }
        } else {
            Result.failure(
                DomainException.NoCurrencyRate(result.exceptionOrNull())
            )
        }
    }
}