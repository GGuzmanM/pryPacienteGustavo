package com.example.apppaciente.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object ApiClient {

    const val BASE_URL = "https://pryclinicamoviles.up.railway.app"
   // const val BASE_URL = "https://127.0.0.1:5000"
    var API_TOKEN: String? = null

    private class AuthInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val original = chain.request()
            val requestBuilder = original.newBuilder().header("Content-Type", "application/json")

            // Verificar si ya tenemos un token disponible para agregarlo a la cabecera
            val token = API_TOKEN
            if (!token.isNullOrEmpty()) {
                requestBuilder.header("Authorization", "Bearer $token")
            }

            val request = requestBuilder.build()
            return chain.proceed(request)
        }
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
