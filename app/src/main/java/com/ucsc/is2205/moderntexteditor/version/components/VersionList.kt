package com.ucsc.is2205.moderntexteditor.version.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ucsc.is2205.moderntexteditor.domain.model.FileVersion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VersionList(
    versions: List<FileVersion>,
    selectedVersion: FileVersion?,
    onVersionSelected: (FileVersion) -> Unit,
    modifier: Modifier = Modifier
) {

    if (versions.isEmpty()) {

        EmptyVersionList(
            modifier = modifier
        )

        return
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        /*
         * -------------------------------------------------
         * List header
         * -------------------------------------------------
         */

        item {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 4.dp,
                        vertical = 4.dp
                    ),
                verticalAlignment =
                    Alignment.CenterVertically,
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Saved Versions",
                    style =
                        MaterialTheme.typography.titleSmall,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Surface(
                    color =
                        MaterialTheme.colorScheme.surfaceVariant,
                    shape =
                        MaterialTheme.shapes.small
                ) {

                    Text(
                        text = versions.size.toString(),
                        modifier =
                            Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 3.dp
                            ),
                        style =
                            MaterialTheme.typography.labelSmall,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(2.dp)
            )
        }

        /*
         * -------------------------------------------------
         * Versions
         * -------------------------------------------------
         */

        items(
            items = versions,
            key = { version ->
                version.id
            }
        ) { version ->

            val isSelected =
                selectedVersion?.id ==
                        version.id

            VersionCard(
                version = version,
                isSelected = isSelected,
                onClick = {
                    onVersionSelected(
                        version
                    )
                }
            )
        }
    }
}

/*
 * -------------------------------------------------------------
 * Version card
 * -------------------------------------------------------------
 */

@Composable
private fun VersionCard(
    version: FileVersion,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
        shape =
            MaterialTheme.shapes.medium,
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    if (isSelected) {
                        2.dp
                    } else {
                        1.dp
                    }
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            /*
             * -------------------------------------------------
             * Version title
             * -------------------------------------------------
             */

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                /*
                 * Small version indicator
                 */

                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .background(
                            color =
                                if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.outline
                                },
                            shape =
                                CircleShape
                        )
                )

                Spacer(
                    modifier =
                        Modifier.size(8.dp)
                )

                Text(
                    text =
                        "Version #${version.id}",
                    modifier =
                        Modifier.weight(1f),
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight =
                        FontWeight.SemiBold,
                    color =
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                )

                if (isSelected) {

                    Surface(
                        color =
                            MaterialTheme.colorScheme.primary,
                        contentColor =
                            MaterialTheme.colorScheme.onPrimary,
                        shape =
                            MaterialTheme.shapes.small
                    ) {

                        Text(
                            text = "SELECTED",
                            modifier =
                                Modifier.padding(
                                    horizontal = 7.dp,
                                    vertical = 3.dp
                                ),
                            style =
                                MaterialTheme.typography.labelSmall,
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            /*
             * -------------------------------------------------
             * File name
             * -------------------------------------------------
             */

            Text(
                text = version.fileName,
                style =
                    MaterialTheme.typography.bodyMedium,
                fontWeight =
                    FontWeight.Medium,
                color =
                    if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                maxLines = 1
            )

            Spacer(
                modifier =
                    Modifier.height(5.dp)
            )

            /*
             * -------------------------------------------------
             * Timestamp
             * -------------------------------------------------
             */

            Text(
                text =
                    formatTimestamp(
                        version.timestamp
                    ),
                style =
                    MaterialTheme.typography.bodySmall,
                color =
                    if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                            .copy(alpha = 0.75f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
            )

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            /*
             * -------------------------------------------------
             * Content information
             * -------------------------------------------------
             */

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        "${version.content.length} characters",
                    style =
                        MaterialTheme.typography.labelSmall,
                    color =
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                                .copy(alpha = 0.75f)
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                )

                Text(
                    text =
                        "${countLines(version.content)} lines",
                    style =
                        MaterialTheme.typography.labelSmall,
                    color =
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                                .copy(alpha = 0.75f)
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                )
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
private fun EmptyVersionList(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 28.dp
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Surface(
            modifier =
                Modifier.size(48.dp),
            color =
                MaterialTheme.colorScheme.surfaceVariant,
            shape =
                CircleShape
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = "0",
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        Text(
            text = "No version history",
            style =
                MaterialTheme.typography.titleMedium,
            fontWeight =
                FontWeight.SemiBold
        )

        Spacer(
            modifier =
                Modifier.height(5.dp)
        )

        Text(
            text =
                "Saved versions of this file will appear here.",
            style =
                MaterialTheme.typography.bodySmall,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
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