package com.example.projetodeteste

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this, TaskActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Erro no login: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, TaskActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Erro ao registrar: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}