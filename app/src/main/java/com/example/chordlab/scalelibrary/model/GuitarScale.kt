package com.example.chordlab.scalelibrary.model

import com.example.chordlab.common.model.FretboardPosition

data class GuitarScale(
    val scaleName: String,
    val rootNote: String,
    val scaleType: String,
    val notes: List<String>,
    val fretboardPositions: List<FretboardPosition>,
    val intervalFormula: String,
    val difficulty: ScaleDifficulty,
    val beginnerTip: String,
    val startFret: Int,
    val displayFretCount: Int = ScaleRepository.DISPLAY_FRET_COUNT
) {
    fun notesSummaryLabel(): String = notes.joinToString("  ·  ")
}
