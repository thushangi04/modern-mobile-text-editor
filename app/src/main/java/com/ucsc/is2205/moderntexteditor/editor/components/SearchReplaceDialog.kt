package com.ucsc.is2205.moderntexteditor.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
fun SearchReplaceDialog(
    initialText: String,
    onReplace: (
        String,
        String,
        Boolean
    ) -> Unit,
    onReplaceAll: (
        String,
        String,
        Boolean
    ) -> Unit,
    onFindNext: (
        String,
        Boolean
    ) -> Unit,
    onFindPrevious: (
        String,
        Boolean
    ) -> Unit,
    onClose: () -> Unit
) {

    var searchText by remember {
        mutableStateOf("")
    }

    var replacementText by remember {
        mutableStateOf("")
    }

    var caseSensitive by remember {
        mutableStateOf(false)
    }

    val matchCount =
        remember(
            initialText,
            searchText,
            caseSensitive
        ) {

            countMatches(
                text = initialText,
                search = searchText,
                caseSensitive = caseSensitive
            )
        }

    val hasSearchText =
        searchText.isNotEmpty()

    AlertDialog(
        onDismissRequest = onClose,

        /*
         * -----------------------------------------------------
         * Title
         * -----------------------------------------------------
         */

        title = {

            Column {

                Text(
                    text = "Search & Replace",
                    style =
                        MaterialTheme.typography.headlineSmall,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "Find text and replace matches in the current document",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },

        /*
         * -----------------------------------------------------
         * Content
         * -----------------------------------------------------
         */

        text = {

            Column(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                /*
                 * ---------------------------------------------
                 * Search field
                 * ---------------------------------------------
                 */

                OutlinedTextField(
                    value = searchText,

                    onValueChange = {
                        searchText = it
                    },

                    label = {
                        Text(
                            text = "Search for"
                        )
                    },

                    placeholder = {
                        Text(
                            text = "Enter text to find"
                        )
                    },

                    singleLine = true,

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                /*
                 * ---------------------------------------------
                 * Replace field
                 * ---------------------------------------------
                 */

                OutlinedTextField(
                    value = replacementText,

                    onValueChange = {
                        replacementText = it
                    },

                    label = {
                        Text(
                            text = "Replace with"
                        )
                    },

                    placeholder = {
                        Text(
                            text = "Enter replacement text"
                        )
                    },

                    singleLine = true,

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                /*
                 * ---------------------------------------------
                 * Search options
                 * ---------------------------------------------
                 */

                Surface(
                    modifier =
                        Modifier.fillMaxWidth(),

                    color =
                        MaterialTheme.colorScheme.surfaceVariant,

                    shape =
                        MaterialTheme.shapes.medium
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 10.dp,
                                vertical = 6.dp
                            ),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Checkbox(
                            checked =
                                caseSensitive,

                            onCheckedChange = {
                                caseSensitive = it
                            }
                        )

                        Column {

                            Text(
                                text = "Case sensitive",
                                style =
                                    MaterialTheme.typography.bodyMedium
                            )

                            Text(
                                text =
                                    if (caseSensitive) {
                                        "Uppercase and lowercase must match"
                                    } else {
                                        "Uppercase and lowercase are ignored"
                                    },
                                style =
                                    MaterialTheme.typography.labelSmall,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                /*
                 * ---------------------------------------------
                 * Match information
                 * ---------------------------------------------
                 */

                Surface(
                    modifier =
                        Modifier.fillMaxWidth(),

                    color =
                        if (hasSearchText && matchCount > 0) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },

                    contentColor =
                        if (hasSearchText && matchCount > 0) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },

                    shape =
                        MaterialTheme.shapes.medium
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement =
                            Arrangement.SpaceBetween,
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text =
                                if (!hasSearchText) {
                                    "Enter text to search"
                                } else if (matchCount == 0) {
                                    "No matches found"
                                } else {
                                    "Matches found"
                                },
                            style =
                                MaterialTheme.typography.bodyMedium
                        )

                        if (hasSearchText) {

                            Text(
                                text = matchCount.toString(),
                                style =
                                    MaterialTheme.typography.titleMedium,
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                /*
                 * ---------------------------------------------
                 * Find navigation
                 * ---------------------------------------------
                 */

                Text(
                    text = "Navigate matches",
                    style =
                        MaterialTheme.typography.labelLarge,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    OutlinedButton(
                        onClick = {

                            if (hasSearchText) {

                                onFindPrevious(
                                    searchText,
                                    caseSensitive
                                )
                            }
                        },

                        enabled =
                            hasSearchText,

                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Previous"
                        )
                    }

                    Button(
                        onClick = {

                            if (hasSearchText) {

                                onFindNext(
                                    searchText,
                                    caseSensitive
                                )
                            }
                        },

                        enabled =
                            hasSearchText,

                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Next"
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                /*
                 * ---------------------------------------------
                 * Replace controls
                 * ---------------------------------------------
                 */

                Text(
                    text = "Replace",
                    style =
                        MaterialTheme.typography.labelLarge,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    OutlinedButton(
                        onClick = {

                            if (hasSearchText) {

                                onReplace(
                                    searchText,
                                    replacementText,
                                    caseSensitive
                                )
                            }
                        },

                        enabled =
                            hasSearchText,

                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Replace"
                        )
                    }

                    Button(
                        onClick = {

                            if (hasSearchText) {

                                onReplaceAll(
                                    searchText,
                                    replacementText,
                                    caseSensitive
                                )
                            }
                        },

                        enabled =
                            hasSearchText,

                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Replace All"
                        )
                    }
                }
            }
        },

        /*
         * -----------------------------------------------------
         * Close action
         * -----------------------------------------------------
         */

        confirmButton = {

            OutlinedButton(
                onClick =
                    onClose
            ) {

                Text(
                    text = "Close"
                )
            }
        }
    )
}

private fun countMatches(
    text: String,
    search: String,
    caseSensitive: Boolean
): Int {

    if (search.isEmpty()) {
        return 0
    }

    var count = 0
    var currentIndex = 0

    while (currentIndex < text.length) {

        val found =
            if (caseSensitive) {

                text.indexOf(
                    string = search,
                    startIndex = currentIndex
                )

            } else {

                text.indexOf(
                    string = search,
                    startIndex = currentIndex,
                    ignoreCase = true
                )
            }

        if (found < 0) {
            break
        }

        count++

        currentIndex =
            found + search.length
    }

    return count
}