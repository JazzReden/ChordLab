package com.example.chordlab.scalelibrary.presenter

import com.example.chordlab.scalelibrary.model.GuitarScale
import com.example.chordlab.scalelibrary.model.ScalePreferenceStore
import com.example.chordlab.scalelibrary.model.ScaleRepository

class ScaleListPresenter(
    private var view: ScaleListContract.View?,
    private val scaleRepository: ScaleRepository,
    private val preferenceStore: ScalePreferenceStore
) : ScaleListContract.Presenter {

    private var selectedRoot = "A"

    override fun loadScales() {
        view?.showRoots(scaleRepository.getRootNotes())
        view?.updateSelectedRoot(selectedRoot)
        view?.showScales(scaleRepository.getScales(selectedRoot))
    }

    override fun selectRoot(rootNote: String) {
        selectedRoot = rootNote
        view?.updateSelectedRoot(selectedRoot)
        view?.showScales(scaleRepository.getScales(selectedRoot))
    }

    override fun selectScale(scale: GuitarScale) {
        view?.openScaleDetail(scale.scaleName)
    }

    override fun toggleFavorite(scale: GuitarScale) {
        preferenceStore.toggleFavorite(scale.scaleName)
        view?.showScales(scaleRepository.getScales(selectedRoot))
    }

    override fun rateScale(scale: GuitarScale, rating: Int) {
        preferenceStore.saveRating(scale.scaleName, rating)
        view?.showScales(scaleRepository.getScales(selectedRoot))
    }

    override fun detachView() {
        view = null
    }
}
