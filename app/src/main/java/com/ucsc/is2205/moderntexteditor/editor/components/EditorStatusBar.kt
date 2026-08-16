package com.ucsc.is2205.moderntexteditor.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ucsc.is2205.moderntexteditor.editor.EditorUiState

@Composable
fun EditorStatusBar(
    uiState: EditorUiState
) {

    val lineCount =
        if (uiState.text.isEmpty()) {
            1
        } else {
            uiState.text.count {
                it == '\n'
            } + 1
        }

    val characterCount =
        uiState.text.length

    val wordCount =
        uiState.text
            .trim()
            .split(
                Regex("\\s+")
            )
            .count {
                it.isNotBlank()
            }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.small
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 10.dp,
                    vertical = 7.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            /*
             * -------------------------------------------------
             * Left status
             * -------------------------------------------------
             */

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        if (uiState.isReadOnly) {
                            "Read-only"
                        } else {
                            "Editable"
                        },
                    style =
                        MaterialTheme.typography.labelSmall,
                    color =
                        if (uiState.isReadOnly) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text =
                        if (uiState.wordWrapEnabled) {
                            "Wrap: On"
                        } else {
                            "Wrap: Off"
                        },
                    style =
                        MaterialTheme.typography.labelSmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            /*
             * -------------------------------------------------
             * Right status
             * -------------------------------------------------
             */

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "Ln $lineCount",
                    style =
                        MaterialTheme.typography.labelSmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "Words $wordCount",
                    style =
                        MaterialTheme.typography.labelSmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "Chars $characterCount",
                    style =
                        MaterialTheme.typography.labelSmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}