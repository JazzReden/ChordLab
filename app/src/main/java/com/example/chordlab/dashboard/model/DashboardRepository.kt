package com.example.chordlab.dashboard.model

class DashboardRepository {

    fun getDashboardItems(): List<DashboardItem> {
        return listOf(
            DashboardItem("Chord Library", "Learn beginner to advanced guitar chords"),
            DashboardItem("Scale Library", "Practice useful scales for guitar playing"),
            DashboardItem("Quiz System", "Answer easy, medium, and hard quizzes"),
            DashboardItem("Favorites", "Review your saved chords and scales")
        )
    }
}
