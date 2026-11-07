package com.azan.prayertimes.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import kotlinx.coroutines.flow.StateFlow

class AzanPlayerService : Service() {

    companion object {
        const val ACTION_PLAY = "com.azan.prayertimes.action.PLAY"
        const val ACTION_STOP = "com.azan.prayertimes.action.STOP"
        const val EXTRA_PRAYER_NAME = "extra_prayer_name"
        const val EXTRA_CITY_NAME = "extra_city_name"

        val isPlayingState: StateFlow<Boolean> = AzanAudioManager.isPlaying
        val currentPrayer: StateFlow<String> = AzanAudioManager.currentPrayerName

        fun play(context: Context, prayerName: String = "Azan", cityName: String = "") {
            // Use safe in-process AudioManager directly
            AzanAudioManager.play(context, prayerName)
        }

        fun stop(context: Context) {
            AzanAudioManager.stop()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                val prayer = intent.getStringExtra(EXTRA_PRAYER_NAME) ?: "Azan"
                val city = intent.getStringExtra(EXTRA_CITY_NAME) ?: ""
                try {
                    val notification = AzanNotificationHelper.buildAzanNotification(
                        this,
                        prayer,
                        city,
                        isPlayingAudio = true
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        startForeground(
                            AzanNotificationHelper.NOTIFICATION_ID,
                            notification,
                            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                        )
                    } else {
                        startForeground(AzanNotificationHelper.NOTIFICATION_ID, notification)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                AzanAudioManager.play(this, prayer)
            }
            ACTION_STOP -> {
                AzanAudioManager.stop()
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        stopForeground(STOP_FOREGROUND_REMOVE)
                    } else {
                        @Suppress("DEPRECATION")
                        stopForeground(true)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }
}
