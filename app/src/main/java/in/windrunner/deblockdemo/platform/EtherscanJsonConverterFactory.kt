package `in`.windrunner.deblockdemo.platform

import com.google.gson.GsonBuilder
import `in`.windrunner.deblockdemo.platform.api.EtherscanApi
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
}