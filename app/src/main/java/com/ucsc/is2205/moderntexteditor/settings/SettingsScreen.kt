package com.ucsc.is2205.moderntexteditor.settings

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {

    var darkMode by remember {
        mutableStateOf(false)
    }

    var wordWrap by remember {
        mutableStateOf(true)
    }

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

            SettingsHeader()

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            /*
             * -------------------------------------------------
             * Appearance section
             * -------------------------------------------------
             */

            SettingsSectionTitle(
                title = "Appearance",
                description = "Customize how the editor looks."
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            SettingCard(
                title = "Dark mode",
                description = "Use darker colors throughout the application.",
                checked = darkMode,
                onCheckedChange = {
                    darkMode = it
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            /*
             * -------------------------------------------------
             * Editor section
             * -------------------------------------------------
             */

            SettingsSectionTitle(
                title = "Editor",
                description = "Control text editing behavior."
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            SettingCard(
                title = "Word wrap",
                description = "Wrap long lines inside the editor instead of scrolling horizontally.",
                checked = wordWrap,
                onCheckedChange = {
                    wordWrap = it
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            /*
             * -------------------------------------------------
             * Information card
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    Text(
                        text = "Editor information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

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

                    InformationRow(
                        label = "Theme",
                        value =
                            if (darkMode) {
                                "Dark"
                            } else {
                                "Light"
                            }
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    InformationRow(
                        label = "Word wrap",
                        value =
                            if (wordWrap) {
                                "Enabled"
                            } else {
                                "Disabled"
                            }
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    InformationRow(
                        label = "Encoding",
                        value = "UTF-8"
                    )
                }
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            /*
             * -------------------------------------------------
             * Back button
             * -------------------------------------------------
             */

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Back to Editor"
                )
            }
        }
    }
}

/*
 * -------------------------------------------------------------
 * Header
 * -------------------------------------------------------------
 */

@Composable
private fun SettingsHeader() {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = "Customize your editor experience.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/*
 * -------------------------------------------------------------
 * Section title
 * -------------------------------------------------------------
 */

@Composable
private fun SettingsSectionTitle(
    title: String,
    description: String
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/*
 * -------------------------------------------------------------
 * Setting card
 * -------------------------------------------------------------
 */

@Composable
private fun SettingCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(
                modifier = Modifier.padding(6.dp)
            )

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

/*
 * -------------------------------------------------------------
 * Information row
 * -------------------------------------------------------------
 */

@Composable
private fun InformationRow(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}