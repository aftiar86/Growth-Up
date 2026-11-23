package com.growthup.app.ui.history

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.growthup.app.domain.model.DollCatalog
import com.growthup.app.ui.theme.GrowthUpColors
import com.growthup.app.ui.theme.Typography
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GrowthUpColors.BackgroundCream)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Evaluasi & Riwayat",
            style = Typography.headlineMedium.copy(
                color = GrowthUpColors.WarmRose,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )
        )
        Text(
            text = "Refleksi perjalanan konsistensi harianmu",
            style = Typography.bodyMedium.copy(color = GrowthUpColors.TextMuted)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Stat Summary Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Keberhasilan",
                value = "${state.stats.percentage}%",
                subtitle = "Terselesaikan",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Total Selesai",
                value = "${state.stats.completedCount}",
                subtitle = "Dari ${state.stats.totalCount} tugas",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Hari Aktif",
                value = "${state.stats.daysTracked}",
                subtitle = "Hari tercatat",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Encouragement Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GrowthUpColors.SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Pesan untuk Dirimu 🌸",
                    style = Typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = GrowthUpColors.WarmRose
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                val message = when {
                    state.stats.totalCount == 0 -> "Setiap langkah kecil adalah awal pertumbuhan. Mulai rencanakan harimu dengan santai dan konsisten."
                    state.stats.percentage >= 80 -> "Luar biasa! Konsistensimu sangat mengagumkan. Pertahankan ritme indah ini!"
                    state.stats.percentage >= 50 -> "Progres yang sangat baik! Kamu sudah menyelesaikan sebagian besar targetmu."
                    else -> "Tidak apa-apa jika belum semua selesai. Yang terpenting adalah terus mencoba dan bertumbuh setiap hari."
                }
                Text(
                    text = message,
                    style = Typography.bodyMedium.copy(color = GrowthUpColors.TextCharcoal)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Doll Collection Shelf
        val unlockedDollIds = state.unlockedRewards.map { it.dollId }.toSet()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Rak Koleksi Boneka 🎀",
                style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = GrowthUpColors.TextCharcoal
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(GrowthUpColors.SoftBlush)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "${unlockedDollIds.size}/${DollCatalog.ALL_DOLLS.size} Terkumpul",
                    style = Typography.labelSmall.copy(
                        color = GrowthUpColors.WarmRose,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(DollCatalog.ALL_DOLLS, key = { it.id }) { doll ->
                val isUnlocked = unlockedDollIds.contains(doll.id)
                Card(
                    modifier = Modifier.width(105.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUnlocked) GrowthUpColors.SurfaceWhite else GrowthUpColors.SurfaceWhite.copy(alpha = 0.6f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 1.dp else 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isUnlocked) Color(doll.accentColorHex).copy(alpha = 0.2f)
                                    else GrowthUpColors.CardBorder.copy(alpha = 0.5f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isUnlocked) doll.emoji else "🔒",
                                fontSize = 26.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = doll.name.split(" ").firstOrNull() ?: doll.name,
                            style = Typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isUnlocked) GrowthUpColors.TextCharcoal else GrowthUpColors.TextMuted
                            ),
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isUnlocked) "✨ Terbuka" else "Terkunci",
                            style = Typography.labelSmall.copy(
                                fontSize = 10.sp,
                                color = if (isUnlocked) GrowthUpColors.CompletedGreen else GrowthUpColors.TextMuted
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Catatan Kegiatan",
            style = Typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = GrowthUpColors.TextCharcoal
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.tasksByDate.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Belum ada riwayat tercatat.\nMulai hari pertamamu dengan mencatat rencana!",
                    style = Typography.bodyMedium.copy(color = GrowthUpColors.TextMuted),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                state.tasksByDate.forEach { (dateStr, dateTasks) ->
                    item(key = "header_$dateStr") {
                        val headerLabel = try {
                            val parsed = LocalDate.parse(dateStr)
                            val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale("id", "ID"))
                            parsed.format(formatter)
                        } catch (e: Exception) {
                            dateStr
                        }
                        Text(
                            text = headerLabel,
                            style = Typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = GrowthUpColors.TextMuted
                            ),
                            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                        )
                    }

                    items(dateTasks, key = { it.id }) { task ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = GrowthUpColors.SurfaceWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (task.isCompleted) GrowthUpColors.CompletedGreen
                                            else GrowthUpColors.CardBorder
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (task.isCompleted) Icons.Rounded.Check else Icons.Rounded.Close,
                                        contentDescription = null,
                                        tint = if (task.isCompleted) GrowthUpColors.SurfaceWhite else GrowthUpColors.TextMuted,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = task.title,
                                    style = Typography.bodyMedium.copy(
                                        color = if (task.isCompleted) GrowthUpColors.TextCharcoal else GrowthUpColors.TextMuted
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GrowthUpColors.SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = Typography.labelMedium.copy(fontSize = 11.sp),
                color = GrowthUpColors.TextMuted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = Typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = GrowthUpColors.WarmRose,
                    fontSize = 20.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = Typography.labelMedium.copy(fontSize = 10.sp),
                color = GrowthUpColors.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}
