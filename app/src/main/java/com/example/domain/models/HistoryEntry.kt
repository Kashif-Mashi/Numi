package com.example.domain.models

data class HistoryEntry(
    val id: Long = 0L,
    val expression: String,
    val result: String,
    val timestampMillis: Long = System.currentTimeMillis()
)
