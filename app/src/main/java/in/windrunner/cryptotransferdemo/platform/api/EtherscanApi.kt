package `in`.windrunner.cryptotransferdemo.platform.api

import com.google.gson.annotations.SerializedName
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
        @SerializedName("status") val statusCode: Int,
        @SerializedName("message") val message: String?,
        @SerializedName("result") val result: GasPrice?
    )

    data class GasPrice(
        val gasPrice: BigDecimal
    ) {
        companion object {
            const val GAS_PRICE_FIELD = "SafeGasPrice"
        }
    }

    companion object {
        const val ETHERSCAN_BASE_URL = "https://api.etherscan.io/"
    }
}