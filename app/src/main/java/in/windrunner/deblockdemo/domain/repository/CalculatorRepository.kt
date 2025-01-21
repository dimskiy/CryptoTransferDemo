package `in`.windrunner.deblockdemo.domain.repository

import `in`.windrunner.deblockdemo.CustomCurrencyAmount
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.util.Currency

interface CalculatorRepository {

    suspend fun getEthGasPrice(): Result<CustomCurrencyAmount>

    suspend fun getEthConversionRate(fiatCurrency: String): Result<BigDecimal>

    suspend fun getCurrenciesSupported(): List<Currency>

    fun observeWalletEthBalance(): Flow<BigDecimal>

}