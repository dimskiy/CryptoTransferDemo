package `in`.windrunner.deblockdemo.domain.repository

import `in`.windrunner.deblockdemo.CustomCurrencyAmount
import java.util.Currency

interface CalculatorRepository {

    suspend fun getEthGasPrice(): Result<CustomCurrencyAmount>

    suspend fun getEthConversionRate(fiatCurrency: String): Result<CustomCurrencyAmount>

    suspend fun getCurrenciesSupported(): List<Currency>

}