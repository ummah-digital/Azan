package com.azan.prayertimes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azan.prayertimes.data.PrayerTimesState
import com.azan.prayertimes.ui.theme.*

@Composable
fun AdditionalTimingsCard(
    state: PrayerTimesState,
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
        Text(
            text = "ADDITIONAL ISLAMIC TIMINGS",
            color = MakkahGold,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AdditionalTimingRow(label = "Sunrise", arabic = "الشروق", time = state.sunriseTime)
            AdditionalTimingRow(label = "Ishraq", arabic = "الإشراق", time = state.ishraqTime)
            AdditionalTimingRow(label = "Chasht / Dhuha", arabic = "الضحى", time = state.chashtTime)
            AdditionalTimingRow(label = "Tahajjud / Qiyam", arabic = "التهجد", time = state.tahajjudTime)
            AdditionalTimingRow(label = "Suhoor End / Imsak", arabic = "الإمساك", time = state.suhoorEndTime)
        }
    }
}

@Composable
private fun AdditionalTimingRow(label: String, arabic: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceCardElevated.copy(alpha = 0.5f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = arabic,
                color = TextMuted,
                fontSize = 11.sp
            )
        }
        Text(
            text = time,
            color = MakkahGoldLight,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
