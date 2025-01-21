package `in`.windrunner.deblockdemo.domain.repository

import `in`.windrunner.deblockdemo.CustomCurrencyAmount
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject

class CalculatorRepositoryImpl @Inject constructor() : CalculatorRepository {
    override suspend fun getEthGasPrice(): Result<CustomCurrencyAmount> {
        return Result.success(
            CustomCurrencyAmount(
                0.123.toBigDecimal().setScale(8),
                CustomCurrencyAmount.CURRENCY_ETH_CODE
            )
        )
    }

    override suspend fun getEthConversionRate(fiatCurrency: String): Result<BigDecimal> {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrenciesSupported(): List<Currency> = listOf(
        Currency.getInstance("USD")
    )
}