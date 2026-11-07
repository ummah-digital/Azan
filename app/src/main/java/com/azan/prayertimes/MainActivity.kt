package com.azan.prayertimes

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.azan.prayertimes.data.PrayerRepository
import com.azan.prayertimes.service.AzanAlarmReceiver
import com.azan.prayertimes.ui.AzanMainScreen
import com.azan.prayertimes.ui.theme.AzanTheme

class MainActivity : ComponentActivity() {

    private lateinit var repository: PrayerRepository

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                AzanAlarmReceiver.scheduleAllPrayers(this)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repository = PrayerRepository(applicationContext)

        // Request notification permission on Android 13+
        checkAndRequestNotificationPermission()

        setContent {
            AzanTheme {
                AzanMainScreen(repository = repository)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        repository.refreshTimes()
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermissionLauncher.launch(permission)
            }
        }
    }
}
