package com.example.chordlab.scalelibrary.model

import android.content.Context

class ScalePreferenceStore(context: Context) {

    private val preferences = context.getSharedPreferences("scale_preferences", Context.MODE_PRIVATE)

    fun isFavorite(scaleName: String): Boolean {
        return preferences.getBoolean("favorite_$scaleName", false)
    }

    fun toggleFavorite(scaleName: String): Boolean {
        val newValue = !isFavorite(scaleName)
        preferences.edit().putBoolean("favorite_$scaleName", newValue).apply()
        return newValue
    }

    fun getRating(scaleName: String): Int {
        return preferences.getInt("rating_$scaleName", 0)
    }

    fun saveRating(scaleName: String, rating: Int) {
        preferences.edit().putInt("rating_$scaleName", rating).apply()
    }

    fun getFavoriteNames(): List<String> {
        return preferences.all
            .filter { (entryKey, value) ->
                entryKey.startsWith("favorite_") && value == true
            }
            .map { it.key.removePrefix("favorite_") }
            .sorted()
    }
}
