package com.example.chordlab.favorites.presenter

import com.example.chordlab.chordlibrary.model.GuitarChord
import com.example.chordlab.scalelibrary.model.GuitarScale

interface FavoritesContract {

    interface View {
        fun showFavoriteChords(chords: List<GuitarChord>)
        fun showFavoriteScales(scales: List<GuitarScale>)
        fun showEmptyState(isEmpty: Boolean)
        fun openChordDetail(chordName: String)
        fun openScaleDetail(scaleName: String)
    }

    interface Presenter {
        fun loadFavorites()
        fun selectChord(chord: GuitarChord)
        fun selectScale(scale: GuitarScale)
        fun toggleChordFavorite(chord: GuitarChord)
        fun toggleScaleFavorite(scale: GuitarScale)
        fun rateScale(scale: GuitarScale, rating: Int)
        fun detachView()
    }
}
