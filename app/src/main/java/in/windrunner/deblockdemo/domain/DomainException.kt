package `in`.windrunner.deblockdemo.domain

import `in`.windrunner.deblockdemo.R
import timber.log.Timber

sealed class DomainException : Throwable() {

    abstract val wrappedThrowable: Throwable?

    init {
        Timber.d("Error created: ${this.javaClass}:$wrappedThrowable")
    }

    abstract fun getTextResource(): Int?

    data class NoCurrencyRate(override val wrappedThrowable: Throwable?) : DomainException() {
        override fun getTextResource(): Int = R.string.error_exchange_rate_unavailable
    }

    data class NoTransferFee(override val wrappedThrowable: Throwable?) : DomainException() {
        override fun getTextResource(): Int = R.string.error_gas_price_unavailable
    }

    data class NoValue(override val wrappedThrowable: Throwable?) : DomainException() {
        override fun getTextResource(): Int = R.string.error_something_went_wrong
    }

    data class GeneralError(override val wrappedThrowable: Throwable?) : DomainException() {
        override fun getTextResource(): Int = R.string.error_something_went_wrong
    }
}