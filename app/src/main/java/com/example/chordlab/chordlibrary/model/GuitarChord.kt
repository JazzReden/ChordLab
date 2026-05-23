package com.example.chordlab.chordlibrary.model

data class GuitarChord(
    val chordName: String,
    val chordType: ChordType,
    val chordImage: String,
    val fingerPlacement: String,
    val stringIndicators: List<String>,
    val bottomStringLabels: List<String> = emptyList(),
    val description: String,
    val markers: List<FretMarker>,
    val barreFret: Int? = null,
    val startingFret: Int = 0
)
