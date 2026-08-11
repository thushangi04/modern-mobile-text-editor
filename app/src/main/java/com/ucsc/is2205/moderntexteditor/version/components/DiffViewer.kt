package com.ucsc.is2205.moderntexteditor.version.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DiffViewer(
    oldContent: String,
    newContent: String,
    modifier: Modifier = Modifier
) {

    val oldLines =
        oldContent.lines()

    val newLines =
        newContent.lines()

    val maxLines =
        maxOf(
            oldLines.size,
            newLines.size
        )

    val changedLineCount =
        (0 until maxLines).count { index ->

            val oldLine =
                oldLines.getOrNull(index)

            val newLine =
                newLines.getOrNull(index)

            oldLine != newLine
        }

    /*
     * ---------------------------------------------------------
     * Main diff card
     * ---------------------------------------------------------
     */

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surface
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            ),
        shape =
            MaterialTheme.shapes.large
    ) {

        Column(
            modifier =
                Modifier.fillMaxSize()
        ) {

            /*
             * -------------------------------------------------
             * Header
             * -------------------------------------------------
             */

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 14.dp,
                        vertical = 10.dp
                    ),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "Differences",
                        style =
                            MaterialTheme.typography.titleMedium,
                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Text(
                        text =
                            if (changedLineCount == 0) {
                                "No changes detected"
                            } else {
                                "$changedLineCount changed line(s)"
                            },
                        style =
                            MaterialTheme.typography.labelSmall,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color =
                        if (changedLineCount == 0) {
                            MaterialTheme.colorScheme.surfaceVariant
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        },
                    contentColor =
                        if (changedLineCount == 0) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        },
                    shape =
                        MaterialTheme.shapes.small
                ) {

                    Text(
                        text =
                            if (changedLineCount == 0) {
                                "IDENTICAL"
                            } else {
                                "CHANGED"
                            },
                        modifier =
                            Modifier.padding(
                                horizontal = 9.dp,
                                vertical = 4.dp
                            ),
                        style =
                            MaterialTheme.typography.labelSmall,
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            HorizontalDivider(
                color =
                    MaterialTheme.colorScheme.outlineVariant
            )

            /*
             * -------------------------------------------------
             * Column titles
             * -------------------------------------------------
             */

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                    .padding(
                        horizontal = 8.dp,
                        vertical = 7.dp
                    )
            ) {

                Text(
                    text = "Previous",
                    modifier =
                        Modifier.weight(1f),
                    style =
                        MaterialTheme.typography.labelLarge,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(
                    text = "Current",
                    modifier =
                        Modifier.weight(1f),
                    style =
                        MaterialTheme.typography.labelLarge,
                    fontWeight =
                        FontWeight.SemiBold
                )
            }

            HorizontalDivider(
                color =
                    MaterialTheme.colorScheme.outlineVariant
            )

            /*
             * -------------------------------------------------
             * Diff content
             * -------------------------------------------------
             */

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        vertical = 4.dp
                    )
            ) {

                if (maxLines == 0) {

                    EmptyDiffState()

                } else {

                    for (index in 0 until maxLines) {

                        val oldLine =
                            oldLines.getOrNull(index)

                        val newLine =
                            newLines.getOrNull(index)

                        DiffLine(
                            lineNumber =
                                index + 1,

                            oldLine =
                                oldLine,

                            newLine =
                                newLine
                        )
                    }
                }
            }
        }
    }
}

/*
 * -------------------------------------------------------------
 * Diff line
 * -------------------------------------------------------------
 */

@Composable
private fun DiffLine(
    lineNumber: Int,
    oldLine: String?,
    newLine: String?
) {

    val changed =
        oldLine != newLine

    val removed =
        oldLine != null &&
                newLine == null

    val added =
        oldLine == null &&
                newLine != null

    val rowBackground =
        when {

            added ->
                MaterialTheme.colorScheme
                    .secondaryContainer
                    .copy(
                        alpha = 0.45f
                    )

            removed ->
                MaterialTheme.colorScheme
                    .errorContainer
                    .copy(
                        alpha = 0.45f
                    )

            changed ->
                MaterialTheme.colorScheme
                    .primaryContainer
                    .copy(
                        alpha = 0.35f
                    )

            else ->
                MaterialTheme.colorScheme.surface
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                rowBackground
            )
            .padding(
                horizontal = 6.dp,
                vertical = 5.dp
            )
    ) {

        /*
         * -----------------------------------------------------
         * Previous version
         * -----------------------------------------------------
         */

        Row(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(
                text =
                    lineNumber.toString(),

                modifier =
                    Modifier.width(28.dp),

                style =
                    MaterialTheme.typography.labelSmall,

                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text =
                    oldLine ?: "",

                modifier =
                    Modifier.weight(1f),

                style =
                    MaterialTheme.typography.bodySmall.copy(
                        fontFamily =
                            FontFamily.Monospace
                    ),

                color =
                    if (removed || changed) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
            )
        }

        Spacer(
            modifier =
                Modifier.width(8.dp)
        )

        /*
         * -----------------------------------------------------
         * Current version
         * -----------------------------------------------------
         */

        Row(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(
                text =
                    lineNumber.toString(),

                modifier =
                    Modifier.width(28.dp),

                style =
                    MaterialTheme.typography.labelSmall,

                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text =
                    newLine ?: "",

                modifier =
                    Modifier.weight(1f),

                style =
                    MaterialTheme.typography.bodySmall.copy(
                        fontFamily =
                            FontFamily.Monospace
                    ),

                color =
                    if (added || changed) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
            )
        }
    }

    HorizontalDivider(
        color =
            MaterialTheme.colorScheme.outlineVariant
                .copy(
                    alpha = 0.5f
                )
    )
}

/*
 * -------------------------------------------------------------
 * Empty state
 * -------------------------------------------------------------
 */

@Composable
private fun EmptyDiffState() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {

        Text(
            text = "No content to compare",
            style =
                MaterialTheme.typography.titleMedium,
            fontWeight =
                FontWeight.SemiBold
        )

        Spacer(
            modifier =
                Modifier.padding(3.dp)
        )

        Text(
            text =
                "Both versions are empty.",
            style =
                MaterialTheme.typography.bodySmall,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}