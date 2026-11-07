package com.azan.prayertimes.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

enum class AzanMode(val displayName: String) {
    AZAN_AUDIO("Full Azan Audio"),
    BEEP("Takbeer / Beep"),
    SILENT("Silent Alert"),
    OFF("Off")
}

data class PrayerTimeItem(
    val name: String,
    val arabicName: String,
    val time: String,
    val isNext: Boolean = false,
    val isCurrent: Boolean = false,
    val azanMode: AzanMode = AzanMode.AZAN_AUDIO,
    val rawTimestamp: Long = 0L
)

data class PrayerTimesState(
    val city: City = CityData.DEFAULT_CITY,
    val hijriDate: String = "",
    val gregorianDate: String = "",
    val nextPrayerName: String = "",
    val nextPrayerTime: String = "",
    val timeRemainingMillis: Long = 0L,
    val progressPercent: Float = 0f,
    val prayerItems: List<PrayerTimeItem> = emptyList(),
    val calculationMethod: PrayerTimesCalculator.CalculationMethod = PrayerTimesCalculator.CalculationMethod.KARACHI,
    val asrJuristicMethod: PrayerTimesCalculator.AsrJuristicMethod = PrayerTimesCalculator.AsrJuristicMethod.HANAFI,
    val fajrMode: AzanMode = AzanMode.AZAN_AUDIO,
    val dhuhrMode: AzanMode = AzanMode.AZAN_AUDIO,
    val asrMode: AzanMode = AzanMode.AZAN_AUDIO,
    val maghribMode: AzanMode = AzanMode.AZAN_AUDIO,
    val ishaMode: AzanMode = AzanMode.AZAN_AUDIO,
    val sunriseTime: String = "",
    val ishraqTime: String = "",
    val chashtTime: String = "",
    val tahajjudTime: String = "",
    val suhoorEndTime: String = ""
)

class PrayerRepository(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("azan_settings_prefs", Context.MODE_PRIVATE)
    private val calculator = PrayerTimesCalculator()

    private val _state = MutableStateFlow(loadInitialState())
    val state: StateFlow<PrayerTimesState> = _state.asStateFlow()

    private fun loadInitialState(): PrayerTimesState {
        val cityName = prefs.getString("selected_city_name", CityData.DEFAULT_CITY.name) ?: CityData.DEFAULT_CITY.name
        val city = CityData.CITIES.find { it.name == cityName } ?: CityData.DEFAULT_CITY

        val calcMethodStr = prefs.getString("calc_method", PrayerTimesCalculator.CalculationMethod.KARACHI.name)
        val calcMethod = try {
            PrayerTimesCalculator.CalculationMethod.valueOf(calcMethodStr ?: PrayerTimesCalculator.CalculationMethod.KARACHI.name)
        } catch (e: Exception) {
            PrayerTimesCalculator.CalculationMethod.KARACHI
        }

        val asrMethodStr = prefs.getString("asr_method", PrayerTimesCalculator.AsrJuristicMethod.HANAFI.name)
        val asrMethod = try {
            PrayerTimesCalculator.AsrJuristicMethod.valueOf(asrMethodStr ?: PrayerTimesCalculator.AsrJuristicMethod.HANAFI.name)
        } catch (e: Exception) {
            PrayerTimesCalculator.AsrJuristicMethod.HANAFI
        }

        val fajrMode = AzanMode.valueOf(prefs.getString("mode_fajr", AzanMode.AZAN_AUDIO.name) ?: AzanMode.AZAN_AUDIO.name)
        val dhuhrMode = AzanMode.valueOf(prefs.getString("mode_dhuhr", AzanMode.AZAN_AUDIO.name) ?: AzanMode.AZAN_AUDIO.name)
        val asrMode = AzanMode.valueOf(prefs.getString("mode_asr", AzanMode.AZAN_AUDIO.name) ?: AzanMode.AZAN_AUDIO.name)
        val maghribMode = AzanMode.valueOf(prefs.getString("mode_maghrib", AzanMode.AZAN_AUDIO.name) ?: AzanMode.AZAN_AUDIO.name)
        val ishaMode = AzanMode.valueOf(prefs.getString("mode_isha", AzanMode.AZAN_AUDIO.name) ?: AzanMode.AZAN_AUDIO.name)

        return computeState(city, calcMethod, asrMethod, fajrMode, dhuhrMode, asrMode, maghribMode, ishaMode)
    }

    fun refreshTimes() {
        val current = _state.value
        _state.value = computeState(
            current.city,
            current.calculationMethod,
            current.asrJuristicMethod,
            current.fajrMode,
            current.dhuhrMode,
            current.asrMode,
            current.maghribMode,
            current.ishaMode
        )
    }

    fun setCity(city: City) {
        prefs.edit().putString("selected_city_name", city.name).apply()
        val cur = _state.value
        val autoMethod = if (city.country.equals("Bahrain", ignoreCase = true) ||
            city.country.equals("Saudi Arabia", ignoreCase = true) ||
            city.country.equals("UAE", ignoreCase = true) ||
            city.country.equals("Qatar", ignoreCase = true) ||
            city.country.equals("Kuwait", ignoreCase = true)
        ) {
            PrayerTimesCalculator.CalculationMethod.UMM_AL_QURA
        } else {
            cur.calculationMethod
        }
        _state.value = computeState(city, autoMethod, cur.asrJuristicMethod, cur.fajrMode, cur.dhuhrMode, cur.asrMode, cur.maghribMode, cur.ishaMode)
    }

    fun setCalculationMethod(method: PrayerTimesCalculator.CalculationMethod) {
        prefs.edit().putString("calc_method", method.name).apply()
        val cur = _state.value
        _state.value = computeState(cur.city, method, cur.asrJuristicMethod, cur.fajrMode, cur.dhuhrMode, cur.asrMode, cur.maghribMode, cur.ishaMode)
    }

    fun setAsrJuristicMethod(method: PrayerTimesCalculator.AsrJuristicMethod) {
        prefs.edit().putString("asr_method", method.name).apply()
        val cur = _state.value
        _state.value = computeState(cur.city, cur.calculationMethod, method, cur.fajrMode, cur.dhuhrMode, cur.asrMode, cur.maghribMode, cur.ishaMode)
    }

    fun setPrayerAzanMode(prayerName: String, mode: AzanMode) {
        val editor = prefs.edit()
        var fajr = _state.value.fajrMode
        var dhuhr = _state.value.dhuhrMode
        var asr = _state.value.asrMode
        var maghrib = _state.value.maghribMode
        var isha = _state.value.ishaMode

        when (prayerName.lowercase()) {
            "fajr" -> { fajr = mode; editor.putString("mode_fajr", mode.name) }
            "dhuhr" -> { dhuhr = mode; editor.putString("mode_dhuhr", mode.name) }
            "asr" -> { asr = mode; editor.putString("mode_asr", mode.name) }
            "maghrib" -> { maghrib = mode; editor.putString("mode_maghrib", mode.name) }
            "isha" -> { isha = mode; editor.putString("mode_isha", mode.name) }
        }
        editor.apply()
        val cur = _state.value
        _state.value = computeState(cur.city, cur.calculationMethod, cur.asrJuristicMethod, fajr, dhuhr, asr, maghrib, isha)
    }

    private fun computeState(
        city: City,
        calcMethod: PrayerTimesCalculator.CalculationMethod,
        asrMethod: PrayerTimesCalculator.AsrJuristicMethod,
        fajrMode: AzanMode,
        dhuhrMode: AzanMode,
        asrMode: AzanMode,
        maghribMode: AzanMode,
        ishaMode: AzanMode
    ): PrayerTimesState {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH) + 1
        val d = cal.get(Calendar.DAY_OF_MONTH)

        val schedule = calculator.calculateTimes(
            year = y,
            month = m,
            day = d,
            latitude = city.latitude,
            longitude = city.longitude,
            timeZoneOffsetHours = city.timeZoneOffset,
            method = calcMethod,
            asrMethod = asrMethod
        )

        val nowMillis = System.currentTimeMillis()

        // 5 obligatory prayers
        val prayers = listOf(
            PrayerTimeItem("Fajr", "الفجر", schedule.fajr, azanMode = fajrMode, rawTimestamp = schedule.rawFajrMillis),
            PrayerTimeItem("Dhuhr", "الظهر", schedule.dhuhr, azanMode = dhuhrMode, rawTimestamp = schedule.rawDhuhrMillis),
            PrayerTimeItem("Asr", "العصر", schedule.asr, azanMode = asrMode, rawTimestamp = schedule.rawAsrMillis),
            PrayerTimeItem("Maghrib", "المغرب", schedule.maghrib, azanMode = maghribMode, rawTimestamp = schedule.rawMaghribMillis),
            PrayerTimeItem("Isha", "العشاء", schedule.isha, azanMode = ishaMode, rawTimestamp = schedule.rawIshaMillis)
        )

        // Find next prayer and current prayer
        var nextPrayerIndex = prayers.indexOfFirst { it.rawTimestamp > nowMillis }
        val isTomorrowFajr = nextPrayerIndex == -1
        if (isTomorrowFajr) {
            nextPrayerIndex = 0
        }

        val nextItem = prayers[nextPrayerIndex]
        val targetMillis = if (isTomorrowFajr) nextItem.rawTimestamp + 24 * 3600 * 1000L else nextItem.rawTimestamp
        val timeRemaining = (targetMillis - nowMillis).coerceAtLeast(0L)

        // Previous prayer for progress
        val prevIndex = if (nextPrayerIndex == 0) prayers.lastIndex else nextPrayerIndex - 1
        val prevMillis = prayers[prevIndex].rawTimestamp
        val totalInterval = (targetMillis - prevMillis).coerceAtLeast(1L)
        val elapsed = (nowMillis - prevMillis).coerceIn(0L, totalInterval)
        val progressPercent = (elapsed.toFloat() / totalInterval.toFloat()).coerceIn(0f, 1f)

        val updatedItems = prayers.mapIndexed { idx, item ->
            item.copy(
                isNext = idx == nextPrayerIndex,
                isCurrent = idx == prevIndex && !isTomorrowFajr
            )
        }

        val gregorianMonths = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val daysOfWeek = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val gregorianDate = "${daysOfWeek[cal.get(Calendar.DAY_OF_WEEK) - 1]}, ${cal.get(Calendar.DAY_OF_MONTH)} ${gregorianMonths[cal.get(Calendar.MONTH)]} $y"
        val hijriDate = calculator.getHijriDate(cal)

        return PrayerTimesState(
            city = city,
            hijriDate = hijriDate,
            gregorianDate = gregorianDate,
            nextPrayerName = nextItem.name,
            nextPrayerTime = nextItem.time,
            timeRemainingMillis = timeRemaining,
            progressPercent = progressPercent,
            prayerItems = updatedItems,
            calculationMethod = calcMethod,
            asrJuristicMethod = asrMethod,
            fajrMode = fajrMode,
            dhuhrMode = dhuhrMode,
            asrMode = asrMode,
            maghribMode = maghribMode,
            ishaMode = ishaMode,
            sunriseTime = schedule.sunrise,
            ishraqTime = schedule.ishraq,
            chashtTime = schedule.chasht,
            tahajjudTime = schedule.tahajjud,
            suhoorEndTime = schedule.suhoorEnd
        )
    }
}
