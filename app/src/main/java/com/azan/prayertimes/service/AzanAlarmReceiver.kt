package com.azan.prayertimes.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.azan.prayertimes.data.AzanMode
import com.azan.prayertimes.data.PrayerRepository
import java.util.Calendar

class AzanAlarmReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_ALARM = "com.azan.prayertimes.ALARM_TRIGGER"
        const val EXTRA_PRAYER = "extra_prayer"

        fun scheduleAllPrayers(context: Context) {
            val repository = PrayerRepository(context)
            val state = repository.state.value
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

            val now = System.currentTimeMillis()

            state.prayerItems.forEachIndexed { index, item ->
                if (item.azanMode != AzanMode.OFF && item.rawTimestamp > now) {
                    val intent = Intent(context, AzanAlarmReceiver::class.java).apply {
                        action = ACTION_ALARM
                        putExtra(EXTRA_PRAYER, item.name)
                    }
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        index + 100,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                item.rawTimestamp,
                                pendingIntent
                            )
                        } else {
                            alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                item.rawTimestamp,
                                pendingIntent
                            )
                        }
                    } catch (e: SecurityException) {
                        // In case exact alarm permission requires user grant on Android 12+
                        alarmManager.set(
                            AlarmManager.RTC_WAKEUP,
                            item.rawTimestamp,
                            pendingIntent
                        )
                    }
                }
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val repository = PrayerRepository(context)
        val state = repository.state.value

        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            scheduleAllPrayers(context)
            return
        }

        val prayerName = intent?.getStringExtra(EXTRA_PRAYER) ?: return
        val prayerItem = state.prayerItems.find { it.name.equals(prayerName, ignoreCase = true) } ?: return

        when (prayerItem.azanMode) {
            AzanMode.AZAN_AUDIO -> {
                AzanPlayerService.play(context, prayerItem.name, state.city.name)
            }
            AzanMode.BEEP, AzanMode.SILENT -> {
                val notification = AzanNotificationHelper.buildAzanNotification(
                    context,
                    prayerItem.name,
                    state.city.name,
                    isPlayingAudio = false
                )
                try {
                    NotificationManagerCompat.from(context).notify(
                        AzanNotificationHelper.NOTIFICATION_ID,
                        notification
                    )
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
            AzanMode.OFF -> {
                // Do nothing
            }
        }

        // Schedule next round of alarms
        scheduleAllPrayers(context)
    }
}
