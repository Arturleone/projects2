package com.example.modulo03

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat.Callback
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Response
import java.util.logging.Handler

class LoginActivity : AppCompatActivity() {

    private var attempCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val botaoAcessar = findViewById<Button>(R.id.acessar_login)
        val botaoCadastrar = findViewById<Button>(R.id.cadastrar_login)
        val userName = findViewById<EditText>(R.id.username_acessar)
        val Password = findViewById<EditText>(R.id.password_login)
        val backgroundRed = ContextCompat.getDrawable(this, R.drawable.borderred)
        val backgroundDefault = ContextCompat.getDrawable(this, R.drawable.borderblack)

        botaoCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastrarActivity::class.java))
        }

        botaoAcessar.setOnClickListener {
            val username = userName.text.toString()
            val password = Password.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                realizarLogin(username, password)
            } else {
                userName.background = backgroundRed
                Password.background = backgroundRed
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                attempCount++
                android.os.Handler(Looper.getMainLooper()).postDelayed({
                    if (attempCount <= 3) {
                        userName.background = backgroundDefault
                        Password.background = backgroundDefault
                    }
                }, 750)
            }

        }

    }

    private fun realizarLogin(nome: String, senha: String) {
        listarUsuarios(nome, senha)
    }

    private fun listarUsuarios(nome: String, senha: String) {
        val call = RetrofitClient.usuarioApi.listarUsuarios()
        call.enqueue(object : retrofit2.Callback<List<UserDetailsResponse>> {
            override fun onResponse(
                call: Call<List<UserDetailsResponse>>,
                response: Response<List<UserDetailsResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val usuarios = response.body() ?: emptyList()
                    val usuario = usuarios.find { it.nome == nome }

                    if (usuario != null) {
                        val DR = usuario.dr
                        val email = usuario.email // Pega o email do usuário encontrado
                        realizarLoginComEmail(email, senha, nome, DR) // Realiza o login com o email
                    } else {
                        attempCount++
                        Toast.makeText(
                            this@LoginActivity,
                            "Usuário não encontrado.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Erro ao listar usuários.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<UserDetailsResponse>>, t: Throwable) {
                Log.d("Login", "Erro ao buscar usuários: ${t.message}")
                Toast.makeText(this@LoginActivity, "Erro de rede: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun realizarLoginComEmail(email: String, senha: String, nome: String, DR: String) {
        val call = RetrofitClient.usuarioApi.realizarLogin(email, senha)
        call.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    val email = intent.putExtra("EMAIL", email)
                    val nome = email.putExtra("NOME", nome)
                    val DR = nome.putExtra("DR", DR)
                    startActivity(DR)
                    finish()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            }
        })
    }
}

