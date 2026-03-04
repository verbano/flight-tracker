package com.bapinaev.flighttracker.screens

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.bapinaev.flighttracker.R

class FavoritesActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FavoritesFragment())
                .commit()
        }
    }
}