package com.example.chordlab.chordlibrary.presenter

import com.example.chordlab.chordlibrary.model.ChordRepository
import com.example.chordlab.chordlibrary.model.ChordType
import com.example.chordlab.chordlibrary.model.FavoriteChordStore
import com.example.chordlab.chordlibrary.model.GuitarChord

class ChordListPresenter(
    private var view: ChordListContract.View?,
    private val chordRepository: ChordRepository,
    private val favoriteChordStore: FavoriteChordStore
) : ChordListContract.Presenter {

    private var selectedType = ChordType.ALL
    private var searchQuery = ""

    override fun loadChords() {
        showFilteredChords()
    }

    override fun searchChords(query: String) {
        searchQuery = query
        showFilteredChords()
    }

    override fun filterByType(type: ChordType) {
        selectedType = type
        showFilteredChords()
    }

    override fun selectChord(chord: GuitarChord) {
        view?.openChordDetail(chord.chordName)
    }

    override fun toggleFavorite(chord: GuitarChord) {
        favoriteChordStore.toggleFavorite(chord.chordName)
        showFilteredChords()
    }

    override fun detachView() {
        view = null
    }

    private fun showFilteredChords() {
        val filteredChords = chordRepository.getChords()
            .filter { selectedType == ChordType.ALL || it.chordType == selectedType }
            .filter { it.chordName.contains(searchQuery, ignoreCase = true) }

        if (filteredChords.isEmpty()) {
            view?.showEmptyMessage()
        } else {
            view?.showChords(filteredChords)
        }
    }
}
