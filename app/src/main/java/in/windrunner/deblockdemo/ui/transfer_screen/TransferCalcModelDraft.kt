package `in`.windrunner.deblockdemo.ui.transfer_screen

import java.math.BigDecimal
import java.util.Currency

data class TransferCalcModelDraft(
    val enteredAmount: BigDecimal,
    val selectedCurrency: Currency,
    val maxAvailableAmount: BigDecimal,
    val isFiatToEthTransfer: Boolean,
    val conversionRate: BigDecimal
)