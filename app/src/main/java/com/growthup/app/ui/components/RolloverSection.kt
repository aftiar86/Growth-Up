package com.growthup.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.growthup.app.data.model.TaskEntity
import com.growthup.app.ui.theme.GrowthUpColors
import com.growthup.app.ui.theme.Typography

@Composable
fun RolloverItem(
    task: TaskEntity,
    onRollover: () -> Unit,
    onIgnore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GrowthUpColors.SurfaceWhite),
        border = BorderStroke(1.dp, GrowthUpColors.CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = task.title,
                style = Typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = GrowthUpColors.TextCharcoal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onIgnore,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, GrowthUpColors.CardBorder),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GrowthUpColors.TextMuted)
                ) {
                    Text("Abaikan", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                FilledTonalButton(
                    onClick = onRollover,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = GrowthUpColors.WarmRose,
                        contentColor = GrowthUpColors.SurfaceWhite
                    )
                ) {
                    Text("Pindahkan ke Besok", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
