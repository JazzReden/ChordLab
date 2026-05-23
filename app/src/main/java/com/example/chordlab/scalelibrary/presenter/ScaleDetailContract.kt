package com.example.chordlab.scalelibrary.presenter

import com.example.chordlab.scalelibrary.model.GuitarScale

interface ScaleDetailContract {

    interface View {
        fun showScale(scale: GuitarScale, isFavorite: Boolean, rating: Int)
        fun showFavoriteStatus(isFavorite: Boolean)
        fun showRating(rating: Int)
        fun closeScreen()
    }

    interface Presenter {
        fun loadScale(scaleName: String)
        fun toggleFavorite()
        fun rateScale(rating: Int)
        fun detachView()
    }
}
