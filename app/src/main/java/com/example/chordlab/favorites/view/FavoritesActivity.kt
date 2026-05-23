package com.example.chordlab.favorites.view

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
import com.example.chordlab.favorites.model.FavoritesRepository
import com.example.chordlab.favorites.presenter.FavoritesContract
import com.example.chordlab.favorites.presenter.FavoritesPresenter
import com.example.chordlab.scalelibrary.model.GuitarScale
import com.example.chordlab.scalelibrary.model.ScalePreferenceStore
import com.example.chordlab.scalelibrary.model.ScaleRepository
import com.example.chordlab.scalelibrary.view.ScaleAdapter
import com.example.chordlab.scalelibrary.view.ScaleDetailActivity

class FavoritesActivity : AppCompatActivity(), FavoritesContract.View {

    private lateinit var emptyFavoritesTextView: TextView
    private lateinit var chordAdapter: ChordAdapter
    private lateinit var scaleAdapter: ScaleAdapter
    private lateinit var presenter: FavoritesContract.Presenter
    private lateinit var favoriteChordStore: FavoriteChordStore
    private lateinit var scalePreferenceStore: ScalePreferenceStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        favoriteChordStore = FavoriteChordStore(this)
        scalePreferenceStore = ScalePreferenceStore(this)

        emptyFavoritesTextView = findViewById(R.id.emptyFavoritesTextView)

        chordAdapter = ChordAdapter(
            favoriteChordStore = favoriteChordStore,
            onChordClick = { presenter.selectChord(it) },
            onFavoriteClick = { presenter.toggleChordFavorite(it) }
        )

        scaleAdapter = ScaleAdapter(
            preferenceStore = scalePreferenceStore,
            onScaleClick = { presenter.selectScale(it) },
            onFavoriteClick = { presenter.toggleScaleFavorite(it) },
            onRatingClick = { scale, rating -> presenter.rateScale(scale, rating) }
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

        val favoritesRepository = FavoritesRepository(
            chordRepository = ChordRepository(),
            favoriteChordStore = favoriteChordStore,
            scaleRepository = ScaleRepository(this),
            scalePreferenceStore = scalePreferenceStore
        )
        presenter = FavoritesPresenter(this, favoritesRepository)
        presenter.loadFavorites()
    }

    override fun onResume() {
        super.onResume()
        if (::presenter.isInitialized) {
            presenter.loadFavorites()
        }
    }

    override fun showFavoriteChords(chords: List<GuitarChord>) {
        chordAdapter.submitChords(chords)
    }

    override fun showFavoriteScales(scales: List<GuitarScale>) {
        scaleAdapter.submitScales(scales)
    }

    override fun showEmptyState(isEmpty: Boolean) {
        emptyFavoritesTextView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    override fun openChordDetail(chordName: String) {
        val intent = Intent(this, ChordDetailActivity::class.java)
        intent.putExtra(ChordDetailActivity.EXTRA_CHORD_NAME, chordName)
        startActivity(intent)
    }

    override fun openScaleDetail(scaleName: String) {
        val intent = Intent(this, ScaleDetailActivity::class.java)
        intent.putExtra(ScaleDetailActivity.EXTRA_SCALE_NAME, scaleName)
        startActivity(intent)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}
