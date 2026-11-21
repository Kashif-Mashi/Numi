package com.example.data.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.models.HistoryEntry

@Entity(tableName = "calculation_history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val expression: String,
    val result: String,
    val timestampMillis: Long = System.currentTimeMillis()
) {
    fun toDomain(): HistoryEntry = HistoryEntry(
        id = id,
        expression = expression,
        result = result,
        timestampMillis = timestampMillis
    )
}
