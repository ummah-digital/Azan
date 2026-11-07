package com.azan.prayertimes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azan.prayertimes.R
import com.azan.prayertimes.data.PrayerRepository
import com.azan.prayertimes.ui.screens.*
import com.azan.prayertimes.ui.theme.*

enum class AppTab(val title: String, val iconRes: Int) {
    PRAYERS("Prayers", R.drawable.ic_mosque),
    QIBLA("Qibla", R.drawable.ic_kaaba),
    SETTINGS("Settings", R.drawable.ic_azan_speaker)
}

@Composable
fun AzanMainScreen(
    repository: PrayerRepository
) {
    var currentTab by remember { mutableStateOf(AppTab.PRAYERS) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = IslamicGreenDark,
                contentColor = MakkahGold,
                tonalElevation = 8.dp
            ) {
                AppTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                painter = painterResource(id = tab.iconRes),
                                contentDescription = tab.title,
                                tint = if (isSelected) MakkahGold else TextMuted,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                color = if (isSelected) MakkahGoldLight else TextMuted,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = IslamicGreenContainer
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
                .background(ObsidianBackground)
        ) {
            when (currentTab) {
                AppTab.PRAYERS -> HomeScreen(repository = repository)
                AppTab.QIBLA -> QiblaScreen(repository = repository)
                AppTab.SETTINGS -> SettingsScreen(repository = repository)
            }
        }
    }
}
