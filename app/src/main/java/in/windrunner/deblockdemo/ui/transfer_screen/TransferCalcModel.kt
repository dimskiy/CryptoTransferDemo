package `in`.windrunner.deblockdemo.ui.transfer_screen

import androidx.annotation.DrawableRes
import `in`.windrunner.deblockdemo.CustomCurrencyAmount

data class TransferCalcModel(
    val selectedAmount: CustomCurrencyAmount,
    @DrawableRes val selectedAmountIconRes: Int,
    val equivalentAmount: CustomCurrencyAmount,
    val maxAvailableAmount: CustomCurrencyAmount,
    val transferFeeAmount: CustomCurrencyAmount?,
)