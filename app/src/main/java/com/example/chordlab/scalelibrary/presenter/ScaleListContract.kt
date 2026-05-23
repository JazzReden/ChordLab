package com.example.chordlab.scalelibrary.presenter

import com.example.chordlab.scalelibrary.model.GuitarScale

interface ScaleListContract {

    interface View {
        fun showRoots(roots: List<String>)
        fun showScales(scales: List<GuitarScale>)
        fun updateSelectedRoot(rootNote: String)
        fun openScaleDetail(scaleName: String)
    }

    interface Presenter {
        fun loadScales()
        fun selectRoot(rootNote: String)
        fun selectScale(scale: GuitarScale)
        fun toggleFavorite(scale: GuitarScale)
        fun rateScale(scale: GuitarScale, rating: Int)
        fun detachView()
    }
}
