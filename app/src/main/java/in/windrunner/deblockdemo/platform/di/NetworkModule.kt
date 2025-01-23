package `in`.windrunner.deblockdemo.platform.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.windrunner.deblockdemo.platform.EtherscanJsonConverterFactory
import `in`.windrunner.deblockdemo.platform.api.EtherscanApi
import `in`.windrunner.deblockdemo.platform.api.EtherscanApi.Companion.ETHERSCAN_BASE_URL
import `in`.windrunner.deblockdemo.platform.api.GeckoApi
import `in`.windrunner.deblockdemo.platform.api.GeckoApi.Companion.GECKO_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val HTTP_TIMEOUT_SEC = 10L

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor { Timber.tag("okhttp").d(it) }.apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .connectTimeout(HTTP_TIMEOUT_SEC, TimeUnit.SECONDS)
        .readTimeout(HTTP_TIMEOUT_SEC, TimeUnit.SECONDS)
        .writeTimeout(HTTP_TIMEOUT_SEC, TimeUnit.SECONDS)
        .build()

    @Provides
    fun provideEtherscanService(okHttpClient: OkHttpClient): EtherscanApi =
        Retrofit.Builder()
            .baseUrl(ETHERSCAN_BASE_URL)
            .addConverterFactory(EtherscanJsonConverterFactory())
            .client(okHttpClient)
            .build()
            .create(EtherscanApi::class.java)

    @Provides
    fun provideGeckoService(okHttpClient: OkHttpClient): GeckoApi =
        Retrofit.Builder()
            .baseUrl(GECKO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(GeckoApi::class.java)
}