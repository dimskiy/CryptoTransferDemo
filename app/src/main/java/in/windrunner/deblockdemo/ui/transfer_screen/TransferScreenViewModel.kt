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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransferScreenViewModel @Inject constructor(
    private val observeTransferFeeCase: ObserveTransferFeeCase,
    private val getConversionRateCase: GetConversionRateCase,
    private val mediaProvider: MediaProvider,
    currencySelectionState: CurrencySelectionState,
    observeEthWalletBalance: ObserveEthWalletBalance,
) : ViewModel() {

    private val amountSelected = MutableStateFlow(0.toBigDecimal())
    private val isFiatToEthDirection = MutableStateFlow(true)

    val transferCalcModel: StateFlow<TransferCalcModel?> = combine(
        amountSelected,
        currencySelectionState.currencySelected,
        isFiatToEthDirection,
        observeEthWalletBalance()
    ) { amount, currency, fiatToEthDirection, walletBalance ->
        currency ?: return@combine null

        TransferCalcModelDraft(
            enteredAmount = amount,
            selectedCurrency = currency,
            maxAvailableAmount = walletBalance,
            isFiatToEthTransfer = fiatToEthDirection
        )
    }.flatMapLatest { transferModelDraft ->
        transferModelDraft?.selectedCurrency ?: return@flatMapLatest emptyFlow()

        getRateWithFee(transferModelDraft.selectedCurrency)
            .map { (rate, fee) ->
                mapTransferCalcModel(transferModelDraft, rate, fee)
            }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private fun mapTransferCalcModel(
        draft: TransferCalcModelDraft,
        convertRate: BigDecimal,
        transferFee: BigDecimal?
    ): TransferCalcModel {
        val enteredAmount = if (draft.isFiatToEthTransfer) {
            draft.enteredAmount.ofFiatCurrency(draft.selectedCurrency)
        } else draft.enteredAmount.ofEtherium()

        val equivalentAmount = if (draft.isFiatToEthTransfer) {
            (draft.enteredAmount * convertRate).ofEtherium()
        } else (draft.enteredAmount / convertRate).ofFiatCurrency(draft.selectedCurrency)

        return TransferCalcModel(
            enteredAmount = enteredAmount,
            equivalentAmount = equivalentAmount,
            maxAvailableAmount = draft.maxAvailableAmount.ofEtherium(),
            transferFeeAmount = transferFee?.ofEtherium(),
            selectedCurrency = draft.selectedCurrency,
            selectedCurrencyIconRes = mediaProvider.getFlagResource(draft.selectedCurrency.currencyCode),
            isTransferAllowed = isTransferAllowed(
                walletBalance = draft.maxAvailableAmount,
                ethAmount = if (draft.isFiatToEthTransfer) equivalentAmount.number else enteredAmount.number,
                transferFee = transferFee
            )
        )
    }

    private fun getRateWithFee(convertCurrency: Currency): Flow<Pair<BigDecimal, BigDecimal>> =
        observeTransferFeeCase().mapNotNull { fee ->
            val rate = getConversionRateCase(convertCurrency.currencyCode)
            if (rate != null && fee != null) {
                rate to fee
            } else null
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

    data class TransferCalcModelDraft(
        val enteredAmount: BigDecimal,
        val selectedCurrency: Currency,
        val maxAvailableAmount: BigDecimal,
        val isFiatToEthTransfer: Boolean
    )
}