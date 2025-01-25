package `in`.windrunner.cryptotransferdemo.domain.usecase

import `in`.windrunner.cryptotransferdemo.domain.model.ExchangeableCurrencyModel
import `in`.windrunner.cryptotransferdemo.domain.repository.CalculatorRepository
import `in`.windrunner.cryptotransferdemo.mapResult
import `in`.windrunner.cryptotransferdemo.ofEtherium
import `in`.windrunner.cryptotransferdemo.ofFiatCurrency
import `in`.windrunner.cryptotransferdemo.wrapError
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency
import javax.inject.Inject

class GetCurrenciesListCase @Inject constructor(
    private val calcRepository: CalculatorRepository,
) {

    suspend operator fun invoke(): Result<List<ExchangeableCurrencyModel>> {
        val baseRateResult = calcRepository.getEthConversionRate(baseExchangeCurrency.currencyCode)

        val exchangeableCurrencies = baseRateResult.getOrNull()?.let { baseRate ->
            calcRepository.getCurrenciesSupported().mapNotNull { currency ->
                val itemResult = getCurrencyItem(currency, baseRate)
                if (itemResult.isFailure) return itemResult.wrapError()
                itemResult.getOrNull()
            }
        }

        return Result.success(exchangeableCurrencies ?: emptyList())
    }

    private suspend fun getCurrencyItem(
        currency: Currency,
        baseRate: BigDecimal
    ): Result<ExchangeableCurrencyModel> =
        calcRepository.getEthConversionRate(currency.currencyCode).mapResult { ethRate ->
            ExchangeableCurrencyModel(
                currency = currency,
                ethAmount = ethRate.ofEtherium(),
                baseFiatCurrencyAmount = ethRate.divide(baseRate, 10, RoundingMode.HALF_UP)
                    .ofFiatCurrency(baseExchangeCurrency)
            )
        }

    companion object {
        val baseExchangeCurrency = Currency.getInstance("USD")
    }
}