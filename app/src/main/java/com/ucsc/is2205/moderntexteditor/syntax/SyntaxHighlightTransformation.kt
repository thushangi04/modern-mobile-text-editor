package com.ucsc.is2205.moderntexteditor.syntax

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class SyntaxHighlightTransformation(
    private val fileName: String
) : VisualTransformation {

    override fun filter(
        text: AnnotatedString
    ): TransformedText {

        val highlighted =
            SyntaxHighlighter.highlight(
                text = text.text,
                fileName = fileName
            )

        return TransformedText(
            text = highlighted,
            offsetMapping = OffsetMapping.Identity
        )
    }
}