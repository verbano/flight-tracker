package com.bapinaev.flighttracker.sdui

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class SduiLoader {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    suspend fun loadScreen(path: String): SduiNode? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("https://alfaitmo.ru/server/echo/$path")
                .build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.string()?.let { gson.fromJson(it, SduiNode::class.java) }
            } else null
        } catch (e: Exception) {
            null
        }
    }
}
