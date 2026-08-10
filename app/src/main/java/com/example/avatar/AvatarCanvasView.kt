package com.example.avatar

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TextPrimary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AvatarCanvasView(
    state: AvatarCustomizationState,
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "aura_transition")

    // Aura pulse animation
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (1800 / state.auraPulseSpeed).toInt(), easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Particle rotation
    val particleRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Eye blinking animation
    val eyeBlink by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blink"
    )

    val auraColor = state.primaryAuraColor

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
            .clickable { onAvatarClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2f
            val centerY = height / 2f - 10f

            // 1. Draw Background Radial Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        auraColor.copy(alpha = 0.45f),
                        auraColor.copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = Offset(centerX, centerY),
                    radius = 180.dp.toPx() * pulseScale
                ),
                radius = 180.dp.toPx() * pulseScale,
                center = Offset(centerX, centerY)
            )

            // 2. Draw Orbiting Swarm Particles
            val numParticles = state.particleDensity.coerceIn(15, 60)
            val baseRadius = 110.dp.toPx()
            for (i in 0 until numParticles) {
                val angle = Math.toRadians((i * (360.0 / numParticles) + particleRotation).toDouble())
                val orbitDist = baseRadius + (i % 5) * 12f * pulseScale
                val px = centerX + orbitDist * cos(angle).toFloat()
                val py = centerY + orbitDist * sin(angle).toFloat()
                drawCircle(
                    color = if (i % 2 == 0) auraColor else Color.White,
                    radius = (3f + (i % 4)),
                    center = Offset(px, py),
                    alpha = 0.6f + (i % 3) * 0.15f
                )
            }

            // 3. Draw Accessory: Floating Halo or Orbs or Ears
            when (state.accessory) {
                "Neon Halo", "Floating Orbs" -> {
                    val haloY = centerY - 110.dp.toPx()
                    drawOval(
                        color = auraColor,
                        topLeft = Offset(centerX - 50.dp.toPx(), haloY),
                        size = Size(100.dp.toPx(), 20.dp.toPx()),
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
                "Cat Ear Sensors" -> {
                    val earsY = centerY - 90.dp.toPx()
                    // Left Ear
                    val leftEar = Path().apply {
                        moveTo(centerX - 40.dp.toPx(), earsY + 20.dp.toPx())
                        lineTo(centerX - 60.dp.toPx(), earsY - 20.dp.toPx())
                        lineTo(centerX - 20.dp.toPx(), earsY)
                        close()
                    }
                    drawPath(leftEar, color = auraColor)

                    // Right Ear
                    val rightEar = Path().apply {
                        moveTo(centerX + 40.dp.toPx(), earsY + 20.dp.toPx())
                        lineTo(centerX + 60.dp.toPx(), earsY - 20.dp.toPx())
                        lineTo(centerX + 20.dp.toPx(), earsY)
                        close()
                    }
                    drawPath(rightEar, color = auraColor)
                }
            }

            // 4. Draw Avatar Shoulders & Outfit Armor
            val shoulderY = centerY + 50.dp.toPx()
            val torsoPath = Path().apply {
                moveTo(centerX - 70.dp.toPx(), height)
                lineTo(centerX - 50.dp.toPx(), shoulderY)
                lineTo(centerX + 50.dp.toPx(), shoulderY)
                lineTo(centerX + 70.dp.toPx(), height)
                close()
            }
            drawPath(
                path = torsoPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2B2930), Color(0xFF1C1B1F))
                )
            )
            // Armor highlight lines
            drawPath(
                path = torsoPath,
                color = auraColor,
                style = Stroke(width = 2.dp.toPx())
            )

            // 5. Draw Neck
            drawRect(
                color = Color(0xFF4A4458),
                topLeft = Offset(centerX - 14.dp.toPx(), centerY + 25.dp.toPx()),
                size = Size(28.dp.toPx(), 30.dp.toPx())
            )

            // 6. Draw Head Silhouette
            val headRadius = 45.dp.toPx()
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF4A4458), Color(0xFF2B2930)),
                    center = Offset(centerX, centerY),
                    radius = headRadius
                ),
                radius = headRadius,
                center = Offset(centerX, centerY)
            )

            // Head Glow Border
            drawCircle(
                color = auraColor,
                radius = headRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 3.dp.toPx())
            )

            // 7. Draw Hairstyle Silhouette
            when (state.hairstyle) {
                "Cyber Bob", "Neon Twintails" -> {
                    val hairPath = Path().apply {
                        moveTo(centerX - 52.dp.toPx(), centerY + 10.dp.toPx())
                        quadraticTo(centerX - 50.dp.toPx(), centerY - 65.dp.toPx(), centerX, centerY - 65.dp.toPx())
                        quadraticTo(centerX + 50.dp.toPx(), centerY - 65.dp.toPx(), centerX + 52.dp.toPx(), centerY + 10.dp.toPx())
                        lineTo(centerX + 40.dp.toPx(), centerY + 30.dp.toPx())
                        lineTo(centerX + 35.dp.toPx(), centerY - 40.dp.toPx())
                        lineTo(centerX - 35.dp.toPx(), centerY - 40.dp.toPx())
                        lineTo(centerX - 40.dp.toPx(), centerY + 30.dp.toPx())
                        close()
                    }
                    drawPath(hairPath, color = auraColor.copy(alpha = 0.85f))
                }
                else -> {
                    val hairPath = Path().apply {
                        moveTo(centerX - 50.dp.toPx(), centerY)
                        quadraticTo(centerX, centerY - 75.dp.toPx(), centerX + 50.dp.toPx(), centerY)
                        lineTo(centerX + 35.dp.toPx(), centerY - 30.dp.toPx())
                        quadraticTo(centerX, centerY - 55.dp.toPx(), centerX - 35.dp.toPx(), centerY - 30.dp.toPx())
                        close()
                    }
                    drawPath(hairPath, color = auraColor.copy(alpha = 0.9f))
                }
            }

            // 8. Draw Eyes & Expression Features
            val eyeY = centerY - 5.dp.toPx()
            val eyeSpacing = 18.dp.toPx()

            val currentEyeHeight = when (state.currentExpression) {
                AvatarExpression.HAPPY -> 8.dp.toPx()
                AvatarExpression.PLAYFUL -> 12.dp.toPx()
                else -> 10.dp.toPx() * eyeBlink.coerceAtLeast(0.2f)
            }

            // Left Eye
            drawOval(
                color = state.eyeColor,
                topLeft = Offset(centerX - eyeSpacing - 7.dp.toPx(), eyeY - currentEyeHeight / 2),
                size = Size(14.dp.toPx(), currentEyeHeight)
            )
            // Left Eye Pupil Glow
            drawCircle(
                color = Color.White,
                radius = 2.5.dp.toPx(),
                center = Offset(centerX - eyeSpacing - 2.dp.toPx(), eyeY - 2.dp.toPx())
            )

            // Right Eye
            if (state.currentExpression == AvatarExpression.PLAYFUL) {
                // Wink right eye
                drawLine(
                    color = state.eyeColor,
                    start = Offset(centerX + eyeSpacing - 8.dp.toPx(), eyeY),
                    end = Offset(centerX + eyeSpacing + 8.dp.toPx(), eyeY),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            } else {
                drawOval(
                    color = state.eyeColor,
                    topLeft = Offset(centerX + eyeSpacing - 7.dp.toPx(), eyeY - currentEyeHeight / 2),
                    size = Size(14.dp.toPx(), currentEyeHeight)
                )
                drawCircle(
                    color = Color.White,
                    radius = 2.5.dp.toPx(),
                    center = Offset(centerX + eyeSpacing + 2.dp.toPx(), eyeY - 2.dp.toPx())
                )
            }

            // Blushing Cheeks when Flirty
            if (state.currentExpression == AvatarExpression.FLIRTY) {
                drawCircle(
                    color = Color(0xFFF2B8B5),
                    radius = 8.dp.toPx(),
                    center = Offset(centerX - 24.dp.toPx(), centerY + 12.dp.toPx()),
                    alpha = 0.5f
                )
                drawCircle(
                    color = Color(0xFFF2B8B5),
                    radius = 8.dp.toPx(),
                    center = Offset(centerX + 24.dp.toPx(), centerY + 12.dp.toPx()),
                    alpha = 0.5f
                )
            }

            // 9. Draw Mouth
            val mouthY = centerY + 20.dp.toPx()
            when (state.currentExpression) {
                AvatarExpression.HAPPY, AvatarExpression.PLAYFUL, AvatarExpression.FLIRTY -> {
                    val mouthPath = Path().apply {
                        moveTo(centerX - 10.dp.toPx(), mouthY)
                        quadraticTo(centerX, mouthY + 8.dp.toPx(), centerX + 10.dp.toPx(), mouthY)
                    }
                    drawPath(
                        path = mouthPath,
                        color = Color.White,
                        style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                AvatarExpression.THOUGHTFUL, AvatarExpression.FOCUSED -> {
                    drawLine(
                        color = Color.White,
                        start = Offset(centerX - 8.dp.toPx(), mouthY + 2.dp.toPx()),
                        end = Offset(centerX + 8.dp.toPx(), mouthY + 2.dp.toPx()),
                        strokeWidth = 2.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
                AvatarExpression.EMPATHETIC -> {
                    val mouthPath = Path().apply {
                        moveTo(centerX - 8.dp.toPx(), mouthY + 3.dp.toPx())
                        quadraticTo(centerX, mouthY + 6.dp.toPx(), centerX + 8.dp.toPx(), mouthY + 3.dp.toPx())
                    }
                    drawPath(
                        path = mouthPath,
                        color = Color.White,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }

            // 10. Visor Goggles Accessory across eyes
            if (state.accessory == "Visor Goggles") {
                drawRoundRect(
                    color = auraColor,
                    topLeft = Offset(centerX - 35.dp.toPx(), eyeY - 10.dp.toPx()),
                    size = Size(70.dp.toPx(), 20.dp.toPx()),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx()),
                    alpha = 0.85f
                )
            }
        }

        // Tap HUD overlay instruction
        Text(
            text = "${state.activePreset.name} • ${state.currentExpression.displayName} ${state.currentExpression.emoji}",
            color = TextPrimary.copy(alpha = 0.9f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}
