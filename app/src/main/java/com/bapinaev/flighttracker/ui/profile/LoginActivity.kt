package com.bapinaev.flighttracker.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bapinaev.domain.model.User
import com.bapinaev.flighttracker.R

class LoginActivity : AppCompatActivity() {

    private val hardcodedUser = User(
        firstName = "Джон",
        surname = "Самолет",
        age = 25,
        login = "admin",
        password = "1234"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        val loginEdit = findViewById<EditText>(R.id.loginEdit)
        val passwordEdit = findViewById<EditText>(R.id.passwordEdit)
        val loginBtn = findViewById<Button>(R.id.loginBtn)

        loginBtn.setOnClickListener {
            val login = loginEdit.text.toString()
            val password = passwordEdit.text.toString()

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (login == hardcodedUser.login && password == hardcodedUser.password) {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }
}