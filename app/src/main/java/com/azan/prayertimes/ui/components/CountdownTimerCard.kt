package com.azan.prayertimes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azan.prayertimes.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun CountdownTimerCard(
    nextPrayerName: String,
    nextPrayerTime: String,
    initialRemainingMillis: Long,
    progressPercent: Float,
    modifier: Modifier = Modifier
) {
    var remainingMillis by remember(initialRemainingMillis) { mutableLongStateOf(initialRemainingMillis) }

    // Live ticking countdown timer
    LaunchedEffect(initialRemainingMillis) {
        remainingMillis = initialRemainingMillis
        while (remainingMillis > 0) {
            delay(1000L)
            remainingMillis = (remainingMillis - 1000L).coerceAtLeast(0L)
        }
    }

    val totalSeconds = (remainingMillis / 1000L)
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    val timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(SurfaceCardElevated, SurfaceCard)
                )
            )
            .border(1.5.dp, MakkahGoldDark.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Next Prayer Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "NEXT PRAYER",
                    color = MakkahGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = nextPrayerTime,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Prayer Name
            Text(
                text = nextPrayerName,
                color = MakkahGoldLight,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Countdown digits (Removed negative '-' sign for a positive, uplifting vibe)
            Text(
                text = timeFormatted,
                color = TextPrimary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Text(
                text = "Time Remaining until Azan (Insha'Allah)",
                color = TextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progressPercent },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MakkahGold,
                trackColor = IslamicGreenContainer
            )
        }
    }
}
