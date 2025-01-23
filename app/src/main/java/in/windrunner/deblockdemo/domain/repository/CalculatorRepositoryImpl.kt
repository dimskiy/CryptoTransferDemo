package `in`.windrunner.deblockdemo.domain.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.windrunner.deblockdemo.CustomCurrencyAmount
import `in`.windrunner.deblockdemo.R
import `in`.windrunner.deblockdemo.mapResult
import `in`.windrunner.deblockdemo.ofEtherium
import `in`.windrunner.deblockdemo.platform.api.EtherscanApi
import `in`.windrunner.deblockdemo.platform.api.GeckoApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject

class CalculatorRepositoryImpl @Inject constructor(
    private val conversionApi: GeckoApi,
    private val gasPriceApi: EtherscanApi,
    @ApplicationContext private val context: Context,
) : CalculatorRepository {

    override suspend fun getEthGasPrice(): Result<CustomCurrencyAmount> {
        val requestResult = gasPriceApi.getEthereumGasPrice()

        return if (requestResult.isSuccess) {
            requestResult.getOrNull()?.result?.gasPrice
                ?.ofEtherium()
                ?.let { Result.success(it) }
                ?: Result.failure(IllegalStateException(context.getString(R.string.no_gas_price_provided)))
        } else {
            Result.failure(IllegalStateException(requestResult.exceptionOrNull()))
        }
    }

    override suspend fun getEthConversionRate(fiatCurrency: String): Result<BigDecimal> {
        val requestResult = conversionApi.getConversionRate(currencyCode = fiatCurrency)

        return requestResult.mapResult { it.conversionRate.rate }
    }

    // Supported currencies list is static for demo purposes
    override suspend fun getCurrenciesSupported(): List<Currency> = listOf(
        Currency.getInstance("USD"),
        Currency.getInstance("GBP"),
        Currency.getInstance("EUR"),
    )

    // Balance is static for demo purposes
    override fun observeWalletEthBalance(): Flow<BigDecimal> = flowOf(3000.toBigDecimal())
}