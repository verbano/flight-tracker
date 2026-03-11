package com.bapinaev.flighttracker.screen

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.bapinaev.domain.model.User
import com.bapinaev.flighttracker.R
@Composable
fun ProfileScreen() {

    val user = User(
        firstName = "Джон",
        surname = "Самолет",
        age = 25,
        login = "admin",
        password = "1234"
    )

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val view: View = LayoutInflater.from(context).inflate(R.layout.profile_screen, null, false)

            view.findViewById<TextView>(R.id.name).text = "${user.firstName} ${user.surname}"
            view.findViewById<TextView>(R.id.login).text = user.login
            view.findViewById<TextView>(R.id.age).text = user.age.toString()

            val editBtn = view.findViewById<Button>(R.id.editBtn)
            editBtn.setOnClickListener {}

            val logoutBtn = view.findViewById<Button>(R.id.logoutBtn)
            logoutBtn.setOnClickListener {}

            val deleteBtn = view.findViewById<Button>(R.id.deleteBtn)
            deleteBtn.setOnClickListener {}

            view
        }
    )
}
