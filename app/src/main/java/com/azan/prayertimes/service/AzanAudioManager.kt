package com.azan.prayertimes.service

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.azan.prayertimes.R

/**
 * Crash-proof in-process Audio Manager for playing the user's attached Azan audio.
 * Does not depend on Android ForegroundService restrictions or Notification permissions.
 */
object AzanAudioManager {
    private var mediaPlayer: MediaPlayer? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPrayerName = MutableStateFlow("Azan")
    val currentPrayerName: StateFlow<String> = _currentPrayerName.asStateFlow()

    fun togglePlay(context: Context, prayerName: String = "Azan") {
        if (_isPlaying.value) {
            stop()
        } else {
            play(context, prayerName)
        }
    }

    fun play(context: Context, prayerName: String = "Azan") {
        stop()
        try {
            _currentPrayerName.value = prayerName
            mediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.azan_audio)?.apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setOnCompletionListener {
                    _isPlaying.value = false
                    try {
                        it.release()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    mediaPlayer = null
                }
                setOnErrorListener { _, _, _ ->
                    _isPlaying.value = false
                    try {
                        mediaPlayer?.release()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    mediaPlayer = null
                    true
                }
                start()
                _isPlaying.value = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _isPlaying.value = false
            mediaPlayer = null
        }
    }

    fun stop() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
            _isPlaying.value = false
        }
    }
}
