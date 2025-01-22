package `in`.windrunner.deblockdemo.platform.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GeckoApi {

    @GET("api/v3/simple/price")
    suspend fun getConversionRate(
        @Query("ids") ids: String = "ethereum",
        @Query("vs_currencies") currencyCode: String
    ): Result<PriceResponse>

    data class PriceResponse(
        val ethRates: Map<String, Double>
    )

    companion object {
        private const val GECKO_BASE_URL = "https://api.coingecko.com/"

        val geckoApi: GeckoApi by lazy {
            Retrofit.Builder()
                .baseUrl(GECKO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GeckoApi::class.java)
        }
    }
}