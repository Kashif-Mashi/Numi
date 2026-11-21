package com.example.animations

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

enum class SoundEffectType {
    DIGIT,
    OPERATOR,
    UTILITY,
    EQUALS
}

class NumiFeedbackManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val sampleRate = 22050
    private val digitTone: ShortArray by lazy { synthesizePop(freqHz = 640.0, durationMs = 18, amp = 0.22) }
    private val operatorTone: ShortArray by lazy { synthesizePop(freqHz = 480.0, durationMs = 22, amp = 0.25) }
    private val utilityTone: ShortArray by lazy { synthesizePop(freqHz = 330.0, durationMs = 24, amp = 0.22) }
    private val equalsTone: ShortArray by lazy { synthesizeChord(freq1 = 523.25, freq2 = 783.99, durationMs = 70, amp = 0.24) }

    /**
     * Immediate tactile mechanical press-down impulse when a keycap is depressed.
     */
    fun triggerPressDownHaptic(
        view: View?,
        hapticFeedback: HapticFeedback?,
        hapticsEnabled: Boolean
    ) {
        if (!hapticsEnabled) return
        runCatching {
            if (view != null) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            } else {
                hapticFeedback?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }
        }
    }

    /**
     * Tactile release / action confirmation haptic + synthesized audio click.
     */
    fun triggerButtonFeedback(
        view: View?,
        soundEnabled: Boolean,
        hapticsEnabled: Boolean,
        type: SoundEffectType,
        hapticFeedback: HapticFeedback? = null
    ) {
        if (hapticsEnabled) {
            runCatching {
                if (view != null) {
                    val constant = when (type) {
                        SoundEffectType.EQUALS -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                HapticFeedbackConstants.CONFIRM
                            } else {
                                HapticFeedbackConstants.LONG_PRESS
                            }
                        }
                        SoundEffectType.UTILITY -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                HapticFeedbackConstants.CONTEXT_CLICK
                            } else {
                                HapticFeedbackConstants.KEYBOARD_TAP
                            }
                        }
                        SoundEffectType.DIGIT, SoundEffectType.OPERATOR -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                                HapticFeedbackConstants.KEYBOARD_RELEASE
                            } else {
                                HapticFeedbackConstants.KEYBOARD_TAP
                            }
                        }
                    }
                    view.performHapticFeedback(constant)
                } else {
                    hapticFeedback?.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            }
        }
        if (soundEnabled) {
            val samples = when (type) {
                SoundEffectType.DIGIT -> digitTone
                SoundEffectType.OPERATOR -> operatorTone
                SoundEffectType.UTILITY -> utilityTone
                SoundEffectType.EQUALS -> equalsTone
            }
            playShortPcm(samples)
        }
    }

    private fun playShortPcm(samples: ShortArray) {
        scope.launch {
            runCatching {
                val byteCount = samples.size * 2
                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(byteCount)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(samples, 0, samples.size)
                track.play()
                kotlinx.coroutines.delay((samples.size * 1000L / sampleRate) + 40L)
                track.stop()
                track.release()
            }
        }
    }

    private fun synthesizePop(freqHz: Double, durationMs: Int, amp: Double): ShortArray {
        val count = (sampleRate * durationMs) / 1000
        return ShortArray(count) { i ->
            val t = i.toDouble() / sampleRate
            val envelope = exp(-t * 180.0) * (1.0 - exp(-t * 900.0))
            val pitchSweep = freqHz * (1.0 + 0.12 * exp(-t * 250.0))
            val sample = sin(2.0 * PI * pitchSweep * t) * envelope * amp
            (sample * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
    }

    private fun synthesizeChord(freq1: Double, freq2: Double, durationMs: Int, amp: Double): ShortArray {
        val count = (sampleRate * durationMs) / 1000
        return ShortArray(count) { i ->
            val t = i.toDouble() / sampleRate
            val envelope = exp(-t * 55.0) * (1.0 - exp(-t * 800.0))
            val wave = (sin(2.0 * PI * freq1 * t) * 0.55 + sin(2.0 * PI * freq2 * t) * 0.45)
            val sample = wave * envelope * amp
            (sample * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
    }
}
