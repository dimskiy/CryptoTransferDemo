package `in`.windrunner.deblockdemo

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Currency

fun BigDecimal.ofFiatCurrency(currency: Currency): CustomCurrencyAmount = CustomCurrencyAmount(
    number = this,
    fiatCurrency = currency
)

fun BigDecimal.ofEtherium(): CustomCurrencyAmount = CustomCurrencyAmount(
    number = this,
    currencyCode = CustomCurrencyAmount.CURRENCY_ETH_CODE
)

private const val AMOUNT_FORMAT = "#,##0.######"

data class CustomCurrencyAmount(
    val number: BigDecimal,
    val currencyCode: String,
    val currencySymbol: String
) {

    constructor(number: BigDecimal, fiatCurrency: Currency) : this(
        number,
        fiatCurrency.currencyCode,
        fiatCurrency.symbol
    )

    constructor(number: BigDecimal, currencyCode: String) : this(
        number,
        currencyCode,
        currencyCode
    )

    fun getFormatted(): String = if (isCurrencyEth()) {
        "${getDecimalFormatted(number)} $currencySymbol"
    } else {
        "$currencySymbol ${getDecimalFormatted(number)}"
    }

    fun isCurrencyEth(): Boolean = currencyCode == CURRENCY_ETH_CODE

    private fun getDecimalFormatted(number: BigDecimal): String {
        val symbols = DecimalFormatSymbols().apply {
            setGroupingSeparator(' ')
            setDecimalSeparator('.')
        }

        return DecimalFormat(AMOUNT_FORMAT, symbols).format(number)
    }

    companion object {
        const val CURRENCY_ETH_CODE = "ETH"
    }
}