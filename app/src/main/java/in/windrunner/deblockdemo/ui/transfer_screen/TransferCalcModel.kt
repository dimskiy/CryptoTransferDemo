package `in`.windrunner.deblockdemo.ui.transfer_screen

import androidx.annotation.DrawableRes
import `in`.windrunner.deblockdemo.CustomCurrencyAmount
import java.util.Currency

data class TransferCalcModel(
    val enteredAmount: CustomCurrencyAmount,
    val selectedCurrency: Currency,
    @DrawableRes val selectedCurrencyIconRes: Int?,
    val equivalentAmount: CustomCurrencyAmount,
    val maxAvailableAmount: CustomCurrencyAmount,
    val transferFeeAmount: CustomCurrencyAmount?,
    val isTransferAllowed: Boolean
)