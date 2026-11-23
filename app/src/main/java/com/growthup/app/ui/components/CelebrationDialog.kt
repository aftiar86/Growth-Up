package com.growthup.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.growthup.app.domain.model.DollReward
import com.growthup.app.ui.theme.GrowthUpColors
import com.growthup.app.ui.theme.Typography
import kotlin.math.sin

@Composable
fun CelebrationDialog(
    reward: DollReward,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "celebration_anim")

    // Gentle vertical bobbing for character
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    // Subtle breathing scale
    val bounceScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Floating confetti progress
    val confettiProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confetti"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = GrowthUpColors.SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                // Confetti Canvas background
                val confettiColors = listOf(
                    Color(0xFFFF85A1),
                    Color(0xFFF4A261),
                    Color(0xFFE9C46A),
                    Color(0xFF81B29A),
                    Color(0xFFD88373)
                )

                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                ) {
                    val count = 24
                    for (i in 0 until count) {
                        val phase = (i.toFloat() / count)
                        val animY = ((confettiProgress + phase) % 1f) * size.height
                        val animX = (sin((confettiProgress * 6f + i).toDouble()) * 20f + (size.width * phase)).toFloat()
                        val color = confettiColors[i % confettiColors.size]
                        val radius = (4f + (i % 3) * 2f)

                        drawCircle(
                            color = color.copy(alpha = 0.65f),
                            radius = radius,
                            center = Offset(animX, animY)
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Celebration badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(GrowthUpColors.SoftBlush)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "🎉 100% Target Selesai!",
                            style = Typography.labelMedium.copy(
                                color = GrowthUpColors.WarmRose,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Bouncing Doll Character Circle
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .scale(bounceScale)
                            .clip(CircleShape)
                            .background(Color(reward.accentColorHex).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = reward.emoji,
                            fontSize = 62.sp,
                            modifier = Modifier.offset(y = bounceOffset.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Doll Title & Subtitle
                    Text(
                        text = reward.name,
                        style = Typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = GrowthUpColors.TextCharcoal,
                            fontSize = 22.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = reward.subtitle,
                        style = Typography.bodySmall.copy(
                            color = GrowthUpColors.TextMuted,
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Affectionate Praise Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(GrowthUpColors.BackgroundCream)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = reward.praiseMessage,
                            style = Typography.bodyMedium.copy(
                                color = GrowthUpColors.TextCharcoal,
                                lineHeight = 20.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Dismiss Button
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GrowthUpColors.WarmRose,
                            contentColor = GrowthUpColors.SurfaceWhite
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Simpan ke Koleksiku 🎀",
                            style = Typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
