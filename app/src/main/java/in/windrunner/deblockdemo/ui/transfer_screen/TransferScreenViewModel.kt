package `in`.windrunner.deblockdemo.ui.transfer_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.windrunner.deblockdemo.CustomCurrencyAmount
import `in`.windrunner.deblockdemo.domain.CurrencySelectionState
import `in`.windrunner.deblockdemo.domain.usecase.GetConversionRateCase
import `in`.windrunner.deblockdemo.domain.usecase.ObserveEthWalletBalance
import `in`.windrunner.deblockdemo.domain.usecase.ObserveTransferFeeCase
import `in`.windrunner.deblockdemo.flatMapLatestResult
import `in`.windrunner.deblockdemo.mapResult
import `in`.windrunner.deblockdemo.ofEtherium
import `in`.windrunner.deblockdemo.ofFiatCurrency
import `in`.windrunner.deblockdemo.ui.MediaProvider
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
            (draft.enteredAmount * draft.conversionRate).ofEtherium()
        } else (draft.enteredAmount / draft.conversionRate).ofFiatCurrency(draft.selectedCurrency)

        return TransferCalcModel(
            baseAmount = baseAmount,
            equivalentAmount = equivalentAmount,
            maxAvailableAmount = draft.maxAvailableAmount.ofEtherium(),
            selectedCurrency = draft.selectedCurrency,
            selectedCurrencyIconRes = mediaProvider.getFlagResource(draft.selectedCurrency.currencyCode),
        )
    }

    fun onSwapCurrencyClick() {
        val newAmount = transferCalcModel.value?.getOrNull()
        isFiatToEthDirection.update { prevState -> !prevState }

        newAmount?.let { newValue ->
            amountSelected.update { newValue.baseAmount.number.stripTrailingZeros() }
        }
    }

    fun onTransferClick() {

    }

    fun onAmountChange(amount: BigDecimal?) {
        amountSelected.update { amount ?: 0.toBigDecimal() }
    }
}