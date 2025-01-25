package `in`.windrunner.cryptotransferdemo.domain

import `in`.windrunner.cryptotransferdemo.domain.repository.CalculatorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.util.Currency
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencySelectionState @Inject constructor(
    private val calculatorRepository: CalculatorRepository
) {

    private val _currencySelected = MutableStateFlow<Currency?>(null)

    val currencySelected: Flow<Currency> = combine(
        _currencySelected,
        flow {
            val firstCurrency = calculatorRepository.getCurrenciesSupported().first()
            Timber.d("Currency selected init: ${firstCurrency.currencyCode}")
            emit(firstCurrency)
        }
    ) { value, initialValue -> value ?: initialValue }

    fun onCurrencySelected(currency: Currency) {
        _currencySelected.update {
            currency
        }
    }
}