package com.azan.prayertimes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azan.prayertimes.data.IslamicData
import com.azan.prayertimes.data.NamazStep
import com.azan.prayertimes.data.QazaTracker
import com.azan.prayertimes.data.QazaState
import com.azan.prayertimes.ui.theme.*

@Composable
fun NamazGuideScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val qazaTracker = remember { QazaTracker(context) }
    var qazaState by remember { mutableStateOf(qazaTracker.getQazaState()) }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Step-by-Step", "Rakat Table", "Qaza Counter")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Namaz & Salah Guide",
            color = MakkahGold,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Learn proper Salah, Rakat counts & track Qaza prayers",
            color = TextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Tabs
        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = SurfaceCard,
            contentColor = MakkahGold
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTab == index) MakkahGold else TextMuted,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (selectedTab) {
            0 -> StepByStepList()
            1 -> RakatTableView()
            2 -> QazaCounterView(
                qazaState = qazaState,
                onUpdate = { prayer, delta ->
                    qazaState = qazaTracker.updateCount(prayer, delta)
                },
                onReset = {
                    qazaState = qazaTracker.resetAll()
                }
            )
        }
    }
}

@Composable
private fun StepByStepList() {
    val steps = IslamicData.NAMAZ_STEPS

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(steps) { step ->
            NamazStepCard(step)
        }
    }
}

@Composable
private fun NamazStepCard(step: NamazStep) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(IslamicGreenLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${step.stepNumber}",
                    color = MakkahGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = step.stepName,
                color = MakkahGoldLight,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = step.arabicRecitation,
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = step.transliteration,
            color = MakkahGold,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = step.explanation,
            color = TextSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
        )
    }
}

@Composable
private fun RakatTableView() {
    val rakats = IslamicData.PRAYER_RAKATS

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(rakats) { item ->
            Column(
                modifier = Modifier
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
                        text = item.prayerName,
                        color = MakkahGold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Total: ${item.total} Rakats",
                        color = MakkahGoldLight,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (item.sunnatMuakkadaBefore > 0) {
                        RakatChip("Sunnat", item.sunnatMuakkadaBefore)
                    }
                    RakatChip("Farz", item.farz)
                    if (item.sunnatMuakkadaAfter > 0) {
                        RakatChip("Sunnat", item.sunnatMuakkadaAfter)
                    }
                    if (item.witr > 0) {
                        RakatChip("Witr", item.witr)
                    }
                    if (item.nafl > 0) {
                        RakatChip("Nafl", item.nafl)
                    }
                }
            }
        }
    }
}

@Composable
private fun RakatChip(label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = TextMuted, fontSize = 10.sp)
        Text(text = "$count", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun QazaCounterView(
    qazaState: QazaState,
    onUpdate: (String, Int) -> Unit,
    onReset: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            // Total missed card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(IslamicGreenContainer)
                    .border(1.dp, MakkahGold, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Qaza Prayers",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Log prayers as you complete them",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
                Text(
                    text = "${qazaState.total}",
                    color = MakkahGoldLight,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        item {
            QazaRow("Fajr", qazaState.fajr, { onUpdate("fajr", 1) }, { onUpdate("fajr", -1) })
        }
        item {
            QazaRow("Dhuhr", qazaState.dhuhr, { onUpdate("dhuhr", 1) }, { onUpdate("dhuhr", -1) })
        }
        item {
            QazaRow("Asr", qazaState.asr, { onUpdate("asr", 1) }, { onUpdate("asr", -1) })
        }
        item {
            QazaRow("Maghrib", qazaState.maghrib, { onUpdate("maghrib", 1) }, { onUpdate("maghrib", -1) })
        }
        item {
            QazaRow("Isha", qazaState.isha, { onUpdate("isha", 1) }, { onUpdate("isha", -1) })
        }
        item {
            QazaRow("Witr", qazaState.witr, { onUpdate("witr", 1) }, { onUpdate("witr", -1) })
        }
    }
}

@Composable
private fun QazaRow(
    prayer: String,
    count: Int,
    onAdd: () -> Unit,
    onSub: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = prayer,
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Minus Button
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(SurfaceCardElevated)
                    .border(1.dp, SurfaceCardBorder, CircleShape)
                    .clickable { onSub() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "−", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "$count",
                color = MakkahGold,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Plus Button
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(IslamicGreenLight)
                    .border(1.dp, MakkahGold, CircleShape)
                    .clickable { onAdd() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "+", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
