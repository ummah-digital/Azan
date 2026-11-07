package com.azan.prayertimes.data

import java.util.Calendar
import java.util.TimeZone
import kotlin.math.*

/**
 * Astronomical Prayer Times Calculator.
 * Implements high-precision solar algorithms with support for:
 * - University of Islamic Sciences, Karachi (Dawat-e-Islami / South Asia standard)
 * - Muslim World League (MWL)
 * - Islamic Society of North America (ISNA)
 * - Umm al-Qura University, Makkah
 * - Egyptian General Authority of Survey
 *
 * Supports Hanafi and Shafi'i juristic calculation for Asr.
 */
class PrayerTimesCalculator {

    enum class CalculationMethod(
        val displayName: String,
        val fajrAngle: Double,
        val ishaAngle: Double,
        val isIshaInterval: Boolean = false,
        val ishaMinutesAfterMaghrib: Int = 0
    ) {
        UMM_AL_QURA("Umm al-Qura (Bahrain & GCC Sunni Standard)", 18.5, 0.0, true, 90),
        KARACHI("Karachi (Univ. of Islamic Sciences / Dawat-e-Islami)", 18.0, 18.0),
        MWL("Muslim World League (MWL - Sunni Standard)", 18.0, 17.0),
        EGYPT("Egyptian General Authority", 19.5, 17.5),
        ISNA("Islamic Society of North America (ISNA)", 15.0, 15.0)
    }

    enum class AsrJuristicMethod(val displayName: String, val shadowFactor: Double) {
        HANAFI("Hanafi (Standard in Dawat-e-Islami / South Asia)", 2.0),
        SHAFI("Shafi'i, Hanbali, Maliki (Standard in Bahrain / GCC)", 1.0)
    }

    data class PrayerSchedule(
        val fajr: String,
        val sunrise: String,
        val ishraq: String,
        val chasht: String,
        val dhuhr: String,
        val asr: String,
        val sunset: String,
        val maghrib: String,
        val isha: String,
        val tahajjud: String,
        val suhoorEnd: String,
        val rawFajrMillis: Long,
        val rawDhuhrMillis: Long,
        val rawAsrMillis: Long,
        val rawMaghribMillis: Long,
        val rawIshaMillis: Long,
        val rawSunriseMillis: Long
    )

    fun calculateTimes(
        year: Int,
        month: Int, // 1-12
        day: Int,   // 1-31
        latitude: Double,
        longitude: Double,
        timeZoneOffsetHours: Double,
        method: CalculationMethod = CalculationMethod.KARACHI,
        asrMethod: AsrJuristicMethod = AsrJuristicMethod.HANAFI
    ): PrayerSchedule {
        val julianDate = julianDate(year, month, day) - longitude / (15.0 * 24.0)

        // Solar parameters
        val sunMeanAnomaly = fixAngle(357.529 + 0.98560028 * (julianDate - 2451545.0))
        val sunMeanLong = fixAngle(280.459 + 0.98564736 * (julianDate - 2451545.0))
        val sunEclipticLong = fixAngle(sunMeanLong + 1.915 * sin(Math.toRadians(sunMeanAnomaly)) + 0.020 * sin(Math.toRadians(2.0 * sunMeanAnomaly)))
        val obliquity = 23.439 - 0.00000036 * (julianDate - 2451545.0)

        val sunRightAscension = Math.toDegrees(atan2(cos(Math.toRadians(obliquity)) * sin(Math.toRadians(sunEclipticLong)), cos(Math.toRadians(sunEclipticLong)))) / 15.0
        val sunDeclination = Math.toDegrees(asin(sin(Math.toRadians(obliquity)) * sin(Math.toRadians(sunEclipticLong))))

        // Equation of Time in minutes
        val equationOfTime = (sunMeanLong / 15.0 - fixHour(sunRightAscension)) * 60.0

        // Solar noon (Dhuhr)
        val solarNoon = 12.0 + timeZoneOffsetHours - longitude / 15.0 - equationOfTime / 60.0

        // Sun angles for prayer times
        val sunriseAngle = -0.8333
        val sunriseHourAngle = calculateHourAngle(latitude, sunDeclination, sunriseAngle)
        val fajrHourAngle = calculateHourAngle(latitude, sunDeclination, -method.fajrAngle)

        // Asr hour angle
        val asrAltitude = -Math.toDegrees(atan(1.0 / (asrMethod.shadowFactor + tan(Math.toRadians(abs(latitude - sunDeclination))))))
        val asrHourAngle = calculateHourAngle(latitude, sunDeclination, asrAltitude)

        val fajrHours = solarNoon - fajrHourAngle
        val sunriseHours = solarNoon - sunriseHourAngle
        val dhuhrHours = solarNoon + (2.0 / 60.0) // 2 min after noon safety buffer
        val asrHours = solarNoon + asrHourAngle
        val sunsetHours = solarNoon + sunriseHourAngle
        val maghribHours = sunsetHours + (2.0 / 60.0) // 2 min after sunset

        val ishaHours = if (method.isIshaInterval) {
            maghribHours + (method.ishaMinutesAfterMaghrib / 60.0)
        } else {
            val ishaHourAngle = calculateHourAngle(latitude, sunDeclination, -method.ishaAngle)
            solarNoon + ishaHourAngle
        }

        // Ishraq: ~15 mins after sunrise
        val ishraqHours = sunriseHours + (15.0 / 60.0)
        // Chasht (Dhuha): midway between sunrise and dhuhr
        val chashtHours = sunriseHours + (dhuhrHours - sunriseHours) / 2.0
        // Tahajjud: last third of the night (between Maghrib and Fajr)
        val nightDuration = (fajrHours + 24.0 - maghribHours) % 24.0
        val tahajjudHours = (fajrHours - nightDuration / 3.0 + 24.0) % 24.0
        // Suhoor end: 10 mins before Fajr
        val suhoorHours = fajrHours - (10.0 / 60.0)

        // Convert to Calendar timestamps
        val baseCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
        }

        return PrayerSchedule(
            fajr = formatTime(fajrHours),
            sunrise = formatTime(sunriseHours),
            ishraq = formatTime(ishraqHours),
            chasht = formatTime(chashtHours),
            dhuhr = formatTime(dhuhrHours),
            asr = formatTime(asrHours),
            sunset = formatTime(sunsetHours),
            maghrib = formatTime(maghribHours),
            isha = formatTime(ishaHours),
            tahajjud = formatTime(tahajjudHours),
            suhoorEnd = formatTime(suhoorHours),
            rawFajrMillis = hoursToMillis(baseCalendar, fajrHours),
            rawDhuhrMillis = hoursToMillis(baseCalendar, dhuhrHours),
            rawAsrMillis = hoursToMillis(baseCalendar, asrHours),
            rawMaghribMillis = hoursToMillis(baseCalendar, maghribHours),
            rawIshaMillis = hoursToMillis(baseCalendar, ishaHours),
            rawSunriseMillis = hoursToMillis(baseCalendar, sunriseHours)
        )
    }

    private fun calculateHourAngle(latitude: Double, declination: Double, angle: Double): Double {
        val cosHA = (sin(Math.toRadians(angle)) - sin(Math.toRadians(latitude)) * sin(Math.toRadians(declination))) /
                (cos(Math.toRadians(latitude)) * cos(Math.toRadians(declination)))
        val clampedCosHA = cosHA.coerceIn(-1.0, 1.0)
        return Math.toDegrees(acos(clampedCosHA)) / 15.0
    }

    private fun julianDate(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun fixAngle(angle: Double): Double {
        var a = angle % 360.0
        if (a < 0) a += 360.0
        return a
    }

    private fun fixHour(hour: Double): Double {
        var h = hour % 24.0
        if (h < 0) h += 24.0
        return h
    }

    fun formatTime(decimalHours: Double): String {
        val normalized = fixHour(decimalHours)
        val totalMinutes = (normalized * 60.0).roundToInt()
        val hours = (totalMinutes / 60) % 24
        val minutes = totalMinutes % 60
        val isPm = hours >= 12
        val displayHour = when {
            hours == 0 -> 12
            hours > 12 -> hours - 12
            else -> hours
        }
        val amPm = if (isPm) "PM" else "AM"
        return String.format("%d:%02d %s", displayHour, minutes, amPm)
    }

    private fun hoursToMillis(calendar: Calendar, decimalHours: Double): Long {
        val normalized = fixHour(decimalHours)
        val totalMinutes = (normalized * 60.0).roundToInt()
        val hours = (totalMinutes / 60) % 24
        val minutes = totalMinutes % 60
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.HOUR_OF_DAY, hours)
        cal.set(Calendar.MINUTE, minutes)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    /**
     * Approximate Islamic Hijri Date representation
     */
    fun getHijriDate(calendar: Calendar = Calendar.getInstance()): String {
        val y = calendar.get(Calendar.YEAR)
        val m = calendar.get(Calendar.MONTH) + 1
        val d = calendar.get(Calendar.DAY_OF_MONTH)
        val jd = julianDate(y, m, d).toInt()

        val l = jd - 1948440 + 10632
        val n = (l - 1) / 10631
        val lRemaining = l - 10631 * n + 354
        val j = ((10985 - lRemaining) / 5316) * ((50 * lRemaining) / 17719) +
                (lRemaining / 5670) * ((43 * lRemaining) / 15238)
        val l2 = lRemaining - ((30 - j) / 15) * ((17719 * j) / 50) -
                (j / 16) * ((15238 * j) / 43) + 29
        val hijriMonth = (24 * l2) / 709
        val hijriDay = l2 - (709 * hijriMonth) / 24
        val hijriYear = 30 * n + j - 30

        val monthNames = arrayOf(
            "Muharram", "Safar", "Rabi-ul-Awwal", "Rabi-us-Sani",
            "Jumada-ul-Awwal", "Jumada-us-Sani", "Rajab", "Sha'ban",
            "Ramadan", "Shawwal", "Dhul-Qa'dah", "Dhul-Hijjah"
        )
        val safeMonth = (hijriMonth - 1).coerceIn(0, 11)
        return "$hijriDay ${monthNames[safeMonth]} $hijriYear AH"
    }
}
