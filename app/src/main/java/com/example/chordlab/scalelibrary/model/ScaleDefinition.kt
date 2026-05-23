package com.example.chordlab.scalelibrary.model

data class ScaleDefinition(
    val type: String,
    val shortName: String,
    val intervals: List<Int>,
    val formula: String,
    val difficulty: ScaleDifficulty,
    val tip: String
)
