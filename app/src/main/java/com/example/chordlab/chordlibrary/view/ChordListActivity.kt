package com.example.chordlab.chordlibrary.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chordlab.R
import com.example.chordlab.chordlibrary.model.ChordRepository
import com.example.chordlab.chordlibrary.model.FavoriteChordStore
import com.example.chordlab.chordlibrary.model.ChordType
import com.example.chordlab.chordlibrary.model.GuitarChord
import com.example.chordlab.chordlibrary.presenter.ChordListContract
import com.example.chordlab.chordlibrary.presenter.ChordListPresenter
import com.example.chordlab.dashboard.view.DashboardActivity

class ChordListActivity : AppCompatActivity(), ChordListContract.View {

    private lateinit var searchEditText: EditText
    private lateinit var filterLayout: LinearLayout
    private lateinit var emptyTextView: TextView
    private lateinit var chordRecyclerView: RecyclerView
    private lateinit var chordAdapter: ChordAdapter
    private lateinit var presenter: ChordListContract.Presenter
    private val filterButtons = mutableMapOf<ChordType, Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chord_list)

        searchEditText = findViewById(R.id.searchChordEditText)
        filterLayout = findViewById(R.id.chordFilterLayout)
        emptyTextView = findViewById(R.id.emptyChordTextView)
        chordRecyclerView = findViewById(R.id.chordRecyclerView)

        val favoriteChordStore = FavoriteChordStore(this)
        chordAdapter = ChordAdapter(
            favoriteChordStore = favoriteChordStore,
            onChordClick = { presenter.selectChord(it) },
            onFavoriteClick = { presenter.toggleFavorite(it) }
        )

        chordRecyclerView.layoutManager = GridLayoutManager(this, 2)
        chordRecyclerView.adapter = chordAdapter

        findViewById<Button>(R.id.chordDashboardButton).setOnClickListener {
            openDashboard()
        }

        presenter = ChordListPresenter(this, ChordRepository(), favoriteChordStore)

        setupSearch()
        setupFilters()
        presenter.loadChords()
    }

    override fun showChords(chords: List<GuitarChord>) {
        emptyTextView.visibility = View.GONE
        chordRecyclerView.visibility = View.VISIBLE
        chordAdapter.submitChords(chords)
    }

    override fun showEmptyMessage() {
        chordRecyclerView.visibility = View.GONE
        emptyTextView.visibility = View.VISIBLE
    }

    override fun openChordDetail(chordName: String) {
        val intent = Intent(this, ChordDetailActivity::class.java)
        intent.putExtra(ChordDetailActivity.EXTRA_CHORD_NAME, chordName)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if (::presenter.isInitialized) {
            presenter.loadChords()
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.searchChords(text.toString())
            }

            override fun afterTextChanged(text: Editable?) = Unit
        })
    }

    private fun setupFilters() {
        ChordType.values().forEach { type ->
            val button = Button(this).apply {
                text = type.displayName
                setAllCaps(false)
                setTextColor(getColor(android.R.color.white))
                background = getDrawable(R.drawable.chord_filter_button_background)
                setPadding(22, 0, 22, 0)
                setOnClickListener {
                    presenter.filterByType(type)
                    updateSelectedFilter(type)
                }
            }

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                resources.getDimensionPixelSize(R.dimen.chord_filter_height)
            ).apply {
                marginEnd = 10
            }

            filterButtons[type] = button
            filterLayout.addView(button, params)
        }

        updateSelectedFilter(ChordType.ALL)
    }

    private fun updateSelectedFilter(selectedType: ChordType) {
        filterButtons.forEach { (type, button) ->
            button.background = getDrawable(
                if (type == selectedType) {
                    R.drawable.chord_filter_button_selected_background
                } else {
                    R.drawable.chord_filter_button_background
                }
            )
            button.setTextColor(
                getColor(if (type == selectedType) android.R.color.white else R.color.chord_dark_teal)
            )
        }
    }

    private fun openDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }
}
