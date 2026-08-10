package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatMessageEntity
import com.example.ui.theme.BorderGlass
import com.example.ui.theme.CardGlass
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DeepMidnight
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.MagentaGlow
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceVariantDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun SwarmChatScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isGenerating by viewModel.isGeneratingResponse.collectAsState()
    val avatarState by viewModel.avatarState.collectAsState()
    val lastRouting by viewModel.lastRouting.collectAsState()

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto scroll to bottom on new message
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepMidnight)
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(avatarState.primaryAuraColor.copy(alpha = 0.2f))
                        .border(1.5.dp, avatarState.primaryAuraColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "Companion Icon",
                        tint = avatarState.primaryAuraColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "${avatarState.activePreset.name} AI Companion",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(EmeraldNeon)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Swarm Link Active • ${avatarState.currentExpression.displayName} ${avatarState.currentExpression.emoji}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            IconButton(
                onClick = { viewModel.clearChat() },
                modifier = Modifier.testTag("clear_chat_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear Chat",
                    tint = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Active Routing Decision Header Banner
        lastRouting?.let { decision ->
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Swarm Master",
                        tint = CyanNeon,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SWARM_MASTER -> ${decision.targetNode.nodeName} (${(decision.confidenceScore * 100).toInt()}%)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanNeon
                    )
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                ChatMessageBubble(msg = msg, companionName = avatarState.activePreset.name)
            }

            if (isGenerating) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = CyanNeon,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${avatarState.activePreset.name} is formulating response...",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Quick Prompt Suggestions
        val suggestions = listOf(
            "Change my aura color to Gold Celestial",
            "Remember that I love sci-fi books",
            "Generate an 8K image prompt of us",
            "How are you feeling today?"
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(suggestions) { sugg ->
                AssistChip(
                    onClick = { viewModel.sendMessage(sugg) },
                    label = { Text(sugg, fontSize = 11.sp, color = TextPrimary) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = SurfaceVariantDark),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = CyanNeon,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    modifier = Modifier.testTag("quick_suggestion_chip")
                )
            }
        }

        // Message Input Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Message Aura companion...", color = TextSecondary) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyanNeon,
                    unfocusedBorderColor = BorderGlass,
                    focusedContainerColor = CardGlass,
                    unfocusedContainerColor = CardGlass,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                maxLines = 3
            )

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendMessage(inputText)
                        inputText = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (inputText.isNotBlank()) CyanNeon else SurfaceVariantDark)
                    .testTag("send_message_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (inputText.isNotBlank()) Color.Black else TextSecondary
                )
            }
        }
    }
}

@Composable
fun ChatMessageBubble(msg: ChatMessageEntity, companionName: String) {
    val isUser = msg.sender == "user"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) SurfaceVariantDark else CardGlass
            ),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = if (isUser) CyanNeon.copy(alpha = 0.5f) else MagentaGlow.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .testTag("chat_message_bubble_${msg.id}")
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUser) "You" else companionName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (isUser) CyanNeon else MagentaGlow
                    )

                    if (!isUser && msg.routedNode.isNotBlank()) {
                        Text(
                            text = "[${msg.routedNode}]",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldNeon
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = msg.message,
                    fontSize = 14.sp,
                    color = TextPrimary
                )
            }
        }
    }
}
