package com.example.chordlab.dashboard.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chordlab.R
import com.example.chordlab.profile.UserSession
import com.example.chordlab.chordlibrary.view.ChordListActivity
import com.example.chordlab.favorites.FavoritesActivity
import com.example.chordlab.profile.ProfileActivity
import com.example.chordlab.dashboard.model.DashboardItem
import com.example.chordlab.dashboard.model.DashboardRepository
import com.example.chordlab.dashboard.presenter.DashboardContract
import com.example.chordlab.dashboard.presenter.DashboardPresenter
import com.example.chordlab.quiz.view.QuizActivity
import com.example.chordlab.scalelibrary.view.ScaleListActivity

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var welcomeTextView: TextView
    private lateinit var dashboardItemsLayout: LinearLayout
    private lateinit var dashboardProfileImageView: ImageView
    private lateinit var dashboardProfileHeadIcon: ImageView
    private lateinit var dashboardProfileInitialsTextView: TextView
    private lateinit var presenter: DashboardContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        welcomeTextView = findViewById(R.id.welcomeTextView)
        dashboardItemsLayout = findViewById(R.id.dashboardItemsLayout)
        dashboardProfileImageView = findViewById(R.id.dashboardProfileImageView)
        dashboardProfileHeadIcon = findViewById(R.id.dashboardProfileHeadIcon)
        dashboardProfileInitialsTextView = findViewById(R.id.dashboardProfileInitialsTextView)

        findViewById<FrameLayout>(R.id.dashboardProfileButton).setOnClickListener {
            openProfile()
        }

        val dashboardRepository = DashboardRepository()
        presenter = DashboardPresenter(this, dashboardRepository)
        presenter.loadDashboard()
        bindProfileAvatar()
    }

    override fun onResume() {
        super.onResume()
        if (::dashboardProfileImageView.isInitialized) {
            bindProfileAvatar()
        }
    }

    private fun bindProfileAvatar() {
        val photoUri = UserSession.getPhotoUri(this)
        if (!photoUri.isNullOrBlank()) {
            dashboardProfileImageView.setImageURI(Uri.parse(photoUri))
            dashboardProfileImageView.visibility = View.VISIBLE
            dashboardProfileHeadIcon.visibility = View.GONE
            dashboardProfileInitialsTextView.visibility = View.GONE
            return
        }

        dashboardProfileImageView.visibility = View.GONE
        val initials = UserSession.initials(this)
        if (initials != "CL") {
            dashboardProfileInitialsTextView.text = initials
            dashboardProfileInitialsTextView.visibility = View.VISIBLE
            dashboardProfileHeadIcon.visibility = View.GONE
        } else {
            dashboardProfileInitialsTextView.visibility = View.GONE
            dashboardProfileHeadIcon.visibility = View.VISIBLE
        }
    }

    override fun showWelcomeMessage(message: String) {
        welcomeTextView.text = message
    }

    override fun showDashboardItems(items: List<DashboardItem>) {
        dashboardItemsLayout.removeAllViews()

        items.forEach { item ->
            val itemView = layoutInflater.inflate(
                R.layout.item_dashboard_feature,
                dashboardItemsLayout,
                false
            )

            val titleTextView = itemView.findViewById<TextView>(R.id.featureTitleTextView)
            val subtitleTextView = itemView.findViewById<TextView>(R.id.featureSubtitleTextView)

            titleTextView.text = item.title
            subtitleTextView.text = item.subtitle

            itemView.setOnClickListener {
                presenter.onFeatureSelected(item)
            }

            dashboardItemsLayout.addView(itemView)
        }
    }

    override fun showFeatureMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun openChordLibrary() {
        startActivity(Intent(this, ChordListActivity::class.java))
    }

    override fun openScaleLibrary() {
        startActivity(Intent(this, ScaleListActivity::class.java))
    }

    override fun openQuiz() {
        startActivity(Intent(this, QuizActivity::class.java))
    }

    override fun openFavorites() {
        startActivity(Intent(this, FavoritesActivity::class.java))
    }

    override fun openProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}
