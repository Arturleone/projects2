package com.example.modulo03

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashScreenActivity : AppCompatActivity() {

    private val splashTimer = 3000L
    private val splashTimerFirst = 6000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val duration = if (isFirstTime()) splashTimer else splashTimerFirst
        Toast.makeText(this, "papel", Toast.LENGTH_LONG).show()
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, duration)
    }

    private fun isFirstTime(): Boolean{
        val sharedPreferences: SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val firstTime = sharedPreferences.getBoolean("FIRST_TIME", true)
        if (firstTime){
            sharedPreferences.edit().putBoolean("FIRST_TIME", false).apply()
        }
        return firstTime
    }
}