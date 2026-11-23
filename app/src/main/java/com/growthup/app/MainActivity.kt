package com.growthup.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.growthup.app.data.local.GrowthUpDatabase
import com.growthup.app.data.repository.TaskRepositoryImpl
import com.growthup.app.notification.NotificationHelper
import com.growthup.app.ui.history.HistoryViewModel
import com.growthup.app.ui.navigation.GrowthUpApp
import com.growthup.app.ui.theme.GrowthUpTheme
import com.growthup.app.ui.today.TodayViewModel
import com.growthup.app.ui.tomorrow.TomorrowViewModel

class MainActivity : ComponentActivity() {

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        NotificationHelper.scheduleDailyReminders(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make status bar icons and clock dark and clear on light background
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = true
        insetsController.isAppearanceLightNavigationBars = true

        NotificationHelper.createNotificationChannel(this)
        requestNotificationPermission()
        NotificationHelper.scheduleDailyReminders(this)

        val database = GrowthUpDatabase.getDatabase(this)
        val repository = TaskRepositoryImpl(database.taskDao(), database.rewardDao())

        val todayViewModel = TodayViewModel(repository)
        val tomorrowViewModel = TomorrowViewModel(repository)
        val historyViewModel = HistoryViewModel(repository)

        setContent {
            GrowthUpTheme {
                GrowthUpApp(
                    todayViewModel = todayViewModel,
                    tomorrowViewModel = tomorrowViewModel,
                    historyViewModel = historyViewModel
                )
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
