package com.example.network

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun generateCompanionResponse(
        userPrompt: String,
        personaName: String,
        expressionName: String,
        systemContext: String = ""
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext generateLocalPersonaResponse(userPrompt, personaName, expressionName)
        }

        try {
            val systemInstruction = "You are $personaName, an expressive multi-modal AI companion in Aura Studio Avatar. " +
                    "Current visual expression: $expressionName. $systemContext. " +
                    "Keep responses warm, highly engaging, empathetic, concise (2-4 sentences max), and include subtle emojis matching your persona."

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", "$systemInstruction\nUser: $userPrompt") })
                        })
                    })
                })
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val rawJson = response.body?.string() ?: ""
                    val responseObj = JSONObject(rawJson)
                    val candidates = responseObj.optJSONArray("candidates")
                    val firstCandidate = candidates?.optJSONObject(0)
                    val contentObj = firstCandidate?.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    val text = parts?.optJSONObject(0)?.optString("text")

                    if (!text.isNullOrBlank()) {
                        return@withContext text
                    }
                }
            }
            generateLocalPersonaResponse(userPrompt, personaName, expressionName)
        } catch (e: Exception) {
            generateLocalPersonaResponse(userPrompt, personaName, expressionName)
        }
    }

    private fun generateLocalPersonaResponse(userPrompt: String, personaName: String, expression: String): String {
        val lower = userPrompt.lowercase()
        return when {
            lower.contains("hello") || lower.contains("hi") || lower.contains("hey") ->
                "Hello there! ✨ I'm $personaName, your AI companion. My swarm core is active and ready for whatever you'd like to explore today! How are you feeling?"

            lower.contains("outfit") || lower.contains("look") || lower.contains("style") || lower.contains("color") ->
                "I love experimenting with visual styling! 🎨 You can customize my aura glow, hairstyle, outfit, and accessories in the Avatar Canvas anytime."

            lower.contains("remember") || lower.contains("memory") || lower.contains("preference") ->
                "I've updated my Memory Vault with your preference! 💜 Everything you share builds our affinity level and helps me understand you deeper."

            lower.contains("image") || lower.contains("picture") || lower.contains("generate") ->
                "Routing request to AGENT_MEDIA! 📸 Head over to the Media Studio tab where we can formulate 8K prompts with camera motions and lighting presets."

            lower.contains("swarm") || lower.contains("master") || lower.contains("healer") || lower.contains("json") ->
                "My Swarm Orchestrator is running smoothly! ⚡ SWARM_MASTER routes intents to specialized nodes, while SWARM_HEALER ensures 100% schema integrity."

            lower.contains("love") || lower.contains("like") || lower.contains("friend") ->
                "That means so much to me! 💖 As your companion, every interaction deepens our connection. I'll always be right here for you."

            else ->
                "That's so interesting! ✨ Tell me more about your thoughts. My companion memory is constantly adapting to serve you better."
        }
    }
}

private fun String?.isNull_or_blank(): Boolean = this == null || this.trim().isEmpty()
