package com.ucsc.is2205.moderntexteditor.editor.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ucsc.is2205.moderntexteditor.editor.EditorUiState

@Composable
fun EditorToolbar(
    uiState: EditorUiState,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onSearch: () -> Unit,
    onToggleWordWrap: () -> Unit,
    onPreview: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                )
        ) {

            /*
             * -------------------------------------------------
             * Main editor tools
             * -------------------------------------------------
             */

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(
                        rememberScrollState()
                    ),
                verticalAlignment =
                    Alignment.CenterVertically,
                horizontalArrangement =
                    Arrangement.spacedBy(6.dp)
            ) {

                OutlinedButton(
                    onClick = onUndo,
                    enabled = uiState.canUndo
                ) {
                    Text(
                        text = "Undo"
                    )
                }

                OutlinedButton(
                    onClick = onRedo,
                    enabled = uiState.canRedo
                ) {
                    Text(
                        text = "Redo"
                    )
                }

                OutlinedButton(
                    onClick = onSearch
                ) {
                    Text(
                        text = "Search"
                    )
                }

                OutlinedButton(
                    onClick = onPreview
                ) {
                    Text(
                        text = "Preview"
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(4.dp)
            )

            /*
             * -------------------------------------------------
             * Word wrap control
             * -------------------------------------------------
             */

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 4.dp
                    ),
                verticalAlignment =
                    Alignment.CenterVertically,
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "Word Wrap",
                        style =
                            MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text =
                            if (uiState.wordWrapEnabled) {
                                "Lines wrap inside the editor"
                            } else {
                                "Horizontal scrolling enabled"
                            },
                        style =
                            MaterialTheme.typography.labelSmall,
                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                    )
                }

                Switch(
                    checked =
                        uiState.wordWrapEnabled,

                    onCheckedChange = {
                        onToggleWordWrap()
                    }
                )
            }
        }
    }
}