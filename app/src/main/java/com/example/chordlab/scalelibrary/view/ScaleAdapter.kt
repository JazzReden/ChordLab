package com.example.chordlab.scalelibrary.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chordlab.R
import com.example.chordlab.common.view.FretboardView
import com.example.chordlab.scalelibrary.model.GuitarScale
import com.example.chordlab.scalelibrary.model.ScaleDifficulty
import com.example.chordlab.scalelibrary.model.ScalePreferenceStore

class ScaleAdapter(
    private val preferenceStore: ScalePreferenceStore,
    private val onScaleClick: (GuitarScale) -> Unit,
    private val onFavoriteClick: (GuitarScale) -> Unit,
    private val onRatingClick: (GuitarScale, Int) -> Unit
) : RecyclerView.Adapter<ScaleAdapter.ScaleViewHolder>() {

    private val scales = mutableListOf<GuitarScale>()

    fun submitScales(newScales: List<GuitarScale>) {
        scales.clear()
        scales.addAll(newScales)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScaleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scale, parent, false)
        return ScaleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScaleViewHolder, position: Int) {
        holder.bind(scales[position])
    }

    override fun getItemCount(): Int = scales.size

    inner class ScaleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fretboardView = itemView.findViewById<FretboardView>(R.id.scaleMiniFretboardView)
        private val nameTextView = itemView.findViewById<TextView>(R.id.scaleNameTextView)
        private val favoriteTextView = itemView.findViewById<TextView>(R.id.scaleFavoriteTextView)
        private val ratingTextView = itemView.findViewById<TextView>(R.id.scaleRatingTextView)
        private val difficultyTextView = itemView.findViewById<TextView>(R.id.scaleDifficultyTextView)
        private val practiceTextView = itemView.findViewById<TextView>(R.id.practiceScaleTextView)

        fun bind(scale: GuitarScale) {
            fretboardView.setFretboardData(
                scale.fretboardPositions,
                scale.startFret,
                scale.displayFretCount,
                scale.notesSummaryLabel()
            )
            nameTextView.text = scale.scaleName
            favoriteTextView.text = if (preferenceStore.isFavorite(scale.scaleName)) "★" else "☆"
            ratingTextView.text = buildRating(preferenceStore.getRating(scale.scaleName))
            difficultyTextView.text = scale.difficulty.displayName
            if (scale.difficulty == ScaleDifficulty.BEGINNER) {
                difficultyTextView.setBackgroundResource(R.drawable.chord_level_badge_background)
                difficultyTextView.setTextColor(itemView.context.getColor(R.color.chord_dark_teal))
            } else {
                difficultyTextView.setBackgroundResource(R.drawable.scale_advanced_badge_background)
                difficultyTextView.setTextColor(itemView.context.getColor(android.R.color.white))
            }

            itemView.setOnClickListener { onScaleClick(scale) }
            practiceTextView.setOnClickListener { onScaleClick(scale) }
            favoriteTextView.setOnClickListener { onFavoriteClick(scale) }
            ratingTextView.setOnClickListener {
                val nextRating = preferenceStore.getRating(scale.scaleName) % 5 + 1
                onRatingClick(scale, nextRating)
            }
        }

        private fun buildRating(rating: Int): String {
            val stars = (1..5).joinToString(" ") { index -> if (index <= rating) "★" else "☆" }
            return if (rating == 0) "Rate: $stars" else stars
        }
    }
}
