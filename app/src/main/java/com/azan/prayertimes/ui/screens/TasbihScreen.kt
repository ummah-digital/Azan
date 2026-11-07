package com.azan.prayertimes.ui.screens

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azan.prayertimes.R
import com.azan.prayertimes.data.IslamicData
import com.azan.prayertimes.data.TasbihPreset
import com.azan.prayertimes.ui.theme.*

@Composable
fun TasbihScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val presets = IslamicData.TASBIH_PRESETS

    var selectedPreset by remember { mutableStateOf(presets[0]) }
    var currentCount by remember { mutableIntStateOf(0) }
    var lapCount by remember { mutableIntStateOf(0) }
    var totalCount by remember { mutableIntStateOf(0) }

    var isVibrateEnabled by remember { mutableStateOf(true) }
    var isSoundEnabled by remember { mutableStateOf(true) }

    var isPressed by remember { mutableStateOf(false) }
    val scaleAnim by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        label = "bead_scale"
    )

    fun onBeadTap() {
        val nextCount = currentCount + 1
        totalCount += 1

        if (isVibrateEnabled) {
            triggerVibration(context, if (nextCount == selectedPreset.targetCount) 150 else 40)
        }
        if (isSoundEnabled) {
            playClickSound()
        }

        if (nextCount >= selectedPreset.targetCount) {
            currentCount = 0
            lapCount += 1
        } else {
            currentCount = nextCount
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Digital Tasbeeh",
            color = MakkahGold,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Keep your tongue moist with the remembrance of Allah",
            color = TextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Dhikr Preset Selector Row
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(presets) { preset ->
                val isSelected = preset.id == selectedPreset.id
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) IslamicGreenLight else SurfaceCard)
                        .border(
                            1.dp,
                            if (isSelected) MakkahGold else SurfaceCardBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable {
                            selectedPreset = preset
                            currentCount = 0
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = preset.arabic,
                            color = if (isSelected) MakkahGoldLight else TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${preset.title} (${preset.targetCount})",
                            color = if (isSelected) TextPrimary else TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Selected Dhikr Display Box
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(SurfaceCard)
                .border(1.dp, SurfaceCardBorder, RoundedCornerShape(18.dp))
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = selectedPreset.arabic,
                color = MakkahGold,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = selectedPreset.description,
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Large Bead Counter Circular Button
        Box(
            modifier = Modifier
                .size(220.dp)
                .scale(scaleAnim)
                .clip(CircleShape)
                .background(IslamicGreenDark)
                .border(3.dp, MakkahGold, CircleShape)
                .clickable { onBeadTap() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$currentCount",
                    color = MakkahGoldLight,
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "TARGET: ${selectedPreset.targetCount}",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "TAP TO COUNT",
                    color = TextMuted,
                    fontSize = 10.sp,
                    letterSpacing = 1.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Stats Row (Laps, Total, Reset)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceCard)
                .border(1.dp, SurfaceCardBorder, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TasbihStatItem(label = "Rounds (Laps)", value = "$lapCount")
            TasbihStatItem(label = "Total Dhikr", value = "$totalCount")

            // Reset Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    currentCount = 0
                    lapCount = 0
                }
            ) {
                Text(text = "↺", color = CoralAlert, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "Reset", color = TextMuted, fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Settings (Vibrate & Sound toggles)
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilterChip(
                selected = isVibrateEnabled,
                onClick = { isVibrateEnabled = !isVibrateEnabled },
                label = { Text("Vibration") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = IslamicGreenLight,
                    selectedLabelColor = MakkahGold
                )
            )
            FilterChip(
                selected = isSoundEnabled,
                onClick = { isSoundEnabled = !isSoundEnabled },
                label = { Text("Audio Click") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = IslamicGreenLight,
                    selectedLabelColor = MakkahGold
                )
            )
        }
    }
}

@Composable
private fun TasbihStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = TextMuted, fontSize = 11.sp)
        Text(text = value, color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
    }
}

private fun triggerVibration(context: Context, millis: Long) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator?.vibrate(
                VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            vibrator?.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            vibrator?.vibrate(millis)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun playClickSound() {
    try {
        val toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 40)
        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 30)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
