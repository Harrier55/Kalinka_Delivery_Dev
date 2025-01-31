package com.onecab.core.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/** Вспомогательный класс , добавляет  suffix после ввода значения в TextField**/

class SuffixTransformation(val suffix: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {

        val result = text + AnnotatedString(suffix)

        val textWithSuffixMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (text.isEmpty()) return 0
                if (offset >= text.length) return text.length
                return offset
            }
        }

        return TransformedText(result, textWithSuffixMapping)
    }
}