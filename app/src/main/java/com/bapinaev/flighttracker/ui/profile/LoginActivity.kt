package com.bapinaev.flighttracker.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bapinaev.flighttracker.R
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        val loginEdit = findViewById<EditText>(R.id.loginEdit)
        val passwordEdit = findViewById<EditText>(R.id.passwordEdit)
        val loginBtn = findViewById<Button>(R.id.loginBtn)

        loginBtn.setOnClickListener {
            viewModel.onLoginClicked(
                login = loginEdit.text?.toString().orEmpty(),
                password = passwordEdit.text?.toString().orEmpty()
            )
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        LoginEvent.EmptyFields -> {
                            Toast.makeText(
                                this@LoginActivity,
                                "Заполните все поля",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        LoginEvent.InvalidCredentials -> {
                            Toast.makeText(
                                this@LoginActivity,
                                "Неверный логин или пароль",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        LoginEvent.NavigateToProfile -> {
                            startActivity(Intent(this@LoginActivity, ProfileActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }
    }
}
