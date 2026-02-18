package com.bapinaev.flighttracker.screen

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.bapinaev.flighttracker.R

@Composable
fun LoginScreen(
    onLoginClick: (login: String, password: String) -> Unit
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val view: View = LayoutInflater.from(context).inflate(R.layout.login_screen, null, false)

            val loginEdit = view.findViewById<EditText>(R.id.loginEdit)
            val passwordEdit = view.findViewById<EditText>(R.id.passwordEdit)

            val loginBtn = view.findViewById<Button>(R.id.loginBtn)
            loginBtn.setOnClickListener {
                val loginText = loginEdit.text.toString()
                val passwordText = passwordEdit.text.toString()
                if (loginText.isNotEmpty() && passwordText.isNotEmpty()) {
                    onLoginClick(loginText, passwordText)
                }
            }

            view
        }
    )
}
