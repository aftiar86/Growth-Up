package com.growthup.app.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

object NotificationHelper {
    const val CHANNEL_ID = "growth_up_channel"
    const val CHANNEL_NAME = "Pengingat Growth Up"

    const val TYPE_EVENING = 1001
    const val TYPE_MIDDAY = 1002

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifikasi pengingat rencana dan checklist Growth Up"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun calculateNextTriggerTime(
        targetHour: Int,
        targetMinute: Int,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = currentTimeMillis
            set(Calendar.HOUR_OF_DAY, targetHour)
            set(Calendar.MINUTE, targetMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (calendar.timeInMillis <= currentTimeMillis) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return calendar.timeInMillis
    }

    fun scheduleDailyReminders(context: Context) {
        createNotificationChannel(context)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        // 1. Evening planning reminder at 20:00
        scheduleReminder(
            context = context,
            alarmManager = alarmManager,
            requestCode = TYPE_EVENING,
            hour = 20,
            minute = 0,
            title = "Waktunya Menyiapkan Hari Esok ✨",
            message = "Yuk catat kegiatan apa saja yang ingin kamu lakukan besok di Growth Up!"
        )

        // 2. Afternoon check-in reminder at 13:00
        scheduleReminder(
            context = context,
            alarmManager = alarmManager,
            requestCode = TYPE_MIDDAY,
            hour = 13,
            minute = 0,
            title = "Bagaimana Harimu? 🌸",
            message = "Cek checklist kegiatanmu hari ini dan tandai yang sudah selesai."
        )
    }

    private fun scheduleReminder(
        context: Context,
        alarmManager: AlarmManager,
        requestCode: Int,
        hour: Int,
        minute: Int,
        title: String,
        message: String
    ) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
            putExtra("requestCode", requestCode)
        }
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, flags)
        val triggerTime = calculateNextTriggerTime(hour, minute)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            } catch (e: SecurityException) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }
}
