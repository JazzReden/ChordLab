package com.example.chordlab.favorites.model

import com.example.chordlab.chordlibrary.model.ChordRepository
import com.example.chordlab.chordlibrary.model.FavoriteChordStore
import com.example.chordlab.chordlibrary.model.GuitarChord
import com.example.chordlab.scalelibrary.model.GuitarScale
import com.example.chordlab.scalelibrary.model.ScalePreferenceStore
import com.example.chordlab.scalelibrary.model.ScaleRepository

class FavoritesRepository(
    private val chordRepository: ChordRepository,
    private val favoriteChordStore: FavoriteChordStore,
    private val scaleRepository: ScaleRepository,
    private val scalePreferenceStore: ScalePreferenceStore
) {

    fun getFavoriteChords(): List<GuitarChord> {
        val names = favoriteChordStore.getFavoriteNames().toSet()
        return chordRepository.getChords().filter { names.contains(it.chordName) }
    }

    fun getFavoriteScales(): List<GuitarScale> {
        val names = scalePreferenceStore.getFavoriteNames().toSet()
        return scaleRepository.getRootNotes()
            .flatMap { scaleRepository.getScales(it) }
            .filter { names.contains(it.scaleName) }
    }

    fun toggleChordFavorite(chordName: String) {
        favoriteChordStore.toggleFavorite(chordName)
    }

    fun toggleScaleFavorite(scaleName: String) {
        scalePreferenceStore.toggleFavorite(scaleName)
    }

    fun saveScaleRating(scaleName: String, rating: Int) {
        scalePreferenceStore.saveRating(scaleName, rating)
    }
}
