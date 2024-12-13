package com.example.modulo03

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val textView7 = findViewById<TextView>(R.id.textView7)
        val textView6 = findViewById<TextView>(R.id.textView6)
        val textView8 = findViewById<TextView>(R.id.textView8)

        val userName = intent.getStringExtra("NOME")
        val email = intent.getStringExtra("EMAIL")
        val Dr = intent.getStringExtra("DR")

        val text = findViewById<TextView>(R.id.textView5)
        text.text = "${updateGreeting()}, $userName"

        textView8.text = Dr
        textView6.text = userName
        textView7.text = email
        startCountdown(3700000)
    }

    fun Logout (view: View) {

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

        return greeting
    }

    private fun startCountdown(millis: Long) {
        val tvCountdown = findViewById<TextView>(R.id.cronometro)

        object : android.os.CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = (millisUntilFinished / (1000*60*60)).toInt()
                val minutes = ((millisUntilFinished % (1000*60*60)) / (1000*60)).toInt()
                val seconds = ((millisUntilFinished % (1000*60)) / 1000).toInt()

                tvCountdown.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }

            override fun onFinish() {
                tvCountdown.text = "00:00:00"
            }
        }.start()

    }
}