package com.azan.prayertimes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azan.prayertimes.data.AzanMode
import com.azan.prayertimes.data.CityData
import com.azan.prayertimes.data.PrayerRepository
import com.azan.prayertimes.ui.components.*
import com.azan.prayertimes.ui.theme.*

@Composable
fun HomeScreen(
    repository: PrayerRepository,
    modifier: Modifier = Modifier
) {
    val state by repository.state.collectAsState()
    var showCityDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBackground),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Islamic Bismillah & Two-Line Date Header
        item {
            IslamicHeader(
                hijriDate = state.hijriDate,
                gregorianDate = state.gregorianDate,
                city = state.city,
                onCityClick = { showCityDialog = true }
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Next Prayer Countdown Card (Positive vibe, no '-' sign)
                CountdownTimerCard(
                    nextPrayerName = state.nextPrayerName,
                    nextPrayerTime = state.nextPrayerTime,
                    initialRemainingMillis = state.timeRemainingMillis,
                    progressPercent = state.progressPercent
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Today's 5 Obligatory Prayer Schedule with direct ON/OFF switches
                PrayerScheduleList(
                    state = state,
                    onToggleAzan = { prayerName, isEnabled ->
                        val mode = if (isEnabled) AzanMode.AZAN_AUDIO else AzanMode.OFF
                        repository.setPrayerAzanMode(prayerName, mode)
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Additional Islamic Timings Table directly below the prayer schedule
                AdditionalTimingsCard(state = state)
            }
        }
    }

    // City Selection Modal Dialog (Bahrain at top + Search)
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
            title = {
                Text(
                    text = "Select City (Ahle Sunnah)",
                    color = MakkahGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
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
                                    .padding(vertical = 10.dp, horizontal = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = city.name,
                                        color = if (isSelected) MakkahGold else TextPrimary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "${city.country} • UTC +${city.timeZoneOffset}",
                                        color = TextMuted,
                                        fontSize = 12.sp
                                    )
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
                TextButton(onClick = { showCityDialog = false }) {
                    Text("Close", color = MakkahGold)
                }
            },
            containerColor = SurfaceCardElevated
        )
    }
}
