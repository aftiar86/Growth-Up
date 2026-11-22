package com.growthup.app.ui.tomorrow

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.growthup.app.ui.components.RolloverItem
import com.growthup.app.ui.components.TaskCard
import com.growthup.app.ui.theme.GrowthUpColors
import com.growthup.app.ui.theme.Typography
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TomorrowScreen(
    viewModel: TomorrowViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    val formattedTomorrow = try {
        val parsed = LocalDate.parse(state.tomorrowDate)
        val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale("id", "ID"))
        parsed.format(formatter)
    } catch (e: Exception) {
        state.tomorrowDate
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GrowthUpColors.BackgroundCream)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Header
        Text(
            text = "Rencana Esok Hari",
            style = Typography.headlineMedium.copy(
                color = GrowthUpColors.WarmRose,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )
        )
        Text(
            text = formattedTomorrow,
            style = Typography.bodyMedium.copy(color = GrowthUpColors.TextMuted)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Input Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.inputTaskTitle,
                onValueChange = { viewModel.onInputTitleChanged(it) },
                placeholder = {
                    Text(
                        "Tulis kegiatan esok hari...",
                        style = Typography.bodyMedium.copy(color = GrowthUpColors.TextMuted.copy(alpha = 0.6f))
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = GrowthUpColors.SurfaceWhite,
                    unfocusedContainerColor = GrowthUpColors.SurfaceWhite,
                    focusedBorderColor = GrowthUpColors.WarmRose,
                    unfocusedBorderColor = GrowthUpColors.CardBorder,
                    cursorColor = GrowthUpColors.WarmRose
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { viewModel.addTaskForTomorrow() }
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            FloatingActionButton(
                onClick = { viewModel.addTaskForTomorrow() },
                containerColor = GrowthUpColors.WarmRose,
                contentColor = GrowthUpColors.SurfaceWhite,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(52.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Tambah Rencana"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Uncompleted tasks from previous / today needing evaluation or rollover
            if (state.uncompletedPreviousTasks.isNotEmpty()) {
                item {
                    Text(
                        text = "Kegiatan Belum Selesai (Bahan Evaluasi)",
                        style = Typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = GrowthUpColors.WarmRose
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(state.uncompletedPreviousTasks, key = { "uncompleted_${it.id}" }) { task ->
                    RolloverItem(
                        task = task,
                        onRollover = { viewModel.rolloverTask(task) },
                        onIgnore = { viewModel.ignoreTask(task.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Planned tasks for tomorrow
            item {
                Text(
                    text = "Daftar Kegiatan Esok (${state.tomorrowTasks.size})",
                    style = Typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = GrowthUpColors.TextCharcoal,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            if (state.tomorrowTasks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = GrowthUpColors.SurfaceWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Text(
                                text = "Belum ada rencana untuk besok.\nTuliskan 3 hal terpenting yang ingin kamu selesaikan esok hari ✨",
                                style = Typography.bodyMedium.copy(color = GrowthUpColors.TextMuted),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            )
                        }
                    }
                }
            } else {
                items(state.tomorrowTasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onToggle = { /* Tomorrow tasks are planned, not checked yet */ },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
