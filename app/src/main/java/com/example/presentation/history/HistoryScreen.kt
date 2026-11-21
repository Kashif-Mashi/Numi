package com.example.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.calculator.ExpressionFormatter
import com.example.domain.models.HistoryEntry
import com.example.ui.theme.LocalNumiTheme
import com.example.ui.typography.NumiFonts
import java.text.DateFormat
import java.util.Date

@Composable
fun HistoryScreen(
    entries: List<HistoryEntry>,
    onReuseResult: (String) -> Unit,
    onDeleteEntry: (Long) -> Unit,
    onClearAll: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalNumiTheme.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(theme.backgroundTop, theme.backgroundBottom))
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(theme.displaySurface)
                        .border(1.dp, theme.displayBorder, RoundedCornerShape(16.dp))
                        .clickable(role = Role.Button, onClick = onNavigateBack)
                        .testTag("history_back_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.cd_navigate_back),
                        tint = theme.primaryText
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(R.string.nav_history),
                        style = MaterialTheme.typography.titleLarge,
                        color = theme.primaryText
                    )
                    Text(
                        text = stringResource(R.string.history_tap_to_reuse),
                        style = MaterialTheme.typography.bodyMedium,
                        color = theme.secondaryText
                    )
                }
            }

            if (entries.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(theme.utilityKeyBg)
                        .clickable(role = Role.Button, onClick = onClearAll)
                        .padding(horizontal = 12.dp)
                        .testTag("history_clear_all_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DeleteSweep,
                        contentDescription = stringResource(R.string.cd_clear_history),
                        tint = theme.utilityKeyText
                    )
                }
            }
        }

        if (entries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(theme.displaySurface)
                            .border(1.dp, theme.displayBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = null,
                            tint = theme.accentColor,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                    Text(
                        text = stringResource(R.string.history_empty_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = theme.primaryText
                    )
                    Text(
                        text = stringResource(R.string.history_empty_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = theme.secondaryText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(entries, key = { it.id }) { entry ->
                    HistoryCard(
                        entry = entry,
                        onReuse = { onReuseResult(entry.result) },
                        onDelete = { onDeleteEntry(entry.id) }
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun HistoryCard(
    entry: HistoryEntry,
    onReuse: () -> Unit,
    onDelete: () -> Unit
) {
    val theme = LocalNumiTheme.current
    val timeLabel = remember(entry.timestampMillis) {
        DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(entry.timestampMillis))
    }
    val shape = RoundedCornerShape(22.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(theme.displaySurface)
            .border(1.dp, theme.displayBorder, shape)
            .clickable(role = Role.Button, onClick = onReuse)
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .testTag("history_item_${entry.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = timeLabel,
                style = MaterialTheme.typography.labelMedium,
                color = theme.secondaryText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = ExpressionFormatter.formatForDisplay(entry.expression),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = NumiFonts.displayFamily(theme.fontStyle)
                ),
                color = theme.secondaryText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "= ${ExpressionFormatter.formatForDisplay(entry.result)}",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = NumiFonts.displayFamily(theme.fontStyle),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = theme.primaryText
            )
        }

        Box(
            modifier = Modifier
                .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                .clip(CircleShape)
                .clickable(role = Role.Button, onClick = onDelete)
                .testTag("history_delete_${entry.id}"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.DeleteOutline,
                contentDescription = stringResource(R.string.cd_delete_history_item),
                tint = theme.secondaryText,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
