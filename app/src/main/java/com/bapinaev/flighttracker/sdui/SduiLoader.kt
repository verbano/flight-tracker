package com.bapinaev.flighttracker.sdui

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class SduiLoader {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    suspend fun loadScreen(path: String): SduiNode? = withContext(Dispatchers.IO) {
        try {
            val url = echoGetUrl(path) ?: return@withContext null
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            client.newCall(request).execute().use { response ->
                when (response.code) {
                    200 -> {
                        val raw = response.body?.string()?.trim().orEmpty()
                        if (raw.isEmpty()) return@use null
                        val root = JsonParser.parseString(raw)
                        if (!root.isJsonObject) return@use null
                        parseNode(root.asJsonObject)
                    }
                    404 -> null
                    else -> null
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun echoGetUrl(path: String): HttpUrl? {
        val key = path.trim().trimStart('/')
        if (key.isEmpty()) return null
        return ECHO_BASE.newBuilder()
            .addPathSegment("server")
            .addPathSegment("echo")
            .addPathSegment(key)
            .build()
    }

    private fun parseNode(json: JsonObject): SduiNode {
        val layout = parseLayout(json)
        return when (json.string("type")) {
            "scroll" -> SduiScrollNode(
                layout = layout,
                background = json.string("background"),
                child = json.obj("child")?.let { parseNode(it) }
            )

            "frame" -> SduiFrameNode(
                layout = layout,
                children = json.objList("children").map(::parseNode)
            )

            "column" -> SduiColumnNode(
                layout = layout,
                children = json.objList("children").map(::parseNode)
            )

            "card" -> SduiCardNode(
                layout = layout,
                children = json.objList("children").map(::parseNode)
            )

            "text" -> SduiTextNode(
                layout = layout,
                text = json.string("text"),
                style = json.string("style")
            )

            "button" -> SduiButtonNode(
                layout = layout,
                text = json.string("text"),
                variant = json.string("variant"),
                action = parseAction(json)
            )

            "image" -> SduiImageNode(
                layout = layout,
                src = json.string("src")
            )

            "divider" -> SduiDividerNode(layout = layout)

            "back_button" -> SduiBackButtonNode(
                layout = layout,
                action = parseAction(json)
            )

            else -> SduiColumnNode(layout = layout)
        }
    }

    private fun parseLayout(json: JsonObject): SduiLayout {
        return SduiLayout(
            id = json.string("id"),
            size = json.int("size"),
            minHeight = json.int("minHeight"),
            layoutGravity = json.string("layoutGravity"),
            textAlign = json.string("textAlign"),
            paddingHorizontal = json.int("paddingHorizontal"),
            paddingTop = json.int("paddingTop"),
            paddingBottom = json.int("paddingBottom"),
            marginTop = json.int("marginTop"),
            marginBottom = json.int("marginBottom"),
            marginStart = json.int("marginStart")
        )
    }

    private fun parseAction(json: JsonObject): SduiAction? {
        val actionType = json.string("action")
        return when {
            actionType == null -> null
            actionType.startsWith("navigate:") ->
                SduiAction.Navigate(actionType.removePrefix("navigate:"))
            actionType.startsWith("toast:") ->
                SduiAction.Toast(actionType.removePrefix("toast:"))
            else -> SduiAction.Custom(actionType)
        }
    }

    private fun JsonObject.string(key: String): String? =
        if (has(key) && !get(key).isJsonNull) get(key).asString else null

    private fun JsonObject.int(key: String): Int? =
        if (has(key) && !get(key).isJsonNull) get(key).asInt else null

    private fun JsonObject.obj(key: String): JsonObject? =
        if (has(key) && get(key).isJsonObject) getAsJsonObject(key) else null

    private fun JsonObject.objList(key: String): List<JsonObject> {
        if (!has(key) || !get(key).isJsonArray) return emptyList()
        return getAsJsonArray(key).mapNotNull { if (it.isJsonObject) it.asJsonObject else null }
    }

    private companion object {
        private val ECHO_BASE = "https://alfaitmo.ru".toHttpUrl()
    }
}
