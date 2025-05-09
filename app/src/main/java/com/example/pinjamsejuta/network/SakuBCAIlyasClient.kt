package com.example.pinjamsejuta.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SakuBCAIlyasClient {

    private const val BASE_URL = "https://08c1-182-2-164-116.ngrok-free.app/"
//    private const val BASE_URL = "http://192.168.18.51:8080/"
//    private const val BASE_URL = "http://10.0.2.2:8080/" // gunakan 10.0.2.2 untuk emulator Android

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}