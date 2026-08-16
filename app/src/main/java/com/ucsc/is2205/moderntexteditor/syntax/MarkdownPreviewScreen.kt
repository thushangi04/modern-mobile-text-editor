package com.ucsc.is2205.moderntexteditor.syntax

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
fun MarkdownPreviewScreen(
    markdown: String,
    onBack: () -> Unit
) {

    BackHandler(
        onBack = onBack
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
        ) {

            /*
             * -------------------------------------------------
             * Header
             * -------------------------------------------------
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
                        .padding(14.dp),
                    verticalAlignment =
                        Alignment.CenterVertically,
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Markdown Preview",
                            style =
                                MaterialTheme.typography.headlineSmall,
                            fontWeight =
                                FontWeight.SemiBold
                        )

                        Spacer(
                            modifier = Modifier.height(2.dp)
                        )

                        Text(
                            text = "Rendered document preview",
                            style =
                                MaterialTheme.typography.bodySmall,
                            color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                        )
                    }

                    OutlinedButton(
                        onClick = onBack
                    ) {

                        Text(
                            text = "Back to Editor"
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            /*
             * -------------------------------------------------
             * Preview status
             * -------------------------------------------------
             */

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "Preview",
                    style =
                        MaterialTheme.typography.titleSmall,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Surface(
                    color =
                        if (markdown.isBlank()) {
                            MaterialTheme.colorScheme.surfaceVariant
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        },
                    contentColor =
                        if (markdown.isBlank()) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        },
                    shape =
                        MaterialTheme.shapes.small
                ) {

                    Text(
                        text =
                            if (markdown.isBlank()) {
                                "EMPTY"
                            } else {
                                "RENDERED"
                            },
                        modifier =
                            Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 4.dp
                            ),
                        style =
                            MaterialTheme.typography.labelSmall,
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            /*
             * -------------------------------------------------
             * Preview workspace
             * -------------------------------------------------
             */

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    /*
                     * -----------------------------------------
                     * Preview workspace header
                     * -----------------------------------------
                     */

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 14.dp,
                                vertical = 10.dp
                            ),
                        verticalAlignment =
                            Alignment.CenterVertically,
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Document",
                            style =
                                MaterialTheme.typography.titleSmall,
                            fontWeight =
                                FontWeight.SemiBold
                        )

                        Text(
                            text =
                                "${markdown.length} chars",
                            style =
                                MaterialTheme.typography.labelSmall,
                            color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                        )
                    }

                    HorizontalDivider(
                        color =
                            MaterialTheme.colorScheme.outlineVariant
                    )

                    /*
                     * -----------------------------------------
                     * Rendered content
                     * -----------------------------------------
                     */

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(
                                rememberScrollState()
                            )
                            .padding(16.dp)
                    ) {

                        if (markdown.isBlank()) {

                            EmptyPreviewState()

                        } else {

                            MarkdownRenderer(
                                markdown = markdown,
                                modifier =
                                    Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

/*
 * -------------------------------------------------------------
 * Empty preview
 * -------------------------------------------------------------
 */

@Composable
private fun EmptyPreviewState() {

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
                .padding(20.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text = "Nothing to preview",
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight =
                    FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text =
                    "Enter Markdown text in the editor and open Preview again.",
                style =
                    MaterialTheme.typography.bodySmall,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}