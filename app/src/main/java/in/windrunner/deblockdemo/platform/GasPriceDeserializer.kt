package `in`.windrunner.deblockdemo.platform

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import `in`.windrunner.deblockdemo.platform.api.EtherscanApi
import java.lang.reflect.Type

// Intended to deserialize fields that can be either String or Object
class GasPriceDeserializer : JsonDeserializer<EtherscanApi.GasPrice?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): EtherscanApi.GasPrice? = if (json?.isJsonObject == true) {
        val gasPriceString = json.asJsonObject
            .get(EtherscanApi.GasPrice.GAS_PRICE_FIELD)
            .asString

        gasPriceString.toBigDecimalOrNull()?.let(EtherscanApi::GasPrice)

    } else null
}