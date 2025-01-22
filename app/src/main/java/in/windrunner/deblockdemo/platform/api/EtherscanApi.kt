package `in`.windrunner.deblockdemo.platform.api

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal

interface EtherscanApi {

    @GET("api")
    suspend fun getEthereumGasPrice(
        @Query("module") module: String = "gastracker",
        @Query("action") action: String = "gasoracle"
    ): GasResult


    data class GasResult(
        @SerializedName("result") val result: GasPrice?
    )

    data class GasPrice(
        @SerializedName("SafeGasPrice") val gasPrice: BigDecimal
    )

    companion object {
        private const val ETHERSCAN_BASE_URL = "https://api.etherscan.io/"

        val etherscanApi: EtherscanApi by lazy {
            Retrofit.Builder()
                .baseUrl(ETHERSCAN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EtherscanApi::class.java)
        }
    }
}