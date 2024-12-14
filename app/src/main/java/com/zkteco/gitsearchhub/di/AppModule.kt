package com.zkteco.gitsearchhub.di

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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

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


    @Singleton
    @Provides
    fun createRequestInterceptorClient(
        @ApplicationContext context: Context
    ): OkHttpClient {

        return OkHttpClient.Builder()
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

