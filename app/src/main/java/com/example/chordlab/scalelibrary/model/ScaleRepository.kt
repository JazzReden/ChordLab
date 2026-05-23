package com.example.chordlab.scalelibrary.model

import android.content.Context
import com.example.chordlab.common.model.FretboardPosition
import org.json.JSONArray

class ScaleRepository(private val context: Context) {

    companion object {
        const val DISPLAY_FRET_COUNT = 5
    }

    private val notes = listOf("A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#")
    private val standardTuning = listOf("E", "B", "G", "D", "A", "E")
    private val rootStartFrets = mapOf(
        "A" to 5,
        "A#" to 6,
        "B" to 7,
        "C" to 8,
        "C#" to 9,
        "D" to 10,
        "D#" to 11,
        "E" to 0,
        "F" to 1,
        "F#" to 2,
        "G" to 3,
        "G#" to 4
    )

    fun getRootNotes(): List<String> = notes

    fun getScales(rootNote: String): List<GuitarScale> {
        return loadDefinitions().map { definition ->
            val scaleNotes = definition.intervals.map { interval ->
                notes[(notes.indexOf(rootNote) + interval) % notes.size]
            }

            val startFret = rootStartFrets.getValue(rootNote)
            val allPositions = createPositions(scaleNotes, rootNote)
            val visibleEnd = if (startFret == 0) {
                DISPLAY_FRET_COUNT - 1
            } else {
                startFret + DISPLAY_FRET_COUNT - 1
            }

            GuitarScale(
                scaleName = "$rootNote ${definition.shortName}",
                rootNote = rootNote,
                scaleType = definition.type,
                notes = scaleNotes,
                fretboardPositions = allPositions.filter { it.fret in startFret..visibleEnd },
                intervalFormula = definition.formula,
                difficulty = definition.difficulty,
                beginnerTip = definition.tip,
                startFret = startFret,
                displayFretCount = DISPLAY_FRET_COUNT
            )
        }
    }

    fun getScaleByName(scaleName: String): GuitarScale? {
        return notes.flatMap { getScales(it) }.firstOrNull { it.scaleName == scaleName }
    }

    private fun createPositions(scaleNotes: List<String>, rootNote: String): List<FretboardPosition> {
        val positions = mutableListOf<FretboardPosition>()

        standardTuning.forEachIndexed { stringIndex, openNote ->
            val openIndex = notes.indexOf(openNote)
            for (fret in 0..12) {
                val noteAtFret = notes[(openIndex + fret) % notes.size]
                if (scaleNotes.contains(noteAtFret)) {
                    positions.add(
                        FretboardPosition(
                            stringNumber = stringIndex + 1,
                            fret = fret,
                            noteName = noteAtFret,
                            isRoot = noteAtFret == rootNote
                        )
                    )
                }
            }
        }

        return positions
    }

    private fun loadDefinitions(): List<ScaleDefinition> {
        val json = context.assets.open("scale_definitions.json")
            .bufferedReader()
            .use { it.readText() }
        val array = JSONArray(json)
        val definitions = mutableListOf<ScaleDefinition>()

        for (index in 0 until array.length()) {
            val item = array.getJSONObject(index)
            val intervalsArray = item.getJSONArray("intervals")
            val intervals = (0 until intervalsArray.length()).map { intervalsArray.getInt(it) }

            definitions.add(
                ScaleDefinition(
                    type = item.getString("type"),
                    shortName = item.getString("shortName"),
                    intervals = intervals,
                    formula = item.getString("formula"),
                    difficulty = ScaleDifficulty.valueOf(item.getString("difficulty")),
                    tip = item.getString("tip")
                )
            )
        }

        return definitions
    }
}
