package com.azan.prayertimes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azan.prayertimes.R
import com.azan.prayertimes.data.AzanMode
import com.azan.prayertimes.ui.theme.*

@Composable
fun AzanToggleBar(
    fajrMode: AzanMode,
    dhuhrMode: AzanMode,
    asrMode: AzanMode,
    maghribMode: AzanMode,
    ishaMode: AzanMode,
    onTogglePrayer: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_azan_speaker),
                    contentDescription = "5 Times Azan",
                    tint = MakkahGold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "5 Times Azan Alert",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Tap to Toggle",
                color = MakkahGoldLight,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AzanPrayerChip(name = "Fajr", mode = fajrMode) { onTogglePrayer("Fajr") }
            AzanPrayerChip(name = "Dhuhr", mode = dhuhrMode) { onTogglePrayer("Dhuhr") }
            AzanPrayerChip(name = "Asr", mode = asrMode) { onTogglePrayer("Asr") }
            AzanPrayerChip(name = "Maghrib", mode = maghribMode) { onTogglePrayer("Maghrib") }
            AzanPrayerChip(name = "Isha", mode = ishaMode) { onTogglePrayer("Isha") }
        }
    }
}

@Composable
private fun AzanPrayerChip(
    name: String,
    mode: AzanMode,
    onClick: () -> Unit
) {
    val isEnabled = mode != AzanMode.OFF
    val isFullAzan = mode == AzanMode.AZAN_AUDIO

    val bgColor = when {
        isFullAzan -> IslamicGreenLight
        isEnabled -> IslamicGreenContainer
        else -> SurfaceCardElevated
    }

    val borderColor = when {
        isFullAzan -> MakkahGold
        isEnabled -> MakkahGoldDark.copy(alpha = 0.5f)
        else -> SurfaceCardBorder
    }

    val iconTint = when {
        isFullAzan -> MakkahGold
        isEnabled -> TextSecondary
        else -> TextMuted
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_azan_speaker),
            contentDescription = "$name Azan Mode",
            tint = iconTint,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            color = if (isEnabled) TextPrimary else TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = when (mode) {
                AzanMode.AZAN_AUDIO -> "Azan"
                AzanMode.BEEP -> "Beep"
                AzanMode.SILENT -> "Silent"
                AzanMode.OFF -> "Off"
            },
            color = if (isFullAzan) MakkahGoldLight else if (isEnabled) TextSecondary else TextMuted,
            fontSize = 9.sp
        )
    }
}
