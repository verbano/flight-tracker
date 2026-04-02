package com.bapinaev.flighttracker.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bapinaev.flighttracker.MainActivity
import com.bapinaev.flighttracker.R
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_screen)

        val nameTextView = findViewById<TextView>(R.id.name)
        val loginTextView = findViewById<TextView>(R.id.login)
        val ageTextView = findViewById<TextView>(R.id.age)
        val logoutButton = findViewById<Button>(R.id.logoutBtn)
        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        val editButton = findViewById<Button>(R.id.editBtn)
        val deleteButton = findViewById<Button>(R.id.deleteBtn)

        editButton.setOnClickListener { viewModel.onEditClicked() }
        deleteButton.setOnClickListener { viewModel.onDeleteClicked() }
        logoutButton.setOnClickListener { viewModel.onLogoutClicked() }
        backBtn.setOnClickListener { viewModel.onBackClicked() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        nameTextView.text = getString(
                            R.string.user_full_name,
                            state.user.firstName,
                            state.user.surname
                        )
                        loginTextView.text = getString(R.string.user_login, state.user.login)
                        ageTextView.text = getString(R.string.user_age, state.user.age)
                    }
                }
                launch {
                    viewModel.events.collect { event ->
                        when (event) {
                            ProfileEvent.ShowEditSoon -> {
                                Toast.makeText(
                                    this@ProfileActivity,
                                    R.string.profile_edit_soon,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            ProfileEvent.ShowDeleteSoon -> {
                                Toast.makeText(
                                    this@ProfileActivity,
                                    R.string.profile_delete_soon,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            ProfileEvent.NavigateToLogin -> {
                                val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }

                            ProfileEvent.NavigateToMain -> {
                                val intent = Intent(this@ProfileActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}

