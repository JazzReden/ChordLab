package com.example.chordlab.chordlibrary.model

class ChordRepository {

    private val chromaticNotes = listOf("A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#")

    private val startingFrets = mapOf(
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

    fun getChords(): List<GuitarChord> {
        return majorChords() + minorChords() + powerChords() + major7Chords() + minor7Chords()
    }

    fun getChordByName(chordName: String): GuitarChord? {
        return getChords().firstOrNull { it.chordName == chordName }
    }

    private fun majorChords(): List<GuitarChord> {
        return chromaticNotes.map { note ->
            val start = startingFrets.getValue(note)
            if (note == "E") {
                GuitarChord(
                    chordName = "E Major",
                    chordType = ChordType.MAJOR,
                    chordImage = "diagram_E Major",
                    fingerPlacement = "Index: G1, Middle: A2, Ring: D2",
                    stringIndicators = listOf("0", "2", "2", "1", "0", "0"),
                    description = "A full open major chord with a strong beginner-friendly sound.",
                    markers = listOf(FretMarker(2, 2), FretMarker(3, 2), FretMarker(4, 1)),
                    startingFret = 0
                )
            } else {
                GuitarChord(
                    chordName = "$note Major",
                    chordType = ChordType.MAJOR,
                    chordImage = "diagram_$note Major",
                    fingerPlacement = "Index: fret ${start} barre, Middle: G${start + 1}, Ring: A${start + 2}, Pinky: D${start + 2}",
                    stringIndicators = listOf(start.toString(), (start + 2).toString(), (start + 2).toString(), (start + 1).toString(), start.toString(), start.toString()),
                    description = "$note Major is a bright sounding chord. Practice it slowly and press each string clearly.",
                    markers = listOf(FretMarker(1, 1), FretMarker(2, 3), FretMarker(3, 3), FretMarker(4, 2), FretMarker(5, 1), FretMarker(6, 1)),
                    barreFret = 1,
                    startingFret = start
                )
            }
        }
    }

    private fun minorChords(): List<GuitarChord> {
        return chromaticNotes.map { note ->
            val start = startingFrets.getValue(note)
            if (note == "E") {
                GuitarChord(
                    chordName = "E Minor",
                    chordType = ChordType.MINOR,
                    chordImage = "diagram_E Minor",
                    fingerPlacement = "Middle: A2, Ring: D2",
                    stringIndicators = listOf("0", "2", "2", "0", "0", "0"),
                    description = "E Minor is one of the easiest guitar chords for beginners.",
                    markers = listOf(FretMarker(2, 2), FretMarker(3, 2)),
                    startingFret = 0
                )
            } else {
                GuitarChord(
                    chordName = "$note Minor",
                    chordType = ChordType.MINOR,
                    chordImage = "diagram_$note Minor",
                    fingerPlacement = "Index: fret ${start} barre, Ring: A${start + 2}, Pinky: D${start + 2}",
                    stringIndicators = listOf(start.toString(), (start + 2).toString(), (start + 2).toString(), start.toString(), start.toString(), start.toString()),
                    description = "$note Minor has a softer sound than the major version. Keep the barre finger flat.",
                    markers = listOf(FretMarker(1, 1), FretMarker(2, 3), FretMarker(3, 3), FretMarker(4, 1), FretMarker(5, 1), FretMarker(6, 1)),
                    barreFret = 1,
                    startingFret = start
                )
            }
        }
    }

    private fun powerChords(): List<GuitarChord> {
        return chromaticNotes.map { note ->
            buildPowerChord(note)
        }
    }

    private fun buildPowerChord(root: String): GuitarChord {
        val fifth = chromaticNotes[(chromaticNotes.indexOf(root) + 7) % chromaticNotes.size]
        val start = startingFrets.getValue(root)

        return if (start == 0) {
            GuitarChord(
                chordName = "${root}5",
                chordType = ChordType.POWER,
                chordImage = "diagram_${root}5",
                fingerPlacement = "Open low E, Index: A2, Middle: D2. Mute G, B, and high E.",
                stringIndicators = listOf("0", "2", "2", "X", "X", "X"),
                bottomStringLabels = listOf(root, fifth, root, "", "", ""),
                description = "${root}5 is an open-position power chord. Keep the top three strings muted.",
                markers = listOf(
                    FretMarker(1, 0),
                    FretMarker(2, 2),
                    FretMarker(3, 2)
                ),
                startingFret = 0
            )
        } else {
            val lowFret = start
            val upperFret = start + 2
            GuitarChord(
                chordName = "${root}5",
                chordType = ChordType.POWER,
                chordImage = "diagram_${root}5",
                fingerPlacement = "Index: low E$lowFret, Ring: A$upperFret, Pinky: D$upperFret. Mute G, B, and high E.",
                stringIndicators = listOf("", "", "", "X", "X", "X"),
                bottomStringLabels = listOf(root, fifth, root, "", "", ""),
                description = "${root}5 is a movable power chord shape. Root on fret $lowFret with the fifth on fret $upperFret.",
                markers = listOf(
                    FretMarker(1, 1),
                    FretMarker(2, 3),
                    FretMarker(3, 3)
                ),
                startingFret = lowFret
            )
        }
    }

    private fun major7Chords(): List<GuitarChord> {
        return chromaticNotes.map { note ->
            val start = startingFrets.getValue(note).let { if (it == 0) 12 else it }
            GuitarChord(
                chordName = "${note}maj7",
                chordType = ChordType.MAJOR_7,
                chordImage = "diagram_${note}maj7",
                fingerPlacement = "Index: low E$start, Middle: D${start + 1}, Ring: G${start + 1}, Pinky: B${start + 1}",
                stringIndicators = listOf(start.toString(), "x", (start + 1).toString(), (start + 1).toString(), (start + 1).toString(), "x"),
                description = "${note}maj7 adds a smooth color to a major chord while staying easy to recognize.",
                markers = listOf(FretMarker(1, 1), FretMarker(3, 2), FretMarker(4, 2), FretMarker(5, 2)),
                startingFret = start
            )
        }
    }

    private fun minor7Chords(): List<GuitarChord> {
        return chromaticNotes.map { note ->
            val start = startingFrets.getValue(note).let { if (it == 0) 12 else it }
            GuitarChord(
                chordName = "${note}m7",
                chordType = ChordType.MINOR_7,
                chordImage = "diagram_${note}m7",
                fingerPlacement = "Index: fret ${start} barre, Ring: A${start + 2}",
                stringIndicators = listOf(start.toString(), (start + 2).toString(), start.toString(), start.toString(), start.toString(), start.toString()),
                description = "${note}m7 has a relaxed minor sound. It is useful for simple progressions and practice.",
                markers = listOf(FretMarker(1, 1), FretMarker(2, 3), FretMarker(3, 1), FretMarker(4, 1), FretMarker(5, 1), FretMarker(6, 1)),
                barreFret = 1,
                startingFret = start
            )
        }
    }
}
