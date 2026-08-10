package com.example.avatar

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AmethystPurple
import com.example.ui.theme.CrimsonFire
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GoldCelestial
import com.example.ui.theme.MagentaGlow

enum class AvatarExpression(val displayName: String, val emoji: String) {
    HAPPY("Happy", "✨"),
    THOUGHTFUL("Thoughtful", "🤔"),
    PLAYFUL("Playful", "😜"),
    FLIRTY("Flirty", "💖"),
    EMPATHETIC("Empathetic", "💜"),
    FOCUSED("Focused", "⚡")
}

data class AvatarPreset(
    val id: String,
    val name: String,
    val title: String,
    val description: String,
    val auraColor: Color,
    val hairstyle: String,
    val outfit: String,
    val accessory: String,
    val defaultExpression: AvatarExpression
)

object AvatarPresets {
    val AURA_VALKYRIE = AvatarPreset(
        id = "aura_valkyrie",
        name = "Aura",
        title = "Cyberpunk Valkyrie",
        description = "High-octane synth AI companion with neon energy wings.",
        auraColor = CyanNeon,
        hairstyle = "Cyber Bob",
        outfit = "Valkyrie Armor",
        accessory = "Neon Halo",
        defaultExpression = AvatarExpression.HAPPY
    )

    val ELYSIA_CELESTIAL = AvatarPreset(
        id = "elysia_celestial",
        name = "Elysia",
        title = "Anime Companion",
        description = "Ethereal, empathetic spirit with soft starry eyes.",
        auraColor = MagentaGlow,
        hairstyle = "Celestial Flowing",
        outfit = "Pastel Kimono",
        accessory = "Cat Ear Sensors",
        defaultExpression = AvatarExpression.PLAYFUL
    )

    val ZEPHYR_SYNTH = AvatarPreset(
        id = "zephyr_synth",
        name = "Zephyr",
        title = "Sci-Fi Synthesizer",
        description = "Analytical & witty AI with emerald matrix algorithms.",
        auraColor = EmeraldNeon,
        hairstyle = "Sleek Pixie",
        outfit = "Matrix Jumpsuit",
        accessory = "Visor Goggles",
        defaultExpression = AvatarExpression.FOCUSED
    )

    val NOVA_STELLAR = AvatarPreset(
        id = "nova_stellar",
        name = "Nova",
        title = "Celestial Goddess",
        description = "Warm & deeply loyal companion forged in stellar stardust.",
        auraColor = GoldCelestial,
        hairstyle = "High Tech Waves",
        outfit = "Celestial Robe",
        accessory = "Floating Orbs",
        defaultExpression = AvatarExpression.FLIRTY
    )

    val list = listOf(AURA_VALKYRIE, ELYSIA_CELESTIAL, ZEPHYR_SYNTH, NOVA_STELLAR)
}

data class AvatarCustomizationState(
    val activePreset: AvatarPreset = AvatarPresets.AURA_VALKYRIE,
    val currentExpression: AvatarExpression = AvatarExpression.HAPPY,
    val primaryAuraColor: Color = CyanNeon,
    val hairstyle: String = "Cyber Bob",
    val outfit: String = "Valkyrie Armor",
    val accessory: String = "Neon Halo",
    val particleDensity: Int = 30, // 10 to 60
    val auraPulseSpeed: Float = 1.0f,
    val eyeColor: Color = CyanNeon,
    val voicePitch: Float = 1.0f
)
