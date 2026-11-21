package com.example

import android.app.Application
import com.example.animations.NumiFeedbackManager
import com.example.data.history.HistoryDatabase
import com.example.data.history.HistoryRepository
import com.example.data.preferences.NumiPreferencesRepository
import com.example.data.preferences.numiDataStore

class NumiApplication : Application() {

    val preferencesRepository: NumiPreferencesRepository by lazy {
        NumiPreferencesRepository(numiDataStore)
    }

    val historyDatabase: HistoryDatabase by lazy {
        HistoryDatabase.getInstance(this)
    }

    val historyRepository: HistoryRepository by lazy {
        HistoryRepository(historyDatabase.historyDao())
    }

    val feedbackManager: NumiFeedbackManager by lazy {
        NumiFeedbackManager()
    }
}
