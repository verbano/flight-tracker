package com.bapinaev.domain.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    private val client = OkHttpClient()
        .newBuilder()
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.travelpayouts.com/aviasales")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: AviasalesApi = retrofit.create(AviasalesApi::class.java)
}