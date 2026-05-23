package com.example.chordlab.dashboard.presenter

import com.example.chordlab.dashboard.model.DashboardItem
import com.example.chordlab.dashboard.model.DashboardRepository

class DashboardPresenter(
    private var view: DashboardContract.View?,
    private val dashboardRepository: DashboardRepository
) : DashboardContract.Presenter {

    override fun loadDashboard() {
        view?.showWelcomeMessage("Welcome to ChordLab")
        view?.showDashboardItems(dashboardRepository.getDashboardItems())
    }

    override fun onFeatureSelected(item: DashboardItem) {
        when (item.title) {
            "Chord Library" -> view?.openChordLibrary()
            "Scale Library" -> view?.openScaleLibrary()
            "Quiz System" -> view?.openQuiz()
            "Favorites" -> view?.openFavorites()
            else -> view?.showFeatureMessage("${item.title} will be added next")
        }
    }

    override fun detachView() {
        view = null
    }
}
