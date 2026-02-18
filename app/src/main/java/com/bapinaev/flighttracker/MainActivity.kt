package com.bapinaev.flighttracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.bapinaev.flighttracker.screens.FavoritesActivity
import com.bapinaev.flighttracker.ui.theme.FlightTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlightTrackerTheme {
                OpenFavoritesScreenButton()
            }
        }
    }
}

@Composable
fun OpenFavoritesScreenButton() {
    val context = LocalContext.current

    Button(onClick = {
        val intent = Intent(context, FavoritesActivity::class.java)
        context.startActivity(intent)
    }) {
        Text(text = "Открыть избранное")
    }
}