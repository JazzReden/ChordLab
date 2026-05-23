package com.example.chordlab.dashboard.presenter

import com.example.chordlab.dashboard.model.DashboardItem

interface DashboardContract {

    interface View {
        fun showWelcomeMessage(message: String)
        fun showDashboardItems(items: List<DashboardItem>)
        fun showFeatureMessage(message: String)
        fun openChordLibrary()
        fun openScaleLibrary()
        fun openQuiz()
        fun openFavorites()
        fun openProfile()
    }

    interface Presenter {
        fun loadDashboard()
        fun onFeatureSelected(item: DashboardItem)
        fun detachView()
    }
}
