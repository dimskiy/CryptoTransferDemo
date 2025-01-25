package `in`.windrunner.cryptotransferdemo.platform

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import `in`.windrunner.cryptotransferdemo.platform.api.EtherscanApi
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class EtherscanJsonConverterFactory : Converter.Factory() {

    private val gson = GsonBuilder()
        .registerTypeAdapter(EtherscanApi.GasPrice::class.java, GasPriceDeserializer())
        .create()

    private val gsonConverterFactory = GsonConverterFactory.create(gson)

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? =
        gsonConverterFactory.responseBodyConverter(type, annotations, retrofit)

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? = gsonConverterFactory.requestBodyConverter(
        type,
        parameterAnnotations,
        methodAnnotations,
        retrofit
    )

    // Intended to deserialize fields that can be either String or Object
    private class GasPriceDeserializer : JsonDeserializer<EtherscanApi.GasPrice?> {
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
}