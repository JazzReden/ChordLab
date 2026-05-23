package com.example.chordlab.chordlibrary.presenter

import com.example.chordlab.chordlibrary.model.ChordRepository
import com.example.chordlab.chordlibrary.model.FavoriteChordStore
import com.example.chordlab.chordlibrary.model.GuitarChord

class ChordDetailPresenter(
    private var view: ChordDetailContract.View?,
    private val chordRepository: ChordRepository,
    private val favoriteChordStore: FavoriteChordStore
) : ChordDetailContract.Presenter {

    private var selectedChord: GuitarChord? = null

    override fun loadChord(chordName: String) {
        selectedChord = chordRepository.getChordByName(chordName)

        val chord = selectedChord
        if (chord == null) {
            view?.closeScreen()
            return
        }

        view?.showChord(chord, favoriteChordStore.isFavorite(chord.chordName))
    }

    override fun toggleFavorite() {
        val chord = selectedChord ?: return
        val isFavorite = favoriteChordStore.toggleFavorite(chord.chordName)
        view?.showFavoriteStatus(isFavorite)
    }

    override fun removeFavorite() {
        val chord = selectedChord ?: return
        if (favoriteChordStore.removeFavorite(chord.chordName)) {
            view?.showFavoriteStatus(false)
        }
    }

    override fun detachView() {
        view = null
    }
}
