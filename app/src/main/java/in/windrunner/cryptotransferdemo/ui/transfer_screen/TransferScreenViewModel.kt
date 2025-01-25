package `in`.windrunner.cryptotransferdemo.ui.transfer_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.windrunner.cryptotransferdemo.CustomCurrencyAmount
import `in`.windrunner.cryptotransferdemo.domain.CurrencySelectionState
import `in`.windrunner.cryptotransferdemo.domain.usecase.GetConversionRateCase
import `in`.windrunner.cryptotransferdemo.domain.usecase.ObserveEthWalletBalance
import `in`.windrunner.cryptotransferdemo.domain.usecase.ObserveTransferFeeCase
import `in`.windrunner.cryptotransferdemo.flatMapLatestResult
import `in`.windrunner.cryptotransferdemo.mapResult
import `in`.windrunner.cryptotransferdemo.ofEtherium
import `in`.windrunner.cryptotransferdemo.ofFiatCurrency
import `in`.windrunner.cryptotransferdemo.ui.MediaProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransferScreenViewModel @Inject constructor(
    private val getConversionRateCase: GetConversionRateCase,
    private val mediaProvider: MediaProvider,
    private val observeEthWalletBalance: ObserveEthWalletBalance,
    observeTransferFeeCase: ObserveTransferFeeCase,
    currencySelectionState: CurrencySelectionState,
) : ViewModel() {

    private val amountSelected = MutableStateFlow(0.toBigDecimal())
    private val isFiatToEthDirection = MutableStateFlow(true)

    val transferFee: StateFlow<Result<CustomCurrencyAmount>?> =
        observeTransferFeeCase().map { feeResult ->
            feeResult.mapResult {
                CustomCurrencyAmount(it, CustomCurrencyAmount.CURRENCY_ETH_CODE)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    val transferCalcModel: StateFlow<Result<TransferCalcModel>?> =
        currencySelectionState.currencySelected.flatMapLatest { currency ->
            getConversionRateCase(currency.currencyCode).flatMapLatestResult { exchangeRate ->
                getTransferDraftModel(currency, exchangeRate)
            }
        }.map { transferDraftResult ->
            transferDraftResult.mapResult { transferDraft ->
                mapTransferCalcModel(transferDraft)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private fun getTransferDraftModel(
        currency: Currency,
        conversionRate: BigDecimal
    ): Flow<Result<TransferCalcModelDraft>> = combine(
        amountSelected,
        isFiatToEthDirection,
        observeEthWalletBalance(),
    ) { amount, fiatToEthDirection, walletBalance ->
        Result.success(
            TransferCalcModelDraft(
                enteredAmount = amount,
                selectedCurrency = currency,
                maxAvailableAmount = walletBalance,
                isFiatToEthTransfer = fiatToEthDirection,
                conversionRate = conversionRate
            )
        )
    }

    private fun mapTransferCalcModel(
        draft: TransferCalcModelDraft,
    ): TransferCalcModel {
        val baseAmount = if (draft.isFiatToEthTransfer) {
            draft.enteredAmount.ofFiatCurrency(draft.selectedCurrency)
        } else draft.enteredAmount.ofEtherium()

        val equivalentAmount = if (draft.isFiatToEthTransfer) {
            draft.enteredAmount.divide(draft.conversionRate, 10, RoundingMode.HALF_UP).ofEtherium()
        } else {
            draft.enteredAmount.multiply(draft.conversionRate)
                .ofFiatCurrency(draft.selectedCurrency)
        }

        return TransferCalcModel(
            baseAmount = baseAmount,
            equivalentAmount = equivalentAmount,
            maxAvailableAmount = draft.maxAvailableAmount.ofEtherium(),
            selectedCurrency = draft.selectedCurrency,
            selectedCurrencyIconRes = mediaProvider.getFlagResource(draft.selectedCurrency.currencyCode),
            isTransferAllowed = canAffordTransfer(draft)
        )
    }

    private fun canAffordTransfer(draft: TransferCalcModelDraft): Boolean {
        val ethEquivalent = if (draft.isFiatToEthTransfer) {
            draft.enteredAmount.divide(draft.conversionRate, 10, RoundingMode.HALF_UP)
        } else draft.enteredAmount

        return transferFee.value?.getOrNull()?.number?.let { fee ->
            draft.maxAvailableAmount - ethEquivalent - fee >= 0.0.toBigDecimal()
        } ?: false
    }

    fun onSwapCurrencyClick() {
        val newAmount = transferCalcModel.value?.getOrNull()
        isFiatToEthDirection.update { prevState -> !prevState }

        newAmount?.let { newValue ->
            amountSelected.update { newValue.baseAmount.number.stripTrailingZeros() }
        }
    }

    fun onAmountChange(amount: BigDecimal?) {
        amountSelected.update { amount ?: 0.toBigDecimal() }
    }
}