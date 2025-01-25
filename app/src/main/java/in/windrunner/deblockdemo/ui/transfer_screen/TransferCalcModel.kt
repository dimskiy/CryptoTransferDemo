package `in`.windrunner.deblockdemo.ui.transfer_screen

import androidx.annotation.DrawableRes
import `in`.windrunner.deblockdemo.CustomCurrencyAmount
import java.util.Currency

data class TransferCalcModel(
    val baseAmount: CustomCurrencyAmount,
    val selectedCurrency: Currency,
    @DrawableRes val selectedCurrencyIconRes: Int?,
    val equivalentAmount: CustomCurrencyAmount,
    val maxAvailableAmount: CustomCurrencyAmount,
)