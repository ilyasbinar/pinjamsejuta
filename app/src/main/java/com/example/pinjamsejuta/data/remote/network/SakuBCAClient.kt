package com.example.pinjamsejuta.data.remote.network

import android.content.Context
import com.example.pinjamsejuta.utils.SharedPrefsUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SakuBCAClient {
//    private const val BASE_URL = "http://34.27.191.105/api/v1/"
    private const val BASE_URL = "https://928c-182-2-140-249.ngrok-free.app/api/v1/"

    // Membuat interceptor untuk logging HTTP request dan response
    private fun getHttpClient(context: Context): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")


                val token = SharedPrefsUtils.getToken(context)
                if(!token.isNullOrEmpty()){
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    // Membuat instance Retrofit
    fun getInstance(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getHttpClient(context))
            .build()
    }
}
