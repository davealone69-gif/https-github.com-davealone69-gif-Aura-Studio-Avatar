package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.avatar.AvatarCustomizationState
import com.example.avatar.AvatarExpression
import com.example.avatar.AvatarPreset
import com.example.avatar.AvatarPresets
import com.example.data.AppDatabase
import com.example.data.ChatMessageEntity
import com.example.data.MediaItemEntity
import com.example.data.MemoryEntity
import com.example.network.GeminiService
import com.example.repository.AppRepository
import com.example.swarm.SwarmHealer
import com.example.swarm.SwarmMaster
import com.example.swarm.SwarmNode
import com.example.swarm.SwarmRoutingDecision
import com.example.ui.theme.AmethystPurple
import com.example.ui.theme.CrimsonFire
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GoldCelestial
import com.example.ui.theme.MagentaGlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AffinityState(
    val points: Int = 120,
    val level: Int = 3,
    val stageName: String = "Trusted Companion",
    val titleBadge: String = "Synchronized Swarm Link"
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = AppRepository(db)

    // Room State Flows
    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.allChatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val memories: StateFlow<List<MemoryEntity>> = repository.allMemories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mediaItems: StateFlow<List<MediaItemEntity>> = repository.allMediaItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Avatar State
    private val _avatarState = MutableStateFlow(AvatarCustomizationState())
    val avatarState: StateFlow<AvatarCustomizationState> = _avatarState.asStateFlow()

    // Affinity State
    private val _affinityState = MutableStateFlow(AffinityState())
    val affinityState: StateFlow<AffinityState> = _affinityState.asStateFlow()

    // Swarm Console State
    private val _lastRouting = MutableStateFlow<SwarmRoutingDecision?>(null)
    val lastRouting: StateFlow<SwarmRoutingDecision?> = _lastRouting.asStateFlow()

    private val _isGeneratingResponse = MutableStateFlow(false)
    val isGeneratingResponse: StateFlow<Boolean> = _isGeneratingResponse.asStateFlow()

    private val _speechPlaying = MutableStateFlow(false)
    val speechPlaying: StateFlow<Boolean> = _speechPlaying.asStateFlow()

    init {
        // Seed default memory items & welcome chat message if database is empty
        viewModelScope.launch {
            repository.allChatMessages.collect { list ->
                if (list.isEmpty()) {
                    repository.addChatMessage(
                        ChatMessageEntity(
                            sender = "aura",
                            message = "Greetings! I am Aura, your AI Companion power-linked to the Swarm Master orchestrator. How may I assist or personalize your experience today? ✨",
                            routedNode = "AGENT_CHAT",
                            expression = "HAPPY"
                        )
                    )
                }
            }
        }

        viewModelScope.launch {
            repository.allMemories.collect { list ->
                if (list.isEmpty()) {
                    repository.addMemory(MemoryEntity(category = "Preference", content = "Loves futuristic cyberpunk aesthetics and dark themes", affinityImpact = 10, isPinned = true))
                    repository.addMemory(MemoryEntity(category = "Milestone", content = "First initialization of Aura Studio Avatar system", affinityImpact = 25, isPinned = true))
                    repository.addMemory(MemoryEntity(category = "Interest", content = "Enjoys AI music generation & 8K image prompt synthesis", affinityImpact = 15))
                }
            }
        }

        viewModelScope.launch {
            repository.allMediaItems.collect { list ->
                if (list.isEmpty()) {
                    repository.addMediaItem(
                        MediaItemEntity(
                            prompt = "Cyberpunk Valkyrie avatar standing under glowing neon rain in Neo Tokyo",
                            cameraMotion = "360 Orbit",
                            lightingPreset = "Cinematic Cyberpunk",
                            resolution = "8K Ultra HD",
                            isFavorite = true
                        )
                    )
                }
            }
        }
    }

    fun selectPreset(preset: AvatarPreset) {
        _avatarState.value = _avatarState.value.copy(
            activePreset = preset,
            primaryAuraColor = preset.auraColor,
            hairstyle = preset.hairstyle,
            outfit = preset.outfit,
            accessory = preset.accessory,
            currentExpression = preset.defaultExpression
        )
    }

    fun setExpression(expression: AvatarExpression) {
        _avatarState.value = _avatarState.value.copy(currentExpression = expression)
    }

    fun setAuraColor(color: Color) {
        _avatarState.value = _avatarState.value.copy(primaryAuraColor = color)
    }

    fun setHairstyle(style: String) {
        _avatarState.value = _avatarState.value.copy(hairstyle = style)
    }

    fun setOutfit(outfit: String) {
        _avatarState.value = _avatarState.value.copy(outfit = outfit)
    }

    fun setAccessory(accessory: String) {
        _avatarState.value = _avatarState.value.copy(accessory = accessory)
    }

    fun setParticleDensity(density: Int) {
        _avatarState.value = _avatarState.value.copy(particleDensity = density)
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank() || _isGeneratingResponse.value) return

        viewModelScope.launch {
            _isGeneratingResponse.value = true

            // 1. Save user message
            repository.addChatMessage(
                ChatMessageEntity(
                    sender = "user",
                    message = userText,
                    routedNode = "SWARM_MASTER"
                )
            )

            // 2. Route input through SWARM_MASTER
            val decision = SwarmMaster.routeInput(userText)
            _lastRouting.value = decision

            // 3. Handle Node Action
            when (decision.targetNode) {
                SwarmNode.AGENT_AVATAR -> {
                    // Update avatar expression based on intent
                    val newExpr = when {
                        userText.contains("happy") -> AvatarExpression.HAPPY
                        userText.contains("flirt") || userText.contains("cute") -> AvatarExpression.FLIRTY
                        userText.contains("think") -> AvatarExpression.THOUGHTFUL
                        else -> AvatarExpression.PLAYFUL
                    }
                    setExpression(newExpr)

                    // Auto change aura if color mentioned
                    if (userText.lowercase().contains("purple")) setAuraColor(AmethystPurple)
                    else if (userText.lowercase().contains("gold")) setAuraColor(GoldCelestial)
                    else if (userText.lowercase().contains("red")) setAuraColor(CrimsonFire)
                    else if (userText.lowercase().contains("green")) setAuraColor(EmeraldNeon)
                }
                SwarmNode.AGENT_MEMORY -> {
                    // Extract memory
                    repository.addMemory(
                        MemoryEntity(
                            category = "Preference",
                            content = "User mentioned: $userText",
                            affinityImpact = 10
                        )
                    )
                    addAffinityPoints(10)
                }
                SwarmNode.AGENT_MEDIA -> {
                    // Add media item prompt
                    repository.addMediaItem(
                        MediaItemEntity(
                            prompt = userText,
                            cameraMotion = "360 Orbit",
                            lightingPreset = "Cinematic Cyberpunk"
                        )
                    )
                }
                else -> {
                    addAffinityPoints(5)
                }
            }

            // 4. Generate AI companion response
            val responseText = GeminiService.generateCompanionResponse(
                userPrompt = userText,
                personaName = _avatarState.value.activePreset.name,
                expressionName = _avatarState.value.currentExpression.displayName,
                systemContext = "Routed by ${decision.targetNode.nodeName}"
            )

            repository.addChatMessage(
                ChatMessageEntity(
                    sender = "aura",
                    message = responseText,
                    routedNode = decision.targetNode.nodeName,
                    expression = _avatarState.value.currentExpression.name,
                    confidenceScore = decision.confidenceScore
                )
            )

            _isGeneratingResponse.value = false
        }
    }

    fun addAffinityPoints(pts: Int) {
        val newPts = _affinityState.value.points + pts
        val newLvl = newPts / 50 + 1
        val stage = when {
            newLvl >= 5 -> "Soulmate Companion"
            newLvl >= 3 -> "Trusted Companion"
            else -> "Acquaintance"
        }
        _affinityState.value = _affinityState.value.copy(
            points = newPts,
            level = newLvl,
            stageName = stage
        )
    }

    fun addCustomMemory(category: String, content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            repository.addMemory(
                MemoryEntity(
                    category = category,
                    content = content,
                    affinityImpact = 15,
                    isPinned = true
                )
            )
            addAffinityPoints(15)
        }
    }

    fun deleteMemory(id: Int) {
        viewModelScope.launch { repository.deleteMemory(id) }
    }

    fun togglePinMemory(memory: MemoryEntity) {
        viewModelScope.launch { repository.togglePinMemory(memory) }
    }

    fun generateMediaPrompt(prompt: String, motion: String, lighting: String, resolution: String, mediaType: String = "IMAGE") {
        if (prompt.isBlank()) return
        viewModelScope.launch {
            repository.addMediaItem(
                MediaItemEntity(
                    prompt = prompt,
                    mediaType = mediaType,
                    cameraMotion = motion,
                    lightingPreset = lighting,
                    resolution = resolution
                )
            )
            addAffinityPoints(10)
        }
    }

    fun deleteMedia(id: Int) {
        viewModelScope.launch { repository.deleteMediaItem(id) }
    }

    fun toggleFavoriteMedia(media: MediaItemEntity) {
        viewModelScope.launch { repository.toggleFavoriteMedia(media) }
    }

    fun clearChat() {
        viewModelScope.launch { repository.clearChatHistory() }
    }

    fun testSelfHealer(malformedJson: String) {
        val healedDecision = SwarmHealer.validateAndHeal(malformedJson)
        _lastRouting.value = healedDecision
    }

    fun toggleSpeechSimulation() {
        _speechPlaying.value = !_speechPlaying.value
    }
}
