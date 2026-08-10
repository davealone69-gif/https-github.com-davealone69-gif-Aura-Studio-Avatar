package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // "Preference", "Milestone", "Interest", "Fact"
    val content: String,
    val affinityImpact: Int = 5,
    val timestamp: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String, // "user" or "aura"
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val routedNode: String = "AGENT_CHAT",
    val expression: String = "HAPPY",
    val confidenceScore: Double = 0.98
)

@Entity(tableName = "media_gallery")
data class MediaItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val prompt: String,
    val mediaType: String = "IMAGE", // "IMAGE" or "VIDEO"
    val cameraMotion: String = "360 Orbit",
    val lightingPreset: String = "Cinematic Cyberpunk",
    val resolution: String = "8K Ultra HD",
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
