package `in`.windrunner.cryptotransferdemo.ui.transfer_screen

import androidx.annotation.DrawableRes
import `in`.windrunner.cryptotransferdemo.CustomCurrencyAmount
import java.util.Currency

data class TransferCalcModel(
    val baseAmount: CustomCurrencyAmount,
    val selectedCurrency: Currency,
    @DrawableRes val selectedCurrencyIconRes: Int?,
    val equivalentAmount: CustomCurrencyAmount,
    val maxAvailableAmount: CustomCurrencyAmount,
    val isTransferAllowed: Boolean
)