package `in`.windrunner.deblockdemo.domain.repository

import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.util.Currency

interface CalculatorRepository {

    suspend fun getEthGasPrice(): Result<BigDecimal>

    suspend fun getEthConversionRate(fiatCurrency: String): Result<BigDecimal>

    suspend fun getCurrenciesSupported(): List<Currency>

    fun observeWalletEthBalance(): Flow<BigDecimal>

}