package com.example.chordlab.chordlibrary.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chordlab.R
import com.example.chordlab.chordlibrary.model.FavoriteChordStore
import com.example.chordlab.chordlibrary.model.GuitarChord

class ChordAdapter(
    private val favoriteChordStore: FavoriteChordStore,
    private val onChordClick: (GuitarChord) -> Unit,
    private val onFavoriteClick: (GuitarChord) -> Unit
) : RecyclerView.Adapter<ChordAdapter.ChordViewHolder>() {

    private val chords = mutableListOf<GuitarChord>()

    fun submitChords(newChords: List<GuitarChord>) {
        chords.clear()
        chords.addAll(newChords)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chord, parent, false)
        return ChordViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChordViewHolder, position: Int) {
        holder.bind(chords[position])
    }

    override fun getItemCount(): Int = chords.size

    inner class ChordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val diagramView = itemView.findViewById<ChordDiagramView>(R.id.chordDiagramView)
        private val chordNameTextView = itemView.findViewById<TextView>(R.id.chordNameTextView)
        private val chordTypeTextView = itemView.findViewById<TextView>(R.id.chordTypeTextView)
        private val favoriteTextView = itemView.findViewById<TextView>(R.id.favoriteTextView)

        fun bind(chord: GuitarChord) {
            diagramView.setChord(
                chord.markers,
                chord.stringIndicators,
                chord.barreFret,
                chord.startingFret,
                chord.bottomStringLabels
            )
            chordNameTextView.text = chord.chordName
            chordTypeTextView.text = chord.chordType.displayName.uppercase()
            favoriteTextView.text = if (favoriteChordStore.isFavorite(chord.chordName)) "★" else "☆"

            itemView.setOnClickListener {
                onChordClick(chord)
            }

            favoriteTextView.setOnClickListener {
                onFavoriteClick(chord)
            }
        }
    }
}
