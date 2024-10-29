package com.example.artur_leonel_pe_modulo_3

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat

class LoginActivity : AppCompatActivity() {
    private var attempCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val sharedPreferences: SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val botaoAcessar = findViewById<Button>(R.id.acessar_login)
        val botaoCadastro = findViewById<Button>(R.id.cadastrar_login)
        val userName = findViewById<EditText>(R.id.username_acessar)
        val password = findViewById<EditText>(R.id.password_login)

        val backgroundRed = ContextCompat.getDrawable(this, R.drawable.borderred)
        val backgroudDefault = ContextCompat.getDrawable(this, R.drawable.borderblack)

        botaoCadastro.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        botaoAcessar.setOnClickListener {
            val inputUsername = userName.text.toString()
            val inputPassword = password.text.toString()
            if (verificarConta(inputUsername, inputPassword) || inputUsername == "admin" && inputPassword == "1q2w3e") {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("username", inputUsername)
                val email = sharedPreferences.getString("password", null)
                intent.putExtra("email", email)
                startActivity(intent)
            } else {
                messageShowBlock("Dados Incorretos!!")
                userName.background = backgroundRed
                password.background = backgroundRed
                attempCount++
                Handler().postDelayed({
                    if(attempCount>3) {
                        userName.background = backgroundRed
                        password.background = backgroundRed
                    } else {
                        userName.background = backgroudDefault
                        password.background = backgroudDefault
                    }
                }, 750)
                if (attempCount>3) {
                    //Desabilita os Widgets/Views
                    messageShowBlock("Login bloqueado: aguarde 30s!")
                    userName.isEnabled = false
                    password.isEnabled = false
                    botaoAcessar.isEnabled = false
                    Handler(Looper.getMainLooper()).postDelayed({
                        //Habilita as Widgets/View
                        userName.background = backgroudDefault
                        password.background = backgroudDefault
                        userName.isEnabled = true
                        password.isEnabled = true
                        botaoAcessar.isEnabled = true
                        attempCount = 0
                    }, 10000)

                }
            }
        }

    }
    private fun messageShowBlock(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun verificarConta (usuario: String, senha: String):Boolean {
        val sharedPreferences: SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val usuario2 = sharedPreferences.getString("username", null)
        val senha2 = sharedPreferences.getString("password", null)
        if (usuario2 == usuario && senha2 == senha) return true else return false
    }
}