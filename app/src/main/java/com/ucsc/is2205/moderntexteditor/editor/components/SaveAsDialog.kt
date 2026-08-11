package com.ucsc.is2205.moderntexteditor.editor.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SaveAsDialog(
    currentFileName: String,
    onSave: (String) -> Unit,
    onCancel: () -> Unit
) {

    var fileName by remember(
        currentFileName
    ) {
        mutableStateOf(
            currentFileName
        )
    }

    val trimmedFileName =
        fileName.trim()

    val isValidFileName =
        trimmedFileName.isNotEmpty()

    AlertDialog(
        onDismissRequest =
            onCancel,

        /*
         * -----------------------------------------------------
         * Title
         * -----------------------------------------------------
         */

        title = {

            Column {

                Text(
                    text = "Save As",
                    style =
                        MaterialTheme.typography.headlineSmall,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "Choose a name for this file",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )
            }
        },

        /*
         * -----------------------------------------------------
         * Dialog content
         * -----------------------------------------------------
         */

        text = {

            Column(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(
                    value = fileName,

                    onValueChange = {
                        fileName = it
                    },

                    label = {
                        Text(
                            text = "File name"
                        )
                    },

                    supportingText = {

                        Text(
                            text =
                                if (isValidFileName) {
                                    "The file will be saved using this name."
                                } else {
                                    "File name cannot be empty."
                                }
                        )
                    },

                    isError =
                        !isValidFileName,

                    singleLine = true,

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                /*
                 * ---------------------------------------------
                 * File information
                 * ---------------------------------------------
                 */

                Surface(
                    modifier =
                        Modifier.fillMaxWidth(),

                    color =
                        MaterialTheme.colorScheme
                            .surfaceVariant,

                    shape =
                        MaterialTheme.shapes.medium
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {

                        Text(
                            text = "File information",
                            style =
                                MaterialTheme.typography
                                    .labelLarge,
                            fontWeight =
                                FontWeight.SemiBold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        Text(
                            text = "Encoding: UTF-8",
                            style =
                                MaterialTheme.typography
                                    .bodySmall,
                            color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                        )

                        Spacer(
                            modifier =
                                Modifier.height(2.dp)
                        )

                        Text(
                            text =
                                if (trimmedFileName.isEmpty()) {
                                    "Name: Not specified"
                                } else {
                                    "Name: $trimmedFileName"
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
        },

        /*
         * -----------------------------------------------------
         * Save action
         * -----------------------------------------------------
         */

        confirmButton = {

            Button(
                onClick = {

                    if (isValidFileName) {

                        onSave(
                            trimmedFileName
                        )
                    }
                },

                enabled =
                    isValidFileName
            ) {

                Text(
                    text = "Save"
                )
            }
        },

        /*
         * -----------------------------------------------------
         * Cancel action
         * -----------------------------------------------------
         */

        dismissButton = {

            OutlinedButton(
                onClick =
                    onCancel
            ) {

                Text(
                    text = "Cancel"
                )
            }
        }
    )
}