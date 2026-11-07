package com.azan.prayertimes.data

import android.content.Context
import android.content.SharedPreferences

data class QazaState(
    val fajr: Int = 0,
    val dhuhr: Int = 0,
    val asr: Int = 0,
    val maghrib: Int = 0,
    val isha: Int = 0,
    val witr: Int = 0
) {
    val total: Int get() = fajr + dhuhr + asr + maghrib + isha + witr
}

class QazaTracker(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("azan_qaza_prefs", Context.MODE_PRIVATE)

    fun getQazaState(): QazaState {
        return QazaState(
            fajr = prefs.getInt("qaza_fajr", 0),
            dhuhr = prefs.getInt("qaza_dhuhr", 0),
            asr = prefs.getInt("qaza_asr", 0),
            maghrib = prefs.getInt("qaza_maghrib", 0),
            isha = prefs.getInt("qaza_isha", 0),
            witr = prefs.getInt("qaza_witr", 0)
        )
    }

    fun updateCount(prayer: String, delta: Int): QazaState {
        val current = getQazaState()
        val editor = prefs.edit()
        when (prayer.lowercase()) {
            "fajr" -> editor.putInt("qaza_fajr", (current.fajr + delta).coerceAtLeast(0))
            "dhuhr" -> editor.putInt("qaza_dhuhr", (current.dhuhr + delta).coerceAtLeast(0))
            "asr" -> editor.putInt("qaza_asr", (current.asr + delta).coerceAtLeast(0))
            "maghrib" -> editor.putInt("qaza_maghrib", (current.maghrib + delta).coerceAtLeast(0))
            "isha" -> editor.putInt("qaza_isha", (current.isha + delta).coerceAtLeast(0))
            "witr" -> editor.putInt("qaza_witr", (current.witr + delta).coerceAtLeast(0))
        }
        editor.apply()
        return getQazaState()
    }

    fun resetAll(): QazaState {
        prefs.edit().clear().apply()
        return QazaState()
    }
}
