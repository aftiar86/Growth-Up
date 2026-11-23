package com.growthup.app.ui.today

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
import com.growthup.app.ui.components.CelebrationDialog
import com.growthup.app.ui.components.DailyProgressBar
import com.growthup.app.ui.components.TaskCard
import com.growthup.app.ui.theme.GrowthUpColors
import com.growthup.app.ui.theme.Typography
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TodayScreen(
    viewModel: TodayViewModel,
    onNavigateToPlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    val formattedDate = try {
        val parsed = LocalDate.parse(state.date)
        val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale("id", "ID"))
        parsed.format(formatter)
    } catch (e: Exception) {
        state.date
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GrowthUpColors.BackgroundCream)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header
        Text(
            text = "Growth Up",
            style = Typography.headlineMedium.copy(
                color = GrowthUpColors.WarmRose,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
        )
        Text(
            text = formattedDate,
            style = Typography.bodyMedium.copy(color = GrowthUpColors.TextMuted)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Progress Section
        DailyProgressBar(
            completed = state.completedCount,
            total = state.totalCount,
            percent = state.progressPercent
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Input Row for Today
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.inputTaskTitle,
                onValueChange = { viewModel.onInputTitleChanged(it) },
                placeholder = {
                    Text(
                        "Tambah kegiatan hari ini...",
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
                    onDone = { viewModel.addTaskForToday() }
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            FloatingActionButton(
                onClick = { viewModel.addTaskForToday() },
                containerColor = GrowthUpColors.WarmRose,
                contentColor = GrowthUpColors.SurfaceWhite,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(52.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Tambah Kegiatan"
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Rencana Hari Ini (${state.tasks.size})",
            style = Typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = GrowthUpColors.TextCharcoal
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GrowthUpColors.SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Belum Ada Rencana Hari Ini",
                            style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = GrowthUpColors.TextCharcoal,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ketik di atas untuk langsung menambah kegiatan hari ini, atau rencanakan esok di tab Rencana Besok ✨",
                            style = Typography.bodyMedium.copy(color = GrowthUpColors.TextMuted),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(state.tasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onToggle = { isChecked -> viewModel.toggleTask(task.id, isChecked) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        if (state.showCelebrationDialog && state.celebrationReward != null) {
            CelebrationDialog(
                reward = state.celebrationReward!!,
                evolution = state.celebrationEvolution,
                onDismiss = viewModel::dismissCelebrationDialog
            )
        }
    }
}
