package `in`.windrunner.deblockdemo.ui.transfer_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.windrunner.deblockdemo.R
import `in`.windrunner.deblockdemo.ui.CURRENCY_ETH_CODE
import `in`.windrunner.deblockdemo.ui.CustomCurrencyAmount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Currency
import javax.inject.Inject

@HiltViewModel
class TransferScreenViewModel @Inject constructor(): ViewModel() {

    private val _transferModel = MutableStateFlow<TransferModel?>(null)
    val transferModel: StateFlow<TransferModel?> = _transferModel

    init {
        _transferModel.update {
            TransferModel(
                selectedAmount = CustomCurrencyAmount(1998.0, Currency.getInstance("USD")),
                selectedAmountIconRes = R.drawable.ic_us,
                equivalentAmount = CustomCurrencyAmount(1.2, CURRENCY_ETH_CODE),
                maxAvailableAmount = CustomCurrencyAmount(3450.0, CURRENCY_ETH_CODE),
                transferFeeAmount = CustomCurrencyAmount(0.0013, CURRENCY_ETH_CODE),
            )
        }
    }

    fun onSwapCurrencyClick() {

    }

    fun onTransferClick() {

    }
}