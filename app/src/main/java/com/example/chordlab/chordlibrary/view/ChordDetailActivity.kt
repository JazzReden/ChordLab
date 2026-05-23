package com.example.chordlab.chordlibrary.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.chordlab.R
import com.example.chordlab.chordlibrary.model.ChordRepository
import com.example.chordlab.chordlibrary.model.FavoriteChordStore
import com.example.chordlab.chordlibrary.model.GuitarChord
import com.example.chordlab.chordlibrary.presenter.ChordDetailContract
import com.example.chordlab.chordlibrary.presenter.ChordDetailPresenter

class ChordDetailActivity : AppCompatActivity(), ChordDetailContract.View {

    private lateinit var chordNameTextView: TextView
    private lateinit var chordTypeTextView: TextView
    private lateinit var chordDiagramView: ChordDiagramView
    private lateinit var fingerPlacementTextView: TextView
    private lateinit var stringIndicatorsTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var favoriteButton: Button
    private lateinit var deleteFavoriteButton: Button
    private lateinit var backButton: Button
    private lateinit var presenter: ChordDetailContract.Presenter
    private var isDiagramZoomed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chord_detail)

        chordNameTextView = findViewById(R.id.detailChordNameTextView)
        chordTypeTextView = findViewById(R.id.detailChordTypeTextView)
        chordDiagramView = findViewById(R.id.detailChordDiagramView)
        fingerPlacementTextView = findViewById(R.id.detailFingerPlacementTextView)
        stringIndicatorsTextView = findViewById(R.id.detailStringIndicatorsTextView)
        descriptionTextView = findViewById(R.id.detailDescriptionTextView)
        favoriteButton = findViewById(R.id.detailFavoriteButton)
        deleteFavoriteButton = findViewById(R.id.detailDeleteFavoriteButton)
        backButton = findViewById(R.id.backToChordListButton)

        presenter = ChordDetailPresenter(this, ChordRepository(), FavoriteChordStore(this))

        favoriteButton.setOnClickListener {
            presenter.toggleFavorite()
        }

        deleteFavoriteButton.setOnClickListener {
            presenter.removeFavorite()
        }

        backButton.setOnClickListener {
            finish()
        }

        chordDiagramView.setOnClickListener {
            toggleDiagramZoom()
        }

        val chordName = intent.getStringExtra(EXTRA_CHORD_NAME).orEmpty()
        presenter.loadChord(chordName)
    }

    override fun showChord(chord: GuitarChord, isFavorite: Boolean) {
        chordNameTextView.text = chord.chordName
        chordTypeTextView.text = chord.chordType.displayName
        chordDiagramView.setChord(
            chord.markers,
            chord.stringIndicators,
            chord.barreFret,
            chord.startingFret,
            chord.bottomStringLabels
        )
        fingerPlacementTextView.text = chord.fingerPlacement
        stringIndicatorsTextView.text = chord.stringIndicators.joinToString("  ")
        descriptionTextView.text = chord.description
        showFavoriteStatus(isFavorite)
    }

    override fun showFavoriteStatus(isFavorite: Boolean) {
        favoriteButton.text = if (isFavorite) "★ Favorited" else "☆ Add to Favorites"
        deleteFavoriteButton.visibility = if (isFavorite) View.VISIBLE else View.GONE
    }

    override fun closeScreen() {
        finish()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_CHORD_NAME = "extra_chord_name"
    }

    private fun toggleDiagramZoom() {
        isDiagramZoomed = !isDiagramZoomed
        val newHeight = if (isDiagramZoomed) 380 else 260
        chordDiagramView.layoutParams = chordDiagramView.layoutParams.apply {
            height = (newHeight * resources.displayMetrics.density).toInt()
        }
    }
}
