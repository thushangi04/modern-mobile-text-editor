package com.ucsc.is2205.moderntexteditor.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ucsc.is2205.moderntexteditor.editor.EditorUiState

@Composable
fun EditorTopBar(
    uiState: EditorUiState,
    onNewFile: () -> Unit,
    onSaveFile: () -> Unit,
    onSaveAs: () -> Unit,
    onOpenFile: () -> Unit,
    onRecentFiles: () -> Unit,
    onToggleReadOnly: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        /*
         * -----------------------------------------------------
         * File information card
         * -----------------------------------------------------
         */

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 14.dp,
                        vertical = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = uiState.fileName,
                        style =
                            MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text =
                            if (uiState.isModified) {
                                "Unsaved changes • ${uiState.encoding}"
                            } else {
                                "Saved • ${uiState.encoding}"
                            },
                        style =
                            MaterialTheme.typography.labelMedium,
                        color =
                            if (uiState.isModified) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                            }
                    )
                }

                OutlinedButton(
                    onClick = onToggleReadOnly
                ) {

                    Text(
                        text =
                            if (uiState.isReadOnly) {
                                "Read-only"
                            } else {
                                "Editable"
                            }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        /*
         * -----------------------------------------------------
         * Primary file actions
         * -----------------------------------------------------
         */

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = onNewFile,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "New"
                )
            }

            Button(
                onClick = onSaveFile,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Save"
                )
            }

            OutlinedButton(
                onClick = onSaveAs,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Save As"
                )
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        /*
         * -----------------------------------------------------
         * Secondary file actions
         * -----------------------------------------------------
         */

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 1.dp
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                OutlinedButton(
                    onClick = onOpenFile,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Open"
                    )
                }

                OutlinedButton(
                    onClick = onRecentFiles,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Recent"
                    )
                }
            }
        }
    }
}