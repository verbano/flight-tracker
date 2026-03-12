package com.bapinaev.flighttracker.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bapinaev.domain.model.User
import com.bapinaev.flighttracker.MainActivity
import com.bapinaev.flighttracker.ui.profile.LoginActivity
import com.bapinaev.flighttracker.R

class ProfileActivity : AppCompatActivity() {

    private val user = User(
        firstName = "Джон",
        surname = "Самолет",
        age = 25,
        login = "admin",
        password = "1234"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_screen)

        val nameTextView = findViewById<TextView>(R.id.name)
        val loginTextView = findViewById<TextView>(R.id.login)
        val ageTextView = findViewById<TextView>(R.id.age)
        val logoutButton = findViewById<Button>(R.id.logoutBtn)
        val backBtn = findViewById<ImageButton>(R.id.backBtn)

        nameTextView.text = getString(R.string.user_full_name, user.firstName, user.surname)
        loginTextView.text = getString(R.string.user_login, user.login)
        ageTextView.text = getString(R.string.user_age, user.age)

        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}

