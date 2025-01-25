package `in`.windrunner.cryptotransferdemo.domain.model

import `in`.windrunner.cryptotransferdemo.CustomCurrencyAmount
import java.util.Currency

data class ExchangeableCurrencyModel(
    val currency: Currency,
    val ethAmount: CustomCurrencyAmount,
    val baseFiatCurrencyAmount: CustomCurrencyAmount,
)