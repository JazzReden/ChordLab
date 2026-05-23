package com.example.chordlab.chordlibrary.presenter

import com.example.chordlab.chordlibrary.model.ChordType
import com.example.chordlab.chordlibrary.model.GuitarChord

interface ChordListContract {

    interface View {
        fun showChords(chords: List<GuitarChord>)
        fun showEmptyMessage()
        fun openChordDetail(chordName: String)
    }

    interface Presenter {
        fun loadChords()
        fun searchChords(query: String)
        fun filterByType(type: ChordType)
        fun selectChord(chord: GuitarChord)
        fun toggleFavorite(chord: GuitarChord)
        fun detachView()
    }
}
