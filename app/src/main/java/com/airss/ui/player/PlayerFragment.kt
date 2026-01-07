package com.airss.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airss.R
import com.airss.tts.TtsManager

class PlayerFragment : Fragment() {
    private var tts: TtsManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val text = view.findViewById<TextView>(R.id.text)
        val play = view.findViewById<Button>(R.id.play)
        tts = TtsManager(requireContext())
        play.setOnClickListener {
            tts?.speak(text.text.toString())
        }
    }

    override fun onDestroyView() {
        tts?.release()
        super.onDestroyView()
    }
}
