package com.ucsc.is2205.moderntexteditor.syntax

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

object SyntaxHighlighter {

    private val keywordColor = Color(0xFFD97706)
    private val stringColor = Color(0xFF16A34A)
    private val commentColor = Color(0xFF78716C)
    private val annotationColor = Color(0xFF7C3AED)

    private val kotlinKeywords = setOf(
        "as",
        "break",
        "class",
        "continue",
        "do",
        "else",
        "false",
        "for",
        "fun",
        "if",
        "in",
        "interface",
        "is",
        "null",
        "object",
        "package",
        "return",
        "super",
        "this",
        "throw",
        "true",
        "try",
        "typealias",
        "typeof",
        "val",
        "var",
        "when",
        "while",
        "by",
        "catch",
        "constructor",
        "delegate",
        "dynamic",
        "field",
        "file",
        "finally",
        "get",
        "import",
        "init",
        "param",
        "property",
        "receiver",
        "set",
        "setparam",
        "where",
        "actual",
        "abstract",
        "annotation",
        "companion",
        "const",
        "crossinline",
        "data",
        "enum",
        "expect",
        "external",
        "final",
        "infix",
        "inline",
        "inner",
        "internal",
        "lateinit",
        "noinline",
        "open",
        "operator",
        "out",
        "override",
        "private",
        "protected",
        "public",
        "reified",
        "sealed",
        "suspend",
        "tailrec",
        "vararg"
    )

    fun highlight(
        text: String,
        fileName: String
    ): AnnotatedString {

        return when {
            isKotlinFile(fileName) -> {
                highlightKotlin(text)
            }

            isMarkdownFile(fileName) -> {
                highlightMarkdown(text)
            }

            else -> {
                AnnotatedString(text)
            }
        }
    }

    private fun isKotlinFile(
        fileName: String
    ): Boolean {
        return fileName
            .lowercase()
            .endsWith(".kt")
    }

    private fun isMarkdownFile(
        fileName: String
    ): Boolean {
        return fileName
            .lowercase()
            .endsWith(".md")
    }

    private fun highlightKotlin(
        text: String
    ): AnnotatedString {

        return buildAnnotatedString {

            append(text)

            highlightComments(
                text = text,
                builder = this
            )

            highlightStrings(
                text = text,
                builder = this
            )

            highlightAnnotations(
                text = text,
                builder = this
            )

            highlightKeywords(
                text = text,
                builder = this
            )
        }
    }

    private fun highlightKeywords(
        text: String,
        builder: AnnotatedString.Builder
    ) {

        var index = 0

        while (index < text.length) {

            if (isWordChar(text[index])) {

                val start = index

                while (
                    index < text.length &&
                    isWordChar(text[index])
                ) {
                    index++
                }

                val word =
                    text.substring(start, index)

                if (word in kotlinKeywords) {

                    builder.addStyle(
                        style = SpanStyle(
                            color = keywordColor
                        ),
                        start = start,
                        end = index
                    )
                }

            } else {
                index++
            }
        }
    }

    private fun highlightStrings(
        text: String,
        builder: AnnotatedString.Builder
    ) {

        var index = 0

        while (index < text.length) {

            if (text[index] == '"') {

                val start = index
                index++

                while (index < text.length) {

                    if (
                        text[index] == '"' &&
                        text.getOrNull(index - 1) != '\\'
                    ) {
                        index++
                        break
                    }

                    index++
                }

                builder.addStyle(
                    style = SpanStyle(
                        color = stringColor
                    ),
                    start = start,
                    end = index.coerceAtMost(text.length)
                )

            } else {
                index++
            }
        }
    }

    private fun highlightComments(
        text: String,
        builder: AnnotatedString.Builder
    ) {

        var index = 0

        while (index < text.length - 1) {

            if (
                text[index] == '/' &&
                text[index + 1] == '/'
            ) {

                val start = index

                while (
                    index < text.length &&
                    text[index] != '\n'
                ) {
                    index++
                }

                builder.addStyle(
                    style = SpanStyle(
                        color = commentColor
                    ),
                    start = start,
                    end = index
                )

            } else {
                index++
            }
        }
    }

    private fun highlightAnnotations(
        text: String,
        builder: AnnotatedString.Builder
    ) {

        var index = 0

        while (index < text.length) {

            if (text[index] == '@') {

                val start = index
                index++

                while (
                    index < text.length &&
                    isWordChar(text[index])
                ) {
                    index++
                }

                builder.addStyle(
                    style = SpanStyle(
                        color = annotationColor
                    ),
                    start = start,
                    end = index
                )

            } else {
                index++
            }
        }
    }

    private fun highlightMarkdown(
        text: String
    ): AnnotatedString {

        val markdownColor = Color(0xFF2563EB)

        return buildAnnotatedString {

            append(text)

            val lines =
                text.split("\n")

            var currentPosition = 0

            lines.forEach { line ->

                val trimmed =
                    line.trimStart()

                val headingStart =
                    currentPosition +
                            (line.length -
                                    trimmed.length)

                if (trimmed.startsWith("#")) {

                    val headingEnd =
                        currentPosition +
                                line.length

                    addStyle(
                        style = SpanStyle(
                            color = markdownColor
                        ),
                        start = headingStart,
                        end = headingEnd
                    )
                }

                currentPosition +=
                    line.length + 1
            }
        }
    }

    private fun isWordChar(
        character: Char
    ): Boolean {
        return character.isLetterOrDigit() ||
                character == '_'
    }
}