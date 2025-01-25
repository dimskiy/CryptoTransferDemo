package `in`.windrunner.cryptotransferdemo.ui.currency_selector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.windrunner.cryptotransferdemo.domain.CurrencySelectionState
import `in`.windrunner.cryptotransferdemo.domain.model.DomainException
import `in`.windrunner.cryptotransferdemo.domain.model.ExchangeableCurrencyModel
import `in`.windrunner.cryptotransferdemo.domain.usecase.GetCurrenciesListCase
import `in`.windrunner.cryptotransferdemo.mapResult
import `in`.windrunner.cryptotransferdemo.ui.MediaProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.util.Currency
import javax.inject.Inject

@HiltViewModel
class CurrenciesSelectorViewModel @Inject constructor(
    private val getCurrenciesListCase: GetCurrenciesListCase,
    private val mediaProvider: MediaProvider,
    private val currencySelectionState: CurrencySelectionState,
) : ViewModel() {

    val currenciesList: StateFlow<Result<List<SelectableCurrencyItem>>?> = combine(
        flow { emit(getCurrenciesListCase()) },
        currencySelectionState.currencySelected
    ) { currencies, selected ->
        if (currencies.getOrNull()?.isEmpty() == true) {
            Result.failure(DomainException.ConversionRatesUnavailable(null))
        } else {
            currencies.mapResult { mapCurrencies(it, selected) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private fun mapCurrencies(
        currencies: List<ExchangeableCurrencyModel>,
        selected: Currency
    ): List<SelectableCurrencyItem> = currencies.map { currencyItem ->
        SelectableCurrencyItem(
            currency = currencyItem.currency,
            currencyFlagRes = mediaProvider.getFlagResource(currencyItem.currency.currencyCode),
            ethAmount = currencyItem.ethAmount,
            baseFiatCurrencyAmount = currencyItem.baseFiatCurrencyAmount,
            isSelected = selected.currencyCode == currencyItem.currency.currencyCode
        )
    }

    fun onCurrencySelected(currency: Currency) {
        currencySelectionState.onCurrencySelected(currency)
    }
}