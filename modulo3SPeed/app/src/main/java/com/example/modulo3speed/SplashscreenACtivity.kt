package com.example.modulo3speed

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashscreenACtivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splashscreen)

    }

    private fun isFirst(): Boolean {
        sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE)
        sharedPreferences.getString("IsFirst", false )
    }
  }