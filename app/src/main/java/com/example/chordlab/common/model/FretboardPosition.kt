package com.example.chordlab.common.model

data class FretboardPosition(
    val stringNumber: Int,
    val fret: Int,
    val noteName: String,
    val isRoot: Boolean
)
