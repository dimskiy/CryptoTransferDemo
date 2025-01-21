package `in`.windrunner.deblockdemo.ui.transfer_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.windrunner.deblockdemo.CustomCurrencyAmount
import `in`.windrunner.deblockdemo.CustomCurrencyAmount.Companion.CURRENCY_ETH_CODE
import `in`.windrunner.deblockdemo.R
import `in`.windrunner.deblockdemo.domain.CurrencySelectionState
import `in`.windrunner.deblockdemo.domain.usecase.GetConversionRateCase
import `in`.windrunner.deblockdemo.domain.usecase.ObserveTransferFeeCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransferScreenViewModel @Inject constructor(
    private val observeTransferFeeCase: ObserveTransferFeeCase,
    private val currencySelectionState: CurrencySelectionState,
    private val getConversionRateCase: GetConversionRateCase,
) : ViewModel() {

    private val fiatAmount = MutableStateFlow(0.toBigDecimal())
    private val ethAmount = MutableStateFlow(0.toBigDecimal())
    private val isFiatToEthDirection = MutableStateFlow(true)

    val transferCalcModel: StateFlow<TransferCalcModel?> = combine(
        currencySelectionState.currencySelected,
        observeTransferFeeCase(),
    ) { currencySelected, transferFee ->
        currencySelected ?: return@combine null

        TransferCalcModel(
            selectedAmount = getAmountUpper(currencySelected),
            selectedAmountIconRes = R.drawable.ic_us, //TODO implement this
            equivalentAmount = getAmountBottom(currencySelected),
            maxAvailableAmount = CustomCurrencyAmount(
                3000.toBigDecimal(),
                CURRENCY_ETH_CODE
            ), // TODO implement
            transferFeeAmount = transferFee?.let { CustomCurrencyAmount(it, CURRENCY_ETH_CODE) }
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private fun getAmountUpper(currencySelected: Currency): CustomCurrencyAmount =
        if (isFiatToEthDirection.value) {
            CustomCurrencyAmount(fiatAmount.value, currencySelected)
        } else {
            CustomCurrencyAmount(ethAmount.value, CURRENCY_ETH_CODE)
        }

    private fun getAmountBottom(currencySelected: Currency): CustomCurrencyAmount =
        if (isFiatToEthDirection.value) {
            CustomCurrencyAmount(ethAmount.value, CURRENCY_ETH_CODE)
        } else {
            CustomCurrencyAmount(fiatAmount.value, currencySelected)
        }

    private suspend fun getEthFor(amount: CustomCurrencyAmount?): BigDecimal? {
        if (amount == null) return null

        val rate = getConversionRateCase(amount.currencyCode)
        return rate?.times(amount.number)
    }

    fun onSwapCurrencyClick() {

    }

    fun onTransferClick() {

    }
}