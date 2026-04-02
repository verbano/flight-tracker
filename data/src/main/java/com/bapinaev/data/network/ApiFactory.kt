package com.bapinaev.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    fun createAviasalesApi(baseUrl: String): AviasalesApi {
        val client = OkHttpClient.Builder().build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AviasalesApi::class.java)
    }
}
