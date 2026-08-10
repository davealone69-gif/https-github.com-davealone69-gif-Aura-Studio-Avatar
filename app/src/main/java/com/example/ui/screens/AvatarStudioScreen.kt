package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.avatar.AvatarCanvasView
import com.example.avatar.AvatarExpression
import com.example.avatar.AvatarPresets
import com.example.ui.theme.AmethystPurple
import com.example.ui.theme.BorderGlass
import com.example.ui.theme.CardGlass
import com.example.ui.theme.CrimsonFire
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
fun AvatarStudioScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.avatarState.collectAsState()
    val speechPlaying by viewModel.speechPlaying.collectAsState()
    var reactionDialogue by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepMidnight)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "AURA STUDIO AVATAR",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanNeon,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Avatar Canvas & Styling",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            }

            IconButton(
                onClick = { viewModel.toggleSpeechSimulation() },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (speechPlaying) MagentaGlow else SurfaceVariantDark)
                    .testTag("voice_toggle_button")
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Voice Simulation",
                    tint = if (speechPlaying) Color.Black else CyanNeon
                )
            }
        }

        // Live Avatar Canvas View
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(SurfaceDark, SurfaceVariantDark)
                    )
                )
                .border(1.5.dp, BorderGlass, RoundedCornerShape(24.dp))
        ) {
            AvatarCanvasView(
                state = state,
                onAvatarClick = {
                    val reactions = listOf(
                        "I feel your touch! ✨ Our swarm link is synchronized.",
                        "Looking sharp today, master! ⚡",
                        "I'm always listening. What's on your mind? 💖",
                        "Swarm routing optimal! All 4 nodes are online. 💜"
                    )
                    reactionDialogue = reactions.random()
                }
            )

            // Voice Speech Simulation Wave
            if (speechPlaying) {
                Text(
                    text = "🔊 Voice Synthesis: '${state.activePreset.name}' speaking...",
                    color = MagentaGlow,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        // Tap Reaction Dialogue Box
        AnimatedVisibility(visible = reactionDialogue != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardGlass),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Reaction",
                        tint = CyanNeon,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = reactionDialogue ?: "",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Expression Selector Bar
        Text(
            text = "EXPRESSIVE STATES",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 1.sp
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(AvatarExpression.entries.toTypedArray()) { expr ->
                val isSelected = state.currentExpression == expr
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setExpression(expr) },
                    label = {
                        Text(
                            text = "${expr.emoji} ${expr.displayName}",
                            color = if (isSelected) Color.Black else TextPrimary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = state.primaryAuraColor,
                        containerColor = SurfaceVariantDark
                    ),
                    modifier = Modifier.testTag("expression_chip_${expr.name}")
                )
            }
        }

        // Customization Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = SurfaceDark,
            contentColor = CyanNeon,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = CyanNeon
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("PRESETS", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("AURA & COLORS", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("VECTORS", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
        }

        when (selectedTab) {
            0 -> {
                // Preset Companions List
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AvatarPresets.list.forEach { preset ->
                        val isSelected = state.activePreset.id == preset.id
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) SurfaceVariantDark else CardGlass
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) preset.auraColor else BorderGlass
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectPreset(preset) }
                                .testTag("preset_card_${preset.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(14.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(preset.auraColor.copy(alpha = 0.25f))
                                        .border(1.5.dp, preset.auraColor, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Face,
                                        contentDescription = preset.name,
                                        tint = preset.auraColor
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = preset.name,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary,
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "• ${preset.title}",
                                            color = preset.auraColor,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Text(
                                        text = preset.description,
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = preset.auraColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Aura Colors & Particle Density Slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardGlass)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Primary Aura Glow Spectrum",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 14.sp
                    )

                    val colors = listOf(
                        CyanNeon to "Cyan Neon",
                        MagentaGlow to "Magenta Glow",
                        EmeraldNeon to "Emerald Matrix",
                        GoldCelestial to "Gold Celestial",
                        AmethystPurple to "Amethyst Purple",
                        CrimsonFire to "Crimson Fire"
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        colors.forEach { (color, name) ->
                            val isSelected = state.primaryAuraColor == color
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (isSelected) 3.dp else 0.dp,
                                        color = Color.White,
                                        shape = CircleShape
                                    )
                                    .clickable { viewModel.setAuraColor(color) }
                                    .testTag("color_picker_${name.lowercase().replace(" ", "_")}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Aura Particle Swarm Density: ${state.particleDensity}",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 14.sp
                    )

                    Slider(
                        value = state.particleDensity.toFloat(),
                        onValueChange = { viewModel.setParticleDensity(it.toInt()) },
                        valueRange = 15f..60f,
                        colors = SliderDefaults.colors(
                            thumbColor = state.primaryAuraColor,
                            activeTrackColor = state.primaryAuraColor
                        ),
                        modifier = Modifier.testTag("particle_slider")
                    )
                }
            }

            2 -> {
                // Vector Attributes: Hairstyle, Outfit, Accessory
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardGlass)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Hairstyle
                    Text("HAIRSTYLE VECTOR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanNeon)
                    val hairstyles = listOf("Cyber Bob", "Celestial Flowing", "Neon Twintails", "Sleek Pixie")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(hairstyles) { style ->
                            val sel = state.hairstyle == style
                            FilterChip(
                                selected = sel,
                                onClick = { viewModel.setHairstyle(style) },
                                label = { Text(style, color = if (sel) Color.Black else TextPrimary) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CyanNeon)
                            )
                        }
                    }

                    // Outfit
                    Text("OUTFIT ARMOR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MagentaGlow)
                    val outfits = listOf("Valkyrie Armor", "Pastel Kimono", "Matrix Jumpsuit", "Celestial Robe")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(outfits) { outfit ->
                            val sel = state.outfit == outfit
                            FilterChip(
                                selected = sel,
                                onClick = { viewModel.setOutfit(outfit) },
                                label = { Text(outfit, color = if (sel) Color.Black else TextPrimary) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MagentaGlow)
                            )
                        }
                    }

                    // Accessory
                    Text("SWARM ACCESSORY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldNeon)
                    val accessories = listOf("Neon Halo", "Visor Goggles", "Cat Ear Sensors", "Floating Orbs")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(accessories) { acc ->
                            val sel = state.accessory == acc
                            FilterChip(
                                selected = sel,
                                onClick = { viewModel.setAccessory(acc) },
                                label = { Text(acc, color = if (sel) Color.Black else TextPrimary) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldNeon)
                            )
                        }
                    }
                }
            }
        }
    }
}
