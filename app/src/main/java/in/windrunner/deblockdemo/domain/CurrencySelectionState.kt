package `in`.windrunner.deblockdemo.domain

import `in`.windrunner.deblockdemo.domain.repository.CalculatorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.util.Currency
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencySelectionState @Inject constructor(
    private val calculatorRepository: CalculatorRepository
) {

    private val _currencySelected = MutableStateFlow<Currency?>(null)

    val currencySelected: Flow<Currency?> = combine(
        _currencySelected,
        flow {
            val firstCurrency = calculatorRepository.getCurrenciesSupported().firstOrNull()
            Timber.d("Currency selected init: ${firstCurrency?.currencyCode}")
            emit(firstCurrency)
        }
    ) { value, initialValue -> value ?: initialValue }
}