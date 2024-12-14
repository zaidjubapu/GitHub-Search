package com.zkteco.gitsearchhub.di

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


import com.zkteco.gitsearchhub.network.ApiHelper
import com.zkteco.gitsearchhub.network.ApiService
import com.zkteco.gitsearchhub.network.NetworkResponseAdapterFactory

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideBaseUrl(): String = ApiHelper.BASE_AUTH



    @Singleton
    @Provides
    fun provideGsonBuilder(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun createRequestInterceptorClient(
        @ApplicationContext context: Context
    ): OkHttpClient {

        val trustAllCerts = arrayOf<TrustManager>(@SuppressLint("CustomX509TrustManager")
        object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        })
        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory


        val interceptor = Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()

            chain.proceed(requestBuilder.build())


        }
        return OkHttpClient.Builder().addInterceptor(interceptor)
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(ApiHelper.REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .readTimeout(ApiHelper.REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .writeTimeout(ApiHelper.REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS).build()

    }

    @Singleton
    @Provides
    fun provideRetrofit(
        moshiBuilder: Moshi,
        baseUrl: String,
        okHttpClient: OkHttpClient,
        networkResponseAdapterFactory: NetworkResponseAdapterFactory
    ): Retrofit {
        val builder = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshiBuilder))
            .addCallAdapterFactory(networkResponseAdapterFactory)
            .client(okHttpClient)

        return builder.build()
    }

    @Singleton
    @Provides
    fun provideNetworkAdapter(): NetworkResponseAdapterFactory {
        return NetworkResponseAdapterFactory()
    }

    @Provides
    fun provideResources(application: Application): Resources {
        return application.resources
    }



    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)


}

