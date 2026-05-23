package com.example.chordlab.scalelibrary.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chordlab.R
import com.example.chordlab.dashboard.view.DashboardActivity
import com.example.chordlab.scalelibrary.model.GuitarScale
import com.example.chordlab.scalelibrary.model.ScalePreferenceStore
import com.example.chordlab.scalelibrary.model.ScaleRepository
import com.example.chordlab.scalelibrary.presenter.ScaleListContract
import com.example.chordlab.scalelibrary.presenter.ScaleListPresenter

class ScaleListActivity : AppCompatActivity(), ScaleListContract.View {

    private lateinit var rootLayout: LinearLayout
    private lateinit var scaleRecyclerView: RecyclerView
    private lateinit var scaleAdapter: ScaleAdapter
    private lateinit var presenter: ScaleListContract.Presenter
    private val rootButtons = mutableMapOf<String, Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale_list)

        rootLayout = findViewById(R.id.scaleRootLayout)
        scaleRecyclerView = findViewById(R.id.scaleRecyclerView)

        val preferenceStore = ScalePreferenceStore(this)
        scaleAdapter = ScaleAdapter(
            preferenceStore = preferenceStore,
            onScaleClick = { presenter.selectScale(it) },
            onFavoriteClick = { presenter.toggleFavorite(it) },
            onRatingClick = { scale, rating -> presenter.rateScale(scale, rating) }
        )

        scaleRecyclerView.layoutManager = GridLayoutManager(this, 2)
        scaleRecyclerView.adapter = scaleAdapter

        findViewById<Button>(R.id.scaleDashboardButton).setOnClickListener {
            openDashboard()
        }

        presenter = ScaleListPresenter(this, ScaleRepository(this), preferenceStore)
        presenter.loadScales()
    }

    override fun showRoots(roots: List<String>) {
        rootLayout.removeAllViews()
        rootButtons.clear()

        roots.forEach { root ->
            val button = Button(this).apply {
                text = root
                setAllCaps(false)
                setTextColor(getColor(R.color.chord_dark_teal))
                background = getDrawable(R.drawable.chord_filter_button_background)
                setOnClickListener { presenter.selectRoot(root) }
            }

            val params = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.scale_root_width),
                resources.getDimensionPixelSize(R.dimen.chord_filter_height)
            ).apply {
                marginEnd = 10
            }

            rootButtons[root] = button
            rootLayout.addView(button, params)
        }
    }

    override fun showScales(scales: List<GuitarScale>) {
        scaleAdapter.submitScales(scales)
    }

    override fun updateSelectedRoot(rootNote: String) {
        rootButtons.forEach { (root, button) ->
            button.background = getDrawable(
                if (root == rootNote) {
                    R.drawable.chord_filter_button_selected_background
                } else {
                    R.drawable.chord_filter_button_background
                }
            )
            button.setTextColor(getColor(if (root == rootNote) android.R.color.white else R.color.chord_dark_teal))
        }
    }

    override fun openScaleDetail(scaleName: String) {
        val intent = Intent(this, ScaleDetailActivity::class.java)
        intent.putExtra(ScaleDetailActivity.EXTRA_SCALE_NAME, scaleName)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if (::presenter.isInitialized) {
            presenter.loadScales()
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    private fun openDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }
}
