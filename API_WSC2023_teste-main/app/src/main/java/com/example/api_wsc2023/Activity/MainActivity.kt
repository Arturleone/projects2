package com.example.api_wsc2023.Activity

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.api_wsc2023.R

class MainActivity : AppCompatActivity() {

    private lateinit var tvNome: TextView
    private lateinit var tvDr: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializando os TextViews
        tvNome = findViewById(R.id.tvNome)
        tvDr = findViewById(R.id.tvDr)

        // Receber dados da Intent
        val nome = intent.getStringExtra("NOME")
        val dr = intent.getStringExtra("DR")

        // Exibir dados nos TextViews
        tvNome.text = nome ?: "Nome não disponível"
        tvDr.text = dr ?: "DR não disponível"

    }
}
