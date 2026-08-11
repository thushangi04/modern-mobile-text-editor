package com.ucsc.is2205.moderntexteditor.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucsc.is2205.moderntexteditor.syntax.SyntaxHighlightTransformation

@Composable
fun EditorTextArea(
    text: String,
    fileName: String,
    isReadOnly: Boolean,
    wordWrapEnabled: Boolean,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    /*
     * ---------------------------------------------------------
     * Scroll states
     * ---------------------------------------------------------
     */

    val verticalScrollState =
        rememberScrollState()

    val horizontalScrollState =
        rememberScrollState()

    /*
     * ---------------------------------------------------------
     * Editor typography
     * ---------------------------------------------------------
     */

    val editorTextStyle =
        TextStyle(
            color =
                MaterialTheme.colorScheme.onSurface,

            fontFamily =
                FontFamily.Monospace,

            fontSize = 14.sp,

            lineHeight = 22.sp
        )

    val lineNumberStyle =
        TextStyle(
            color =
                MaterialTheme.colorScheme.onSurfaceVariant,

            fontFamily =
                FontFamily.Monospace,

            fontSize = 12.sp,

            lineHeight = 22.sp,

            textAlign =
                TextAlign.End
        )

    /*
     * ---------------------------------------------------------
     * Line numbers
     * ---------------------------------------------------------
     */

    val lineCount =
        remember(text) {

            if (text.isEmpty()) {
                1
            } else {
                text.count {
                    it == '\n'
                } + 1
            }
        }

    val lineNumbers =
        remember(lineCount) {

            buildString {

                for (lineNumber in 1..lineCount) {

                    append(lineNumber)

                    if (lineNumber != lineCount) {
                        append('\n')
                    }
                }
            }
        }

    /*
     * ---------------------------------------------------------
     * Scrolling
     * ---------------------------------------------------------
     */

    val scrollModifier =
        if (wordWrapEnabled) {

            Modifier.verticalScroll(
                verticalScrollState
            )

        } else {

            Modifier
                .verticalScroll(
                    verticalScrollState
                )
                .horizontalScroll(
                    horizontalScrollState
                )
        }

    /*
     * ---------------------------------------------------------
     * Syntax highlighting
     * ---------------------------------------------------------
     */

    val syntaxTransformation =
        remember(fileName) {

            SyntaxHighlightTransformation(
                fileName
            )
        }

    /*
     * ---------------------------------------------------------
     * Editor
     * ---------------------------------------------------------
     */

    BasicTextField(
        value = text,

        onValueChange = { newText ->

            if (!isReadOnly) {

                onTextChanged(
                    newText
                )
            }
        },

        modifier = modifier
            .then(
                scrollModifier
            ),

        enabled = !isReadOnly,

        readOnly = isReadOnly,

        singleLine = false,

        maxLines = Int.MAX_VALUE,

        textStyle =
            editorTextStyle,

        visualTransformation =
            syntaxTransformation,

        cursorBrush =
            SolidColor(
                MaterialTheme.colorScheme.primary
            ),

        decorationBox = { innerTextField ->

            Surface(
                modifier =
                    Modifier.fillMaxSize(),

                color =
                    MaterialTheme.colorScheme.surface,

                tonalElevation = 1.dp,

                shadowElevation = 1.dp,

                shape =
                    MaterialTheme.shapes.medium
            ) {

                /*
                 * -------------------------------------------------
                 * Editor layout
                 *
                 * Line numbers | Editor content
                 * -------------------------------------------------
                 */

                Row(
                    modifier =
                        Modifier.fillMaxSize()
                ) {

                    /*
                     * ---------------------------------------------
                     * Line number gutter
                     * ---------------------------------------------
                     */

                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .fillMaxHeight()
                            .background(
                                MaterialTheme.colorScheme
                                    .surfaceVariant
                                    .copy(
                                        alpha = 0.30f
                                    )
                            )
                            .padding(
                                top = 14.dp,
                                end = 10.dp,
                                bottom = 14.dp
                            )
                    ) {

                        Text(
                            text =
                                lineNumbers,

                            style =
                                lineNumberStyle,

                            modifier =
                                Modifier.fillMaxSize()
                        )
                    }

                    /*
                     * ---------------------------------------------
                     * Gutter divider
                     * ---------------------------------------------
                     */

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(
                                MaterialTheme.colorScheme
                                    .outlineVariant
                            )
                    )

                    /*
                     * ---------------------------------------------
                     * Text editing area
                     * ---------------------------------------------
                     */

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = 14.dp,
                                top = 14.dp,
                                end = 14.dp,
                                bottom = 14.dp
                            )
                    ) {

                        /*
                         * Placeholder
                         */

                        if (text.isEmpty()) {

                            Text(
                                text =
                                    when {

                                        fileName
                                            .lowercase()
                                            .endsWith(".kt") -> {

                                            "Start typing Kotlin..."
                                        }

                                        fileName
                                            .lowercase()
                                            .endsWith(".kts") -> {

                                            "Start typing Kotlin script..."
                                        }

                                        fileName
                                            .lowercase()
                                            .endsWith(".md") -> {

                                            "Start typing Markdown..."
                                        }

                                        fileName
                                            .lowercase()
                                            .endsWith(".markdown") -> {

                                            "Start typing Markdown..."
                                        }

                                        !wordWrapEnabled -> {

                                            "Start typing... (word wrap disabled)"
                                        }

                                        else -> {

                                            "Start typing..."
                                        }
                                    },

                                style =
                                    editorTextStyle.copy(
                                        color =
                                            MaterialTheme.colorScheme
                                                .onSurfaceVariant
                                                .copy(
                                                    alpha = 0.65f
                                                )
                                    )
                            )
                        }

                        /*
                         * Actual editable text field
                         */

                        innerTextField()
                    }
                }
            }
        }
    )
}