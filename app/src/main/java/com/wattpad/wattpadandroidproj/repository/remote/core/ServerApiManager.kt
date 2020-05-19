package com.wattpad.wattpadandroidproj.repository.remote.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServerApiManager() {
    private var retrofit: Retrofit? = null
    private val baseUrl: String = "https://your_base_url";

    init {
        val gson: Gson = GsonBuilder().setLenient().create()
        val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(ConnectionReqInterceptor())
            .build()

        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun getRetrofit(): Retrofit? {
        return retrofit
    }

    private class ConnectionReqInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain?): Response {
            val originalRequest: Request? = chain?.request()
            return chain?.proceed(originalRequest)!!
        }

    }
}
