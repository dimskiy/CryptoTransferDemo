package `in`.windrunner.deblockdemo.ui.transfer_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.windrunner.deblockdemo.domain.CurrencySelectionState
import `in`.windrunner.deblockdemo.domain.usecase.GetConversionRateCase
import `in`.windrunner.deblockdemo.domain.usecase.ObserveEthWalletBalance
import `in`.windrunner.deblockdemo.domain.usecase.ObserveTransferFeeCase
import `in`.windrunner.deblockdemo.ofEtherium
import `in`.windrunner.deblockdemo.ofFiatCurrency
import `in`.windrunner.deblockdemo.ui.MediaProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransferScreenViewModel @Inject constructor(
    private val observeTransferFeeCase: ObserveTransferFeeCase,
    private val currencySelectionState: CurrencySelectionState,
    private val getConversionRateCase: GetConversionRateCase,
    private val observeEthWalletBalance: ObserveEthWalletBalance,
    private val mediaProvider: MediaProvider
) : ViewModel() {

    private val amountSelected = MutableStateFlow(0.toBigDecimal())
    private val isFiatToEthDirection = MutableStateFlow(true)

    val transferCalcModel: StateFlow<TransferCalcModel?> = currencySelectionState.currencySelected
        .flatMapLatest { currency ->
            val convertRate = currency?.currencyCode?.let { getConversionRateCase(it) }
            convertRate ?: return@flatMapLatest emptyFlow<TransferCalcModel>()

            getTransferCalcModel(currency, convertRate)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private fun getTransferCalcModel(
        currency: Currency,
        convertRate: BigDecimal
    ): Flow<TransferCalcModel> = combine(
        amountSelected,
        isFiatToEthDirection,
        observeTransferFeeCase(),
        observeEthWalletBalance()
    ) { amount, fiatToEthDirection, transferFee, walletBalance ->
        val enteredAmount = if (fiatToEthDirection) {
            amount.ofFiatCurrency(currency)
        } else amount.ofEtherium()

        val equivalentAmount = if (fiatToEthDirection) {
            (amount * convertRate).ofEtherium()
        } else (amount / convertRate).ofFiatCurrency(currency)

        TransferCalcModel(
            enteredAmount = enteredAmount,
            equivalentAmount = equivalentAmount,
            maxAvailableAmount = walletBalance.ofEtherium(),
            transferFeeAmount = transferFee?.ofEtherium(),
            selectedCurrency = currency,
            selectedCurrencyIconRes = mediaProvider.getFlagResource(currency.currencyCode),
            isTransferAllowed = isTransferAllowed(
                walletBalance = walletBalance,
                ethAmount = if (fiatToEthDirection) equivalentAmount.number else enteredAmount.number,
                transferFee = transferFee
            )
        )
    }

    private fun isTransferAllowed(
        walletBalance: BigDecimal,
        ethAmount: BigDecimal,
        transferFee: BigDecimal?
    ): Boolean {
        transferFee ?: return false
        return (walletBalance - ethAmount - transferFee) >= 0.0.toBigDecimal()
    }

    fun onSwapCurrencyClick() {
        val newAmount = transferCalcModel.value?.equivalentAmount
        isFiatToEthDirection.update { prevState -> !prevState }
        newAmount?.let { newValue ->
            amountSelected.update { newValue.number.stripTrailingZeros() }
        }
    }

    fun onTransferClick() {

    }

    fun onAmountChange(amount: BigDecimal?) {
        amountSelected.update { amount ?: 0.toBigDecimal() }
    }
}