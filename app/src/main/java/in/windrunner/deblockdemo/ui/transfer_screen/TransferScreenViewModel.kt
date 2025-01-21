package `in`.windrunner.deblockdemo.ui.transfer_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.windrunner.deblockdemo.CURRENCY_ETH_CODE
import `in`.windrunner.deblockdemo.CustomCurrencyAmount
import `in`.windrunner.deblockdemo.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Currency
import javax.inject.Inject

@HiltViewModel
class TransferScreenViewModel @Inject constructor(): ViewModel() {

    private val _transferCalcModel = MutableStateFlow<TransferCalcModel?>(null)
    val transferCalcModel: StateFlow<TransferCalcModel?> = _transferCalcModel

    init {
        _transferCalcModel.update {
            TransferCalcModel(
                selectedAmount = CustomCurrencyAmount(1998.toBigDecimal(), Currency.getInstance("USD")),
                selectedAmountIconRes = R.drawable.ic_us,
                equivalentAmount = CustomCurrencyAmount(1.2.toBigDecimal(), CURRENCY_ETH_CODE),
                maxAvailableAmount = CustomCurrencyAmount(3450.toBigDecimal(), CURRENCY_ETH_CODE),
                transferFeeAmount = CustomCurrencyAmount(0.0013.toBigDecimal(), CURRENCY_ETH_CODE),
            )
        }
    }

    fun onSwapCurrencyClick() {

    }

    fun onTransferClick() {

    }
}