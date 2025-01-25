package `in`.windrunner.cryptotransferdemo.ui.currency_selector

import androidx.annotation.DrawableRes
import `in`.windrunner.cryptotransferdemo.CustomCurrencyAmount
import java.util.Currency

data class SelectableCurrencyItem(
    val currency: Currency,
    @DrawableRes val currencyFlagRes: Int,
    val ethAmount: CustomCurrencyAmount,
    val baseFiatCurrencyAmount: CustomCurrencyAmount,
    val isSelected: Boolean = false
)