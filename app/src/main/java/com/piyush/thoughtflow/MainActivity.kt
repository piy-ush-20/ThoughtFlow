package com.piyush.thoughtflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.piyush.thoughtflow.navigation.ThoughtFlowNavHost
import com.piyush.thoughtflow.speech.SpeechEngine
import com.piyush.thoughtflow.ui.theme.ThoughtFlowTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var speechEngine: SpeechEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThoughtFlowTheme {
                ThoughtFlowNavHost()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // SpeechRecognizer must use Activity context on many devices.
        speechEngine.bindToActivity(this)
    }

    override fun onStop() {
        speechEngine.unbindFromActivity()
        super.onStop()
    }
}
