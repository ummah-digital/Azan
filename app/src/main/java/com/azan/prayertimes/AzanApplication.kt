package com.azan.prayertimes

import android.app.Application
import com.azan.prayertimes.service.AzanAlarmReceiver
import com.azan.prayertimes.service.AzanNotificationHelper

class AzanApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Create high-importance notification channel for Azan alerts
        AzanNotificationHelper.createNotificationChannel(this)
        // Ensure prayers are scheduled
        try {
            AzanAlarmReceiver.scheduleAllPrayers(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
