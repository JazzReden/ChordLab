package com.example.chordlab.favorites.presenter

import com.example.chordlab.chordlibrary.model.GuitarChord
import com.example.chordlab.favorites.model.FavoritesRepository
import com.example.chordlab.scalelibrary.model.GuitarScale

class FavoritesPresenter(
    private var view: FavoritesContract.View?,
    private val favoritesRepository: FavoritesRepository
) : FavoritesContract.Presenter {

    override fun loadFavorites() {
        val chords = favoritesRepository.getFavoriteChords()
        val scales = favoritesRepository.getFavoriteScales()

        view?.showFavoriteChords(chords)
        view?.showFavoriteScales(scales)
        view?.showEmptyState(chords.isEmpty() && scales.isEmpty())
    }

    override fun selectChord(chord: GuitarChord) {
        view?.openChordDetail(chord.chordName)
    }

    override fun selectScale(scale: GuitarScale) {
        view?.openScaleDetail(scale.scaleName)
    }

    override fun toggleChordFavorite(chord: GuitarChord) {
        favoritesRepository.toggleChordFavorite(chord.chordName)
        loadFavorites()
    }

    override fun toggleScaleFavorite(scale: GuitarScale) {
        favoritesRepository.toggleScaleFavorite(scale.scaleName)
        loadFavorites()
    }

    override fun rateScale(scale: GuitarScale, rating: Int) {
        favoritesRepository.saveScaleRating(scale.scaleName, rating)
        loadFavorites()
    }

    override fun detachView() {
        view = null
    }
}
