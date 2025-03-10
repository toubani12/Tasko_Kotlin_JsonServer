package com.badhacker.tasko.data.api

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.net.InetAddress
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            try {
                // Try to resolve the host before making the request
                val request = chain.request()
                val host = request.url.host
                Log.d("RetrofitClient", "Attempting to resolve host: $host")
                InetAddress.getByName(host)
                Log.d("RetrofitClient", "Successfully resolved host: $host")

                chain.proceed(request)
            } catch (e: Exception) {
                Log.e("RetrofitClient", "Network error", e)
                throw e
            }
        }
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    val apiService: TaskApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskApiService::class.java)
    }
}