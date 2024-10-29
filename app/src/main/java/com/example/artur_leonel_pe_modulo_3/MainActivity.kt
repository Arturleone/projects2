package com.example.artur_leonel_pe_modulo_3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val userName = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")
        val text = findViewById<TextView>(R.id.textView5)
        text.text = "${updateGreeting()}, $userName"
        val textView6 = findViewById<TextView>(R.id.textView6)
        textView6.text = userName
        val textView7 = findViewById<TextView>(R.id.textView7)
        textView7.text = email
        val textView8 = findViewById<TextView>(R.id.textView8)
        textView8.text = "PE"
    }

    fun Logout(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun updateGreeting(): String {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hourOfDay < 12 -> "Bom dia"
            hourOfDay < 18 -> "Boa tarde"
            else -> "Boa noite"
        }

        val userName = "Usuário" // Substitua pelo nome do usuário real
        return greeting
    }
}