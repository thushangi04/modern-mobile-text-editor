package com.ucsc.is2205.moderntexteditor.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun FileSelectionPanel(
    title: String,
    files: List<String>,
    onFileSelected: (String) -> Unit,
    onClose: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {

            /*
             * -------------------------------------------------
             * Header
             * -------------------------------------------------
             */

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically,
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = title,
                        style =
                            MaterialTheme.typography.titleMedium,
                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text =
                            if (files.isEmpty()) {
                                "No files available"
                            } else {
                                "${files.size} file(s) available"
                            },
                        style =
                            MaterialTheme.typography.labelSmall,
                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                    )
                }

                Surface(
                    color =
                        MaterialTheme.colorScheme.primaryContainer,
                    contentColor =
                        MaterialTheme.colorScheme.onPrimaryContainer,
                    shape =
                        MaterialTheme.shapes.small
                ) {

                    Text(
                        text =
                            files.size.toString(),
                        modifier =
                            Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 4.dp
                            ),
                        style =
                            MaterialTheme.typography.labelMedium,
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            /*
             * -------------------------------------------------
             * File list
             * -------------------------------------------------
             */

            if (files.isEmpty()) {

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color =
                        MaterialTheme.colorScheme.surfaceVariant,
                    shape =
                        MaterialTheme.shapes.medium
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "No files available.",
                            style =
                                MaterialTheme.typography.bodyMedium,
                            color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "Save a file first and it will appear here.",
                            style =
                                MaterialTheme.typography.labelSmall,
                            color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                        )
                    }
                }

            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(
                            max = 220.dp
                        ),
                    verticalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {

                    items(
                        items = files,
                        key = { fileName ->
                            fileName
                        }
                    ) { fileName ->

                        OutlinedButton(
                            onClick = {
                                onFileSelected(
                                    fileName
                                )
                            },
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            Text(
                                text = fileName,
                                modifier =
                                    Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            /*
             * -------------------------------------------------
             * Close action
             * -------------------------------------------------
             */

            OutlinedButton(
                onClick = onClose,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Close"
                )
            }
        }
    }
}