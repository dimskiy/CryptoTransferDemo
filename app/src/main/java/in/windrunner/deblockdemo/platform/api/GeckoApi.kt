package `in`.windrunner.deblockdemo.platform.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal

interface GeckoApi {

    @GET("api/v3/simple/price")
    suspend fun getConversionRate(
        @Query("ids") ids: String = "ethereum",
        @Query("vs_currencies") currencyCode: String
    ): PriceResponse

    data class PriceResponse(
        @SerializedName("ethereum") val conversionRate: ConversionRate
    )

    data class ConversionRate(
        val rate: BigDecimal
    )

    companion object {
        const val GECKO_BASE_URL = "https://api.coingecko.com/"
    }
}