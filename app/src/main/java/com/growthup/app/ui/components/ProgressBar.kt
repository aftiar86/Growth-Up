package com.growthup.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.growthup.app.ui.theme.GrowthUpColors
import com.growthup.app.ui.theme.Typography

@Composable
fun DailyProgressBar(
    completed: Int,
    total: Int,
    percent: Int,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (total > 0) completed.toFloat() / total.toFloat() else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "progressAnim"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(GrowthUpColors.SurfaceWhite)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Progres Hari Ini",
                style = Typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = GrowthUpColors.TextCharcoal
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "$completed / $total selesai",
                style = Typography.labelMedium.copy(
                    fontSize = 13.sp,
                    color = GrowthUpColors.WarmRose,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Progress track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(GrowthUpColors.CardBorder)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = animatedProgress)
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(GrowthUpColors.WarmRose)
            )
        }
    }
}
