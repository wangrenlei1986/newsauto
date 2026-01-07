package com.airss.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TtsManager(context: Context, private val onReady: (() -> Unit)? = null) :
    TextToSpeech.OnInitListener {

    private var tts: TextToSpeech = TextToSpeech(context, this)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.SIMPLIFIED_CHINESE
            tts.setSpeechRate(1.0f)
            tts.setPitch(1.0f)
            onReady?.invoke()
        }
    }

    fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, System.currentTimeMillis().toString())
    }

    fun stop() { tts.stop() }
    fun release() { tts.shutdown() }
}
