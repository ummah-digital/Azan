package com.azan.prayertimes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azan.prayertimes.R
import com.azan.prayertimes.data.IslamicData
import com.azan.prayertimes.service.AzanAudioManager
import com.azan.prayertimes.ui.theme.*

@Composable
fun AzanAudioPreviewCard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isPlaying by AzanAudioManager.isPlaying.collectAsState()
    var showDuaDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceCard)
            .border(1.dp, if (isPlaying) MakkahGold else SurfaceCardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Play/Stop circular button
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (isPlaying) CoralAlert else IslamicGreenLight)
                        .border(1.5.dp, MakkahGold, CircleShape)
                        .clickable {
                            AzanAudioManager.togglePlay(context, "Attached Azan")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isPlaying) android.R.drawable.ic_media_pause else R.drawable.ic_azan_speaker
                        ),
                        contentDescription = "Play/Stop Azan",
                        tint = if (isPlaying) TextPrimary else MakkahGold,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = if (isPlaying) "Playing Attached Azan Audio" else "Play Attached Azan Audio",
                        color = if (isPlaying) MakkahGold else TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isPlaying) "Tap button to stop audio" else "Tap play to test sound",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            // Du'a button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(IslamicGreenContainer)
                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
                    .clickable { showDuaDialog = true }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Azan Du'a",
                    color = MakkahGoldLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    // Azan Du'a Dialog
    if (showDuaDialog) {
        val azanDua = IslamicData.DUAS[0] // Du'a After Azan
        AlertDialog(
            onDismissRequest = { showDuaDialog = false },
            confirmButton = {
                TextButton(onClick = { showDuaDialog = false }) {
                    Text("Close", color = MakkahGold)
                }
            },
            title = {
                Text(
                    text = azanDua.title,
                    color = MakkahGold,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = azanDua.arabic,
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = azanDua.transliteration,
                        color = MakkahGoldLight,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = azanDua.translation,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            },
            containerColor = SurfaceCardElevated
        )
    }
}
