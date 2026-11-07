package com.azan.prayertimes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azan.prayertimes.data.AzanMode
import com.azan.prayertimes.data.PrayerTimeItem
import com.azan.prayertimes.data.PrayerTimesState
import com.azan.prayertimes.ui.theme.*

@Composable
fun PrayerScheduleList(
    state: PrayerTimesState,
    onToggleAzan: (prayerName: String, isEnabled: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TODAY'S PRAYER TIMETABLE",
                color = MakkahGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "5 Azan Alarms",
                color = TextSecondary,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 5 Daily Obligatory Prayers with ON/OFF Switches
        state.prayerItems.forEach { item ->
            PrayerScheduleRow(
                item = item,
                onToggle = { isChecked ->
                    onToggleAzan(item.name, isChecked)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PrayerScheduleRow(
    item: PrayerTimeItem,
    onToggle: (Boolean) -> Unit
) {
    val isNext = item.isNext
    val isCurrent = item.isCurrent
    val isAzanOn = item.azanMode != AzanMode.OFF

    val rowBg = when {
        isNext -> IslamicGreenContainer
        isCurrent -> SurfaceCardElevated
        else -> SurfaceCard
    }

    val rowBorder = when {
        isNext -> MakkahGold
        else -> SurfaceCardBorder.copy(alpha = 0.5f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(rowBg)
            .border(1.dp, rowBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Prayer Name & Arabic
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.name,
                    color = if (isNext) MakkahGoldLight else TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = if (isNext) FontWeight.Bold else FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = item.arabicName,
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
            if (isNext) {
                Text(
                    text = "Next Prayer",
                    color = MakkahGold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Time
        Text(
            text = item.time,
            color = if (isNext) MakkahGoldLight else TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // Clean ON / OFF Switch for Azan
        Switch(
            checked = isAzanOn,
            onCheckedChange = { onToggle(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MakkahGold,
                checkedTrackColor = IslamicGreenLight,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = SurfaceCardElevated
            )
        )
    }
}
