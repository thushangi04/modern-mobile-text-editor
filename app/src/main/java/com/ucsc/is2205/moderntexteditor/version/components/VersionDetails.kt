package com.ucsc.is2205.moderntexteditor.version.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ucsc.is2205.moderntexteditor.domain.model.FileVersion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VersionDetails(
    version: FileVersion?,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        if (version == null) {

            EmptyVersionDetails()

        } else {

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

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Version #${version.id}",
                            style =
                                MaterialTheme.typography.titleMedium,
                            fontWeight =
                                FontWeight.SemiBold
                        )

                        Spacer(
                            modifier = Modifier.height(2.dp)
                        )

                        Text(
                            text = version.fileName,
                            style =
                                MaterialTheme.typography.bodySmall,
                            color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant,
                            maxLines = 1
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
                            text = "VERSION",
                            modifier =
                                Modifier.padding(
                                    horizontal = 8.dp,
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
                    modifier = Modifier.height(10.dp)
                )

                HorizontalDivider(
                    color =
                        MaterialTheme.colorScheme.outlineVariant
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                /*
                 * -------------------------------------------------
                 * Version metadata
                 * -------------------------------------------------
                 */

                VersionInfoRow(
                    label = "Created",
                    value =
                        formatTimestamp(
                            version.timestamp
                        )
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                VersionInfoRow(
                    label = "Characters",
                    value =
                        version.content.length.toString()
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                VersionInfoRow(
                    label = "Lines",
                    value =
                        countLines(
                            version.content
                        ).toString()
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                VersionInfoRow(
                    label = "Status",
                    value =
                        if (version.content.isEmpty()) {
                            "Empty"
                        } else {
                            "Saved"
                        }
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                /*
                 * -------------------------------------------------
                 * Content header
                 * -------------------------------------------------
                 */

                Text(
                    text = "Content",
                    style =
                        MaterialTheme.typography.titleSmall,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                /*
                 * -------------------------------------------------
                 * Content preview
                 * -------------------------------------------------
                 */

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color =
                        MaterialTheme.colorScheme.surfaceVariant,
                    shape =
                        MaterialTheme.shapes.medium
                ) {

                    Text(
                        text =
                            if (version.content.isEmpty()) {
                                "(Empty file)"
                            } else {
                                version.content
                            },
                        modifier = Modifier.padding(12.dp),
                        style =
                            MaterialTheme.typography.bodySmall.copy(
                                fontFamily =
                                    FontFamily.Monospace
                            ),
                        color =
                            if (version.content.isEmpty()) {
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                            } else {
                                MaterialTheme.colorScheme
                                    .onSurface
                            }
                    )
                }
            }
        }
    }
}

/*
 * -------------------------------------------------------------
 * Empty state
 * -------------------------------------------------------------
 */

@Composable
private fun EmptyVersionDetails() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {

        Text(
            text = "No version selected",
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
                "Select a version from the history list to view its details and content.",
            style =
                MaterialTheme.typography.bodySmall,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/*
 * -------------------------------------------------------------
 * Metadata row
 * -------------------------------------------------------------
 */

@Composable
private fun VersionInfoRow(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.CenterVertically,
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            style =
                MaterialTheme.typography.bodySmall,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style =
                MaterialTheme.typography.bodySmall,
            fontWeight =
                FontWeight.Medium
        )
    }
}

/*
 * -------------------------------------------------------------
 * Helpers
 * -------------------------------------------------------------
 */

private fun countLines(
    content: String
): Int {

    if (content.isEmpty()) {
        return 0
    }

    return content.count {
        it == '\n'
    } + 1
}

private fun formatTimestamp(
    timestamp: Long
): String {

    if (timestamp <= 0L) {
        return "Unknown time"
    }

    return try {

        SimpleDateFormat(
            "MMM dd, yyyy • HH:mm:ss",
            Locale.getDefault()
        ).format(
            Date(timestamp)
        )

    } catch (
        exception: Exception
    ) {

        "Unknown time"
    }
}