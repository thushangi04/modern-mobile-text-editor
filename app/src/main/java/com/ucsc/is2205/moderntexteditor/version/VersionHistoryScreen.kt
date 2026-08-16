package com.ucsc.is2205.moderntexteditor.version

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ucsc.is2205.moderntexteditor.version.components.DiffViewer
import com.ucsc.is2205.moderntexteditor.version.components.VersionDetails
import com.ucsc.is2205.moderntexteditor.version.components.VersionList

@Composable
fun VersionHistoryScreen(
    fileName: String,
    onBack: () -> Unit,
    onRollback: (String) -> Unit = {},
    viewModel: VersionViewModel = viewModel()
) {

    val uiState by
    viewModel.uiState.collectAsState()

    /*
     * Local references prevent delegated-state smart cast issues.
     */

    val selectedVersion =
        uiState.selectedVersion

    val compareVersion =
        uiState.compareVersion

    /*
     * ---------------------------------------------------------
     * Android back button / gesture
     * ---------------------------------------------------------
     */

    BackHandler(
        onBack = onBack
    )

    /*
     * ---------------------------------------------------------
     * Load versions
     * ---------------------------------------------------------
     */

    LaunchedEffect(fileName) {

        if (fileName.isNotBlank()) {

            viewModel.loadVersions(
                fileName
            )
        }
    }

    /*
     * ---------------------------------------------------------
     * Screen
     * ---------------------------------------------------------
     */

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        color =
            MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 14.dp,
                    vertical = 10.dp
                )
        ) {

            /*
             * -------------------------------------------------
             * Header
             * -------------------------------------------------
             */

            VersionHistoryHeader(
                fileName = fileName,
                versionCount =
                    uiState.versions.size,
                onBack = onBack
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            /*
             * -------------------------------------------------
             * Version list
             * -------------------------------------------------
             */

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.38f),
                shape =
                    MaterialTheme.shapes.large,
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surface
                    ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
            ) {

                VersionList(
                    versions =
                        uiState.versions,

                    selectedVersion =
                        selectedVersion,

                    onVersionSelected = { version ->

                        viewModel.selectVersion(
                            version
                        )
                    },

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            /*
             * -------------------------------------------------
             * Details / comparison workspace
             * -------------------------------------------------
             */

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.62f)
            ) {

                /*
                 * ---------------------------------------------
                 * Selected version details
                 * ---------------------------------------------
                 */

                VersionDetails(
                    version =
                        selectedVersion,
                        
                    onRollback = onRollback,

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                /*
                 * ---------------------------------------------
                 * Comparison workspace
                 * ---------------------------------------------
                 */

                if (
                    selectedVersion != null &&
                    compareVersion != null
                ) {

                    DiffViewer(
                        oldContent =
                            compareVersion.content,

                        newContent =
                            selectedVersion.content,

                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                } else {

                    DiffPlaceholder(
                        hasSelection =
                            selectedVersion != null,

                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            /*
             * -------------------------------------------------
             * Comparison controls
             * -------------------------------------------------
             */

            ComparisonActions(
                canCompare =
                    selectedVersion != null &&
                            uiState.versions.size > 1,

                isComparing =
                    compareVersion != null,

                onCompare = {

                    viewModel.compareWithPrevious()
                },

                onClear = {

                    viewModel.clearComparison()
                }
            )
        }
    }
}

/*
 * -------------------------------------------------------------
 * Header
 * -------------------------------------------------------------
 */

@Composable
private fun VersionHistoryHeader(
    fileName: String,
    versionCount: Int,
    onBack: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            MaterialTheme.shapes.large,

        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surface
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            /*
             * -------------------------------------------------
             * Title
             * -------------------------------------------------
             */

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text = "Version History",

                    style =
                        MaterialTheme.typography
                            .headlineSmall,

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(2.dp)
                )

                Text(
                    text =
                        if (fileName.isBlank()) {
                            "Current document"
                        } else {
                            fileName
                        },

                    style =
                        MaterialTheme.typography
                            .bodySmall,

                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant,

                    maxLines = 1
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(
                    text =
                        when (versionCount) {

                            0 ->
                                "No saved versions"

                            1 ->
                                "1 saved version"

                            else ->
                                "$versionCount saved versions"
                        },

                    style =
                        MaterialTheme.typography
                            .labelSmall,

                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )
            }

            /*
             * -------------------------------------------------
             * Version counter
             * -------------------------------------------------
             */

            Surface(
                color =
                    MaterialTheme.colorScheme
                        .primaryContainer,

                contentColor =
                    MaterialTheme.colorScheme
                        .onPrimaryContainer,

                shape =
                    MaterialTheme.shapes.small
            ) {

                Text(
                    text =
                        versionCount.toString(),

                    modifier =
                        Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        ),

                    style =
                        MaterialTheme.typography
                            .labelMedium,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                modifier =
                    Modifier.padding(4.dp)
            )

            /*
             * -------------------------------------------------
             * Back
             * -------------------------------------------------
             */

            OutlinedButton(
                onClick =
                    onBack
            ) {

                Text(
                    text = "Back"
                )
            }
        }
    }
}

/*
 * -------------------------------------------------------------
 * Empty diff state
 * -------------------------------------------------------------
 */

@Composable
private fun DiffPlaceholder(
    hasSelection: Boolean,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier =
            modifier,

        color =
            MaterialTheme.colorScheme
                .surfaceVariant,

        shape =
            MaterialTheme.shapes.large
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),

            verticalArrangement =
                Arrangement.Center,

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Surface(
                color =
                    MaterialTheme.colorScheme
                        .primaryContainer,

                contentColor =
                    MaterialTheme.colorScheme
                        .onPrimaryContainer,

                shape =
                    MaterialTheme.shapes.small
            ) {

                Text(
                    text = "DIFF",

                    modifier =
                        Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 4.dp
                        ),

                    style =
                        MaterialTheme.typography
                            .labelSmall,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(
                text =
                    if (hasSelection) {
                        "Ready to compare"
                    } else {
                        "No version selected"
                    },

                style =
                    MaterialTheme.typography
                        .titleMedium,

                fontWeight =
                    FontWeight.SemiBold
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text =
                    if (hasSelection) {

                        "Tap Compare Previous to view the changes between this version and the previous saved version."

                    } else {

                        "Select a saved version above to inspect it and compare changes."
                    },

                style =
                    MaterialTheme.typography
                        .bodySmall,

                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
            )
        }
    }
}

/*
 * -------------------------------------------------------------
 * Comparison controls
 * -------------------------------------------------------------
 */

@Composable
private fun ComparisonActions(
    canCompare: Boolean,
    isComparing: Boolean,
    onCompare: () -> Unit,
    onClear: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            MaterialTheme.shapes.large,

        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surface
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick =
                    onCompare,

                enabled =
                    canCompare,

                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text = "Compare Previous"
                )
            }

            OutlinedButton(
                onClick =
                    onClear,

                enabled =
                    isComparing,

                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text = "Clear"
                )
            }
        }
    }
}