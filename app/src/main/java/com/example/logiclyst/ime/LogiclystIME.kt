package com.example.logiclyst.ime

import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.ExtractedTextRequest
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.logiclyst.data.local.AnalysisEntity
import com.example.logiclyst.data.local.AppDatabase
import com.example.logiclyst.data.remote.AnalysisRequest
import com.example.logiclyst.data.remote.RetrofitClient
import kotlinx.coroutines.*

class LogiclystIME : InputMethodService(), LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner, android.speech.RecognitionListener {

    private var analysisJob: Job? = null
    private var speechRecognizer: SpeechRecognizer? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // --- Lifecycle & State Registry Setup (Mencegah Crash di Compose) ---
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    override val viewModelStore: ViewModelStore = ViewModelStore()

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer?.setRecognitionListener(this)
    }

    override fun onCreateInputView(): View {
        val composeView = ComposeView(this)

        // Menetapkan Owners agar Compose dapat berjalan di Service
        window?.window?.decorView?.let { decorView ->
            decorView.setViewTreeLifecycleOwner(this)
            decorView.setViewTreeViewModelStoreOwner(this)
            decorView.setViewTreeSavedStateRegistryOwner(this)
        }

        composeView.setContent {
            val fallacy by KeyboardState.detectedFallacy
            LogiclystKeyboard(
                detectedFallacy = fallacy,
                onKeyClick = { label -> handleInput(label) },
                onMicClick = { triggerVoiceInput() }
            )
        }
        return composeView
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    private fun handleInput(label: String) {
        val ic = currentInputConnection ?: return
        when (label) {
            "BACKSPACE" -> {
                ic.deleteSurroundingText(1, 0)
                analyzeTextContent()
            }
            " " -> {
                ic.commitText(" ", 1)
                analyzeTextContent() // Logika Fallacy
            }
            "ENTER" -> {
                val action = currentInputEditorInfo.actionId
                if (action != 0) ic.performEditorAction(action)
                else ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            }
            "MIC" -> triggerVoiceInput()
            "SHIFT_TOGGLE", "?123", "ABC", "=\\<", "1?2" -> {
                // Hanya untuk perpindahan UI di Compose, tidak ada commit teks
            }
            else -> {
                // Menangani input huruf, angka, simbol, dan emoji dari picker
                ic.commitText(label, 1)
                analyzeTextContent()
            }
        }
    }

    private fun analyzeTextContent() {
        val ic = currentInputConnection ?: return
        val extractedText = ic.getExtractedText(ExtractedTextRequest(), 0)
        val textToAnalyze = extractedText?.text?.toString() ?: ""

        analysisJob?.cancel()

        KeyboardState.isAnalyzing.value = false
        KeyboardState.detectedFallacy.value = null
        KeyboardState.fullResponse.value = null

        if (!KeyboardState.isDetectionOn.value || textToAnalyze.isBlank() || textToAnalyze.length < 15) {
            return
        }

        analysisJob = serviceScope.launch {
            delay(2000)
            KeyboardState.isAnalyzing.value = true

            try {
                val request = AnalysisRequest(
                    text = textToAnalyze,
                    sensitivity = KeyboardState.aiSensitivity.value
                )

                val response = RetrofitClient.instance.analyzeText(request)

                if (isActive) {
                    // SIMPAN KE DATABASE LOKAL (Jalankan di background thread IO)
                    if (response.isFallacy == true) {
                        val db = AppDatabase.getDatabase(applicationContext)
                        db.analysisDao().insertAnalysis(
                            AnalysisEntity(
                                originalText = textToAnalyze,
                                fallacyType = response.label ?: "Unknown fallacy"
                            )
                        )
                    }

                    withContext(Dispatchers.Main) {
                        KeyboardState.isAnalyzing.value = false
                        if (response.isFallacy == true) {
                            KeyboardState.detectedFallacy.value = response.label
                            KeyboardState.fullResponse.value = response
                        }
                    }
                }
            } catch (e: Exception) {
                if (isActive) {
                    withContext(Dispatchers.Main) {
                        KeyboardState.isAnalyzing.value = false
                    }
                }
            }
        }
    }

    private fun triggerVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID") // Paksa bahasa Indonesia
        }
        speechRecognizer?.startListening(intent)
        Toast.makeText(this, "Mendengarkan...", Toast.LENGTH_SHORT).show()
    }
    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (!matches.isNullOrEmpty()) {
            val text = matches[0]
            currentInputConnection?.commitText("$text ", 1)
            analyzeTextContent() // Jalankan AI setelah bicara
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        // Bisa digunakan jika ingin teks muncul real-time saat bicara
    }

    override fun onError(error: Int) {
        val message = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio error"
            SpeechRecognizer.ERROR_NO_MATCH -> "Tidak ada suara terdeteksi"
            else -> "Mic Error: $error"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() {}
    override fun onEvent(eventType: Int, params: Bundle?) {}
    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        serviceScope.cancel()
    }
}