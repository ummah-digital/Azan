package com.azan.prayertimes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azan.prayertimes.data.DuaItem
import com.azan.prayertimes.data.IslamicData
import com.azan.prayertimes.data.Kalima
import com.azan.prayertimes.ui.theme.*

@Composable
fun DuasAndKalimaScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("6 Kalimas", "Essential Du'as")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Kalimas & Daily Du'as",
            color = MakkahGold,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Six Kalimas of Islam and daily supplications",
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
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTab == 0) {
            KalimaListView()
        } else {
            DuaListView()
        }
    }
}

@Composable
private fun KalimaListView() {
    val kalimas = IslamicData.KALIMAS

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(kalimas) { kalima ->
            KalimaCard(kalima)
        }
    }
}

@Composable
private fun KalimaCard(kalima: Kalima) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = kalima.title,
            color = MakkahGold,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Arabic Text
        Text(
            text = kalima.arabic,
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Transliteration
        Text(
            text = kalima.transliteration,
            color = MakkahGoldLight,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        // English Translation
        Text(
            text = kalima.englishMeaning,
            color = TextSecondary,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun DuaListView() {
    val duas = IslamicData.DUAS

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(duas) { dua ->
            DuaCard(dua)
        }
    }
}

@Composable
private fun DuaCard(dua: DuaItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = dua.title,
            color = MakkahGold,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = dua.occasion,
            color = TextMuted,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Arabic
        Text(
            text = dua.arabic,
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 30.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Transliteration
        Text(
            text = dua.transliteration,
            color = MakkahGoldLight,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Translation
        Text(
            text = dua.translation,
            color = TextSecondary,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}
