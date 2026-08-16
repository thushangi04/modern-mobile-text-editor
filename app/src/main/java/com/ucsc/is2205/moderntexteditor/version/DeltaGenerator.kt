package com.ucsc.is2205.moderntexteditor.version

import com.github.difflib.DiffUtils
import com.github.difflib.UnifiedDiffUtils
import com.github.difflib.patch.Patch

object DeltaGenerator {

    /**
     * Computes the unified diff patch string to convert [originalText] into [newText].
     * The unified diff format is a standard way to represent patches.
     */
    fun computePatch(originalText: String, newText: String): String {
        val originalLines = originalText.lines()
        val newLines = newText.lines()

        val patch: Patch<String> = DiffUtils.diff(originalLines, newLines)
        
        // Generate unified diff
        val unifiedDiffLines = UnifiedDiffUtils.generateUnifiedDiff(
            "original",
            "new",
            originalLines,
            patch,
            3 // Context size
        )
        
        return unifiedDiffLines.joinToString("\n")
    }

    /**
     * Applies a unified diff [patchData] to the [originalText] to reconstruct the new text.
     */
    fun applyPatch(originalText: String, patchData: String): String {
        if (patchData.isBlank()) {
            return originalText
        }
        val originalLines = originalText.lines()
        val patchLines = patchData.lines()

        val patch = UnifiedDiffUtils.parseUnifiedDiff(patchLines)
        
        val newLines = DiffUtils.patch(originalLines, patch)
        return newLines.joinToString("\n")
    }
}
