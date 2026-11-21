package com.example.data.history

import com.example.domain.models.HistoryEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoryRepository(private val historyDao: HistoryDao) {

    val historyFlow: Flow<List<HistoryEntry>> = historyDao.observeHistory()
        .map { entities -> entities.map { it.toDomain() } }

    suspend fun saveCalculation(expression: String, result: String) {
        if (expression.isBlank() || result.isBlank()) return
        historyDao.insert(
            HistoryEntity(
                expression = expression,
                result = result,
                timestampMillis = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteEntry(id: Long) {
        historyDao.deleteById(id)
    }

    suspend fun clearHistory() {
        historyDao.clearAll()
    }
}
