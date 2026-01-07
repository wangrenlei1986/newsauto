package com.airss.domain

import android.content.Context
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class CloudSummarizer(private val ctx: Context) {
    private val client = OkHttpClient()

    // 可在设置中配置：兼容OpenAI的Base URL与Key
    fun summarize(text: String, maxSentences: Int = 4): String? {
        val baseUrl = SettingsManager.getBaseUrl(ctx) ?: return null
        val apiKey = SettingsManager.getApiKey(ctx) ?: return null
        val url = "$baseUrl/v1/chat/completions"
        val json = JSONObject().apply {
            put("model", SettingsManager.getModel(ctx) ?: "gpt-4.1-mini")
            put("messages", listOf(
                JSONObject().apply {
                    put("role", "system")
                    put("content", "请用中文为新闻内容生成简洁摘要，保留关键信息与数字，限制在${maxSentences}句内。")
                },
                JSONObject().apply {
                    put("role", "user")
                    put("content", text)
                }
            ))
            put("temperature", 0.3)
        }
        val body = json.toString().toRequestBody("application/json".toMediaType())
        val req = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $apiKey")
            .post(body)
            .build()
        return try {
            client.newCall(req).execute().use { resp ->
                val s = resp.body?.string() ?: return null
                val obj = JSONObject(s)
                val content = obj.getJSONArray("choices").getJSONObject(0)
                    .getJSONObject("message").getString("content")
                content
            }
        } catch (e: Exception) {
            Log.e("CloudSummarizer", "error", e)
            null
        }
    }
}
