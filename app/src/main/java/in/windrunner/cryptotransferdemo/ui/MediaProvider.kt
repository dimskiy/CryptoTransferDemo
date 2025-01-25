package `in`.windrunner.cryptotransferdemo.ui

import `in`.windrunner.cryptotransferdemo.R
import javax.inject.Inject

class MediaProvider @Inject constructor() {

    private val flagsMap = mapOf(
        "USD" to R.drawable.ic_us,
        "EUR" to R.drawable.ic_eu,
        "GBP" to R.drawable.ic_uk
    )

    fun getFlagResource(currencyCode: String): Int =
        flagsMap[currencyCode] ?: R.drawable.ic_question_mark
}