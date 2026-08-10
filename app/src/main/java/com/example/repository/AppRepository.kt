package com.example.repository

import com.example.data.AppDatabase
import com.example.data.ChatMessageEntity
import com.example.data.MediaItemEntity
import com.example.data.MemoryEntity
import kotlinx.coroutines.flow.Flow

class AppRepository(private val db: AppDatabase) {

    val allMemories: Flow<List<MemoryEntity>> = db.memoryDao().getAllMemories()
    val allChatMessages: Flow<List<ChatMessageEntity>> = db.chatMessageDao().getAllMessages()
    val allMediaItems: Flow<List<MediaItemEntity>> = db.mediaDao().getAllMedia()

    suspend fun addMemory(memory: MemoryEntity) {
        db.memoryDao().insertMemory(memory)
    }

    suspend fun deleteMemory(id: Int) {
        db.memoryDao().deleteMemory(id)
    }

    suspend fun togglePinMemory(memory: MemoryEntity) {
        db.memoryDao().updateMemory(memory.copy(isPinned = !memory.isPinned))
    }

    suspend fun addChatMessage(message: ChatMessageEntity) {
        db.chatMessageDao().insertMessage(message)
    }

    suspend fun clearChatHistory() {
        db.chatMessageDao().clearHistory()
    }

    suspend fun addMediaItem(media: MediaItemEntity) {
        db.mediaDao().insertMedia(media)
    }

    suspend fun deleteMediaItem(id: Int) {
        db.mediaDao().deleteMedia(id)
    }

    suspend fun toggleFavoriteMedia(media: MediaItemEntity) {
        db.mediaDao().updateMedia(media.copy(isFavorite = !media.isFavorite))
    }
}
