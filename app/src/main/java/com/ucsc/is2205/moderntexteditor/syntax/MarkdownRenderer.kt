package com.ucsc.is2205.moderntexteditor.syntax

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MarkdownRenderer(
    markdown: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        markdown
            .lines()
            .forEach { line ->

                when {
                    line.startsWith("### ") -> {
                        Text(
                            text = line.removePrefix("### "),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    line.startsWith("## ") -> {
                        Text(
                            text = line.removePrefix("## "),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }

                    line.startsWith("# ") -> {
                        Text(
                            text = line.removePrefix("# "),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    line.startsWith("- ") -> {
                        Text(
                            text = "• ${line.removePrefix("- ")}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }

                    line.startsWith("* ") -> {
                        Text(
                            text = "• ${line.removePrefix("* ")}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }

                    line.isBlank() -> {
                        Text(
                            text = "",
                            modifier = Modifier.padding(vertical = 3.dp)
                        )
                    }

                    else -> {
                        Text(
                            text = parseInlineMarkdown(line),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
    }

}

private fun parseInlineMarkdown(
    text: String
): androidx.compose.ui.text.AnnotatedString {

    return buildAnnotatedString {

        var index = 0

        while (index < text.length) {

            if (
                index + 1 < text.length &&
                text[index] == '*' &&
                text[index + 1] == '*'
            ) {
                val end =
                    text.indexOf(
                        "**",
                        index + 2
                    )

                if (end >= 0) {

                    pushStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    append(
                        text.substring(
                            index + 2,
                            end
                        )
                    )

                    pop()

                    index = end + 2
                    continue
                }
            }

            if (text[index] == '*') {

                val end =
                    text.indexOf(
                        '*',
                        index + 1
                    )

                if (end >= 0) {

                    pushStyle(
                        SpanStyle(
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    append(
                        text.substring(
                            index + 1,
                            end
                        )
                    )

                    pop()

                    index = end + 1
                    continue
                }
            }

            append(text[index])
            index++
        }
    }
}
