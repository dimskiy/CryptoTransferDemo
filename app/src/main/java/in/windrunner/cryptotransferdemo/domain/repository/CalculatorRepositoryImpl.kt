package `in`.windrunner.cryptotransferdemo.domain.repository

import `in`.windrunner.cryptotransferdemo.mapResult
import `in`.windrunner.cryptotransferdemo.platform.api.EtherscanApi
import `in`.windrunner.cryptotransferdemo.platform.api.GeckoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.Currency
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class CalculatorRepositoryImpl @Inject constructor(
    private val conversionApi: GeckoApi,
    private val gasPriceApi: EtherscanApi
) : CalculatorRepository {

    // Added due to API limitations - just a few requests and then "too much requests" error
    private val ratesCache = ConcurrentHashMap<String, BigDecimal>()

    override suspend fun getEthGasPrice(): Result<BigDecimal> =
        withContext(Dispatchers.IO) {
            val requestResult = kotlin.runCatching {
                gasPriceApi.getEthereumGasPrice()
            }

            requestResult.getOrNull()?.result?.gasPrice?.let {
                Result.success(it)
            } ?: Result.failure(
                IllegalStateException(requestResult.exceptionOrNull())
            )
        }

    override suspend fun getEthConversionRate(fiatCurrency: String): Result<BigDecimal> =
        ratesCache[fiatCurrency]?.let {
            Result.success(it)
        } ?: withContext(Dispatchers.IO) {
            val requestResult = runCatching {
                conversionApi.getConversionRate(currencyCode = fiatCurrency)
            }

            requestResult.mapResult {
                val rate = it.conversionRate.rate
                ratesCache[fiatCurrency] = rate
                rate
            }
        }

    // Supported currencies list is static for demo purposes
    override suspend fun getCurrenciesSupported(): List<Currency> = listOf(
        Currency.getInstance("USD"),
        Currency.getInstance("GBP"),
        Currency.getInstance("EUR"),
    )

    // Balance is static for demo purposes
    override fun observeWalletEthBalance(): Flow<BigDecimal> = flow {
        emit(3000.toBigDecimal())
        suspendCancellableCoroutine<Unit> {}
    }
}