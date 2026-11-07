package com.azan.prayertimes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azan.prayertimes.data.CityData
import com.azan.prayertimes.data.PrayerRepository
import com.azan.prayertimes.data.PrayerTimesCalculator
import com.azan.prayertimes.ui.components.AzanAudioPreviewCard
import com.azan.prayertimes.ui.theme.*

@Composable
fun SettingsScreen(
    repository: PrayerRepository,
    modifier: Modifier = Modifier
) {
    val state by repository.state.collectAsState()

    var showMethodDialog by remember { mutableStateOf(false) }
    var showAsrDialog by remember { mutableStateOf(false) }
    var showCityDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            Text(
                text = "Settings & Configuration",
                color = MakkahGold,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Location, Ahle Sunnah calculation methods & Azan audio",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        // Section 1: Attached Azan Audio (Moved from Home as requested)
        item {
            SectionHeader("ATTACHED AZAN AUDIO FILE")
        }

        item {
            AzanAudioPreviewCard()
        }

        // Section 2: Location & Calculation
        item {
            SectionHeader("LOCATION & SUNNI CALCULATION")
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceCard)
                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Location / City
                SettingClickableRow(
                    title = "Selected City",
                    subtitle = "${state.city.name}, ${state.city.country} (UTC +${state.city.timeZoneOffset})",
                    onClick = { showCityDialog = true }
                )

                HorizontalDivider(color = DividerColor)

                // Calculation Method
                SettingClickableRow(
                    title = "Calculation Authority (Ahle Sunnah)",
                    subtitle = state.calculationMethod.displayName,
                    onClick = { showMethodDialog = true }
                )

                HorizontalDivider(color = DividerColor)

                // Asr Method
                SettingClickableRow(
                    title = "Asr Juristic Method",
                    subtitle = state.asrJuristicMethod.displayName,
                    onClick = { showAsrDialog = true }
                )
            }
        }

        // Section 3: About & Version Tracking (Key on Line 1, Value on Line 2!)
        item {
            SectionHeader("ABOUT APPLICATION & MODULES")
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(IslamicGreenContainer)
                    .border(1.dp, MakkahGoldDark, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Azan App",
                        color = MakkahGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "v1.5.0 Release",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                HorizontalDivider(color = MakkahGoldDark.copy(alpha = 0.3f), thickness = 1.dp)

                // Key on Line 1, Value on Line 2
                AboutDetailItem("Theology & Methodology", "Ahle Sunnah wal Jama'ah (Sunni)")
                AboutDetailItem("Active Modules", "1. Prayers & 5x Azan • 2. Qibla Compass • 3. Settings & About")
                AboutDetailItem("Active Location", "${state.city.name}, ${state.city.country} (UTC +${state.city.timeZoneOffset})")
                AboutDetailItem("Calculation Standard", state.calculationMethod.displayName)
                AboutDetailItem("Asr Juristic Rule", state.asrJuristicMethod.displayName)
                AboutDetailItem("Azan Audio Source", "Attached Custom MPEG/MP3 Audio (Bundled in APK)")
                AboutDetailItem("Target Architecture", "Android 7.0 - Android 15 (SDK 36)")
            }
        }
    }

    // Calculation Method Dialog
    if (showMethodDialog) {
        AlertDialog(
            onDismissRequest = { showMethodDialog = false },
            title = { Text("Select Calculation Authority", color = MakkahGold, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    PrayerTimesCalculator.CalculationMethod.values().forEach { method ->
                        val isSelected = method == state.calculationMethod
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    repository.setCalculationMethod(method)
                                    showMethodDialog = false
                                }
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = method.displayName,
                                color = if (isSelected) MakkahGold else TextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                            if (isSelected) {
                                Text("✓", color = MakkahGold, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMethodDialog = false }) { Text("Close", color = MakkahGold) }
            },
            containerColor = SurfaceCardElevated
        )
    }

    // Asr Method Dialog
    if (showAsrDialog) {
        AlertDialog(
            onDismissRequest = { showAsrDialog = false },
            title = { Text("Select Asr Juristic Method", color = MakkahGold, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    PrayerTimesCalculator.AsrJuristicMethod.values().forEach { method ->
                        val isSelected = method == state.asrJuristicMethod
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    repository.setAsrJuristicMethod(method)
                                    showAsrDialog = false
                                }
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = method.displayName,
                                color = if (isSelected) MakkahGold else TextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                            if (isSelected) {
                                Text("✓", color = MakkahGold, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAsrDialog = false }) { Text("Close", color = MakkahGold) }
            },
            containerColor = SurfaceCardElevated
        )
    }

    // City Selection Dialog (Bahrain at top + Search)
    if (showCityDialog) {
        var searchQuery by remember { mutableStateOf("") }
        val filteredCities = remember(searchQuery) {
            if (searchQuery.isBlank()) {
                CityData.CITIES
            } else {
                CityData.CITIES.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                    it.country.contains(searchQuery, ignoreCase = true)
                }
            }
        }

        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = { Text("Select City (Ahle Sunnah)", color = MakkahGold, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search city (e.g. Bahrain, Manama, Karachi)...", color = TextMuted, fontSize = 12.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MakkahGold,
                            unfocusedBorderColor = SurfaceCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    LazyColumn(modifier = Modifier.heightIn(max = 350.dp)) {
                        items(filteredCities) { city ->
                            val isSelected = city.name == state.city.name
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        repository.setCity(city)
                                        showCityDialog = false
                                    }
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = city.name,
                                        color = if (isSelected) MakkahGold else TextPrimary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                    Text(text = "${city.country} • UTC +${city.timeZoneOffset}", color = TextMuted, fontSize = 11.sp)
                                }
                                if (isSelected) {
                                    Text("✓ Selected", color = MakkahGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) { Text("Close", color = MakkahGold) }
            },
            containerColor = SurfaceCardElevated
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = MakkahGold,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 4.dp, top = 6.dp)
    )
}

@Composable
private fun SettingClickableRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 12.sp
            )
        }
        Text(text = "›", color = TextMuted, fontSize = 18.sp)
    }
}

// Key on Line 1, Value on Line 2
@Composable
private fun AboutDetailItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
            text = value,
            color = MakkahGoldLight,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
