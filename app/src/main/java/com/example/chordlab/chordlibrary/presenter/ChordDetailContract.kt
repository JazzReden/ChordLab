package com.example.chordlab.chordlibrary.presenter

import com.example.chordlab.chordlibrary.model.GuitarChord

interface ChordDetailContract {

    interface View {
        fun showChord(chord: GuitarChord, isFavorite: Boolean)
        fun showFavoriteStatus(isFavorite: Boolean)
        fun closeScreen()
    }

    interface Presenter {
        fun loadChord(chordName: String)
        fun toggleFavorite()
        fun removeFavorite()
        fun detachView()
    }
}
