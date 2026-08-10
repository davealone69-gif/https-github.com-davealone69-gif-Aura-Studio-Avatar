package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MemoryEntity
import com.example.ui.theme.AmethystPurple
import com.example.ui.theme.BorderGlass
import com.example.ui.theme.CardGlass
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DeepMidnight
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GoldCelestial
import com.example.ui.theme.MagentaGlow
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceVariantDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun MemoryVaultScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val memories by viewModel.memories.collectAsState()
    val affinity by viewModel.affinityState.collectAsState()

    var newMemoryContent by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Preference") }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepMidnight)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Column {
            Text(
                text = "AGENT_MEMORY VAULT",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AmethystPurple,
                letterSpacing = 2.sp
            )
            Text(
                text = "Affinity Link & Long-Term Memory",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
        }

        // Affinity Level Progress Card
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, AmethystPurple),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
                                .background(AmethystPurple.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = AmethystPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Lvl ${affinity.level} • ${affinity.stageName}",
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 16.sp
                            )
                            Text(
                                text = affinity.titleBadge,
                                color = CyanNeon,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Text(
                        text = "${affinity.points} PTS",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GoldCelestial
                    )
                }

                // Progress Bar
                val progress = ((affinity.points % 50) / 50f).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = AmethystPurple,
                    trackColor = BorderGlass
                )

                Text(
                    text = "${50 - (affinity.points % 50)} points to reach next companion sync stage",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }

        // Add New Memory Box
        Card(
            colors = CardDefaults.cardColors(containerColor = CardGlass),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderGlass),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "ADD MEMORY DIRECTIVE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanNeon,
                    letterSpacing = 1.sp
                )

                OutlinedTextField(
                    value = newMemoryContent,
                    onValueChange = { newMemoryContent = it },
                    placeholder = { Text("e.g., Prefers dark roast coffee, loves synthwave music...", color = TextSecondary) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_memory_input_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmethystPurple,
                        unfocusedBorderColor = BorderGlass,
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val categories = listOf("Preference", "Milestone", "Interest", "Fact")
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        categories.forEach { cat ->
                            val isSel = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) AmethystPurple else SurfaceDark)
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(cat, fontSize = 11.sp, color = if (isSel) Color.Black else TextPrimary)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (newMemoryContent.isNotBlank()) {
                                viewModel.addCustomMemory(selectedCategory, newMemoryContent)
                                newMemoryContent = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AmethystPurple),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("save_memory_button")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SAVE", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }
        }

        // Memories List
        Text(
            text = "PERSISTED MEMORY VAULT (${memories.size})",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 1.sp
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            memories.forEach { mem ->
                MemoryItemCard(
                    memory = mem,
                    onPinToggle = { viewModel.togglePinMemory(mem) },
                    onDelete = { viewModel.deleteMemory(mem.id) }
                )
            }
        }
    }
}

@Composable
fun MemoryItemCard(
    memory: MemoryEntity,
    onPinToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardGlass),
        border = androidx.compose.foundation.BorderStroke(
            width = if (memory.isPinned) 1.5.dp else 1.dp,
            color = if (memory.isPinned) GoldCelestial else BorderGlass
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("memory_card_${memory.id}")
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AmethystPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = AmethystPurple,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = memory.category.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldCelestial
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "+${memory.affinityImpact} PTS",
                        fontSize = 10.sp,
                        color = EmeraldNeon,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = memory.content,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            IconButton(onClick = onPinToggle) {
                Icon(
                    imageVector = if (memory.isPinned) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Pin",
                    tint = if (memory.isPinned) GoldCelestial else TextSecondary
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = TextSecondary
                )
            }
        }
    }
}
