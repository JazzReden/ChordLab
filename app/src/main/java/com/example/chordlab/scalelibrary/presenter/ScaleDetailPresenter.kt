package com.example.chordlab.scalelibrary.presenter

import com.example.chordlab.scalelibrary.model.GuitarScale
import com.example.chordlab.scalelibrary.model.ScalePreferenceStore
import com.example.chordlab.scalelibrary.model.ScaleRepository

class ScaleDetailPresenter(
    private var view: ScaleDetailContract.View?,
    private val scaleRepository: ScaleRepository,
    private val preferenceStore: ScalePreferenceStore
) : ScaleDetailContract.Presenter {

    private var selectedScale: GuitarScale? = null

    override fun loadScale(scaleName: String) {
        selectedScale = scaleRepository.getScaleByName(scaleName)

        val scale = selectedScale
        if (scale == null) {
            view?.closeScreen()
            return
        }

        view?.showScale(
            scale = scale,
            isFavorite = preferenceStore.isFavorite(scale.scaleName),
            rating = preferenceStore.getRating(scale.scaleName)
        )
    }

    override fun toggleFavorite() {
        val scale = selectedScale ?: return
        view?.showFavoriteStatus(preferenceStore.toggleFavorite(scale.scaleName))
    }

    override fun rateScale(rating: Int) {
        val scale = selectedScale ?: return
        preferenceStore.saveRating(scale.scaleName, rating)
        view?.showRating(rating)
    }

    override fun detachView() {
        view = null
    }
}
