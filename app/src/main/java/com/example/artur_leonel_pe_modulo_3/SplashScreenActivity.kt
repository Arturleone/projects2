package com.example.artur_leonel_pe_modulo_3

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val splashTimer = 3000L // Duração padrão (3 segundos)
    private val splashTimerFirst = 6000L // Duração se for a primeira vez (6 segundos)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Configura tela cheia
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // Obtém o ProgressBar
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        // Define o tempo de exibição com base na primeira execução
        val duration = if (isFirstTime()) splashTimer else splashTimerFirst

        // Inicia a MainActivity após o tempo especificado
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Fecha a SplashScreen Activity para não voltar pra ela
        }, duration)
    }

    // Verifica se é a primeira vez que o app está sendo executado
    private fun isFirstTime(): Boolean {
        val sharedPreferences: SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val firstTime = sharedPreferences.getBoolean("firstTime", true)
        if (firstTime) {
            sharedPreferences.edit().putBoolean("firstTime", false).apply()
        }
        return firstTime
    }
}
