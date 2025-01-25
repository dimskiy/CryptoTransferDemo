package `in`.windrunner.cryptotransferdemo.domain.usecase

import `in`.windrunner.cryptotransferdemo.domain.model.DomainException
import `in`.windrunner.cryptotransferdemo.domain.repository.CalculatorRepository
import java.math.BigDecimal
import javax.inject.Inject

class GetConversionRateCase @Inject constructor(
    private val conversionRepo: CalculatorRepository
) {
    suspend operator fun invoke(fiatCurrencyCode: String): Result<BigDecimal> {
        val result = conversionRepo.getEthConversionRate(fiatCurrencyCode)

        return if (result.isSuccess) {
            result
        } else {
            Result.failure(
                DomainException.NoCurrencyRate(result.exceptionOrNull())
            )
        }
    }
}