package com.growthup.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.EditCalendar
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.growthup.app.ui.history.HistoryScreen
import com.growthup.app.ui.history.HistoryViewModel
import com.growthup.app.ui.theme.GrowthUpColors
import com.growthup.app.ui.today.TodayScreen
import com.growthup.app.ui.today.TodayViewModel
import com.growthup.app.ui.tomorrow.TomorrowScreen
import com.growthup.app.ui.tomorrow.TomorrowViewModel

enum class Screen(val label: String, val icon: ImageVector) {
    TODAY("Hari Ini", Icons.Rounded.CheckCircleOutline),
    TOMORROW("Besok", Icons.Rounded.EditCalendar),
    HISTORY("Riwayat", Icons.Rounded.FavoriteBorder)
}

@Composable
fun GrowthUpApp(
    todayViewModel: TodayViewModel,
    tomorrowViewModel: TomorrowViewModel,
    historyViewModel: HistoryViewModel
) {
    var currentScreen by remember { mutableStateOf(Screen.TODAY) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        bottomBar = {
            NavigationBar(
                containerColor = GrowthUpColors.SurfaceWhite,
                tonalElevation = 6.dp
            ) {
                Screen.values().forEach { screen ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            currentScreen = screen
                            if (screen == Screen.TODAY) todayViewModel.loadTasks()
                            if (screen == Screen.TOMORROW) tomorrowViewModel.loadData()
                            if (screen == Screen.HISTORY) historyViewModel.loadHistory()
                        },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.label
                            )
                        },
                        label = {
                            Text(
                                text = screen.label,
                                fontSize = 12.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GrowthUpColors.WarmRose,
                            selectedTextColor = GrowthUpColors.WarmRose,
                            indicatorColor = GrowthUpColors.BackgroundCream,
                            unselectedIconColor = GrowthUpColors.TextMuted,
                            unselectedTextColor = GrowthUpColors.TextMuted
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.TODAY -> TodayScreen(
                    viewModel = todayViewModel,
                    onNavigateToPlan = { currentScreen = Screen.TOMORROW }
                )
                Screen.TOMORROW -> TomorrowScreen(
                    viewModel = tomorrowViewModel
                )
                Screen.HISTORY -> HistoryScreen(
                    viewModel = historyViewModel
                )
            }
        }
    }
}
