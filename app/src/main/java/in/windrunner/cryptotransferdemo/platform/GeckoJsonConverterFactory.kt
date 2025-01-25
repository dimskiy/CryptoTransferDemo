package `in`.windrunner.cryptotransferdemo.platform

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import `in`.windrunner.cryptotransferdemo.platform.api.GeckoApi
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class GeckoJsonConverterFactory : Converter.Factory() {

    private val gson = GsonBuilder()
        .registerTypeAdapter(GeckoApi.ConversionRate::class.java, ConversionRateDeserializer())
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

    // Intended to deserialize conversion rate fields with dynamic keys
    private class ConversionRateDeserializer : JsonDeserializer<GeckoApi.ConversionRate?> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): GeckoApi.ConversionRate? = if (json?.isJsonObject == true) {
            val conversionRateString = json.asJsonObject
                .entrySet()
                .firstOrNull()
                ?.value

            conversionRateString?.asBigDecimal?.let(GeckoApi::ConversionRate)

        } else null
    }
}