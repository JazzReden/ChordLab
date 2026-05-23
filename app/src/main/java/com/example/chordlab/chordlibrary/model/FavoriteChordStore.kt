package com.example.chordlab.chordlibrary.model

import android.content.Context

class FavoriteChordStore(context: Context) {

    private val preferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isFavorite(chordName: String): Boolean {
        return preferences.getBoolean(key(chordName), false)
    }

    fun toggleFavorite(chordName: String): Boolean {
        val newValue = !isFavorite(chordName)
        preferences.edit().putBoolean(key(chordName), newValue).apply()
        return newValue
    }

    fun removeFavorite(chordName: String): Boolean {
        return if (isFavorite(chordName)) {
            preferences.edit().remove(key(chordName)).apply()
            true
        } else {
            false
        }
    }

    fun getFavoriteNames(): List<String> {
        return preferences.all
            .filter { (entryKey, value) ->
                entryKey.startsWith(KEY_PREFIX) && value == true
            }
            .map { it.key.removePrefix(KEY_PREFIX) }
            .sorted()
    }

    private fun key(chordName: String) = "$KEY_PREFIX$chordName"

    companion object {
        private const val PREFS_NAME = "chord_favorites"
        private const val KEY_PREFIX = "favorite_"
    }
}
