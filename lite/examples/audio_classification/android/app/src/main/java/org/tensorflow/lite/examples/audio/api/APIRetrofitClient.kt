package org.tensorflow.lite.examples.audio.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIRetrofitClient {
    private lateinit var retrofit: Retrofit
    fun getClient(baseUrl: String): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(1000000, TimeUnit.MILLISECONDS)
            .writeTimeout(1000000, TimeUnit.MILLISECONDS)
            .connectTimeout(1000000, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .protocols(listOf(Protocol.HTTP_1_1))
            .build()

        val gson = GsonBuilder().setLenient().create()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit
    }
}
