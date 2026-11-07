package com.azan.prayertimes.ui.screens

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.azan.prayertimes.data.City
import com.azan.prayertimes.data.CityData
import com.azan.prayertimes.data.PrayerRepository
import com.azan.prayertimes.sensors.CompassSensorManager
import com.azan.prayertimes.ui.theme.*

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun QiblaScreen(
    repository: PrayerRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prayerState by repository.state.collectAsState()

    val compassManager = remember { CompassSensorManager(context) }
    val compassState by compassManager.compassState.collectAsState()

    var showCityDialog by remember { mutableStateOf(false) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var isPageLoaded by remember { mutableStateOf(false) }

    // Start/Stop native hardware compass sensors
    DisposableEffect(Unit) {
        compassManager.setLocation(prayerState.city.latitude, prayerState.city.longitude)
        compassManager.start()
        onDispose {
            compassManager.stop()
        }
    }

    // Synchronize native sensor azimuth with WebView
    LaunchedEffect(compassState.azimuthDegrees, isPageLoaded) {
        if (isPageLoaded) {
            webViewRef?.evaluateJavascript(
                "window.setHeading(${compassState.azimuthDegrees});",
                null
            )
        }
    }

    // Synchronize city coordinates dynamically whenever city changes
    LaunchedEffect(prayerState.city, isPageLoaded) {
        compassManager.setLocation(prayerState.city.latitude, prayerState.city.longitude)
        if (isPageLoaded) {
            val city = prayerState.city
            val label = "${city.name}, ${city.country}"
            webViewRef?.evaluateJavascript(
                "window.setLocation(${city.latitude}, ${city.longitude}, '$label');",
                null
            )
        }
    }

    // Formatted GPS coordinates: e.g. 26°13'42" N, 50°35'10" E
    val formattedCoords = remember(prayerState.city) {
        val lat = prayerState.city.latitude
        val lng = prayerState.city.longitude
        val latDeg = lat.toInt()
        val latMin = ((lat - latDeg) * 60).toInt()
        val latSec = ((((lat - latDeg) * 60) - latMin) * 60).toInt()
        val latDir = if (lat >= 0) "N" else "S"

        val lngDeg = lng.toInt()
        val lngMin = ((lng - lngDeg) * 60).toInt()
        val lngSec = ((((lng - lngDeg) * 60) - lngMin) * 60).toInt()
        val lngDir = if (lng >= 0) "E" else "W"

        "$latDeg°$latMin'$latSec\" $latDir, $lngDeg°$lngMin'$lngSec\" $lngDir"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Top Dynamic Location Bar (Tap to select any city!)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceCardElevated)
                .border(1.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
                .clickable { showCityDialog = true }
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "📍",
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${prayerState.city.name}, ${prayerState.city.country}",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = formattedCoords,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                // Change pill button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(MakkahGold.copy(alpha = 0.15f))
                        .border(1.dp, MakkahGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Change",
                        color = MakkahGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. High-Precision Interactive Qibla Finder (SVG + Compass Dial + Live Guidance)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
        ) {
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setBackgroundColor(0x00000000) // Transparent background
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            allowFileAccess = true
                            allowContentAccess = true
                            databaseEnabled = true
                            setSupportZoom(false)
                            builtInZoomControls = false
                            displayZoomControls = false
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onGeolocationPermissionsShowPrompt(
                                origin: String?,
                                callback: GeolocationPermissions.Callback?
                            ) {
                                callback?.invoke(origin, true, false)
                            }
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isPageLoaded = true
                                val city = prayerState.city
                                val label = "${city.name}, ${city.country}"
                                view?.evaluateJavascript(
                                    "window.setLocation(${city.latitude}, ${city.longitude}, '$label');",
                                    null
                                )
                                view?.evaluateJavascript(
                                    "window.setHeading(${compassState.azimuthDegrees});",
                                    null
                                )
                            }
                        }

                        loadUrl("file:///android_asset/qibla.html")
                        webViewRef = this
                    }
                },
                update = { webView ->
                    webViewRef = webView
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    // Dynamic City Selection Dialog
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
                    text = "Select Location for Qibla",
                    color = MakkahGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                "Search city (Bahrain, London, Karachi)...",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        },
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
                            val isSelected = city.name == prayerState.city.name
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        repository.setCity(city)
                                        showCityDialog = false
                                    }
                                    .padding(vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = city.name,
                                        color = if (isSelected) MakkahGold else TextPrimary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${city.country} • Lat: ${city.latitude}, Lon: ${city.longitude}",
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                                if (isSelected) {
                                    Text(
                                        text = "✓ Active",
                                        color = EmeraldAccent,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            HorizontalDivider(
                                color = SurfaceCardBorder.copy(alpha = 0.5f),
                                thickness = 0.5.dp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) {
                    Text("Close", color = MakkahGold, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = SurfaceCardElevated
        )
    }
}
