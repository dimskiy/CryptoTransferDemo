package `in`.windrunner.deblockdemo.ui

import `in`.windrunner.deblockdemo.R
import javax.inject.Inject

class MediaProvider @Inject constructor() {

    private val flagsMap = mapOf(
        "USD" to R.drawable.ic_us,
        "EUR" to R.drawable.ic_eu,
        "GBP" to R.drawable.ic_uk
    )

    fun getFlagResource(currencyCode: String): Int? = flagsMap[currencyCode]
}