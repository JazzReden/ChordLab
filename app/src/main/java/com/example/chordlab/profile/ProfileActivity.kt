package com.example.chordlab.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.chordlab.BuildConfig
import com.example.chordlab.R
import com.example.chordlab.auth.view.LoginActivity
import com.example.chordlab.dashboard.view.DashboardActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var profilePhotoImageView: ImageView
    private lateinit var profileInitialsTextView: TextView
    private lateinit var displayNameTextView: TextView
    private lateinit var displayNameEditText: EditText
    private lateinit var displayNameActionsLayout: LinearLayout
    private lateinit var bioTextView: TextView
    private lateinit var bioEditText: EditText
    private lateinit var bioActionsLayout: LinearLayout

    private val pickPhotoLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
                // Gallery may not grant persistable permission for every provider.
            }
            UserSession.savePhotoUri(this, uri.toString())
            showPhoto(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profilePhotoImageView = findViewById(R.id.profilePhotoImageView)
        profileInitialsTextView = findViewById(R.id.profileInitialsTextView)
        displayNameTextView = findViewById(R.id.displayNameTextView)
        displayNameEditText = findViewById(R.id.displayNameEditText)
        displayNameActionsLayout = findViewById(R.id.displayNameActionsLayout)
        bioTextView = findViewById(R.id.bioTextView)
        bioEditText = findViewById(R.id.bioEditText)
        bioActionsLayout = findViewById(R.id.bioActionsLayout)

        findViewById<Button>(R.id.profileBackButton).setOnClickListener {
            finish()
        }

        findViewById<FrameLayout>(R.id.profilePhotoContainer).setOnClickListener {
            pickPhotoLauncher.launch("image/*")
        }

        findViewById<ImageButton>(R.id.editDisplayNameButton).setOnClickListener {
            startEditingDisplayName()
        }
        findViewById<Button>(R.id.saveDisplayNameButton).setOnClickListener {
            saveDisplayName()
        }
        findViewById<Button>(R.id.cancelDisplayNameButton).setOnClickListener {
            cancelDisplayNameEdit()
        }

        findViewById<ImageButton>(R.id.editBioButton).setOnClickListener {
            startEditingBio()
        }
        findViewById<Button>(R.id.saveBioButton).setOnClickListener {
            saveBio()
        }
        findViewById<Button>(R.id.cancelBioButton).setOnClickListener {
            cancelBioEdit()
        }

        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            confirmLogout()
        }

        findViewById<TextView>(R.id.appVersionTextView).text =
            getString(R.string.app_version_label, BuildConfig.VERSION_NAME)

        bindProfile()
    }

    private fun bindProfile() {
        val displayName = UserSession.getDisplayName(this)
        displayNameTextView.text = displayName
        profileInitialsTextView.text = UserSession.initials(this)

        val bio = UserSession.getBio(this)
        if (bio.isBlank()) {
            bioTextView.text = getString(R.string.bio_placeholder)
            bioTextView.setTextColor(getColor(R.color.chord_dark_teal))
            bioTextView.alpha = 0.45f
        } else {
            bioTextView.text = bio
            bioTextView.setTextColor(getColor(R.color.chord_dark_teal))
            bioTextView.alpha = 1f
        }

        val photoUri = UserSession.getPhotoUri(this)
        if (photoUri.isNullOrBlank()) {
            showInitials(displayName)
        } else {
            showPhoto(Uri.parse(photoUri))
        }
    }

    private fun showInitials(displayName: String) {
        profilePhotoImageView.visibility = View.GONE
        profileInitialsTextView.visibility = View.VISIBLE
        profileInitialsTextView.text = UserSession.initials(this)
    }

    private fun showPhoto(uri: Uri) {
        profilePhotoImageView.visibility = View.VISIBLE
        profileInitialsTextView.visibility = View.GONE
        profilePhotoImageView.setImageURI(uri)
    }

    private fun startEditingDisplayName() {
        displayNameEditText.setText(UserSession.getDisplayName(this))
        displayNameTextView.visibility = View.GONE
        displayNameEditText.visibility = View.VISIBLE
        displayNameActionsLayout.visibility = View.VISIBLE
        findViewById<ImageButton>(R.id.editDisplayNameButton).visibility = View.GONE
    }

    private fun saveDisplayName() {
        val name = displayNameEditText.text.toString().trim()
        if (name.isEmpty()) {
            Toast.makeText(this, "Display name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        UserSession.saveDisplayName(this, name)
        cancelDisplayNameEdit()
        bindProfile()
    }

    private fun cancelDisplayNameEdit() {
        displayNameEditText.visibility = View.GONE
        displayNameActionsLayout.visibility = View.GONE
        displayNameTextView.visibility = View.VISIBLE
        findViewById<ImageButton>(R.id.editDisplayNameButton).visibility = View.VISIBLE
        displayNameTextView.text = UserSession.getDisplayName(this)
    }

    private fun startEditingBio() {
        val currentBio = UserSession.getBio(this)
        bioEditText.setText(currentBio)
        bioTextView.visibility = View.GONE
        bioEditText.visibility = View.VISIBLE
        bioActionsLayout.visibility = View.VISIBLE
        findViewById<ImageButton>(R.id.editBioButton).visibility = View.GONE
    }

    private fun saveBio() {
        UserSession.saveBio(this, bioEditText.text.toString().trim())
        cancelBioEdit()
        bindProfile()
    }

    private fun cancelBioEdit() {
        bioEditText.visibility = View.GONE
        bioActionsLayout.visibility = View.GONE
        bioTextView.visibility = View.VISIBLE
        findViewById<ImageButton>(R.id.editBioButton).visibility = View.VISIBLE
        bindProfile()
    }

    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ -> logout() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logout() {
        UserSession.clearSession(this)
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
