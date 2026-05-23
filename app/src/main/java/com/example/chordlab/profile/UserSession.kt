package com.example.chordlab.profile

import android.content.Context

object UserSession {

    private const val PREFS_NAME = "user_session"
    private const val KEY_LOGGED_IN = "logged_in"
    private const val KEY_EMAIL = "email"
    private const val KEY_DISPLAY_NAME = "display_name"
    private const val KEY_BIO = "bio"
    private const val KEY_PHOTO_URI = "photo_uri"

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveLogin(context: Context, email: String) {
        prefs(context).edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_EMAIL, email)
            .apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_LOGGED_IN, false)
    }

    fun getEmail(context: Context): String {
        return prefs(context).getString(KEY_EMAIL, "").orEmpty()
    }

    fun getDisplayName(context: Context): String {
        val stored = prefs(context).getString(KEY_DISPLAY_NAME, null)
        if (!stored.isNullOrBlank()) return stored
        val email = getEmail(context)
        return email.substringBefore("@").replaceFirstChar { it.uppercase() }
    }

    fun saveDisplayName(context: Context, name: String) {
        prefs(context).edit().putString(KEY_DISPLAY_NAME, name.trim()).apply()
    }

    fun getBio(context: Context): String {
        return prefs(context).getString(KEY_BIO, "").orEmpty()
    }

    fun saveBio(context: Context, bio: String) {
        prefs(context).edit().putString(KEY_BIO, bio.trim()).apply()
    }

    fun getPhotoUri(context: Context): String? {
        return prefs(context).getString(KEY_PHOTO_URI, null)
    }

    fun savePhotoUri(context: Context, uri: String?) {
        prefs(context).edit().apply {
            if (uri.isNullOrBlank()) remove(KEY_PHOTO_URI) else putString(KEY_PHOTO_URI, uri)
        }.apply()
    }

    fun clearSession(context: Context) {
        prefs(context).edit().clear().apply()
    }

    fun initials(context: Context): String {
        val name = getDisplayName(context)
        val parts = name.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
        return when {
            parts.isEmpty() -> "CL"
            parts.size == 1 -> parts[0].take(2).uppercase()
            else -> "${parts.first().first()}${parts.last().first()}".uppercase()
        }
    }
}
