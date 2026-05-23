package com.example.chordlab.favorites

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chordlab.R
import com.example.chordlab.chordlibrary.model.ChordRepository
import com.example.chordlab.chordlibrary.model.FavoriteChordStore
import com.example.chordlab.chordlibrary.model.GuitarChord
import com.example.chordlab.chordlibrary.view.ChordAdapter
import com.example.chordlab.chordlibrary.view.ChordDetailActivity
import com.example.chordlab.dashboard.view.DashboardActivity
import com.example.chordlab.scalelibrary.model.GuitarScale
import com.example.chordlab.scalelibrary.model.ScalePreferenceStore
import com.example.chordlab.scalelibrary.model.ScaleRepository
import com.example.chordlab.scalelibrary.view.ScaleAdapter
import com.example.chordlab.scalelibrary.view.ScaleDetailActivity

class FavoritesActivity : AppCompatActivity() {

    private lateinit var favoriteChordStore: FavoriteChordStore
    private lateinit var scalePreferenceStore: ScalePreferenceStore
    private lateinit var chordRepository: ChordRepository
    private lateinit var scaleRepository: ScaleRepository
    private lateinit var emptyFavoritesTextView: TextView
    private lateinit var chordAdapter: ChordAdapter
    private lateinit var scaleAdapter: ScaleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        favoriteChordStore = FavoriteChordStore(this)
        scalePreferenceStore = ScalePreferenceStore(this)
        chordRepository = ChordRepository()
        scaleRepository = ScaleRepository(this)

        emptyFavoritesTextView = findViewById(R.id.emptyFavoritesTextView)

        chordAdapter = ChordAdapter(
            favoriteChordStore = favoriteChordStore,
            onChordClick = { openChordDetail(it) },
            onFavoriteClick = {
                favoriteChordStore.toggleFavorite(it.chordName)
                refreshFavorites()
            }
        )

        scaleAdapter = ScaleAdapter(
            preferenceStore = scalePreferenceStore,
            onScaleClick = { openScaleDetail(it) },
            onFavoriteClick = {
                scalePreferenceStore.toggleFavorite(it.scaleName)
                refreshFavorites()
            },
            onRatingClick = { scale, rating ->
                scalePreferenceStore.saveRating(scale.scaleName, rating)
                refreshFavorites()
            }
        )

        findViewById<RecyclerView>(R.id.favoriteChordsRecyclerView).apply {
            layoutManager = GridLayoutManager(this@FavoritesActivity, 2)
            adapter = chordAdapter
        }

        findViewById<RecyclerView>(R.id.favoriteScalesRecyclerView).apply {
            layoutManager = GridLayoutManager(this@FavoritesActivity, 2)
            adapter = scaleAdapter
        }

        findViewById<Button>(R.id.favoritesDashboardButton).setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        refreshFavorites()
    }

    override fun onResume() {
        super.onResume()
        if (::favoriteChordStore.isInitialized) {
            refreshFavorites()
        }
    }

    private fun refreshFavorites() {
        val favoriteChords = loadFavoriteChords()
        val favoriteScales = loadFavoriteScales()

        chordAdapter.submitChords(favoriteChords)
        scaleAdapter.submitScales(favoriteScales)

        val hasFavorites = favoriteChords.isNotEmpty() || favoriteScales.isNotEmpty()
        emptyFavoritesTextView.visibility = if (hasFavorites) View.GONE else View.VISIBLE
    }

    private fun loadFavoriteChords(): List<GuitarChord> {
        val names = favoriteChordStore.getFavoriteNames().toSet()
        return chordRepository.getChords().filter { names.contains(it.chordName) }
    }

    private fun loadFavoriteScales(): List<GuitarScale> {
        val names = scalePreferenceStore.getFavoriteNames().toSet()
        return scaleRepository.getRootNotes()
            .flatMap { scaleRepository.getScales(it) }
            .filter { names.contains(it.scaleName) }
    }

    private fun openChordDetail(chord: GuitarChord) {
        val intent = Intent(this, ChordDetailActivity::class.java)
        intent.putExtra(ChordDetailActivity.EXTRA_CHORD_NAME, chord.chordName)
        startActivity(intent)
    }

    private fun openScaleDetail(scale: GuitarScale) {
        val intent = Intent(this, ScaleDetailActivity::class.java)
        intent.putExtra(ScaleDetailActivity.EXTRA_SCALE_NAME, scale.scaleName)
        startActivity(intent)
    }
}
