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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraRoll
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.MediaItemEntity
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
fun MediaStudioScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val mediaItems by viewModel.mediaItems.collectAsState()

    var isVideoMode by remember { mutableStateOf(true) }
    var promptInput by remember { mutableStateOf("") }
    var selectedMotion by remember { mutableStateOf("360 Orbit") }
    var selectedLighting by remember { mutableStateOf("Cinematic Cyberpunk") }
    var selectedResolution by remember { mutableStateOf("8K Ultra HD") }
    var selectedDuration by remember { mutableStateOf("10 Secs") }
    var selectedFps by remember { mutableStateOf("60 FPS") }
    var selectedAspect by remember { mutableStateOf("16:9 Landscape") }

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
                text = "AGENT_MEDIA STUDIO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MagentaGlow,
                letterSpacing = 2.sp
            )
            Text(
                text = if (isVideoMode) "Text to Video Motion Synthesis" else "8K High-Res Image Generation",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
        }

        // Mode Switcher Tabs (Text to Video vs Text to Image)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceDark)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = { isVideoMode = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isVideoMode) MagentaGlow else Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("tab_text_to_video")
            ) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = null,
                    tint = if (isVideoMode) Color.Black else TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Text to Video",
                    fontWeight = FontWeight.Bold,
                    color = if (isVideoMode) Color.Black else TextSecondary,
                    fontSize = 13.sp
                )
            }

            Button(
                onClick = { isVideoMode = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isVideoMode) CyanNeon else Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("tab_text_to_image")
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = null,
                    tint = if (!isVideoMode) Color.Black else TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Text to Image",
                    fontWeight = FontWeight.Bold,
                    color = if (!isVideoMode) Color.Black else TextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        // Prompt Formulator Box
        Card(
            colors = CardDefaults.cardColors(containerColor = CardGlass),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderGlass),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isVideoMode) Icons.Default.Videocam else Icons.Default.AutoAwesome,
                        contentDescription = "AGENT_MEDIA",
                        tint = if (isVideoMode) MagentaGlow else CyanNeon,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isVideoMode) "Text-to-Video Engine Configuration" else "8K Visual Prompt Formulation",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 15.sp
                    )
                }

                OutlinedTextField(
                    value = promptInput,
                    onValueChange = { promptInput = it },
                    placeholder = {
                        Text(
                            text = if (isVideoMode)
                                "Describe your video scene, motion dynamics & camera action..."
                            else
                                "Describe the avatar artwork or high-res concept image...",
                            color = TextSecondary
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("media_prompt_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isVideoMode) MagentaGlow else CyanNeon,
                        unfocusedBorderColor = BorderGlass,
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    maxLines = 3
                )

                // Quick Prompt Templates / Inspiration
                Text("QUICK INSPIRATION PROMPTS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                val templates = if (isVideoMode) listOf(
                    "Cyberpunk avatar under glowing neon rain with 360 orbit camera",
                    "Ethereal cosmic goddess floating in crystal vortex with dolly zoom",
                    "Anime futuristic mecha pilot turning to screen with cinematic camera pan",
                    "Neon city skyline at midnight with FPV flythrough motion"
                ) else listOf(
                    "Ultra-detailed anime avatar with glowing purple aura armor",
                    "Cyberpunk android warrior with iridescent holographic wings",
                    "Celestial oracle surrounded by floating golden runes"
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(templates) { template ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceDark)
                                .border(1.dp, BorderGlass, RoundedCornerShape(8.dp))
                                .clickable { promptInput = template }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = template,
                                fontSize = 11.sp,
                                color = TextPrimary,
                                maxLines = 1
                            )
                        }
                    }
                }

                if (isVideoMode) {
                    // Video Specific Controls: Duration & FPS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("VIDEO DURATION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldCelestial)
                            val durations = listOf("5 Secs", "10 Secs", "15 Secs", "30s Loop")
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(durations) { dur ->
                                    val isSel = selectedDuration == dur
                                    FilterChip(
                                        selected = isSel,
                                        onClick = { selectedDuration = dur },
                                        label = { Text(dur, fontSize = 11.sp, color = if (isSel) Color.Black else TextPrimary) },
                                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = GoldCelestial)
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("FRAME RATE & ASPECT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldNeon)
                            val fpsOptions = listOf("24 FPS", "30 FPS", "60 FPS")
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(fpsOptions) { fps ->
                                    val isSel = selectedFps == fps
                                    FilterChip(
                                        selected = isSel,
                                        onClick = { selectedFps = fps },
                                        label = { Text(fps, fontSize = 11.sp, color = if (isSel) Color.Black else TextPrimary) },
                                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldNeon)
                                    )
                                }
                            }
                        }
                    }
                }

                // Camera Motion Selector
                Text(if (isVideoMode) "CAMERA MOTION VECTOR" else "CAMERA ANGLE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanNeon)
                val motions = listOf("360 Orbit", "Dolly Zoom", "Cinematic Pan", "Crane Shot", "FPV Flythrough")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(motions) { motion ->
                        val isSel = selectedMotion == motion
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedMotion = motion },
                            label = { Text(motion, color = if (isSel) Color.Black else TextPrimary) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CyanNeon),
                            modifier = Modifier.testTag("motion_chip_$motion")
                        )
                    }
                }

                // Lighting Preset
                Text("LIGHTING PRESET", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldCelestial)
                val lightings = listOf("Cinematic Cyberpunk", "Volumetric Studio", "Neon Dream", "Sunset Glow")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(lightings) { light ->
                        val isSel = selectedLighting == light
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedLighting = light },
                            label = { Text(light, color = if (isSel) Color.Black else TextPrimary) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = GoldCelestial)
                        )
                    }
                }

                // Resolution
                Text("RESOLUTION PARAMETER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldNeon)
                val resolutions = listOf("8K Ultra HD", "4K High Dynamic", "1080p Native")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(resolutions) { res ->
                        val isSel = selectedResolution == res
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedResolution = res },
                            label = { Text(res, color = if (isSel) Color.Black else TextPrimary) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldNeon)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        if (promptInput.isNotBlank()) {
                            val fullPrompt = if (isVideoMode) {
                                "$promptInput [Video Duration: $selectedDuration, $selectedFps, Camera: $selectedMotion]"
                            } else promptInput

                            viewModel.generateMediaPrompt(
                                prompt = fullPrompt,
                                motion = selectedMotion,
                                lighting = selectedLighting,
                                resolution = selectedResolution,
                                mediaType = if (isVideoMode) "VIDEO" else "IMAGE"
                            )
                            promptInput = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isVideoMode) MagentaGlow else CyanNeon
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("formulate_media_button")
                ) {
                    Icon(
                        imageVector = if (isVideoMode) Icons.Default.Videocam else Icons.Default.HighQuality,
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isVideoMode) "SYNTHESIZE TEXT-TO-VIDEO PROMPT" else "SYNTHESIZE 8K IMAGE PROMPT",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }

        // Media Gallery Section
        Text(
            text = "MEDIA GALLERY & PROMPT VAULT (${mediaItems.size})",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 1.sp
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            mediaItems.forEach { media ->
                MediaItemCard(
                    media = media,
                    onFavoriteClick = { viewModel.toggleFavoriteMedia(media) },
                    onDeleteClick = { viewModel.deleteMedia(media.id) }
                )
            }
        }
    }
}

@Composable
fun MediaItemCard(
    media: MediaItemEntity,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var isPlayingPreview by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = CardGlass),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderGlass),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("media_card_${media.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (media.mediaType == "VIDEO") MagentaGlow.copy(alpha = 0.25f) else CyanNeon.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (media.mediaType == "VIDEO") Icons.Default.Movie else Icons.Default.CameraRoll,
                            contentDescription = null,
                            tint = if (media.mediaType == "VIDEO") MagentaGlow else CyanNeon,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (media.mediaType == "VIDEO") "VIDEO • ${media.resolution}" else "IMAGE • ${media.resolution}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (media.mediaType == "VIDEO") MagentaGlow else CyanNeon
                    )
                }

                Row {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (media.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (media.isFavorite) CrimsonFire else TextSecondary
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = media.prompt,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Motion: ${media.cameraMotion}",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceDark)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Text(
                    text = "Lighting: ${media.lightingPreset}",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceDark)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                if (media.mediaType == "VIDEO") {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isPlayingPreview) MagentaGlow else SurfaceDark)
                            .clickable { isPlayingPreview = !isPlayingPreview }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isPlayingPreview) "▶ PLAYING MOTION..." else "▶ PREVIEW VIDEO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPlayingPreview) Color.Black else MagentaGlow
                        )
                    }
                }
            }
        }
    }
}
