package `in`.windrunner.deblockdemo.ui.transfer_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.windrunner.deblockdemo.CustomCurrencyAmount
import `in`.windrunner.deblockdemo.CustomCurrencyAmount.Companion.CURRENCY_ETH_CODE
import `in`.windrunner.deblockdemo.R
import `in`.windrunner.deblockdemo.domain.CurrencySelectionState
import `in`.windrunner.deblockdemo.domain.usecase.ObserveTransferFeeCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransferScreenViewModel @Inject constructor(
    private val observeTransferFeeCase: ObserveTransferFeeCase,
    private val currencySelectionState: CurrencySelectionState,
) : ViewModel() {

    val transferCalcModel: StateFlow<TransferCalcModel?> = currencySelectionState.currencySelected
        .flatMapLatest { currency ->
            observeTransferFeeCase().map { fee ->
                if (fee != null && currency != null) {
                    TransferCalcModel(
                        selectedAmount = CustomCurrencyAmount(1998.toBigDecimal(), currency),
                        selectedAmountIconRes = R.drawable.ic_us,
                        equivalentAmount = CustomCurrencyAmount(
                            1.2.toBigDecimal(),
                            CURRENCY_ETH_CODE
                        ),
                        maxAvailableAmount = CustomCurrencyAmount(
                            3450.toBigDecimal(),
                            CURRENCY_ETH_CODE
                        ),
                        transferFeeAmount = CustomCurrencyAmount(fee, currency.currencyCode),
                    )
                } else null
            }
        }
        .catch { Timber.d(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun onSwapCurrencyClick() {

    }

    fun onTransferClick() {

    }
}