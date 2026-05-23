package com.example.chordlab.scalelibrary.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.chordlab.R
import com.example.chordlab.common.view.FretboardView
import com.example.chordlab.scalelibrary.model.GuitarScale
import com.example.chordlab.scalelibrary.model.ScalePreferenceStore
import com.example.chordlab.scalelibrary.model.ScaleRepository
import com.example.chordlab.scalelibrary.presenter.ScaleDetailContract
import com.example.chordlab.scalelibrary.presenter.ScaleDetailPresenter

class ScaleDetailActivity : AppCompatActivity(), ScaleDetailContract.View {

    private lateinit var titleTextView: TextView
    private lateinit var typeTextView: TextView
    private lateinit var favoriteButton: Button
    private lateinit var ratingTextView: TextView
    private lateinit var fretboardView: FretboardView
    private lateinit var notesTextView: TextView
    private lateinit var formulaTextView: TextView
    private lateinit var tipTextView: TextView
    private lateinit var presenter: ScaleDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale_detail)

        titleTextView = findViewById(R.id.detailScaleTitleTextView)
        typeTextView = findViewById(R.id.detailScaleTypeTextView)
        favoriteButton = findViewById(R.id.detailScaleFavoriteButton)
        ratingTextView = findViewById(R.id.detailScaleRatingTextView)
        fretboardView = findViewById(R.id.detailScaleFretboardView)
        notesTextView = findViewById(R.id.detailScaleNotesTextView)
        formulaTextView = findViewById(R.id.detailScaleFormulaTextView)
        tipTextView = findViewById(R.id.detailScaleTipTextView)

        findViewById<Button>(R.id.backToScaleListButton).setOnClickListener {
            finish()
        }

        presenter = ScaleDetailPresenter(this, ScaleRepository(this), ScalePreferenceStore(this))

        favoriteButton.setOnClickListener {
            presenter.toggleFavorite()
        }

        ratingTextView.setOnClickListener {
            val currentRating = ratingTextView.tag as? Int ?: 0
            presenter.rateScale(currentRating % 5 + 1)
        }

        presenter.loadScale(intent.getStringExtra(EXTRA_SCALE_NAME).orEmpty())
    }

    override fun showScale(scale: GuitarScale, isFavorite: Boolean, rating: Int) {
        titleTextView.text = scale.scaleName
        typeTextView.text = scale.scaleType
        fretboardView.setFretboardData(
            scale.fretboardPositions,
            scale.startFret,
            scale.displayFretCount,
            scale.notesSummaryLabel()
        )
        notesTextView.text = scale.notes.joinToString("   ")
        formulaTextView.text = scale.intervalFormula
        tipTextView.text = scale.beginnerTip
        showFavoriteStatus(isFavorite)
        showRating(rating)
    }

    override fun showFavoriteStatus(isFavorite: Boolean) {
        favoriteButton.text = if (isFavorite) "★ Favorited" else "☆ Add Favorite"
    }

    override fun showRating(rating: Int) {
        ratingTextView.tag = rating
        ratingTextView.text = (1..5).joinToString("") { index -> if (index <= rating) "★" else "☆" }
    }

    override fun closeScreen() {
        finish()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_SCALE_NAME = "extra_scale_name"
    }
}
