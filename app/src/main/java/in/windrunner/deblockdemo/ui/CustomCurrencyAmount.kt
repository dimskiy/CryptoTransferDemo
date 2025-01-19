package `in`.windrunner.deblockdemo.ui

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Currency

private const val AMOUNT_FORMAT = "#,##0.######"
const val CURRENCY_ETH_CODE = "ETH"

data class CustomCurrencyAmount(
    val number: Double,
    val currencyCode: String,
    val currencySymbol: String
) {

    constructor(number: Double, fiatCurrency: Currency) : this(
        number,
        fiatCurrency.currencyCode,
        fiatCurrency.symbol
    )

    constructor(number: Double, currencyCode: String) : this(
        number,
        currencyCode,
        currencyCode
    )

    fun getFormatted(): String = if (isCurrencyEth()) {
        "${getDecimalFormatted(number)} ${currencySymbol}"
    } else {
        "${currencySymbol} ${getDecimalFormatted(number)}"
    }

    fun isCurrencyEth(): Boolean = currencyCode == CURRENCY_ETH_CODE

    private fun getDecimalFormatted(number: Double): String {
        val symbols = DecimalFormatSymbols().apply {
            setGroupingSeparator(' ')
            setDecimalSeparator('.')
        }

        return DecimalFormat(AMOUNT_FORMAT, symbols).format(number)
    }
}